FROM amazoncorretto:21.0.3

ENV SERVICE_HOME_DIR /home/vscan-api
WORKDIR $SERVICE_HOME_DIR

RUN yum update -y && yum -y install shadow-utils.x86_64 && yum clean all

RUN groupadd -r vscan-api && useradd -r -s /bin/false -g vscan-api vscan-api
RUN chown -R vscan-api:vscan-api $SERVICE_HOME_DIR

COPY build/libs/vscan-api-0.0.1-SNAPSHOT.jar app.jar

USER vscan-api

ENTRYPOINT ["java", "-jar", "app.jar"]
