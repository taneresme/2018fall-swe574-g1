# Mnemosyne

> Mnemosyne is the goddess of memory in Greek mythology.

## Project

Brief project description

For detailed information please visit [Wiki](https://github.com/swe-ms-boun/2018fall-swe574-g1/wiki)

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

