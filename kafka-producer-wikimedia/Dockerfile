FROM gradle:7.6-jdk17 as builder
WORKDIR /kafka-producer-wikimedia
COPY ./ .
RUN gradle clean build -x test

FROM amazoncorretto:17-alpine-jdk
ARG JAR_FILE=/kafka-producer-wikimedia/build/libs/kafka-producer-wikimedia.jar
COPY --from=builder ${JAR_FILE} app.jar
EXPOSE "8050"
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]