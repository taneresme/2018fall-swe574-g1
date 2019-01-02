package com.boun.swe.mnemosyne.annotation.annotationservice.service;

import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import com.boun.swe.mnemosyne.annotation.annotationservice.model.Body;
import com.boun.swe.mnemosyne.annotation.annotationservice.model.Target;
import com.boun.swe.mnemosyne.annotation.annotationservice.repository.AnnotationRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AnnotationServiceTest {

    @Mock
    private AnnotationRepository annotationRepositoryMock;

    @InjectMocks
    private AnnotationService annotationService;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldSaveAnnotation() {
        // GIVEN
        Annotation annotation = createAnnotation();
        when(annotationRepositoryMock.save(any(Annotation.class))).thenReturn(annotation);

        // WHEN
        annotationService.save(annotation);

        // THEN
        verify(annotationRepositoryMock, times(2)).save(any(Annotation.class));
    }

    @Test
    public void shouldFindAnnotationById() {
        // GIVEN
        String id = "123";
        Annotation annotation = createAnnotation();
        when(annotationRepositoryMock.findByAnnotationId(id)).thenReturn(annotation);

        // WHEN
        annotationService.findAnnotationById(id);

        // THEN
        verify(annotationRepositoryMock).findByAnnotationId(id);
    }

    private Annotation createAnnotation() {
        Annotation annotation = new Annotation();
        annotation.setContext("http://www.w3.org/ns/anno.jsonld");
        annotation.setType("Annotation");
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
