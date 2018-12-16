package com.boun.swe.mnemosyne.annotation.annotationservice.model;

/**  Some Design Decisions by KUVETT Group
*       1)    Bodies of Annotations are text-only
*           1.1) Later on, by the requirements, it's changed to accept images as well.
*       2)    Annotations are 1-1 relations, ie. *only one* Target
*           and *at most one* Body.
*/

/*  This is how an annotation looks:
    {
      "@context": "http://www.w3.org/ns/anno.jsonld",
            AT LEAST 1: ABOVE STRING MUST BE ONE OF THOSE
      "id": "http://example.org/anno1",
            ONLY 1: IRI OF ANNOTATION
      "type": "Annotation",
            AT LEAST 1: Annotation MUST BE ONE OF THEM

            KEEP THEM CONSTANT

      "body": "http://example.org/post1",
            GENERALLY MORE THAN 1, MAY BE 0:
                RELATIONSHIP BETWEEN ANNOTATION AND BODY
      "target": "http://example.com/page1"
            AT LEAST 1:
                RELATIONSHIP BETWEEN ANNOTATION AND TARGET
    }
*/

import com.github.anno4j.model.Target;
import eu.fbk.rdfpro.jsonld.JSONLD;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import com.github.anno4j.Anno4j;

import javax.persistence.*;


@Entity
@Table(name = "annotations")
public class Annotation {
    private com.github.anno4j.model.Annotation annotation;
    private String annotationId;
    private String targetId;
    private String bodyCreator;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idNumeric;

    public Annotation(com.github.anno4j.model.Annotation annotation){
        this.annotation = annotation;
        annotationId = annotation.getResourceAsString();
        if(!annotation.getTargets().isEmpty())
            this.targetId = ((Target) annotation.getTargets().toArray()[0]).getResourceAsString();
        // TODO: what is Resource?
        if(!annotation.getBodies().isEmpty())
            this.bodyCreator = ((Target) annotation.getBodies().toArray()[0]).getCreator().getName();
    }
    public void setId(){
        if (this.idNumeric != null)
            annotation.setModified(idNumeric.toString());
        // TODO: More on ID's how to assign them, how to access them?
    }

    public String getId(){
        return annotation.getGenerated();
        // TODO: More on ID's how to assign them, how to access them?
    }

}