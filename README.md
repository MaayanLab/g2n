## Build instructions
Gradle is used to fetch dependencies, build and debug the project. The two commands below in different terminals can be used to automatically rebuild/deploy the project to an embedded tomcat server for a continuous development experience.
```
# Continuous build (rebuilds on file change)
gradle build -t

# Tomcat development server (reflects file changes)
gradle tomcatRun
```

### Docker
Gradle is used to assemble a war file to be deployed with a tomcat8-base docker image.
```
# Build and assemble war file
gradle install

# Construct docker image
docker build -t maayanlab/g2n:latest .
```
