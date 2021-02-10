1) installare prometheus operator in namespace "default"

    oc apply -f ./PrometheusOperator/bundle.yaml

2) deployare l'applicazione da monitorare in namespace "default"

    oc apply -f ./AutoscalingSampleApp/my-deploy-sampleapp.yaml

3) deployare i componenti di prometheus operator per il monitoraggio in namespace "default"

    oc apply -f ./AutoscalingSampleApp/my-deploy-prometheus.yaml

4) deployare prometheus adapter in namespace "default"

    custom:
        oc apply -f ./AutoscalingSampleApp/my-deploy-prometheus-adapter.yaml
    external:
        helm install --name prometheus-adapter ./AutoscalingSampleApp/prometheus-adapter-helm-chart/prometheus-adapter

5) verificare api esposta:

    custom:
        oc get --raw "/apis/custom.metrics.k8s.io/v1beta1/namespaces/default/pods/*/nginx_http_requests_per_second"
    external:
        oc get --raw "/apis/external.metrics.k8s.io/v1beta1/namespaces/default/nginx_http_requests_per_second"

6) associare al servizio monitorato l' HorizontalPodAutoscaler agganciato alla metrica esposta dal prometheus adapter:

    custom:
        oc apply -f ./AutoscalingSampleApp/my-deploy-hpa.yaml
    external:
        