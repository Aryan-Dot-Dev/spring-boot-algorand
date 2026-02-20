FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY . .

# Give execution permission to Maven wrapper
RUN chmod +x mvnw

# Build inside container
RUN ./mvnw clean package -DskipTests

ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar target/*.jar"]
