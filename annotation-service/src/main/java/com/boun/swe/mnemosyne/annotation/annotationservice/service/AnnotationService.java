package com.boun.swe.mnemosyne.annotation.annotationservice.service;

import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import com.boun.swe.mnemosyne.annotation.annotationservice.repository.AnnotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnnotationService {

    private final String serverUrl;
    private final String suffix = "/anno";
    private final AnnotationRepository annotationRepository;

    @Autowired
    public AnnotationService(AnnotationRepository annotationRepository, @Value("${deployment.url}") String serverUrl) {
        this.annotationRepository = annotationRepository;
        this.serverUrl = serverUrl;
    }

    @Transactional
    public Annotation save(Annotation annotation) {
        Annotation storedAnnotation = annotationRepository.save(annotation);
        storedAnnotation.setAnnotationId(serverUrl + suffix + annotation.getId());
        return annotationRepository.save(storedAnnotation);
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

    public Annotation findAnnotationByGeneratorId(String id) {
        return annotationRepository.findByGenerator(id);
    }
}
