package org.example;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    private static final int SERVER_PORT = 9999;
    private static final int THREAD_POOL_SIZE = 64;
//    private static final List<String> filePath = List.of("/index.html", "/spring.svg", "/spring.png",
//            "/resources.html",
//            "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

    public static void main(String[] args) {
        Server server = new Server(SERVER_PORT, THREAD_POOL_SIZE);


//        for (int i = 0; i < filePath.size(); i++) {
//           String path = filePath.get(i);
//           //String filePath = path;
        server.addHandler(HttpMethod.GET, "/messages", ((request, responseStream) -> {

            try {
//                final var filePath = Path.of(".", "public", request.getPath());
//                final var mimeType = Files.probeContentType(filePath);
                String path = request.getPath();
                System.out.println("in handler: " + path);

                if (path.equals("/messages")) {
//                    final var template = Files.readString(filePath);
//                    final var content = template.replace(
//                            "{time}",
//                            LocalDateTime.now().toString()
//                    ).getBytes();
                    responseStream.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + "text/html" + "\r\n" +
                                    "Content-Length: " + path.length() + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    responseStream.write(path.getBytes());
                    responseStream.flush();
                    return;
                }
//                final var length = Files.size(filePath);
                responseStream.write((
                        "HTTP/1.1 404 OK\r\n" +
                                "Content-Type: " + "text/html" + "\r\n" +
                                "Content-Length: " + 0 + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
//                Files.copy(filePath, responseStream);
                responseStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        server.addHandler(HttpMethod.POST, "/", (_, responseStream) ->

        {
            String response = "Hello from Post";
            var responseBytes = response.getBytes();
            responseStream.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: plain/text\r\n" +
                            "Content-Length: " + responseBytes.length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            responseStream.write(responseBytes);
            responseStream.flush();
        });

        server.addHandler(HttpMethod.DELETE, "/", (_, responseStream) ->

        {
            String response = "Hello from DELETE";
            var responseBytes = response.getBytes();
            responseStream.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: plain/text\r\n" +
                            "Content-Length: " + responseBytes.length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            responseStream.write(responseBytes);
            responseStream.flush();
        });


        server.start();
    }


}
