package com.boun.swe.mnemosyne.validator;

import java.util.Objects;

public class MemoryValidator {

    public static void validateTitle(final String title) {
        if (Objects.isNull(title) || title.isEmpty()) {
            throw new IllegalArgumentException("Title is not valid: " + title);
        }
    }
}
