# builder image
FROM gradle:8.1.1-jdk17-focal AS build
WORKDIR /home/build
COPY . .
RUN gradle assemble


# base image
FROM  amazoncorretto:17-alpine-jdk

# args
ARG VERSION="*"

# labels
LABEL maintainer = "Eugene Shmorgun <eugen.shmorgun@gmail.com>"
LABEL name = "Pizza manager"
LABEL version = "${VERSION}"


# user and workdir
RUN addgroup -g 1001 docker \
 && adduser -h /home/docker -D -G docker -u 1001 docker
USER docker
WORKDIR /home/docker


# copy files
COPY --from=build --chown=docker:docker /home/build/build/libs/manager-0.0.1-SNAPSHOT.jar service.jar

# expose and entrypoint
EXPOSE 9090

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/home/docker/service.jar"]
