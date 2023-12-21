FROM adoptopenjdk:11-jdk-hotspot AS builder
RUN apt-get update && \
    apt-get install -y curl

RUN apt-get update && apt-get install -y unzip
WORKDIR /gradle
RUN curl -L https://services.gradle.org/distributions/gradle-5.6.1-bin.zip -o gradle-5.6.1-bin.zip
RUN unzip gradle-5.6.1-bin.zip
ENV GRADLE_HOME=/gradle/gradle-5.6.1
ENV PATH=$PATH:$GRADLE_HOME/bin
RUN gradle --version

WORKDIR /app
COPY . .
RUN gradle clean build

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app-1.0.jar"]