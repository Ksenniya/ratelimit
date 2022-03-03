FROM adoptopenjdk/openjdk11:latest
RUN mkdir -p /app/
ADD ratelimit-0.0.1-SNAPSHOT.jar /app/ratelimit.jar
COPY application.yaml /app/application.yaml
ENTRYPOINT ["java","-jar","/app/ratelimit.jar", "--spring.config.location=/app/application.yaml"]

