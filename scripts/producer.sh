#!/usr/bin/env bash

[[ -z $1 ]] && echo "Please specify a topic" && exit 1
topic=$1
shift 1

docker run --rm --net kafkastarter_default -it wurstmeister/kafka /opt/kafka/bin/kafka-console-producer.sh --broker-list kafka-starter:9092 \
           --topic $topic $@ --property "parse.key=true" --property "key.separator=:"
