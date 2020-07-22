FROM openjdk:8
RUN apt-get update && apt-get install -y maven

WORKDIR /ubuntu-package-viewer
COPY . . 
RUN mvn clean install package

EXPOSE 8081
CMD ["java", "-jar", "target/ubuntu-package-viewer-1.0-SNAPSHOT-jar-with-dependencies.jar"]

# To build the image, run "docker build -t {IMAGE_NAME}"
# To run the docker image, run "docker run -p 8081:8081 {IMAGE_NAME}"
# Then go to localhost:8081/api/packages to view all packages.