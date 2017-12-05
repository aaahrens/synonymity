FROM openjdk:8
COPY ./docker_jars/ /usr/src/app
WORKDIR /usr/src/app/
CMD ["java", "-jar","synonymity-1.0-SNAPSHOT.jar"]
EXPOSE 4567