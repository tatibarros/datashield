package com.datashield.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HashAnonymizationStrategy Tests")
class HashAnonymizationStrategyTest {

    @Test
    @DisplayName("Should hash value consistently")
    void testHashConsistency() {
        HashAnonymizationStrategy strategy = new HashAnonymizationStrategy();
        String value = "john.doe@example.com";
        String result1 = strategy.anonymize(value);
        String result2 = strategy.anonymize(value);
        assertEquals(result1, result2, "Hash should be consistent for same input");
    }

    @Test
    @DisplayName("Should produce different hashes for different values")
    void testHashDifferentValues() {
        HashAnonymizationStrategy strategy = new HashAnonymizationStrategy();
        String hash1 = strategy.anonymize("email1@example.com");
        String hash2 = strategy.anonymize("email2@example.com");
        assertNotEquals(hash1, hash2);
    }

    @Test
    @DisplayName("Should handle null value")
    void testHashNullValue() {
        HashAnonymizationStrategy strategy = new HashAnonymizationStrategy();
        String result = strategy.anonymize(null);
        assertNull(result);
    }

    @Test
    @DisplayName("Should produce 16-character hash")
    void testHashLength() {
        HashAnonymizationStrategy strategy = new HashAnonymizationStrategy();
        String result = strategy.anonymize("test@example.com");
        assertNotNull(result);
        assertEquals(16, result.length(), "Hash should be 16 characters");
    }
}
