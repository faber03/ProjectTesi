## Neo4jConnector

Una semplice JavaApplication che realizza un client Neo4j di test. 
Apre una connessione con il DBMS, crea un nodo e ne aggiorna continuamente un'attributo.

La cartella Neo4jConnector contiene sia il codice della JavaApplication, sia i file .yaml e il Dockerfile per il deploy in Openshift.

## JmxMonitor

Una JavaApplication che realizza un Client JMX per Neo4j. 
Neo4j espone metriche che ne consentono il monitoraggio, attraverso un MBeanServer.
Tramite connettore RMI, l'applicazione apre una connessione all' MBeanServer di Neo4j, 
crea un proxy ad all'MBean di una metrica specifica e continuamente legge e stampa il valore di questa metrica.

La cartella JmxMonitor contiene sia il codice della JavaApplication, sia i file .yaml e il Dockerfile per il deploy in Openshift. in Openshift.

##Neo4j

All'interno della sottocartella "Neo4j-Jmx" è presente il Dockerfile ed il file .yaml per il deploy in Openshift
di Neo4j con abilitazione all'esportazione delle metriche tramite JMX.

la sottocartella "Neo4j-Prometheus/Neo4j" continene il Dockerfile per il deploy in Openshift
di Neo4j con abilitazione all'esportazione delle metriche tramite Prometheus endpoint.

la sottocartella "Neo4j-Prometheus/Prometheus" continene il Dockerfile per il deploy di Prometheus 
configurato per la lettura delle metriche dall'endpoint di Neo4j.

la sottocartella "Neo4j-Prometheus/PrometheusOperator" contiene il bundle per l'installazione del
Prometheus Operator in Openshift

All'interno della sottocartella "Neo4j-Prometheus/yaml" sono presenti i file .yaml per il deploy in Openshift
di una serie di componenti per il test dell'autoscaling di un'applicazione sulla base di una metrica custom.
Il file README.txt contiene le info sugli step da seguire per il deploy in Openshift.


