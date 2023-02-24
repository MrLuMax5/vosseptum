# vosseptum Project

## Overview

This project is used as a learning project for Dev-Ops methods, Cloud-nativity and Deployment.
It is not thought to have a particularly pretty frontend, or to be used in practice.
It does, however, combine and include some functionalities that are of interest in the deployment of software:

1. An Angular Frontend
2. A [Quarkus](https://quarkus.io/) Backend
3. Database-Connections

Quarkus is used in order to explore its strengths and weaknesses compared to Spring.

### History

Originally, as to be found in the initial commit, this project was build with docker-compose and consisted of 4 distinct images:

1. An Angular Frontend image
2. A Quarkus Backend image
3. A Postgres image
4. A MongoDB image

After building the project using `docker compose up`, 4 containers were started, each on their own port, and an end-user was
able to interact with the website via port _9000_, while the backend sat on port _8080_.
The initial project also used two database-connections and different paradigms (Postgres and Mongo), mainly to explore
the differences between those paradigms and how a transition from one to the other would feel like.

However, the focus of this project shifted away from databases to exploring the Cloud, 
which is why I decided to remove the MongoDB-integration in order to concentrate on Google's [CloudSQL](https://cloud.google.com/sql) functionality.

Now, instead of 4 distinct images, the build only uses 1: An [uber-jar](https://blog.payara.fish/what-is-a-java-uber-jar) gets created that contains both the backend-services
and the frontend-code, and the database is not set up on its own but instead connected to a Cloud-hosted database.
It has to be mentioned that Quarkus offers the neat possibilities of

* Using different [profiles](https://antoniogoncalves.org/2019/11/07/configuring-a-quarkus-application-with-profiles/) for production and development
* [Dev Services](https://quarkus.io/guides/datasource#dev-services) for ad-hoc, zero-config, containerized databases

This allows me to deploy the project using the connection to a Cloud-hosted database, while development is fast
with a local on-demand db.
This is currently necessary, because a bug exists where it is not possible to use Postgres Socket Factory in dev mode (see [here](https://github.com/quarkusio/quarkus/issues/15782)).

## Building, Running, Packaging

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

The application can be packaged using:

```shell script
./mvnw package
```

The application, packaged as an _uber-jar_, is now runnable using `java -jar service/target/*-runner.jar`.

### Deploying in the Cloud

1. Create a Google Cloud Account, Cloud-SQL Postgres
2. Build the docker-image via Google Cloud Build, by using the Dockerfile
3. Deploy the image that is found in your Container Registry. Remember to set the environment variables for the application.properties
4. It is easiest to deploy the application to Cloud Run. They even offer some computational resources for free. Here, you can enable the Cloud SQL API for direct connection.

## Useful Resources

* [Quarkus with Google Cloud](https://quarkus.io/guides/deploying-to-google-cloud)
* [Quarkus uber-jar with Maven](https://quarkus.io/guides/maven-tooling#uber-jar-maven)
* [Create and connect to Google Cloud SQL](https://cloud.google.com/sql/docs/postgres/connect-instance-auth-proxy)