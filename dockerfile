FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY /build/libs/*.jar /app/app.jar

ENV DB_PASS=secret
ENV DB_USER=root
ENV ZIPKIN_HOST=localhost
ENV ZIPKIN_HTTP=http
ENV ZIPKIN_PORT=9411

EXPOSE 8080
ENTRYPOINT ["java","-XX:+UseG1GC", "-Xmx350m", "-jar","/app/app.jar"]