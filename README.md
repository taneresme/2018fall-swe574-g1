[![Build Status](https://travis-ci.org/swe-ms-boun/2018fall-swe574-g1.png?branch=develop)](https://travis-ci.org/swe-ms-boun/2018fall-swe574-g1)

# Mnemosyne

> Mnemosyne is the goddess of memory in Greek mythology.

## Project

Brief project description

For detailed information please visit [Wiki](https://github.com/swe-ms-boun/2018fall-swe574-g1/wiki)

## Supported Annotation Models

Annotation server is developed within the project but it lives independently from the application.
Thus, any annotation that are compliant with the following structure as defined in W3C model, will be able to stored in an annotation-server. 


For Media (Image) Annotation (Example 4 - W3C):
```json
{
  "@context": "http://www.w3.org/ns/anno.jsonld",
  "id": "http://example.org/anno4",
  "type": "Annotation",
   "generator": {
    "id": "http://example.org/client1",
    "type": "Software",
    "name": "Code v2.1",
    "homepage": "http://example.org/client1/homepage1"
  },
  "body": "http://example.org/description1",
  "target": {
    "id": "http://example.com/image1#xywh=100,100,300,300",
    "type": "Image",
    "format": "image/jpeg"
  }
}
```

For Textual Annotation (Example 23 - WC3):
```json
{
  "@context": "http://www.w3.org/ns/anno.jsonld",
  "id": "http://example.org/anno23",
  "type": "Annotation",
  "generator": {
    "id": "http://example.org/client1",
    "type": "Software",
    "name": "Code v2.1",
    "homepage": "http://example.org/client1/homepage1"
  },
  "body": "http://example.org/comment1",
  "target": {
    "source": "http://example.org/page1",
    "selector": {
      "type": "TextQuoteSelector",
      "exact": "anotation",
      "prefix": "this is an ",
      "suffix": " that has some"
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
https://github.com/swe-ms-boun/2018fall-swe574-g1/wiki/6.1-Database-Design
