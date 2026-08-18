# syntax=docker/dockerfile:1.7

# ============================================================
# Build stage
# ============================================================
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /workspace

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# COPY gradle.properties .

RUN chmod +x gradlew

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew dependencies --no-daemon

COPY src src

# Build aplikacji
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar --no-daemon

# ============================================================
# Runtime stage
# ============================================================
FROM eclipse-temurin:25-jre AS runtime

WORKDIR /app

# Spring Boot / Java nie potrzebują root
RUN groupadd --system spring && \
    useradd --system --gid spring spring

COPY --from=builder /workspace/build/libs/*.jar app.jar

RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

# JVM ustawienia odpowiednie dla kontenera
ENV JAVA_TOOL_OPTIONS="\
-XX:+UseG1GC \
-XX:MaxRAMPercentage=75 \
-XX:InitialRAMPercentage=25 \
-XX:+ExitOnOutOfMemoryError \
-Djava.security.egd=file:/dev/./urandom \
"

ENTRYPOINT ["java", "-jar", "app.jar"]