1) installare prometheus operator in namespace "default"
oc apply -f ./PrometheusOperator/bundle.yaml

2) deployare l'applicazione da monitorare in namespace "default"
oc apply -f ./yaml/my-deploy-neo4j.yaml
oc apply -f ./yaml/my-deploy-sampleapp.yaml

3) deployare i componenti di prometheus operator per il monitoraggio in namespace "default"
oc apply -f ./yaml/my-deploy-prometheus.yaml

4) deployare prometheus adapter in namespace "default"
oc apply -f ./yaml/my-deploy-prometheus-adapter.yaml

5) verificare api esposta:
oc get --raw "/apis/custom.metrics.k8s.io/v1beta1/namespaces/default/pods/*/nginx_http_requests_per_second

6) associare al servizio monitorato l' HorizontalPodAutoscaler agganciato alla metrica esposta dal
prometheus adapter:
oc apply -f ./yaml/my-deploy-hpa.yaml