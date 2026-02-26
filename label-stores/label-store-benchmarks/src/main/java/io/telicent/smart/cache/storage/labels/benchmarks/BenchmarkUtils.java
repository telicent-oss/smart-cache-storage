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
package io.telicent.smart.cache.storage.labels.benchmarks;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.output.TeeOutputStream;

import java.io.*;

/**
 * Helper utilities for running JMH benchmarks
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BenchmarkUtils {

    /**
     * Runs a JMH benchmark for a specific benchmark class, primarily intended for use for running classes via an IDE.
     * The log and results of the run are captured to the {@code target/} directory so they can be inspected later if
     * necessary.
     *
     * @param benchmarkClass The benchmark class to run
     */
    public static void run(Class<?> benchmarkClass) {
        PrintStream originalOut = System.out;
        File logFile = new File("target", benchmarkClass.getSimpleName() + "-log.txt");
        System.out.println("Benchmark Log File: " + logFile.getAbsolutePath());
        try (FileOutputStream log = new FileOutputStream(logFile)) {
            // Print the progress output to both our log file and to StdOut so developers can see progress in the IDE
            // console
            try (PrintStream output = new PrintStream(new TeeOutputStream(System.out, log))) {
                System.setOut(output);

                // Prepare a results file where the benchmark results are placed in machine-readable (JSON) format
                File resultsFile = new File("target/", benchmarkClass.getSimpleName() + "-results.json");
                System.out.println("Benchmark Results File: " + resultsFile.getAbsolutePath());
                System.out.print("NB - Won't be populated until benchmark completes!");
                System.out.println();

                // Run a single benchmark class with JMH, requested JSON format results to our results file
                org.openjdk.jmh.Main.main(new String[] {
                        "-rf",
                        "JSON",
                        "-rff",
                        resultsFile.getAbsolutePath(),
                        benchmarkClass.getSimpleName()
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                System.setOut(originalOut);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
