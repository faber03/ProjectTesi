FROM prom/prometheus

#SHELL ["/bin/bash", "-c"]

USER root

#ADD prometheus.yml /etc/prometheus/
#ADD prometheus.yml /path/to/config

RUN chown -R 1000170000 /etc/prometheus/*
RUN chmod 755 /etc/prometheus/queries.active

EXPOSE 9090