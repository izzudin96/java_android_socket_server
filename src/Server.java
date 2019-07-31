import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Server {
    /**
     * Server entry point
     * @param args
     * @throws IOException
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
            System.out.println("=====================");


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
        try {
            InputStream inputStream = client.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            OutputStream outputStream = client.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            Request clientRequest = (Request) objectInputStream.readObject();
            processClientRequest(clientRequest, objectOutputStream);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processClientRequest(Request request, ObjectOutputStream objectOutputStream) throws IOException {
        System.out.println("Processing client request");

        switch (request.getAction()) {
            case "createRaidRoom": {
                RaidRoom raidRoom = new RaidRoom(request.getParameter()[0], request.getParameter()[1]);
                User user = DataStore.getInstance().findOrCreateUser(request.getEmail());

                raidRoom.addParticipant(user);
                raidRoom.setAdmin(raidRoom.userList.get(0));

                System.out.println("Room created by " + raidRoom.userList.get(0) + " Room id:" + raidRoom.getId() + " Location: " + raidRoom.getLocation() + " Time: " + raidRoom.getTime());
                DataStore.getInstance().addRaidRoom(raidRoom);

                System.out.println("Sending response to client");

                Response response = new Response("Raid room created", raidRoom);
                objectOutputStream.writeObject(response);
                break;
            }
            case "fetchAllRaidRoom": {
                ArrayList<RaidRoom> raidRooms = DataStore.getInstance().raidRooms;

                Response response = new Response("Raid rooms fetched", raidRooms);
                objectOutputStream.writeObject(response);
                break;
            }
            case "getRaidRoomById": {
                ArrayList<RaidRoom> raidRooms = DataStore.getInstance().raidRooms;
                int requestedId = Integer.parseInt(request.getParameter()[0]);
                RaidRoom requestedRoom = null;

                System.out.println("Getting raid room with ID: " + requestedId);
                for (RaidRoom raidRoom : raidRooms) {
                    if (raidRoom.getId() == requestedId) {
                        requestedRoom = raidRoom;
                    }
                }

                Response response = new Response("Raid with id " + requestedId + " fetched", requestedRoom);
                objectOutputStream.writeObject(response);
                break;
            }
            case "joinRoom": {
                ArrayList<RaidRoom> raidRooms = DataStore.getInstance().raidRooms;
                int requestedId = Integer.parseInt(request.getParameter()[0]);
                System.out.println("Receive join room " + requestedId + " request");
                RaidRoom requestedRoom = null;

                System.out.println("Getting raid room with ID: " + requestedId);
                for (RaidRoom raidRoom : raidRooms) {
                    if (raidRoom.getId() == requestedId) {
                        requestedRoom = raidRoom;
                    }
                }

                boolean userExists = false;
                for (User user : requestedRoom.userList) {
                    if (user.getEmail().equals(request.getEmail())) {
                        user.updateLocation(request.getParameter()[1], request.getParameter()[2]);
                        Response response = new Response("Raid with id " + requestedId + " fetched", requestedRoom);
                        objectOutputStream.writeObject(response);
                        userExists = true;
                        break;
                    }
                }

                if (!userExists) {
                    User user = new User(request.getEmail());
                    user.updateLocation(request.getParameter()[1], request.getParameter()[2]);
                    requestedRoom.addParticipant(user);
                }

                Response response = new Response("Raid with id " + requestedId + " fetched", requestedRoom);
                objectOutputStream.writeObject(response);
                break;
            }
            case "updateLocation": {
                ArrayList<RaidRoom> raidRooms = DataStore.getInstance().raidRooms;
                int requestedId = Integer.parseInt(request.getParameter()[0]);
                System.out.println("Updating location for " + request.getEmail() + " at room id " + requestedId);
                RaidRoom requestedRoom = null;

                System.out.println("Getting raid room with ID: " + requestedId);
                for (RaidRoom raidRoom : raidRooms) {
                    if (raidRoom.getId() == requestedId) {
                        requestedRoom = raidRoom;
                    }
                }

                for (User user : requestedRoom.userList) {
                    if (user.getEmail().equals(request.getEmail())) {
                        user.updateLocation(request.getParameter()[1], request.getParameter()[2]);
                        System.out.println("User location updated");
                    }
                }

                Response response = new Response("User location updated" + requestedId + " fetched", requestedRoom);
                objectOutputStream.writeObject(response);
                break;
            }
        }
    }
}

class RaidRoom implements Serializable {
    private final int id;
    private final String time;
    private static int count = 0;
    private final String location;

    private User admin = null;
    public ArrayList<User> userList = new ArrayList<>();

    RaidRoom(String location, String time) {
        this.location = location;
        this.time = time;
        count++;
        this.id = count;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public void addParticipant(User user) {
        userList.add(user);
    }

    public String getParticipants() {
        return userList.toString();
    }

    @Override
    public String toString() {
        return "Room ID: " + getId() + " || Location: " + getLocation() + " || Time: " + getTime();
    }
}

class User implements Serializable {
    private final int id;
    private final String email;
    private String latitude = "0";
    private static int count = 0;
    private String longitude = "0";
    private static final long serialVersionUID = 1L;

    User(String email) {
        count++;
        this.id = count;
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public Integer getId() {
        return this.id;
    }

    public void updateLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    @Override
    public String toString() {
        return "User ID: " + getId() + " || Email: " + getEmail() + " || Latitude: " + getLatitude() + " || Longitude: " + getLongitude();
    }
}

class DataStore {
    static DataStore object = new DataStore();
    public ArrayList<RaidRoom> raidRooms = new ArrayList<>();
    public ArrayList<User> users = new ArrayList<>();

    private DataStore() {}
    public static DataStore getInstance() {
        return object;
    }

    public void addRaidRoom(RaidRoom room) {
        raidRooms.add(room);
    }

    public User addUser(User user) {
        System.out.println("Adding user to datastore");
        users.add(user);

        return getUser(user.getEmail());
    }

    public User getUser(String email) {
        User foundUser = null;

        for(User user : users) {
            if(user.getEmail().equals(email)) {
                foundUser = user;
                break;
            }
        }

        return foundUser;
    }

    public User findOrCreateUser(String email) {
        if(getUser(email) == null) {
            System.out.println("Creating new user...");
            return addUser(new User(email));
        } else {
            return getUser(email);
        }
    }
}

class Response implements Serializable {
    private final String message;
    private final Object object;

    public Response(String message, Object object) {
        this.message = message;
        this.object = object;
    }

    public String getMessage()
    {
        return this.message;
    }

    public Object getObject()
    {
        return this.object;
    }
}