#!/bin/bash

DIR=/opt/application

mkdir -p $DIR/lib

wget https://repo1.maven.org/maven2/org/mongodb/scala/mongo-scala-driver_2.12/4.5.0/mongo-scala-driver_2.12-4.5.0.jar -P $DIR/lib
wget https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-core/4.5.0/mongodb-driver-core-4.5.0.jar -P $DIR/lib
wget https://repo1.maven.org/maven2/org/mongodb/scala/mongo-scala-bson_2.12/4.5.0/mongo-scala-bson_2.12-4.5.0.jar -P $DIR/lib
wget https://repo1.maven.org/maven2/org/mongodb/bson/4.5.0/bson-4.5.0.jar -P $DIR/lib
wget https://repo1.maven.org/maven2/org/reactivestreams/reactive-streams/1.0.3/reactive-streams-1.0.3.jar -P $DIR/lib
wget https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-reactivestreams/4.5.0/mongodb-driver-reactivestreams-4.5.0.jar -P $DIR/lib
wget https://repo1.maven.org/maven2/io/projectreactor/reactor-core/3.3.22.RELEASE/reactor-core-3.3.22.RELEASE.jar -P $DIR/lib
wget https://repo1.maven.org/maven2/org/mongodb/spark/mongo-spark-connector_2.12/3.0.1/mongo-spark-connector_2.12-3.0.1.jar -P $DIR/lib
wget https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-sync/4.5.0/mongodb-driver-sync-4.5.0.jar -P $DIR/lib
