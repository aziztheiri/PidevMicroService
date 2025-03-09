FROM openjdk:17
EXPOSE 8083
ADD target/PidevMicroService-0.0.1-SNAPSHOT.jar user.jar
ENTRYPOINT ["java", "-jar","user.jar"]