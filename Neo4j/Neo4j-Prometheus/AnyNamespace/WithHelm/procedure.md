Apply the clusterroles_binding, as a cluster-admin, so that the tiller service account can create the clusterroles.

Keep the pod security policies, the pod disruption budget, the tls and the cert manager to false.

In values, select the name for the service account.


To create as as a cluster-admin
- custom-metrics-cluster-role.yaml
- custom-metrics-cluster-role.binding.yaml
- external-metrics-cluster-role.yaml
- external metrics-cluster-role-binding.yaml
- resource-metrics-cluster-role.yaml
- resource-metrics-cluster-role-binding.yaml
- role-binding-auth-reader.yaml

