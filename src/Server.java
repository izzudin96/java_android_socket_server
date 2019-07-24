import java.io.*;
import java.net.*;

class Server {

    /**
     * Server entry point
     * @param args
     */
    public static void main(String args[]) {
        int portNumber;
        InetAddress host;
        ServerSocket server;

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
            System.out.println("Server running on port: " + server.getLocalPort());

            ServerThread serverThread = new ServerThread(server);
            serverThread.start();
        } catch (IOException e) {
            System.out.println("IO exception trying to create server socket");
            e.printStackTrace();
        }
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

class ServerThread extends Thread implements Runnable {
    private final ServerSocket server;

    ServerThread(ServerSocket server) {
        this.server = server;
    }

    
    public void run() {
        while(true) {
            System.out.println("Receiving client request...");

            Socket socket;
            try {
                socket = server.accept();
                InputStream is;
                try {
                    is = socket.getInputStream();
                } catch (IOException e) {
                    System.out.println("Couldn't get input stream from socket");
                    return;
                }
                byte[] buffer = new byte[7];
                int n;
                while (true) {

                    // read from socket into byte array
                    try {
                        n = is.read(buffer);
                    } catch (IOException e) {
                        System.out.println("IO exception when reading from socket");
                        break;
                    }

                    System.out.println("Have read " + n + " bytes from socket");
                    if (n <= 0) break;
                    System.out.println(new String(buffer, 0, n));
                }

                // close stream and socket
                try {
                    is.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close socket");
                    return;
                }
            } catch (IOException e) {
                System.out.println("IO exception waiting for client request");
                return;
            }
        }
    }
}