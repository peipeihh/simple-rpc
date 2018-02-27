echo "this scripts is for reference only. you may start each java app on separate shell windows, so that you are able to observe the runtime inputs by each java app. "

::echo "start 3 service providers on port 9090/9091/9092"
set service_provider_jar=../service-provider/target/service-provider-v4-1.4-SNAPSHOT.jar
java -Dserver.port=9090 -jar %service_provider_jar%
java -Dserver.port=9091 -jar %service_provider_jar%
java -Dserver.port=9092 -jar %service_provider_jar%

::echo "start a service consumer on port 9000 with direct registry"
set service_consumer_jar=../service-consumer/target/service-consumer-v4-1.4-SNAPSHOT.jar
set service_remote=http://localhost:9090/rpc,http://localhost:9091/rpc,http://localhost:9092/rpc
java -Dserver.port=9001 -Drpc.registry.type=direct -Drpc.registry.direct.remote=%service_remote% -jar %service_consumer_jar%

::echo "start 3 service provider on port 9090/9091/9092 with file registry"
set service_provider_jar=../service-provider/target/service-provider-v4-1.4-SNAPSHOT.jar
java -Dserver.port=9090 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
java -Dserver.port=9091 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
java -Dserver.port=9092 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%

::echo "start a service consumer on port 9000 with file registry"
set service_consumer_jar=../service-consumer/target/service-consumer-v4-1.4-SNAPSHOT.jar
java -Dserver.port=9001 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_consumer_jar%

::echo "start a service consumer with local registry on port 9001"
set service_consumer_local_jar=./service-consumer-local/target/service-consumer-local-v4-1.4-SNAPSHOT.jar
java -Dserver.port=9001 -Drpc.registry.type=local -jar %service_consumer_local_jar%