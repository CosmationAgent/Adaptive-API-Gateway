# Quick Reference - Maven Commands

## Building the Project

```bash
# Clean and compile
mvn clean compile

# Build package (JAR)
mvn clean package

# Skip tests during build
mvn clean package -DskipTests

# Build without running tests but compile them
mvn clean package -Dmaven.test.skip=false -DskipTests
```

## Running the Application

```bash
# Run using Maven plugin
mvn spring-boot:run

# Run with development profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Run the JAR directly
java -jar target/adaptive-api-gateway-1.0.0.jar

# Run with specific profile
java -jar target/adaptive-api-gateway-1.0.0.jar --spring.profiles.active=dev
```

## Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TestControllerTest

# Run with coverage
mvn test jacoco:report
```

## Cleaning

```bash
# Clean build artifacts
mvn clean

# Clean and remove IDE files
mvn clean
rm -rf .idea *.iml
```

## IDE Setup

### IntelliJ IDEA
1. File → Open → Select `pom.xml`
2. Choose "Open as Project"
3. Wait for Maven to download dependencies
4. Run `AdaptiveApiGatewayApplication.java`

### VS Code
1. Install "Extension Pack for Java"
2. Install "Spring Boot Extension Pack"
3. Open project folder
4. Run using Spring Boot Dashboard or Debug view

### Eclipse
1. File → Import → Maven → Existing Maven Projects
2. Select project directory
3. Right-click project → Run As → Spring Boot App

## Useful Maven Commands

```bash
# Display dependency tree
mvn dependency:tree

# Check for dependency updates
mvn versions:display-dependency-updates

# Format code (if formatter plugin added)
mvn formatter:format

# Show effective POM
mvn help:effective-pom

# Analyze dependencies
mvn dependency:analyze
```

## Troubleshooting

### Port Already in Use
```bash
# Change port in application.properties
server.port=8081

# Or pass as command line argument
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Out of Memory
```bash
# Increase Maven memory
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=256m"

# Or run JAR with more memory
java -Xmx512m -jar target/adaptive-api-gateway-1.0.0.jar
```

### Clean Install Everything
```bash
mvn clean install -U
```
The `-U` flag forces update of snapshots and releases.

## Environment Variables

```bash
# Set Java version
export JAVA_HOME=/path/to/jdk-17

# Set Maven home (if not using wrapper)
export MAVEN_HOME=/path/to/maven
export PATH=$MAVEN_HOME/bin:$PATH
```

## Production Build

```bash
# Build optimized JAR
mvn clean package -Pprod

# Build with specific Spring profile
mvn clean package -Dspring.profiles.active=prod
```

## Docker Build (Future)

```bash
# Build Docker image
docker build -t adaptive-api-gateway:latest .

# Run container
docker run -p 8080:8080 adaptive-api-gateway:latest
```
