#!/usr/bin/env bash

command=$1
namespace=tesi-delucia
app_to_monitor_name=neo4j
app_to_monitor_port=metrics

sed -i "s/namespace: NAMESPACE/namespace: ${namespace}/" prometheus_tmp.yaml
sed -i "s/app: APP_TO_MONITOR_NAME/app: ${app_to_monitor_name}/" prometheus_tmp.yaml
sed -i "s/- port: APP_TO_MONITOR_PORT/- port: ${app_to_monitor_port}/" prometheus_tmp.yaml

oc ${command} -f prometheus_tmp.yaml

sed -i "s/namespace: ${namespace}/namespace: NAMESPACE/" prometheus_tmp.yaml
sed -i "s/app: ${app_to_monitor_name}/app: APP_TO_MONITOR_NAME/" prometheus_tmp.yaml
sed -i "s/- port: ${app_to_monitor_port}/- port: APP_TO_MONITOR_PORT/" prometheus_tmp.yaml