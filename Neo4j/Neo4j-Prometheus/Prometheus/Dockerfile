FROM prom/prometheus

USER 1000:1000

ADD prometheus.yml /etc/prometheus/

RUN chmod -R 777 /data/prometheus

EXPOSE 9090