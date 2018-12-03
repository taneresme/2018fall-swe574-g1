package com.boun.swe.mnemosyne.annotation.annotationservice.service;

import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import com.boun.swe.mnemosyne.annotation.annotationservice.repository.AnnotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnotationService {
    private AnnotationRepository annotationRepository;

    @Autowired
    public AnnotationService(AnnotationRepository annRepo){
        annotationRepository = annRepo;
    }

    public String createAnnotation(String body, String target){
        Annotation save = annotationRepository.save(new Annotation(body, target));
        save.setId();
        return save.getId();
    }

    public Annotation getAnnotation(String id) {
        return annotationRepository.findById(id).orElse(null);
        //POSSIBLE NULL-PTR EXCEPTION
    }
}
