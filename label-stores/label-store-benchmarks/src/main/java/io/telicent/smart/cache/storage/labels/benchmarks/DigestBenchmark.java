/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks;

import io.telicent.smart.cache.storage.labels.DigestHelper;
import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class DigestBenchmark {

    @Param({ "SHA512", "SHA256"})
    private String algorithm;

    private int counter = 0;
    private ThreadLocal<MessageDigest> digestThreadLocal;
    private DigestHelper digestHelper;

    public static void main(String[] args) {
        BenchmarkUtils.run(DigestBenchmark.class);
    }

    private static final List<byte[]> RANDOM_LABELS;

    static {
        RANDOM_LABELS = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            RANDOM_LABELS.add(RandomUtils.insecure().randomBytes(256));
        }
    }

    @Setup
    public void setup() {
        this.digestThreadLocal = ThreadLocal.withInitial(this::getDigest);
        this.digestHelper = new DigestHelper(this.algorithm);
    }

    private MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance(this.algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    @Threads(4)
    public void digestInstancePerComputation(Blackhole blackhole) {
        try {
            MessageDigest digest = MessageDigest.getInstance(this.algorithm);
            blackhole.consume(digest.digest(RANDOM_LABELS.get(this.counter++ % RANDOM_LABELS.size())));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    @Threads(4)
    public void digestInstancePerThread(Blackhole blackhole) {
        blackhole.consume(this.digestThreadLocal.get().digest(RANDOM_LABELS.get(this.counter++ % RANDOM_LABELS.size())));
    }

    @Threads(4)
    @Benchmark
    public void digestHelper(Blackhole blackhole) {
        blackhole.consume(this.digestHelper.digest(RANDOM_LABELS.get(this.counter++ % RANDOM_LABELS.size())));
    }
}
