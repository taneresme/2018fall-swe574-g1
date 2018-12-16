package com.boun.swe.mnemosyne.annotation.annotationservice.controller;

import com.boun.swe.mnemosyne.annotation.annotationservice.model.Annotation;
import com.boun.swe.mnemosyne.annotation.annotationservice.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**     AnnotationController: what the users interact with.
 *      It accepts HTTP requests:
 *
 *      1) POST: user provides a JSON object, Controller checks if it is a legitimate Annotation.
 *         This is achieved by controlling
 *              i) if the object is a legitimate JSON-LD object.
 *             ii) if the object contains "@context = "http://www.w3.org/ns/anno.jsonld"," value
 *         These two conditions guarantee a W3C conforming Annotation.
 *         If successful, then it is passed onto Service for database insertion.
 *
 *      2)  GET: user provides an id in the form of an IRI, this is passed onto Service for querying.
 *          As an additional feature, user may also provide Target id or Body creator id. Management of queries
 *      is left to Service level.
 *
 *      TODO: Ways to implement HEAD and OPTIONS calls.
 * */
@Controller
@RequestMapping("annotations")
public class AnnotationController {

    private AnnotationService annotationService;
    @Autowired
    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

/*
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String createAnnotation(@RequestBody String body, @RequestParam("target") String target){
        return annotationService.createAnnotation(null);
    }
*/ // TODO: Implement!

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Annotation getAnnotation(@PathVariable("id") String id){
        return annotationService.getAnnotation(id);
    }
}
