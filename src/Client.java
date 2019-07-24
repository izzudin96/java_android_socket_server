import java.io.*;
import java.net.*;

class Client {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage:  client <host> <port>");
            return;
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        // establish socket connection to server
        Socket socket;
        try {
            socket = new Socket(hostName, portNumber);
        } catch (UnknownHostException e) {
            System.out.println("Couldn't establish socket connection");
            return;
        } catch (IOException e) {
            System.out.println("IO exception on attempting to connect socket");
            return;
        }

        // write to socket using OutputStream
        try {
            OutputStream os;
            os = socket.getOutputStream();
            os.write(("Message Hello Mello Telo").getBytes());  // 11
            os.close();
        } catch (IOException e) {
            System.out.println("IO exception trying to write to socket");
            return;
        }
    }
}