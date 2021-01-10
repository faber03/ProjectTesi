FROM prom/prometheus

#SHELL ["/bin/bash", "-c"]

USER 1000:65534

#ADD prometheus.yml /etc/prometheus/
#ADD prometheus.yml /path/to/config

#RUN chown -R 1000170000 /etc/prometheus/*
#RUN chmod -R 755 /etc/prometheus/*

RUN chown -R 1000:65534 /etc/prometheus/*
RUN chmod -R 755 /etc/prometheus/*

EXPOSE 9090