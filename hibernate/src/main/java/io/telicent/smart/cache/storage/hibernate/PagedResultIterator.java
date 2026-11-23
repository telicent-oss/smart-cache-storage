/**
 * Copyright (C) Telicent Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.telicent.smart.cache.storage.hibernate;

import jakarta.persistence.TypedQuery;
import org.hibernate.query.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * A paged results iterator that allows results to be iterated over with only a maximum of the page size held in memory
 * at any given time
 *
 * @param <TStored> Stored type
 * @param <T>       Result type
 */
class PagedResultIterator<TStored, T> implements Iterator<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PagedResultIterator.class);

    private final TransactionContext context;
    private final TypedQuery<TStored> query;
    private final Function<TStored, T> loader;
    private Page page;
    private final Queue<T> currentPage = new LinkedList<>();
    private final boolean shouldCloseTransaction;
    private boolean finished = false;

    /**
     * Creates a new paged results iterator
     *
     * @param context                Transaction context
     * @param query                  Query for loading results
     * @param loader                 Loader function to transform between stored and result type
     * @param page                   First page of results to iterate from
     * @param shouldCloseTransaction Whether to call {@link TransactionContext#close()} when the end of the iterator is
     *                               reached
     */
    PagedResultIterator(TransactionContext context, TypedQuery<TStored> query, Function<TStored, T> loader, Page page,
                        boolean shouldCloseTransaction) {
        this.context = Objects.requireNonNull(context, "Transaction context cannot be null");
        this.query = Objects.requireNonNull(query, "Query cannot be null");
        this.loader = Objects.requireNonNull(loader, "Loader function cannot be null");
        this.page = Objects.requireNonNull(page, "Page cannot be null");
        this.shouldCloseTransaction = shouldCloseTransaction;
    }

    @Override
    public boolean hasNext() {
        boolean anyInMemory = !this.currentPage.isEmpty();
        if (anyInMemory) {
            return true;
        } else if (this.finished) {
            return false;
        } else {
            this.loadNextPage();
        }
        return !this.currentPage.isEmpty();
    }

    /**
     * Tries to load the next page of results from the database
     */
    private void loadNextPage() {
        // If the transaction gets closed outside our control then iterator can't return any more items, mark ourselves
        // as finished so hasNext() returns appropriately
        if (!this.context.isActive()) {
            LOGGER.warn(
                    "Transaction, or its underlying storage, closed outside of iterator control, only remaining in-memory items will be returned");
            markFinished();
            return;
        }

        // Before loading the next page of results flush() and clear() the entity manager otherwise we might continue
        // to hold those entities in-memory beyond the lifetime of that page.  For iteration over very large query
        // results this could cause memory consumption problems.
        this.context.getEntityManager().flush();
        this.context.getEntityManager().clear();

        // Then load the next page of results
        this.query.setFirstResult(this.page.getNumber() * this.page.getSize());
        this.query.setMaxResults(this.page.getSize());
        LOGGER.debug("Loading page {} of size {} results", this.page.getNumber(), this.page.getSize());
        this.currentPage.addAll(this.query.getResultList().stream().map(this.loader).toList());

        // Check whether this is the last page of results and update state accordingly
        if (this.currentPage.isEmpty()) {
            // If no results were returned we've reached the end of the results and don't need to attempt any further
            // queries.  At this point we close the transaction if we've been configured to do so.
            LOGGER.debug("No further results returned for page {}, paging completed", this.page.getNumber());
            markFinished();
            return;
        } else if (this.currentPage.size() > this.page.getSize()) {
            // If the database returns more results than the page size this means it isn't implementing paging properly,
            // log a warning since this may cause issues with memory consumption and developers may wish to choose a
            // different database driver as a result
            LOGGER.warn(
                    "Underlying database returned {} results when only {} were requested, the database driver may not implement paging correctly",
                    this.currentPage.size(), this.page.getSize());
        } else if (this.currentPage.size() < this.page.getSize()) {
            // If the database returns fewer results than the page size then we're reached the end of results and can
            // mark ourselves as finished so we don't make any further queries
            LOGGER.debug(
                    "Database returned {} results for page size {} meaning page {} is the last page of results, paging completed",
                    this.currentPage.size(), this.page.getSize(), this.page.getNumber());
            markFinished();
            return;
        }
        // Increment the page so next time loadNextPage() is called we'll attempt to load the next page
        this.page = this.page.next();
    }

    private void markFinished() {
        this.finished = true;
        if (this.shouldCloseTransaction && this.context.isActive()) {
            this.context.close();
        }
    }

    @Override
    public T next() {
        if (this.hasNext()) {
            return this.currentPage.poll();
        } else {
            throw new NoSuchElementException("No further elements available");
        }
    }
}
