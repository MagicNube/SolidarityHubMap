# 1. Imagen base Java
FROM eclipse-temurin:17-jdk-alpine

# 2. Declaramos build args (opcional para BACKEND_URL)
ARG FRONTEND_PORT=8080
ARG SERVER_CONTEXT_PATH=/
ARG BACKEND_URL

# 3. Lo pasamos a variables de entorno
ENV FRONTEND_PORT=${FRONTEND_PORT}
ENV SERVER_CONTEXT_PATH=${SERVER_CONTEXT_PATH}
ENV BACKEND_URL=${BACKEND_URL}

WORKDIR /app
COPY target/*.jar app.jar

# 4. Exponemos el puerto dinámico
EXPOSE ${FRONTEND_PORT}

# 5. Arrancamos pasando todos los parámetros a Spring Boot
ENTRYPOINT ["sh","-c", "\
  java -jar /app/app.jar \
    --server.port=${FRONTEND_PORT} \
    --vaadin.productionMode=true \
    --backend.url=${BACKEND_URL} \
    --debug \
"]
