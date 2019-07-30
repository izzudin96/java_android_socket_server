import java.io.*;
import java.net.*;

class Client {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

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
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        // receive response using InputStream
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        Request request = new Request("izzudinanuar96@gmail.com", "createRaidRoom", new String[]{"Uni Mart", "11.30AM"});

        System.out.println("Sending request to client");
        objectOutputStream.writeObject(request);
        System.out.println("Request sent");

        System.out.println("Receiving response");

        Response response = (Response) objectInputStream.readObject();
        RaidRoom raidRoom = (RaidRoom) response.getObject();
        System.out.println("Message from server: " + response.getMessage());
        System.out.println("Raid room id: " + raidRoom.getId());
        System.out.println("Raid room location: " + raidRoom.getLocation());
        System.out.println("Raid room time: " + raidRoom.getTime());

        objectOutputStream.close();
        objectInputStream.close();
    }
}

class Request implements Serializable {
    private final String email;
    private final String action;
    private final String[] parameter;

    Request(String email, String action, String[] parameter) {
        this.email = email;
        this.action = action;
        this.parameter = parameter;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getAction()
    {
        return this.action;
    }

    public String[] getParameter()
    {
        return this.parameter;
    }
}