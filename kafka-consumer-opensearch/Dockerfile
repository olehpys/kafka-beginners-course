FROM gradle:7.6-jdk17 as builder
WORKDIR /kafka-consumer-opensearch
COPY ./ .
RUN gradle clean build -x test

FROM amazoncorretto:17-alpine-jdk
ARG JAR_FILE=/kafka-consumer-opensearch/build/libs/kafka-consumer-opensearch.jar
COPY --from=builder ${JAR_FILE} app.jar
EXPOSE "8051"
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]