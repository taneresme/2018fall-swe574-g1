package com.boun.swe.mnemosyne.annotation.annotationservice.service;

import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import com.boun.swe.mnemosyne.annotation.annotationservice.repository.AnnotationRepository;
import com.github.anno4j.Anno4j;
import eu.fbk.rdfpro.jsonld.JSONLD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**     AnnotationService, as a class, acts as a middleman between user interaction and database.
 *      So that it is both "business logic" and "persistence". What it does:
 *
 *   1) Since our system offers additional features than W3C Annotations, we will be using custom Annotation classes.
 *      Upon obtaining a JSON-LD Annotation from Controller, Service will first it convert to an Anno4j.Annotation,
 *      then a custom one. This will be saved in our database.
 *
 *   2) On queries, it will first determine query type, then obtain relevant result from database and pass it back
 *      to Controller for relaying to user.
 *
 * */

@Service
public class AnnotationService {
    private AnnotationRepository annotationRepository;

    @Autowired
    public AnnotationService(AnnotationRepository annRepo){
        annotationRepository = annRepo;
    }

    public String createAnnotation(JSONLD userInput){
        Annotation save = annotationRepository.save(new Annotation((com.github.anno4j.model.Annotation) userInput));
        save.setId();
        return save.getId();
    }

    public Annotation getAnnotation(String id) {
        return annotationRepository.findById(id).orElse(null);
        //POSSIBLE NULL-PTR EXCEPTION
    }
}
