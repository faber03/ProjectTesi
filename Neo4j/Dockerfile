FROM neo4j:latest

# ENV MYSQL_ROOT_PASSWORD DEFAULT_PASS
ENV NEO4J_AUTH=none

RUN sed -i 's/#dbms.connector.bolt.listen_address=:7687/dbms.connector.bolt.listen_address=0.0.0.0:7687/' ./conf/neo4j.conf 

EXPOSE 7474
#EXPOSE 7687

USER neo4j

#ENTRYPOINT mysqld --initialize --user=mysql && mysqld --skip-symbolic-links