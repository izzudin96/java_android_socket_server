import java.io.*;
import java.net.*;

class Server {

    /**
     * Server entry point
     * @param args
     */
    public static void main(String args[]) throws IOException {
        int portNumber;
        InetAddress host;
        ServerSocket server = null;

        if (args.length != 1) {
            System.out.println("Usage:  server <port>");
        }

        portNumber = Integer.parseInt(args[0]);

        try {
            host = InetAddress.getLocalHost();
            displayHostName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            server = new ServerSocket(portNumber);
            server.setReuseAddress(true);
            System.out.println("Server running on port: " + server.getLocalPort());


            while(true) {
                Socket client = server.accept();
                System.out.println("New client connected " + client.getInetAddress().getHostAddress());
                ClientHandler clientSock = new ClientHandler(client);

                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            System.out.println("IO exception trying to create server socket");
            e.printStackTrace();
        }

        assert server != null;
        server.close();
    }

    /**
     * Display host name.
     * @param host
     */
    private static void displayHostName(InetAddress host) {
        String hostName = host.getHostName();
        System.out.println("Local host is " + host.toString());
        System.out.println("Local host name is " + hostName);
    }
}



class ClientHandler implements Runnable {
    private final Socket client;

    ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            String line;

            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while ((line = in.readLine()) != null) {
                System.out.printf("Sent from the client: %s\n", line);
                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert out != null;
        out.close();
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}