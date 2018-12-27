package com.boun.swe.mnemosyne.annotation.annotationservice.service;

import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import com.boun.swe.mnemosyne.annotation.annotationservice.repository.AnnotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnotationService {

    private final AnnotationRepository annotationRepository;

    @Autowired
    public AnnotationService(AnnotationRepository annotationRepository) {
        this.annotationRepository = annotationRepository;
    }

    public Annotation save(Annotation annotation) {
        return annotationRepository.save(annotation);
    }

    public Annotation findAnnotationById(String id) {
        return annotationRepository.findByAnnotationId(id);
    }

    public Annotation findAnnotationByEntityId(Long entityId) {
        return annotationRepository.findOne(entityId);
    }

    public List<Annotation> findAll() {
        return annotationRepository.findAll();
    }

    public List<Annotation> findAllByCreator(String creator) {
        return annotationRepository.findByCreator(creator);
    }
}
