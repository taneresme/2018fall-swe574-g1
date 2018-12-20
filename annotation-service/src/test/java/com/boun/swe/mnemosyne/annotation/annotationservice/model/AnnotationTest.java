package com.boun.swe.mnemosyne.annotation.annotationservice.model;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.*;
import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.model.impl.agent.Person;
import com.github.anno4j.model.impl.agent.Software;
import com.github.anno4j.model.impl.body.TextualBody;
import com.github.anno4j.model.impl.multiplicity.Choice;
import com.github.anno4j.model.impl.selector.FragmentSelector;
import com.github.anno4j.model.impl.state.HttpRequestState;
import com.github.anno4j.model.impl.targets.SpecificResource;
import com.github.jsonldjava.core.RDFDataset;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.object.RDFObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/*
{
  "@context": "http://www.w3.org/ns/anno.jsonld",
  "id": "http://example.org/anno38",
  "type": "Annotation",
  "stylesheet": {
    "id": "http://example.org/stylesheet1",
    "type": "CssStylesheet"
  },
}
*/

public class AnnotationTest {
    public AnnotationTest() throws RepositoryConfigException, RepositoryException, InstantiationException, IllegalAccessException {
    }

    Anno4j anno4j = new Anno4j();


    private Annotation  annotationTester = anno4j.createObject(Annotation.class);

    {
/*      "motivation": "commenting",                                         */
        annotationTester.addMotivation(MotivationFactory.getCommenting(anno4j));

/*      "created": "2015-10-13T13:00:00Z",
        "generated": "2015-10-14T15:13:28Z",                                */
        annotationTester.setCreated("2015-10-13T13:00:00Z");
        annotationTester.setGenerated("2015-10-14T15:13:28Z");

/*      "generator": {
            "id": "http://example.org/client1",
            "type": "Software",
            "name": "Code v2.1",
            "homepage": "http://example.org/homepage1"
        },                                                                  */
        Software generatorAnno = anno4j.createObject(Software.class);
        generatorAnno.setName("Code v2.1");
        generatorAnno.setHomepage("http://example.org/homepage1");
        annotationTester.setGenerator(generatorAnno);

/*      "creator": {
            "id": "http://example.org/user1",
            "type": "Person",
            "name": "A. Person",
            "nickname": "user1"
        },                                                                  */
        Person creatorAnno = anno4j.createObject(Person.class);
        creatorAnno.setName("A. Person");
        creatorAnno.setNickname("user1");
        annotationTester.setCreator(creatorAnno);

        // TODO: stylesheet

/*      "body": [
          {
            "type": "TextualBody",
            "purpose": "tagging",
            "value": "love"
          },
          {
            "type": "Choice",
            "items": [
              {
                "type": "TextualBody",
                "purpose": "describing",
                "value": "I really love this particular bit of text in this XML. No really.",
                "format": "text/plain",
                "language": "en",
                "creator": "http://example.org/user1"
              },
              {
                "type": "SpecificResource",
                "purpose": "describing",
                "source": {
                  "id": "http://example.org/comment1",
                  "type": "Audio",
                  "format": "audio/mpeg",
                  "language": "de",
                  "creator": {
                    "id": "http://example.org/user2",
                    "type": "Person"
                  }
                }
              }
            ]
          }
        ],                                                                  */
        TextualBody body1 = anno4j.createObject(TextualBody.class);
        body1.addPurpose(MotivationFactory.getTagging(anno4j));
        body1.setValue("love");
        annotationTester.addBody(body1);

        Body body2 = anno4j.createObject(Choice.class);
            TextualBody item1 = anno4j.createObject(TextualBody.class);
            item1.addPurpose(MotivationFactory.getDescribing(anno4j));
            item1.setValue("I really love this particular bit of text in this XML. No really.");
            item1.addLanguage("en");
            item1.setCreator(anno4j.createObject(Agent.class));
        ((Choice) body2).addItem(item1);
            SpecificResource item2 = anno4j.createObject(SpecificResource.class);
            // TODO: item2.purpose()
        ResourceObject source = anno4j.createObject(ResourceObject.class);
        try {
            source.setResourceAsString("http://example.org/comment1"); // TODO: Does it copy all the info?
        } catch (MalformedQueryException e) { e.printStackTrace();
        } catch (UpdateExecutionException e) { e.printStackTrace();
        }
            item2.setSource(source);
        ((Choice) body2).addItem(item2);
        annotationTester.addBody(body2);

/*      "target": {
          "type": "SpecificResource",
          "styleClass": "mystyle",
          "source": "http://example.com/document1",
          "state": [
            {
              "type": "HttpRequestState",
              "value": "Accept: application/xml",
              "refinedBy": {
                "type": "TimeState",
                "sourceDate": "2015-09-25T12:00:00Z"
              }
            }
          ],
          "selector": {
            "type": "FragmentSelector",
            "value": "xpointer(/doc/body/section[2]/para[1])",
            "refinedBy": {
              "type": "TextPositionSelector",
              "start": 6,
              "end": 27
            }
          }
        }                                                                       */
        Target target1 = anno4j.createObject(Target.class);
        try {
            target1.setResourceAsString("http://example.com/document1");
        } catch (MalformedQueryException e) { e.printStackTrace();
        } catch (UpdateExecutionException e) { e.printStackTrace();
        }
        State state = anno4j.createObject(HttpRequestState.class);
        // TODO: state
        Selector selector = anno4j.createObject(FragmentSelector.class);
        // TODO: selector
        annotationTester.addTarget(target1);
    }



    @Test
    public void getId() {
    }
}