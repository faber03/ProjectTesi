1) posizionarsi nella cartella ./Neo4j/Neo4j-Prometheus
cd ./Neo4j/Neo4j-Prometheus

2) installare prometheus operator in namespace "default"
oc apply -f ./PrometheusOperator/bundle.yaml

3) deployare l'applicazione da scalare in namespace "default"
oc apply -f ./AutoscalingNeo4j/my-deploy-sampleapp.yaml

4) deployare l'applicazione da monitorare in namespace "default"
oc apply -f ./AutoscalingNeo4j/my-deploy-neo4j.yaml

5) deployare l'applicazione che condiziona la metrica letta dall'applicazione da monitorare
oc process -f ./AutoscalingNeo4j/my-deploy-neo4jConnector-template.yml | oc apply -f-

6) deployare i componenti di prometheus operator per il monitoraggio in namespace "default"
oc apply -f ./AutoscalingNeo4j/my-deploy-prometheus.yaml

7) verificare dall'interfaccia web di prometheus che questo monitori correttamente l'applicazione
http://prometheus-default.172.16.0.10.nip.io (n.b. accedere alla route tramite la web console di Openshift)

8) deployare prometheus adapter in namespace "default"
oc apply -f ./AutoscalingNeo4j/my-deploy-prometheus-adapter.yaml

9) verificare api esposta:
oc get --raw "/apis/custom.metrics.k8s.io/v1beta1/namespaces/default/pods/*/neo4j_neo4j_transaction_active

10) associare al servizio monitorato l' HorizontalPodAutoscaler agganciato alla metrica esposta dal
prometheus adapter:
oc apply -f ./AutoscalingNeo4j/my-deploy-hpa.yaml