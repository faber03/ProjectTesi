#!/usr/bin/env bash

namespace=$1
user=$2

#Set namespace
sed -i "s/.*namespace:.*/    namespace: ${namespace}/g" crb.yaml
sed -i "s/.*namespace:.*/    namespace: ${namespace}/g" prometheus_cr.yaml
sed -i "s/.*namespace:.*/    namespace: ${namespace}/g" prometheus_adapter_cr.yaml

#Set user
sed -i "/kind: User/{n;g;s/.*/    name: ${user}/}" prometheus_adapter_cr.yaml

#Set names
sed -i "31s/.*/  name: ${namespace}-${user}/g" prometheus_cr.yaml

sed -i "39s/.*/  name: custom-metrics-${namespace}-${user}:system:auth-delegator/g" prometheus_adapter_cr.yaml
sed -i "54s/.*/  name: custom-metrics-auth-reader-${namespace}-${user}/g" prometheus_adapter_cr.yaml
sed -i "55s/.*/  namespace: ${namespace}/g" prometheus_adapter_cr.yaml
sed -i "72s/.*/  name: custom-metrics-resource-reader-${namespace}-${user}/g" prometheus_adapter_cr.yaml
sed -i "87s/.*/  name: hpa-controller-custom-metrics-${namespace}-${user}/g" prometheus_adapter_cr.yaml

sed -i "8s/.*/  name: prometheus-operator-sm-${namespace}-${user}/g" crb.yaml
sed -i "28s/.*/  name: prometheus-operator-amg-${namespace}-${user}/g" crb.yaml
sed -i "48s/.*/  name: prometheus-operator-am-${namespace}-${user}/g" crb.yaml
sed -i "68s/.*/  name: prometheus-operator-pm-${namespace}-${user}/g" crb.yaml
sed -i "88s/.*/  name: prometheus-operator-p-${namespace}-${user}/g" crb.yaml
sed -i "108s/.*/  name: prometheus-operator-pro-${namespace}-${user}/g" crb.yaml
sed -i "128s/.*/  name: prometheus-operator-prorules-${namespace}-${user}/g" crb.yaml
sed -i "148s/.*/  name: prometheus-operator-thanos-${namespace}-${user}/g" crb.yaml
sed -i "168s/.*/  name: prometheus-operator-cm-${namespace}-${user}/g" crb.yaml
sed -i "188s/.*/  name: prometheus-operator-ss-${namespace}-${user}/g" crb.yaml
sed -i "208s/.*/  name: prometheus-operator-i-${namespace}-${user}/g" crb.yaml

oc login master.licit.local:8443 --username=licit --password=licit

oc project prova-operator

oc apply -f cr.yaml; oc apply -f crb.yaml;

oc apply -f prometheus_cr.yaml; oc apply -f prometheus_adapter_cr.yaml