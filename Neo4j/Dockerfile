FROM neo4j:4.1.3-enterprise

#ENV MYSQL_ROOT_PASSWORD DEFAULT_PASS
#ENV NEO4J_AUTH=none
ENV NEO4J_ACCEPT_LICENSE_AGREEMENT=yes
ENV NEO4J_AUTH=neo4j/password
ENV NEO4J_dbms_connector_bolt_address=0.0.0.0:7687
#ENV SECURE_FILE_PERMISSIONS=yes
#ENV NEO4J_dbms_connector_bolt_address=0.0.0.0:7687

USER root
#RUN chmod -R 777 /var/lib/neo4j

ADD neo4j.conf ${HOME}/conf/
ADD jmx.password ${HOME}/conf/
#RUN chown root ./conf/jmx.password
RUN chmod 600 ./conf/jmx.password

#ADD neo4j.conf /var/lib/neo4j/conf/
#ADD jmx.password /var/lib/neo4j/conf/
#RUN sed -i 's/monitor Neo4j/monitor password/' /var/lib/neo4j/conf/jmx.password

#RUN rm -f ${HOME}/conf/neo4j.conf
#ADD neo4j.conf ${HOME}/conf/

EXPOSE 7474
EXPOSE 7687
EXPOSE 7473
#EXPOSE 3637
#EXPOSE 22

#USER neo4j
#oc port-forward neo4j-1-26674 7474:7474 7687:7687 7473:7473 3637:3637 