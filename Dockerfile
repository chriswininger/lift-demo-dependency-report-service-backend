FROM docker-all.repo.sonatype.com/openjdk:17-jdk-slim

ENV DRS_API_PORT=4444

ENV DRS_DB_URL="jdbc:postgresql://localhost:5436/dep_rep_serv"
ENV DRS_DB_USER_NAME="drs"
ENV DRS_DB_PASS_WORD="xxx"
# TODO: This exists to allow the local docker-compose stack time to wait on the db, there are better approaches
ENV DRS_SLEEP_FOR_DB=0

ADD ./ /service-src
WORKDIR /service-src
RUN ls -la
RUN ./mvnw clean package


RUN mv /service-src/target /service
RUN mv /service-src/scripts /scripts
RUN chmod -R +x /scripts

RUN rm -rf /service-src

WORKDIR /service
ENTRYPOINT echo "db url: $DRS_DB_URL" && \
  sleep $DRS_SLEEP_FOR_DB && \
  export DRS_BACKEND_JAR_NAME="$(/scripts/find-jar.sh)" && \
  echo "using jar: $DRS_BACKEND_JAR_NAME" && \
  DRS_DB_URL="$DRS_DB_URL"  java -jar /service/$DRS_BACKEND_JAR_NAME

EXPOSE ${DRS_API_PORT}
