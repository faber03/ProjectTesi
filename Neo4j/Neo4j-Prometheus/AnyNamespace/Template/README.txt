1) installare prometheus operator (con ServiceAccount)
oc process -f prometheus_operator_tmp.yaml | oc apply -f-

2) deployare l'applicazione da monitorare
oc process -f sampleapp_tmp.yaml | oc apply -f-

3) deployare prometheus
oc process -f prometheus_tmp.yaml | oc apply -f-

4) deployare prometheus adapter
oc process -f prometheus_adapter_tmp.yaml | oc apply -f-

5) verificare api esposta:
oc get --raw "/apis/custom.metrics.k8s.io/v1beta1/namespaces/default/pods/*/nginx_http_requests_per_second"

6) deployare l' HorizontalPodAutoscaler
oc process -f hpa_tmp.yaml | oc apply -f-