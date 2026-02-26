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
package io.telicent.smart.cache.storage.mongodb.cluster;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.PrintStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClusterUtils {
    private static final PrintStream ORIGINAL_STDOUT = System.out;

    public static boolean SYSOUT_LOGGING_ENABLED = true;

    public static long logStart(String message) {
        if (!SYSOUT_LOGGING_ENABLED) {
            return System.currentTimeMillis();
        }
        long start = System.currentTimeMillis();
        ORIGINAL_STDOUT.format("%s...\n", message);
        return start;
    }

    public static void logFinished(String message, long start) {
        if (!SYSOUT_LOGGING_ENABLED) {
            return;
        }
        ORIGINAL_STDOUT.format("%s in %,d ms\n", message, System.currentTimeMillis() - start);
    }
}
