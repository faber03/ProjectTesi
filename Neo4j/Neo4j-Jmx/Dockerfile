FROM neo4j:4.1.3-enterprise

#ENV MYSQL_ROOT_PASSWORD DEFAULT_PASS
#ENV NEO4J_AUTH=none
ENV NEO4J_ACCEPT_LICENSE_AGREEMENT=yes
ENV NEO4J_AUTH=neo4j/password
ENV NEO4J_dbms_connector_bolt_address=0.0.0.0:7687
#ENV SECURE_FILE_PERMISSIONS=yes
#ENV NEO4J_dbms_connector_bolt_address=0.0.0.0:7687

SHELL ["/bin/bash", "-c"]

#USER root
#RUN chmod -R 777 /var/lib/neo4j

#RUN whoami

ADD neo4j.conf ${HOME}/conf/
ADD jmx.password /var/lib/neo4j/conf/
ADD jmx.access /var/lib/neo4j/conf/
RUN chown 1000170000 /var/lib/neo4j/conf/jmx.password
#./conf/jmx.password
RUN chmod 0600 /var/lib/neo4j/conf/jmx.password
#./conf/jmx.password

#ADD neo4j.conf /var/lib/neo4j/conf/
#ADD jmx.password /var/lib/neo4j/conf/
#RUN sed -i 's/monitor Neo4j/monitor password/' /var/lib/neo4j/conf/jmx.password

#RUN rm -f ${HOME}/conf/neo4j.conf
#ADD neo4j.conf ${HOME}/conf/

#http
EXPOSE 7474
#bolt
EXPOSE 7687
#https
EXPOSE 7473
#jmx
EXPOSE 3637

#port forwarding from oc
#oc port-forward <neo4j pod name> 7474:7474 7687:7687 7473:7473 3637:3637