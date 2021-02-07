The following operations must be made only once as a cluster admin:

- In order to deploy the operator on OpenShift v3.10, you should change the API version to v1beta1
- As a cluster admin, create the custom resources definitions, the following resources will be created:
    - alertmanagerconfigs
    - alertmanagers
    - podmonitors
    - probes
    - prometheuses
    - prometheusrules
    - servicemonitors
    - thanosrulers
- Again as a cluster admin, create the custom cluster roles to manage the CRD and bind them to the proper users
and service accounts in the proper namespace **oc apply -r cr.yaml; oc apply -f crb.yaml**

The following operations can be executed by any user, granted all the permissions have been correctly set:
(*Change all the namespaces accordingly, in case you ara copypasting*)
- Change the user ID of the SCC to be at least 1000610000
- Create the serviceaccount **oc apply -f promtheus_operator_serviceaccount.yaml**
- Create the prometheus operator **oc apply -f prometheus_operator.yaml**
- Then deploy the application to be monitored **oc apply -f sampleapp.yaml**
- Then use the operator to deploy an instance of prometheus **oc apply -f prometheus.yaml**
    - Before deploying it, as a cluster admin set the proper permissions **oc apply -f prometheus_cr.yaml**
- Then deploy the prometheus adapter **oc apply -f prometheus_adapter.yaml**
    - Before deploying it, as a cluster admin set the proper permissions **oc apply -f prometheus_adapter_cr.yaml**
- Deploy the autoscaler **oc apply -f hpa.yaml**

**TODO make into a template so that namespaces and users and services accounts can be parameterized**
