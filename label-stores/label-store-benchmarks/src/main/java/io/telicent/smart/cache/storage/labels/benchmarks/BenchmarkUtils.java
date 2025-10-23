package io.telicent.smart.cache.storage.labels.benchmarks;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.output.TeeOutputStream;

import java.io.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BenchmarkUtils {

    public static void run(Class<?> benchmarkClass) {
        PrintStream originalOut = System.out;
        try (FileOutputStream file = new FileOutputStream("target/" + benchmarkClass.getSimpleName() + "-log.txt")) {
            try (PrintStream output = new PrintStream(new TeeOutputStream(System.out, file))) {
                System.setOut(output);
                org.openjdk.jmh.Main.main(new String[] {
                        benchmarkClass.getSimpleName(),
                        "-rf",
                        "JSON",
                        "-rff",
                        "target/" + benchmarkClass.getSimpleName() + "-results.json"
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
