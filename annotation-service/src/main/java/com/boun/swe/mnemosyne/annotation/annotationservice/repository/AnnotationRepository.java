package com.boun.swe.mnemosyne.annotation.annotationservice.repository;

import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, String> {
    Optional<Annotation> findById(String id);
}
