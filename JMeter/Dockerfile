FROM openjdk:8-jre-slim

MAINTAINER woland7

RUN apt-get clean && \
    apt-get update && \
    apt-get -qy install \
    wget \
    telnet \
    iputils-ping \
    unzip \
    curl

#Install JMeter

RUN mkdir /jmeter \
    && cd /jmeter

WORKDIR /jmeter

RUN   wget http://it.apache.contactlab.it//jmeter/binaries/apache-jmeter-5.2.1.tgz \
      && tar -xzf apache-jmeter-5.2.1.tgz \
      && rm apache-jmeter-5.2.1.tgz

ENV JMETER_HOME /jmeter/apache-jmeter-5.2.1

ENV PATH $JMETER_HOME/bin:$PATH

EXPOSE 8080
EXPOSE 80

#COPY test.jmx test.jmx
COPY startup.sh startup.sh
COPY microservice-admin_ConfC.jmx microservice-admin_ConfC.jmx
COPY microservice-user_ConfC.jmx microservice-user_ConfC.jmx
COPY Monolith_ConfC.jmx Monolith_ConfC.jmx

RUN chmod 777 -R /jmeter

ENTRYPOINT ["./startup.sh"]
