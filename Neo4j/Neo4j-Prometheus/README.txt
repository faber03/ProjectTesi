1) installare prometheus operator
oc apply -f /PrometheusOperator/bundle.yaml

2) deployare l'applicazione da monitorare
oc apply -f /yaml/my-deploy-neo4j.yaml

3) deployare i componenti di prometheus operator per il monitoraggio
oc apply -f /yaml/my-deploy-prometheus.yaml