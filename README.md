[![Build Status](https://travis-ci.org/swe-ms-boun/2018fall-swe574-g1.png?branch=develop)](https://travis-ci.org/swe-ms-boun/2018fall-swe574-g1)

# Mnemosyne

> Mnemosyne is the goddess of memory in Greek mythology.

## Project

Brief project description

For detailed information please visit [Wiki](https://github.com/swe-ms-boun/2018fall-swe574-g1/wiki)

## Supported Annotation Models

Annotation server is developed within the project but it lives independently from the application.
Thus, any annotation that are compliant with the following structure as defined in W3C model, will be able to stored in an annotation-server. 

### Content-Type

The following type must be selected as described in W3C model:
```
application/ld+json;profile="http://www.w3.org/ns/anno.jsonld"
```

### Annotation Body

For Media (Image) Annotation:
```json
{
  "@context": "http://www.w3.org/ns/anno.jsonld",
  "id": "file:///home/tugcan/scripts/html/index.html",
  "type": "Annotation",
  "body": {
    "type": "TextualBody",
    "value": "This is my comment",
    "creator": "blue"
  },
  "target": {
    "id": "https://example.com/image.jpg#xywh=0.4,0.24344569288389514,0.2525,0.30711610486891383",
    "type": "Image",
    "format": "image/jpeg",
    "ceator": "user2"
  }
}
```

For Textual Annotation:
```json
{
  "@context": "http://www.w3.org/ns/anno.jsonld",
  "id": "http://mayonez/memory/3",
  "type": "Annotation",
  "body": {
    "type": "TextualBody",
    "value": "sadfasdf",
    "creator": "mavi"
  },
  "target": {
    "type": "TextQuoteSelector",
    "exact": "e of an",
    "prefix": 104,
    "suffix": 111,
    "refinedBy": {
      "type": "TextPositionSelector",
      "start": "/div[2]",
      "end": "/div[2]"
    }
  }
}
```
Link to Source: [W3C](https://www.w3.org/TR/annotation-model)

## Running Postgre as a Docker container
List of terminal commands to running docker postgre container and creating project database

> $docker run --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=admin -d postgres \
> $docker ps (See the container_id at first column)
> $docker exec -it <container_id> bin/bash \
> $psql -h localhost -U postgres \
> $CREATE DATABASE mnemosyne; \
> postgres=# \connect mnemosyne \
> mnemosyne=# \dt

**Configuration via Terminal**\
[Wiki Link](https://github.com/swe-ms-boun/2018fall-swe574-g1/wiki/6.1-Database-Design)
