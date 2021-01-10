FROM prom/prometheus

#SHELL ["/bin/bash", "-c"]

USER root

#ADD prometheus.yml /etc/prometheus/
#ADD prometheus.yml /path/to/config

RUN chown 1000170000 /etc/prometheus
RUN chmod -R 444 /etc/prometheus

EXPOSE 9090