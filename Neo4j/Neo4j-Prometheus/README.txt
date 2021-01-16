1) installare prometheus operator in namespace "default"
oc apply -f /PrometheusOperator/bundle.yaml

2) deployare l'applicazione da monitorare in namespace "default"
oc apply -f /yaml/my-deploy-neo4j.yaml

3) deployare i componenti di prometheus operator per il monitoraggio in namespace "default"
oc apply -f /yaml/my-deploy-prometheus.yaml