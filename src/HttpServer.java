import java.net.*;
import java.io.*;
import java.util.*;

public class HttpServer {
    public static void main(String args[]) {
        HttpServer server = new HttpServer();
        server.startServer();
    }

    private void startServer() {
        ArrayList<HttpServerSession> sessions = new ArrayList<HttpServerSession>();

        try {
            ServerSocket server = new ServerSocket(8080);
            System.out.println("Starting server on port: " + server.getLocalPort());

            while(true) {
                Socket client = server.accept();
                System.out.println("\nConnection received from: " + client.getLocalAddress());

                HttpServerSession thread = new HttpServerSession(client);
                synchronized (sessions) {
                    sessions.add(thread);
                }

                thread.start();
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }
    }
}


class HttpServerSession extends Thread {
    private Socket client;
    private String requestedFileName;

    HttpServerSession(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(client.getOutputStream());
            BufferedOutputStream data = new BufferedOutputStream(client.getOutputStream());


            if(client.isConnected()) {
                String request = reader.readLine();

                System.out.println("Method Type: " + parseMethodType(request));
                System.out.println("File requested: " + parseRequestFileName(request));

                writer.println("HTTP/1.1 200 OK");
                writer.println("Server: Pokemeet Server");
                writer.println("Date: " + new Date());
                writer.println("Content-Type: application/json");

                writer.println();
                writer.println("Method Type: " + parseMethodType(request));
                writer.println("File requested: " + parseRequestFileName(request));
                writer.write("{latitude: -117.3232, longitude: 102.233}");
                writer.flush();
            }

            System.out.println("\nClose connection with client...");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String parseRequestFileName(String request) {
        String parts[] = request.split(" ");
        if(parts.length != 3) {
            throw new RuntimeException("Does not understand request");
        } else {
            String filename = parts[1].substring(1);
            return filename;
        }
    }

    private String parseMethodType(String request) {
        String method;

        String parts[] = request.split(" ");
        if(parts.length != 3) {
            throw new RuntimeException("Does not understand request");
        } else {
            method = parts[0];
            return method;
        }
    }
}
