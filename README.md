## Build instructions
Gradle (via the wrapper, `./gradlew`) fetches dependencies, builds the frontend bundles (`src/main/webapp/dist`) with node/grunt, and assembles the war file. JDK 17+ is required.
```
# Continuous build (rebuilds on file change)
./gradlew war -t
```

### Docker
The Dockerfile is multi-stage: it builds the war with Gradle and deploys it to a Tomcat 9 image, so no local Java or node toolchain is needed.
```
# Construct docker image
docker build -t maayanlab/g2n:latest .

# Run locally (http://localhost:8080/G2N/)
docker run --rm -p 8080:8080 maayanlab/g2n:latest
```
