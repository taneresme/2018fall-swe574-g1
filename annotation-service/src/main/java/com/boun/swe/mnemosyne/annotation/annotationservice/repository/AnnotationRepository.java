package com.boun.swe.mnemosyne.annotation.annotationservice.repository;

import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

//@Repository
public interface AnnotationRepository { //extends JpaRepository<Annotation, Long> {
    Annotation findById(Long id);
}
