# 1. Imagen base Java
FROM eclipse-temurin:17-jdk-alpine

# 2. Declaramos un argumento de construcción con valor por defecto
ARG BACKEND_PORT=8081

# 3. Convertimos ese ARG en variable de entorno persistente
ENV BACKEND_PORT=${BACKEND_PORT}

ARG SERVER_CONTEXT_PATH=/
ENV SERVER_CONTEXT_PATH=${SERVER_CONTEXT_PATH}

# 4. Directorio de trabajo
WORKDIR /app

# 5. Copiamos el JAR
COPY target/*.jar app.jar

# 6. Exponemos el mismo puerto que hemos fijado
EXPOSE ${BACKEND_PORT}

# 7. Arrancamos usando la variable
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --debug \
  --server.servlet.context-path=${SERVER_CONTEXT_PATH} \
  --vaadin.url-mapping=${SERVER_CONTEXT_PATH}/* \
  --vaadin.productionMode=true \
  --server.port=${BACKEND_PORT}"]