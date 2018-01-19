package io.grpc;

import com.pphh.demo.helloworld.GreeterGrpc;
import com.pphh.demo.helloworld.HelloReply;
import com.pphh.demo.helloworld.HelloRequest;

import java.util.concurrent.TimeUnit;

/**
 * Created by huangyinhuang on 1/19/2018.
 */
public class HelloWorldClient {

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    /**
     * Construct client for accessing RouteGuide server using the existing channel.
     */
    HelloWorldClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    /**
     * Construct client connecting to HelloWorld server at {@code host:port}.
     */
    public HelloWorldClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext(true)
                .build());
    }

    public static void main(String[] args) throws Exception {
        HelloWorldClient client = new HelloWorldClient("localhost", 50002);

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                /* Access a service running on the local machine on port 50051 */
                String user = "world";
                if (args.length > 0) {
                    user = args[0]; /* Use the arg as the name to greet if provided */
                }
                client.greet(user);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Thread.sleep(2000);
        }

        client.shutdown();
        System.out.println("gRpc demo is finish.");
        System.exit(0);
    }

    /**
     * Say hello to server.
     */
    public void greet(String name) {
        System.out.println("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: " + e.getStatus());
            return;
        }
        System.out.println("Greeting: " + response.getMessage());
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

}
