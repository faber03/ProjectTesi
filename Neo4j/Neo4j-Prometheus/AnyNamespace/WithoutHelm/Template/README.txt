1) installare prometheus operator (con ServiceAccount)
oc process -f prometheus_operator_tmp.yaml | oc apply -f-

2) deployare l'applicazione da monitorare (NAME = sampleapp)
oc process -f sampleapp_tmp.yaml | oc apply -f-

3) deployare l'applicazione da scalare (NAME = sampleapp2)
oc process -f sampleapp_tmp.yaml | oc apply -f-

4) deployare prometheus (APP_TO_MONITOR_NAME = sampleapp)
oc process -f prometheus_tmp.yaml | oc apply -f-

5) deployare prometheus adapter
oc process -f prometheus_adapter_tmp.yaml | oc apply -f-

6) verificare api esposta:
oc get --raw "/apis/custom.metrics.k8s.io/v1beta1/namespaces/default/pods/*/nginx_http_requests_per_second"

6) deployare l' HorizontalPodAutoscaler (APP_TO_SCALE_NAME = sampleapp2)
oc process -f hpa_tmp.yaml | oc apply -f-