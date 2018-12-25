package com.boun.swe.mnemosyne.annotation.annotationservice.repository;

import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Long> {
    // for hibernate id
    Annotation findById(Long id);

    // for id field in annotation request
    Annotation findByAnnotationId(String id);
}
