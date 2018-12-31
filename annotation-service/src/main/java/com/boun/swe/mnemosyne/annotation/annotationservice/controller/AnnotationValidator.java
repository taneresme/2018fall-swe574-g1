package com.boun.swe.mnemosyne.annotation.annotationservice.controller;

import com.boun.swe.mnemosyne.annotation.annotationservice.exception.BadRequestException;
import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;

public class AnnotationValidator {

    private final static String CONTEXT = "http://www.w3.org/ns/anno.jsonld";
    private final static String TYPE = "Annotation";

    public static void validate(Annotation annotation) {

        if (annotation == null) {
            throw new BadRequestException("Annotation must not be null");
        }

        if (!CONTEXT.equals(annotation.getContext())) {
            throw new BadRequestException("Invalid annotation context, " +
                    "@context must be: " + CONTEXT);
        }

        if (!TYPE.equals(annotation.getType())) {
            throw new BadRequestException("Invalid annotation type, " +
                    "@context must be: " + TYPE);
        }

        if (annotation.getTarget() == null) {
            throw new BadRequestException("Target must not be null");
        }
    }
}
