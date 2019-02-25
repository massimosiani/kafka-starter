#!/bin/bash

[[ -z $1 ]] && echo "Please specify an object name, for example java.lang:type=OperatingSystem" && exit 1
objectName=$1

docker run --rm --net kafkastarter_default -it wurstmeister/kafka /opt/kafka/bin/kafka-run-class.sh kafka.tools.JmxTool --object-name $objectName \
           --jmx-url service:jmx:rmi:///jndi/rmi://kafka-starter:1099/jmxrmi --report-format properties \
           --one-time true
