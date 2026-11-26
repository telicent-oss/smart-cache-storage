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

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestPageInfo {

    @DataProvider(name = "scenarios")
    public Object[][] scenarios() {
        return new Object[][] {
                { 10, 100, 10 },
                { 10, 17, 2 },
                { 10, 0, 1 },
                { 1, 100, 100 },
                { 3, 100, 34 },
                { 5, 23, 5 },
                { 25, 88, 4 },
                { 25, 49, 2 }
        };
    }

    @DataProvider(name = "invalid")
    public Object[][] invalid() {
        return new Object[][] {
                { 0, 100 },
                { -7, 100 },
                { 1, -1 }
        };
    }

    @Test(dataProvider = "scenarios")
    public void givenPageSizeAndTotalResults_whenCreatingPageInfo_thenTotalPagesCorrect(int pageSize, long totalResults,
                                                                                        long expectedPages) {
        // Given and When
        PageInfo pageInfo = new PageInfo(pageSize, totalResults);

        // Then
        Assert.assertEquals(pageInfo.getTotalPages(), expectedPages);
    }

    @Test(dataProvider = "invalid", expectedExceptions = IllegalArgumentException.class)
    public void givenInvalidArguments_whenCreatingPageInfo_thenIllegalArgument(int pageSize, long totalResults) {
        // Given, When and Then
        new PageInfo(pageSize, totalResults);
    }

    @Test(dataProvider = "scenarios")
    public void givenPageInfo_whenHalvingPageSize_thenTotalPagesIncreasesAppropriately(int pageSize, long totalResults,
                                                                                       long expectedPages) {
        if (pageSize == 1) {
            throw new SkipException("Test does not apply when page size already at 1");
        }

        // Given
        PageInfo pageInfo = new PageInfo(pageSize, totalResults);

        // When
        int newPageSize = pageSize / 2;
        PageInfo halved = pageInfo.forSize(newPageSize);

        // Then
        if (newPageSize == pageSize) {
            Assert.assertSame(pageInfo, halved, "If page size was unchanged then same page info should be returned");
        } else if (totalResults == 0) {
            Assert.assertEquals(halved.getTotalPages(), 1, "When zero results always 1 page");
        } else if (newPageSize == 1) {
            Assert.assertEquals(halved.getTotalPages(), totalResults,
                                "When new page size is 1 number of pages must equal total results");
        } else if (newPageSize * 2 < pageSize) {
            Assert.assertTrue(halved.getTotalPages() >= expectedPages * 2,
                              "When new page size less than half original page size then total pages should at least double");
        } else {
            Assert.assertEquals(halved.getTotalPages(), expectedPages * 2,
                                "When new page size exactly half original page size then total pages should double");
        }
    }

    @Test(dataProvider = "scenarios")
    public void givenPageInfo_whenDoublingPageSize_thenTotalPagesDecreasesAppropriately(int pageSize, long totalResults,
                                                                                        long expectedPages) {
        // Given
        PageInfo pageInfo = new PageInfo(pageSize, totalResults);

        // When
        int newPageSize = pageSize * 2;
        PageInfo doubled = pageInfo.forSize(newPageSize);

        // Then
        if (expectedPages == 1) {
            Assert.assertEquals(doubled.getTotalPages(), expectedPages,
                                "When expected pages is 1 then doubling page size does not change total pages");
        } else {
            Assert.assertEquals(doubled.getTotalPages(), Math.ceilDiv(expectedPages, 2),
                                "When more than one page was expected doubling page size should halve (rounding up) total pages");
        }
    }
}
