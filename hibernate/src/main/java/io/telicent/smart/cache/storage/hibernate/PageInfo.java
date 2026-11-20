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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Provides information about pages of results from a query
 *
 */
@Getter
@ToString
@EqualsAndHashCode
public class PageInfo {

    private final int pageSize;
    private final long totalResults;
    private final long totalPages;

    /**
     * Creates new page information, the total pages being calculated based upon the given page size and total results
     *
     * @param pageSize     Page Size
     * @param totalResults Total results for the query
     * @throws IllegalArgumentException Page size is zero/negative, or total results are negative
     */
    public PageInfo(int pageSize, long totalResults) {
        if (pageSize <= 0) {
            throw invalidPageSize(pageSize);
        }
        this.pageSize = pageSize;
        if (totalResults < 0) {
            throw new IllegalArgumentException("Total results cannot be negative");
        }
        this.totalResults = totalResults;
        this.totalPages = calculateTotalPages(pageSize, totalResults);
    }

    /**
     * Calculates the page information if the page size were changed
     *
     * @param newPageSize New page size
     * @return New page information
     * @throws IllegalArgumentException If the given page size is negative
     */
    public PageInfo forSize(int newPageSize) {
        if (newPageSize == pageSize) {
            return this;
        } else if (newPageSize <= 0) {
            throw invalidPageSize(newPageSize);
        } else {
            return new PageInfo(newPageSize, totalResults);
        }
    }

    private static IllegalArgumentException invalidPageSize(int pageSize) {
        return new IllegalArgumentException("Page size cannot be zero/negative: " + pageSize);
    }

    /**
     * Calculates the total pages based on the given page size and total results.  This is always at least {@code 1} as
     * even if there are no results that counts as a single empty page of results
     *
     * @param pageSize     Page Size
     * @param totalResults Total Results
     * @return Total pages
     */
    private static long calculateTotalPages(int pageSize, long totalResults) {
        if (totalResults == 0) {
            // There's always at least one empty page
            return 1;
        } else if (pageSize == 1) {
            // If the page size is 1 then the number of pages is the number of results
            return totalResults;
        } else {
            // Calculate how many pages are available, we use ceilDiv here as this rounds up to account for partial
            // pages e.g. if we 10 results with page size 3 there's 4 pages total, 3 with 3 results, and 1 with 1 result
            return Math.ceilDiv(totalResults, pageSize);
        }
    }
}
