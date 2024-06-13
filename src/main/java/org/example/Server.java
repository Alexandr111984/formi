package org.example;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class Server {
    public final ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers;
//    private final int SERVER_SOCKET;
    //private final ExecutorService executorService;


    public Server() {
        handlers = new ConcurrentHashMap<>();
    }

    public void start(int port, int poolSize) {
        final var executorService = Executors.newFixedThreadPool(poolSize);
        try (final var serverSocket = new ServerSocket(port);
        ) {
            while (!serverSocket.isClosed()) {
                final Socket socket = serverSocket.accept();
                executorService.execute(() -> proceedConnection(socket));
            }


        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public void addHandler(String requestMethod, String path, Handler handler) {
        if (!handlers.containsKey(requestMethod)) {
            handlers.put(requestMethod, new ConcurrentHashMap<>());
        }
        handlers.get(requestMethod).put(path, handler);
    }


    private void proceedConnection(Socket socket) {
        try (final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             final var out = new BufferedOutputStream(socket.getOutputStream())) {

            // read only request line for simplicity
            // must be in form GET /path HTTP/1.1
            final var requestLine = in.readLine();
            final var parts = requestLine.split(" ");

            if (parts.length != 3) {
                // just close socket
                socket.close();
                return;
            }

            final var method = parts[0];
            final var path = parts[1];
            Request request = new Request(method, path);

            if (!handlers.containsKey(request.getMethod())) {
                //notFound(out);
                return;
            }
            var methodHandlerMap = handlers.get(request.getMethod());
            if (!methodHandlerMap.containsKey(request.getPath())) {
               // notFound(out);
                return;
            }
            var handler = methodHandlerMap.get(request.getPath());

            handler.handle(request, out);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
