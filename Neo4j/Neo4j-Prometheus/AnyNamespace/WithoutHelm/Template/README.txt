1) installare prometheus operator (con ServiceAccount)
bash prometheus_operator.sh apply

2) deployare l'applicazione da monitorare
oc process -f neo4j_tmp.yaml | oc apply -f-

3) deployare l'applicazione da scalare
oc process -f neo4jConnector_tmp.yaml | oc apply -f-

4) deployare prometheus (APP_TO_MONITOR_NAME = neo4j)
bash prometheus.sh apply

5) deployare prometheus adapter
bash prometheus_adapter.sh apply

6) verificare api esposta:
oc get --raw "http://master.licit.local:8443/apis/external.metrics.k8s.io/v1beta1/namespaces/tesi-delucia/neo4j_neo4j_transaction_active"

7) deployare l' HorizontalPodAutoscaler (APP_TO_SCALE_NAME = neo4j-connector)
bash hpa.sh apply

8) verificare che l'autoscaler legga la metrica
oc describe hpa neo4j-connector-hpa
