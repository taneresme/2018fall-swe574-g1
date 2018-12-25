package com.boun.swe.mnemosyne.annotation.annotationservice.controller;

import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import com.boun.swe.mnemosyne.annotation.annotationservice.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/annotations")
public class AnnotationController {

    private static final String JSON_LD_ANNOTATION_MEDIA_TYPE =
            "application/ld+json;profile=\"http://www.w3.org/ns/anno.jsonld\"";

    private final AnnotationService annotationService;

    @Autowired
    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @PostMapping(consumes = JSON_LD_ANNOTATION_MEDIA_TYPE, produces = JSON_LD_ANNOTATION_MEDIA_TYPE)
    public Annotation create(@RequestBody Annotation annotation) throws IOException {
        AnnotationValidator.validate(annotation);
        return annotationService.save(annotation);
    }

    @GetMapping(value = "/find", produces = JSON_LD_ANNOTATION_MEDIA_TYPE)
    public Annotation get(@RequestParam("id") String id) {
        return annotationService.findAnnotationById(id);
    }

    @GetMapping(produces = JSON_LD_ANNOTATION_MEDIA_TYPE)
    public List<Annotation> getAll() {
        return annotationService.findAll();
    }
}
