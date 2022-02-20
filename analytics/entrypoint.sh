#!/bin/bash
set -e

spark-submit --class analytics.Main --conf "spark.driver.extraJavaOptions=-Dlog4jspark.root.logger=WARN,console" --jars lib/mongo-scala-driver_2.12-4.5.0.jar,lib/mongodb-driver-core-4.5.0.jar,lib/mongo-scala-bson_2.12-4.5.0.jar,lib/bson-4.5.0.jar,lib/reactive-streams-1.0.3.jar,lib/mongodb-driver-reactivestreams-4.5.0.jar,lib/reactor-core-3.3.22.RELEASE.jar,lib/mongo-spark-connector_2.12-3.0.1.jar,lib/mongodb-driver-sync-4.5.0.jar target/scala-2.12/analytics_2.12-0.1.jar

exec "$@"