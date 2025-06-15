FROM gradle:jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar

FROM eclipse-temurin:21-jre-jammy
RUN groupadd -r appuser && useradd -r -g appuser appuser \
    && mkdir -p /home/appuser && chown -R appuser:appuser /home/appuser
WORKDIR /app
ENV JAR_NAME=device-management.jar
ENV SERVER_PORT=8080
COPY --from=build /app/build/libs/$JAR_NAME .
USER appuser
HEALTHCHECK --interval=15s --timeout=5s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:$SERVER_PORT/actuator/health | grep 'UP' || exit 1
EXPOSE $SERVER_PORT
CMD ["sh", "-c", "java -jar $JAR_NAME"]
