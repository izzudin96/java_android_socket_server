import java.net.*;
import java.io.*;
import java.util.*;

public class HttpServer {
    private ArrayList<HttpServerSession> sessions;

    public static void main(String args[]) {
        HttpServer server = new HttpServer();
        server.startServer();
    }

    private void startServer() {
        sessions = new ArrayList<HttpServerSession>();

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
    private String filename;
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
                System.out.println("File requested: " + reader.readLine());

                writer.println("HTTP/1.1 200 OK");
                writer.println("Server: COMPX202 HTTP Server by Izzudin Anuar");
                writer.println("Date: " + new Date());;
                writer.println();
                writer.write("Hello mello tello");
                writer.flush();

            }

            System.out.println("\nClose connection with client...");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
