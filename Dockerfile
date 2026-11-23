FROM gradle:8.14.3-jdk17 AS build
WORKDIR /src
COPY . .
RUN gradle --no-daemon clean war

FROM tomcat:9.0.124-jre25
RUN apt-get update \
 && apt-get upgrade -y \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/* \
 && groupadd --system --gid 10001 tomcat \
 && useradd --system --uid 10001 --gid tomcat --home-dir /usr/local/tomcat \
      --no-create-home --shell /usr/sbin/nologin tomcat \
 && mkdir -p /usr/local/tomcat/conf/Catalina \
 && chown -R tomcat:tomcat /usr/local/tomcat/webapps /usr/local/tomcat/work \
      /usr/local/tomcat/logs /usr/local/tomcat/temp /usr/local/tomcat/conf/Catalina
COPY --chown=tomcat:tomcat --from=build /src/build/libs/g2n*.war /usr/local/tomcat/webapps/G2N.war
ENV JAVA_OPTS="--enable-native-access=ALL-UNNAMED"
# numeric uid so Kubernetes runAsNonRoot can verify it
USER 10001:10001
EXPOSE 8080
CMD ["catalina.sh", "run"]
