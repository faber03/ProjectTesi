#!/usr/bin/env bash

command=$1
namespace=tesi-delucia

sed -i "s/namespace: NAMESPACE/namespace: ${namespace}/" neo4j_metrics_service_tmp.yaml

oc ${command} -f neo4j_metrics_service_tmp.yaml

sed -i "s/namespace: ${namespace}/namespace: NAMESPACE/" neo4j_metrics_service_tmp.yaml