FROM openjdk:17

WORKDIR /app/didacto/

ARG JAR_PATH=../build/libs/
ARG RESOURCES_PATH=../build/resources/main/

COPY ${JAR_PATH}/*.jar /app/didacto/didacto.jar

ARG SPRING_PROFILES_ACTIVE=dev
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE

ENTRYPOINT ["java", "-jar", "didacto.jar"]