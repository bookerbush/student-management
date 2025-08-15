# Java 21 JDK base image
FROM eclipse-temurin:21-jdk

# Workdir inside the container
WORKDIR /app

# Copy Maven wrapper + pom
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Ensure mvnw is executable (fixes "Permission denied" on Linux)
RUN chmod +x mvnw

# Cache dependencies
RUN ./mvnw -B -q dependency:go-offline

# Copy the source
COPY src ./src

# Build the jar
RUN ./mvnw -B -q -DskipTests package

# Render routes traffic to PORT (usually 10000). Default to 8080 locally.
EXPOSE 8080
CMD ["sh","-c","java -Dserver.port=${PORT:-8080} -jar target/student-management-0.0.1-SNAPSHOT.jar"]
