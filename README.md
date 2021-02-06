# Neo4jConnector

Una semplice JavaApplication che realizza un client Neo4j di test. 
Il Client apre una connessione con il DBMS, crea un nodo e ne aggiorna continuamente un'attributo.

La cartella Neo4jConnector contiene sia il codice della JavaApplication, sia i file .yaml e il Dockerfile per il deploy 
in Openshift.

# JmxMonitor

Una JavaApplication che realizza un Client JMX di test per Neo4j. 
Neo4j espone metriche che ne consentono il monitoraggio, attraverso un MBeanServer.
Tramite connettore RMI, il Client apre una connessione JMX all' MBeanServer, 
crea un proxy all'MBean su cui è esposta una metrica specifica e continuamente legge e stampa in console il valore di 
questa metrica.

La cartella JmxMonitor contiene sia il codice della JavaApplication, sia i file .yaml e il Dockerfile per il deploy in 
Openshift.

# Neo4j

### Neo4j-Jmx
All'interno della sottocartella "__Neo4j-Jmx__" è presente il Dockerfile ed il file .yaml per il deploy in Openshift
di Neo4j con abilitazione all'esportazione delle metriche tramite JMX.

### Neo4j-Prometheus
La sottocartella "__Neo4j-Prometheus/Neo4j__" continene il Dockerfile per il deploy in Openshift
di Neo4j con abilitazione all'esportazione delle metriche tramite Prometheus endpoint.

La sottocartella "__Neo4j-Prometheus/Prometheus__" continene il Dockerfile per il deploy di Prometheus 
configurato per la lettura delle metriche dall'endpoint di Neo4j.

La sottocartella "__Neo4j-Prometheus/PrometheusOperator__" contiene il bundle per l'installazione del
Prometheus Operator in Openshift

All'interno della sottocartella "__Neo4j-Prometheus/AutoscalingSampleApp__" sono presenti i file .yaml per il deploy in Openshift
di una serie di componenti per il test dell'autoscaling di un'applicazione (sampleApp), sulla base di una metrica custom.
Il file README.txt contiene gli step da seguire per il deploy in Openshift.

La sottocartella "__Neo4j-Prometheus/Documentation__" contiene informazioni circa le operazioni di installazione e
deployment da effettuare in Openshift, per verificare l'autoscaling di applicazioni all'interno del _Kubernetes Cluster_, tramite
l'impiego di un _Custom Metric Api Service_ realizzato per mezzo di un _Prometheus Adapter_


