package com.boun.swe.mnemosyne.annotation.annotationservice.controller;

import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import com.boun.swe.mnemosyne.annotation.annotationservice.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/annotations")
public class AnnotationController {

    private static final String JSON_LD_ANNOTATION_MEDIA_TYPE =
            "application/ld+json;profile=\"http://www.w3.org/ns/anno.jsonld\"";

    private AnnotationService annotationService;

    @Autowired
    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @PostMapping(value = "/create",
            consumes = JSON_LD_ANNOTATION_MEDIA_TYPE, produces = JSON_LD_ANNOTATION_MEDIA_TYPE)
    public Annotation create(@RequestBody Annotation annotation) {

        return null;
    }

    @GetMapping(value = "/{id}", produces = JSON_LD_ANNOTATION_MEDIA_TYPE)
    public Annotation get(@PathVariable("id") String id) {
        //return annotationService.getAnnotation(id);
        return null;
    }
}
