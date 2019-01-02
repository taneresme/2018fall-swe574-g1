package com.boun.swe.mnemosyne.annotation.annotationservice.controller;

import com.boun.swe.mnemosyne.annotation.annotationservice.exception.BadRequestException;
import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import com.boun.swe.mnemosyne.annotation.annotationservice.model.Body;
import com.boun.swe.mnemosyne.annotation.annotationservice.model.Target;
import org.junit.Test;

public class AnnotationValidatorTest {

    @Test(expected = BadRequestException.class) // THEN
    public void shouldBadRequestWhenAnnotationIsNull() {
        // WHEN
        AnnotationValidator.validate(null);
    }

    @Test(expected = BadRequestException.class) // THEN
    public void shouldBadRequestWhenAnnotationContextIsIncorrect() {
        // GIVEN
        Annotation annotation = createAnnotation("wrongContext", "Annotation");

        // WHEN
        AnnotationValidator.validate(annotation);
    }

    @Test(expected = BadRequestException.class) // THEN
    public void shouldBadRequestWhenAnnotationTypeIsIncorrect() {
        // GIVEN
        Annotation annotation = createAnnotation(
                "http://www.w3.org/ns/anno.jsonld", "wrongType");

        // WHEN
        AnnotationValidator.validate(annotation);
    }

    @Test(expected = BadRequestException.class) // THEN
    public void shouldBadRequestWhenAnnotationTargetIsNull() {
        // GIVEN
        Annotation annotation = createAnnotation(
                "http://www.w3.org/ns/anno.jsonld", "Annotation");
        annotation.setTarget(null);

        // WHEN
        AnnotationValidator.validate(annotation);
    }

    private Annotation createAnnotation(String context, String type) {
        Annotation annotation = new Annotation();
        annotation.setContext(context);
        annotation.setType(type);
        Body body = new Body();
        body.setType("TextualBody");
        body.setValue("This is my comment");
        body.setCreator("blue");
        annotation.setBody(body);
        Target target = new Target();
        target.setType("Image");
        target.setFormat("image/jpeg");
        annotation.setTarget(target);
        return annotation;
    }
}
