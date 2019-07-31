import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Client {
    private static String hostName;
    private static Integer portNumber;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        if (args.length != 2) {
            System.out.println("Usage:  client <host> <port>");
            return;
        }

        hostName = args[0];
        portNumber = Integer.parseInt(args[1]);

        for(int i = 0; i < 10; i++) {
            sendCreateRaidRoomRequest(Integer.toString(i));
        }

        sendGetAllRaidRoomsRequest();
        sendGetSingleRaidRoomRequest(6);
        sendJoinRaidRoomRequest("ivan@gmail.com", 6,"-37.786864", "175.310597");
        sendJoinRaidRoomRequest("izzudin@gmail.com", 6,"-38.786864", "175.310597");

        sendUpdateLocationRequest("ivan@gmail.com", 6,"-40.786864", "768.310597");
        sendUpdateLocationRequest("ivan@gmail.com", 6,"-25.786864", "500.310597");
    }

    public static void sendCreateRaidRoomRequest(String location) throws IOException, ClassNotFoundException {
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

        Request request = new Request("izzudinanuar96@gmail.com", "createRaidRoom", new String[]{"Uni Mart" + location, "11.30AM"});

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

    public static void sendGetAllRaidRoomsRequest() throws IOException, ClassNotFoundException
    {
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

        Request request = new Request("izzudinanuar96@gmail.com", "fetchAllRaidRoom");

        System.out.println("Sending request to client");
        objectOutputStream.writeObject(request);
        System.out.println("Request sent");

        System.out.println("Receiving response");
        Response response = (Response) objectInputStream.readObject();
        ArrayList<RaidRoom> raidRooms = (ArrayList<RaidRoom>) response.getObject();

        System.out.println("Getting raid room location");
        raidRooms.forEach(e -> {
            System.out.println(e.toString());
        });

        objectOutputStream.close();
        objectInputStream.close();
    }

    public static void sendGetSingleRaidRoomRequest(Integer id) throws IOException, ClassNotFoundException
    {
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

        Request request = new Request("izzudinanuar96@gmail.com", "getRaidRoomById", Integer.toString(id));
        System.out.println("Sending request to server");
        objectOutputStream.writeObject(request);
        System.out.println("Request sent");

        System.out.println("Receiving response");
        Response response = (Response) objectInputStream.readObject();
        System.out.println("Getting raid room with id " + id);
        RaidRoom raidRoom = (RaidRoom) response.getObject();
        System.out.println(raidRoom.toString());

        System.out.println("Getting user lists for room id " + id);
        ArrayList<User> users = raidRoom.userList;

        users.forEach(e-> System.out.println(e.toString()));

        objectOutputStream.close();
        objectInputStream.close();
    }

    private static void sendJoinRaidRoomRequest(String email, Integer roomId, String latitude, String longitude) throws IOException, ClassNotFoundException {
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

        Request request = new Request(email, "joinRoom", Integer.toString(roomId), latitude, longitude);
        objectOutputStream.writeObject(request);

        System.out.println("Receiving response");
        Response response = (Response) objectInputStream.readObject();
        System.out.println("Getting raid room with id " + roomId);
        RaidRoom raidRoom = (RaidRoom) response.getObject();
        System.out.println(raidRoom.toString());

        System.out.println("Getting user lists for room id " + roomId);
        ArrayList<User> users = raidRoom.userList;

        users.forEach(e-> System.out.println(e.toString()));

        objectOutputStream.close();
        objectInputStream.close();
    }

    private static void sendUpdateLocationRequest(String email, int roomId, String latitude, String longitude) throws IOException, ClassNotFoundException {
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

        Request request = new Request(email, "updateLocation", Integer.toString(roomId), latitude, longitude);
        objectOutputStream.writeObject(request);
        System.out.println("HERE!!!!!");
        System.out.println("Receiving response");
        Response response = (Response) objectInputStream.readObject();
        System.out.println("Getting raid room with id " + roomId);
        RaidRoom raidRoom = (RaidRoom) response.getObject();
        System.out.println(raidRoom.toString());

        System.out.println("Getting user lists for room id " + roomId);
        ArrayList<User> users = raidRoom.userList;

        users.forEach(e-> System.out.println(e.toString()));

        objectOutputStream.close();
        objectInputStream.close();
    }
}

class Request implements Serializable {
    private final String email;
    private final String action;
    private final String[] parameter;

    Request(String email, String action, String... parameter) {
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