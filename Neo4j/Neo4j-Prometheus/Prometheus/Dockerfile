FROM prom/prometheus

#SHELL ["/bin/bash", "-c"]

USER root

ADD prometheus.yml /etc/prometheus/

RUN chmod -R 777 /data/prometheus

EXPOSE 9090