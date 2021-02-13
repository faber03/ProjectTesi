#!/usr/bin/env bash

command=$1
namespace=tesi-delucia

sed -i "s/namespace: NAMESPACE/namespace: ${namespace}/" prometheus_adapter_tmp.yaml
sed -i "s/prometheus-operated.NAMESPACE.svc:9090/prometheus-operated.${namespace}.svc:9090/" prometheus_adapter_tmp.yaml

oc ${command} -f prometheus_adapter_tmp.yaml

sed -i "s/namespace: ${namespace}/namespace: NAMESPACE/" prometheus_adapter_tmp.yaml
sed -i "s/prometheus-operated.${namespace}.svc:9090/prometheus-operated.NAMESPACE.svc:9090/" prometheus_adapter_tmp.yaml