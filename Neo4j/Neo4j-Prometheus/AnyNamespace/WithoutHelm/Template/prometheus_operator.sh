#!/usr/bin/env bash

command=$1
namespace=tesi-delucia
userid=1000330000

sed -i "s/namespace: NAMESPACE/namespace: ${namespace}/" prometheus_operator_tmp.yaml
sed -i "s/runAsUser: USER_ID/runAsUser: ${userid}/" prometheus_operator_tmp.yaml

oc ${command} -f prometheus_operator_tmp.yaml

sed -i "s/namespace: ${namespace}/namespace: NAMESPACE/" prometheus_operator_tmp.yaml
sed -i "s/runAsUser: ${userid}/runAsUser: USER_ID/" prometheus_operator_tmp.yaml