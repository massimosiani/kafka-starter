#!/usr/bin/env bash

[[ -z $1 ]] && echo "Please specify a topic" && exit 1
topic=$1

docker run --rm --net kafkastarter_default -it wurstmeister/kafka /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server kafka-starter:9092 \
           --property print.key=true --from-beginning --topic $topic