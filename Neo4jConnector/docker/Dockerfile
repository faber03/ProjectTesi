#parto da un'immagine di java
FROM openjdk:latest

#aggiungo il jar creato tramite MAVEN nella folder principale del container
ADD jar/neo4jConnector-1.0.jar neo4jConnector-1.0.jar

#espongo la porta 8085
#EXPOSE 8085

#comandi per l'esecuzione del jar caricato
ENTRYPOINT ["java", "-jar", "neo4jConnector-1.0.jar"]