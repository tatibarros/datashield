package com.datashield.service;

public class MaskAnonymizationStrategy implements AnonymizationStrategy {
    private final int keepLastChars;

    public MaskAnonymizationStrategy(int keepLastChars) {
        this.keepLastChars = keepLastChars;
    }

    @Override
    public String anonymize(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        if (value.length() <= keepLastChars) {
            return "*".repeat(value.length());
        }
        int maskLength = value.length() - keepLastChars;
        return "*".repeat(maskLength) + value.substring(maskLength);
    }
}
