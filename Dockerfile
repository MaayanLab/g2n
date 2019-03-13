FROM library/tomcat:8-jre8

COPY build/libs/G2N*.war webapps/G2N.war

RUN apt-get update && apt-get install -y libtcnative-1 vim && apt-get clean