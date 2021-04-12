#!/usr/bin/env bash

command=$1
namespace=tesi-delucia
app_to_scale=kafka-connect-neo4j-cp-kafka-connect-neo4j
min_replicas=1
max_replicas=3
target_metrica_value=5.988

sed -i "s/namespace: NAMESPACE/namespace: ${namespace}/" hpa_tmp.yaml
sed -i "s/name: APP_TO_SCALE/name: ${app_to_scale}/" hpa_tmp.yaml
sed -i "s/minReplicas: MIN_REPLICAS/minReplicas: ${min_replicas}/" hpa_tmp.yaml
sed -i "s/maxReplicas: MAX_REPLICAS/maxReplicas: ${max_replicas}/" hpa_tmp.yaml
sed -i "s/targetValue: TARGET_METRICA_VALUE/targetValue: ${target_metrica_value}/" hpa_tmp.yaml

oc ${command} -f hpa_tmp.yaml

sed -i "s/namespace: ${namespace}/namespace: NAMESPACE/" hpa_tmp.yaml
sed -i "s/name: ${app_to_scale}/name: APP_TO_SCALE/" hpa_tmp.yaml
sed -i "s/minReplicas: ${min_replicas}/minReplicas: MIN_REPLICAS/" hpa_tmp.yaml
sed -i "s/maxReplicas: ${max_replicas}/maxReplicas: MAX_REPLICAS/" hpa_tmp.yaml
sed -i "s/targetValue: ${target_metrica_value}/targetValue: TARGET_METRICA_VALUE/" hpa_tmp.yaml

