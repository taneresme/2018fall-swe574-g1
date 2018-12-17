package com.boun.swe.mnemosyne.annotation.annotationservice.model;

//  Some Design Decisions by KUVETT Group
/*    1)    Bodies of Annotations are text-only
*     2)    Annotations are 1-1 relations, ie. *only one* Target
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

import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;

import javax.persistence.*;


@Entity
@Table(name = "annotations")
public class Annotation {
    private String context = "\"http://www.w3.org/ns/anno.jsonld\",";
    private String id;
    private String type = "\"Annotation\",";
    private String body;
    private String target;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idNumeric;


    public String getContext() {
        return this.context;
    }
    public String getId() {
        return this.id;
    }
    public String getType() {
        return this.type;
    }
    public String getBody() {
        return this.body;
    }
    public String getTarget() {
        return this.target;
    }

    public void setId() {
        if (this.idNumeric != null)
            this.id = idNumeric.toString();
        else { id = ""; }
    }

    //  public void setTarget(String target) { this.target = target; }
    //  public void setBody(String body) { this.body = body; }

    public Annotation(String body, String target){
        this.body = body;
        this.target = target;
    }

    @Override
    public String toString(){
        return "{" +
                "\"@context\": "    + context +
                "\"id\": "          + id +
                "\"type\": "        + type +
                "\"body\": "        + body +
                "\"target\": "      + target + "}";
    }

}