FROM neo4j:4.1.3-enterprise

ENV NEO4J_ACCEPT_LICENSE_AGREEMENT=yes
ENV NEO4J_AUTH=neo4j/password
ENV NEO4J_dbms_connector_bolt_address=0.0.0.0:7687

SHELL ["/bin/bash", "-c"]

ADD neo4j.conf ${HOME}/conf/

#http
EXPOSE 7474
#bolt
EXPOSE 7687
#https
EXPOSE 7473
#prometheus endpoint
#EXPOSE 2004
EXPOSE 8080