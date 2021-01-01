FROM neo4j:enterprise

# ENV MYSQL_ROOT_PASSWORD DEFAULT_PASS
ENV NEO4J_AUTH=none
ENV NEO4J_ACCEPT_LICENSE_AGREEMENT=yes

#cat ./conf/neo4j.conf | grep dbms.connector.bolt
#RUN sed -i 's/#dbms.connector.bolt.listen_address=:7687/dbms.connector.bolt.listen_address=0.0.0.0:7687/' ./conf/neo4j.conf
#RUN sed -i 's/#dbms.connector.http.listen_address=:7474/dbms.connector.http.listen_address=0.0.0.0:7474/' ./conf/neo4j.conf
#RUN sed -i 's/dbms.connector.https.enabled=false/dbms.connector.https.enabled=true/' ./conf/neo4j.conf
RUN sed -i 's/#dbms.default_database=neo4j/dbms.default_database=neo4j/' ./conf/neo4j.conf

#RUN sed -i 's/#dbms.connector.bolt.tls_level=DISABLED/dbms.connector.bolt.tls_level=OPTIONAL/' ./conf/neo4j.conf 

EXPOSE 7474
EXPOSE 7687

#USER neo4j

#ENTRYPOINT mysqld --initialize --user=mysql && mysqld --skip-symbolic-links