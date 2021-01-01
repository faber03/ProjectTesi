FROM neo4j:enterprise

# ENV MYSQL_ROOT_PASSWORD DEFAULT_PASS
ENV NEO4J_AUTH=none
ENV NEO4J_ACCEPT_LICENSE_AGREEMENT=yes
#ENV NEO4J_dbms_connector_bolt_address=0.0.0.0:7687

#cat ./conf/neo4j.conf | grep dbms.connector.bolt
#cat /logs/neo4j.log
#RUN sed -i 's/#dbms.connector.bolt.listen_address=:7687/dbms.connector.bolt.listen_address=0.0.0.0:7687/' ./conf/neo4j.conf
#RUN sed -i 's/#dbms.connector.http.listen_address=:7474/dbms.connector.http.listen_address=0.0.0.0:7474/' ./conf/neo4j.conf
#RUN sed -i 's/#dbms.ssl.policy.https.client_auth=NONE/dbms.ssl.policy.https.client_auth=NONE/' ./conf/neo4j.conf
#RUN sed -i 's/#dbms.ssl.policy.https.client_auth=NONE/dbms.ssl.policy.https.client_auth=NONE/' ./conf/neo4j.conf

#dbms.ssl.policy.https.client_auth=NONE

EXPOSE 7474
EXPOSE 7687
#EXPOSE 443

#USER neo4j

#ENTRYPOINT ["neo4j", "start"]
#mysqld --initialize --user=mysql && mysqld --skip-symbolic-links