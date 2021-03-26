#!/bin/bash

jmeter -n -t testplan.jmx

#jmeter -n -t microservice-admin_ConfC.jmx -l results_microservice-admin_ConfC.jtl

#jmeter -n -t microservice-user_ConfC.jmx -l microservice-user_ConfC.jtl
#
#jmeter -n -t Monolith_ConfC.jmx -l Monolith_ConfC.jtl

# Sleep for a few minutes to give pipeline time to retrieve dashboard files
#echo "Sleep for 900 seconds [15 minutes] to give pipeline time to retrieve dashboard files."
#sleep 900

exit 0
