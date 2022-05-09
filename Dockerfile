FROM openjdk:11
ARG JAR_FILE=build/libs/mimeli-1.0.0.jar
COPY ${JAR_FILE} mimeli-1.0.jar
ENTRYPOINT ["java", "-jar", "/mimeli-1.0.jar"]