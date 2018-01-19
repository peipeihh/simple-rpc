package io.grpc;

import com.pphh.demo.helloworld.GreeterGrpc;
import com.pphh.demo.helloworld.HelloReply;
import com.pphh.demo.helloworld.HelloRequest;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

/**
 * Created by huangyinhuang on 1/19/2018.
 */
public class HelloWorldServer {

    private Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        HelloWorldServer server = new HelloWorldServer();
        server.start();
        server.blockUntilShutdown();
    }

    private void start() throws IOException {
        int port = 50002;
        server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl()).build().start();
        System.out.println("server is started on port " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                HelloWorldServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

        @Override
        public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
            HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
            System.out.println("receive a request from " + request.getName());
            responseObserver.onNext(reply);
            responseObserver.onCompleted();

        }
    }

}
