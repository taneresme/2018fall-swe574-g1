package com.boun.swe.mnemosyne.annotation.annotationservice.repository;

import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Long> {
    // for hibernate id
    Annotation findById(Long id);

    // for id field in annotation request
    Annotation findByAnnotationId(String id);

    @Query("select a from Annotation a where a.body.creator = ?1")
    List<Annotation> findByCreator(String creator);
}
