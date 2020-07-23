# BUILDING
FROM openjdk:8 as build-stage
RUN apt-get update && apt-get install -y maven
COPY . . 
RUN mvn clean install package

# RUNNING, only copying the created jar file
FROM openjdk:8-slim
COPY --from=build-stage target/ubuntu-package-viewer-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

RUN adduser app_user
USER app_user

EXPOSE 8081
CMD ["java", "-jar", "app.jar"]

# To build the image, run "docker build -t {IMAGE_NAME}"
# To run the docker image, run "docker run -p 8081:8081 {IMAGE_NAME}"
# Then go to localhost:8081/api/packages to view all packages.