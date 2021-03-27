FROM openjdk:8-jre-slim
#FROM openjdk:8
#FROM openjdk:11-jre-slim

MAINTAINER woland7

RUN apt-get clean && \
    apt-get update && \
    apt-get -qy install --no-install-recommends\
    wget \
    telnet \
    iputils-ping \
    unzip \
    curl \
    fontconfig libfreetype6

#Install JMeter

RUN mkdir /jmeter \
    && cd /jmeter

WORKDIR /jmeter

#https://downloads.apache.org//jmeter/binaries/apache-jmeter-5.4.1.tgz
#http://it.apache.contactlab.it//jmeter/binaries/apache-jmeter-5.4.1.tgz
#RUN   wget http://downloads.apache.org//jmeter/binaries/apache-jmeter-5.4.1.tgz \
#      && tar -xzf apache-jmeter-5.4.1.tgz \
#      && rm apache-jmeter-5.4.1.tgz


ENV JMETER_HOME /jmeter/apache-jmeter-5.4.1

ENV PATH $JMETER_HOME/bin:$PATH

EXPOSE 8080
EXPOSE 80

ADD apache-jmeter-5.4.1 apache-jmeter-5.4.1

#ADD lib apache-jmeter-5.4.1/lib
#ADD ext apache-jmeter-5.4.1/lib/ext
#ADD bin apache-jmeter-5.4.1/bin

#COPY test.jmx test.jmx
COPY startup.sh startup.sh
COPY testplan.jmx testplan.jmx
RUN mkdir samples && chmod -R 777 samples
ADD samples samples
#COPY microservice-admin_ConfC.jmx microservice-admin_ConfC.jmx
#COPY microservice-user_ConfC.jmx microservice-user_ConfC.jmx
#COPY Monolith_ConfC.jmx Monolith_ConfC.jmx

RUN chmod 777 -R /jmeter

ENTRYPOINT ["./startup.sh"]
