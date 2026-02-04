/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A helper for computing message digests in a performant thread safe manner
 * <p>
 * This basically wraps a {@link ThreadLocal} that gives each unique thread its own separate reusable
 * {@link MessageDigest} instance transparently from the point of view of the caller.
 * </p>
 */
public final class DigestHelper {

    private final String algorithm;
    private final ThreadLocal<MessageDigest> digest;

    /**
     * Creates a new digest helper
     *
     * @param algorithm Digest algorithm
     * @throws IllegalArgumentException If the given algorithm is null, blank or not supported by this JVM
     */
    public DigestHelper(String algorithm) {
        if (StringUtils.isBlank(algorithm)) {
            throw new IllegalArgumentException("algorithm cannot be null/blank");
        }
        this.algorithm = algorithm;
        this.digest = ThreadLocal.withInitial(this::create);
        // Call create() immediately to create a throwaway instance to validate immediately that the provided algorithm
        // is available, otherwise we wouldn't find this out until we actually tried to compute a digest
        this.create();
    }

    /**
     * Creates a new instance of the digest
     *
     * @return New digest instance
     */
    private MessageDigest create() {
        try {
            return MessageDigest.getInstance(this.algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Computes a thread safe digest of the given input
     *
     * @param input Input
     * @return Digest
     */
    public byte[] digest(byte[] input) {
        return this.digest.get().digest(input);
    }

    @Override
    public String toString() {
        return "DigestHelper(algorithm=" + this.algorithm + ")";
    }
}
