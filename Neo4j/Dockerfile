FROM neo4j

#ENV MYSQL_ROOT_PASSWORD DEFAULT_PASS
#ENV NEO4J_AUTH=none
ENV NEO4J_ACCEPT_LICENSE_AGREEMENT=yes
ENV NEO4J_AUTH=neo4j/password
#ENV SECURE_FILE_PERMISSIONS=yes
#ENV NEO4J_dbms_connector_bolt_address=0.0.0.0:7687

#USER root
#RUN chmod -R 777 /var/lib/neo4j

#RUN rm -f ${HOME}/conf/neo4j.conf
#ADD neo4j.conf ${HOME}/conf/

EXPOSE 7474
EXPOSE 7687

#USER neo4j

#ENTRYPOINT ["neo4j", "start"]