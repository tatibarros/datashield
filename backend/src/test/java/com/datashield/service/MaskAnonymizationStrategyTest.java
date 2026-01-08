package com.datashield.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MaskAnonymizationStrategy Tests")
class MaskAnonymizationStrategyTest {

    @Test
    @DisplayName("Should mask all characters except last 4")
    void testMaskKeepingLastFour() {
        MaskAnonymizationStrategy strategy = new MaskAnonymizationStrategy(4);
        String result = strategy.anonymize("1234567890");
        assertEquals("****567890", result);
    }

    @Test
    @DisplayName("Should handle string shorter than keep length")
    void testMaskShorterString() {
        MaskAnonymizationStrategy strategy = new MaskAnonymizationStrategy(10);
        String result = strategy.anonymize("12345");
        assertEquals("*****", result);
    }

    @Test
    @DisplayName("Should handle null value")
    void testMaskNullValue() {
        MaskAnonymizationStrategy strategy = new MaskAnonymizationStrategy(4);
        String result = strategy.anonymize(null);
        assertNull(result);
    }

    @Test
    @DisplayName("Should handle empty string")
    void testMaskEmptyString() {
        MaskAnonymizationStrategy strategy = new MaskAnonymizationStrategy(4);
        String result = strategy.anonymize("");
        assertNull(result);
    }
}
