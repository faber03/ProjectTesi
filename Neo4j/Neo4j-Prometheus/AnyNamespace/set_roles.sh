#!/usr/bin/env bash

namespace=$1
user=$2

#Set namespace
sed -i "s/.*namespace:.*/    namespace: ${namespace}/g" crb.yaml
sed -i "s/.*namespace:.*/    namespace: ${namespace}/g" prometheus_cr.yaml
sed -i "s/.*namespace:.*/    namespace: ${namespace}/g" prometheus_adapter_cr.yaml

#Set user
sed -i "/kind: User/{n;g;s/.*/    name: ${user}/}" prometheus_adapter_cr.yaml

oc login master.licit.local:8443 --username=licit --password=licit

oc apply -f cr.yaml; oc apply -f crb.yaml; oc apply -f; oc apply -f prometheus_cr.yaml; oc apply -f prometheus_adapter_cr.yaml