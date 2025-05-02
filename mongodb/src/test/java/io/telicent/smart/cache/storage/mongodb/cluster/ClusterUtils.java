package io.telicent.smart.cache.storage.mongodb.cluster;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.PrintStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClusterUtils {
    private static final PrintStream ORIGINAL_STDOUT = System.out;

    public static long logStart(String message) {
        long start = System.currentTimeMillis();
        ORIGINAL_STDOUT.format("%s...\n", message);
        return start;
    }

    public static void logFinished(String message, long start) {
        ORIGINAL_STDOUT.format("%s in %,d ms\n", message, System.currentTimeMillis() - start);
    }
}
