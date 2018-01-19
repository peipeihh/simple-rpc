echo "this scripts is for reference only. you may start each java app on separate shell windows, so that you are able to observe the runtime inputs by each java app. "

echo "start the service consumer on port 9000"
set service_consumer_jar=../service-consumer/target/service-consumer-v3-1.3-SNAPSHOT.jar
java -Dserver.port=9000 -Drpc.client.lb=roundrobin -Drpc.client.remote.service=http://localhost:9090/rpc -jar %service_consumer_jar%
::java -Dserver.port=9000 -Drpc.client.lb=roundrobin -Drpc.client.ha=failover -Drpc.client.remote.service=http://localhost:9090/rpc,http://localhost:9091/rpc,,http://localhost:9092/rpc -jar %service_consumer_jar%

::echo "start the service provider on port 9090/9091/9092"
set service_provider_jar=../service-provider/target/service-provider-v3-1.3-SNAPSHOT.jar
java -Dserver.port=9090 -jar %service_provider_jar%
java -Dserver.port=9091 -jar %service_provider_jar%
java -Dserver.port=9092 -jar %service_provider_jar%