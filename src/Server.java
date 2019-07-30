import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Server {
    public static ArrayList<RaidRoom> raidRooms = new ArrayList<>();

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
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            InputStream inputStream = client.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            Request clientRequest = (Request) objectInputStream.readObject();
            processClientRequest(clientRequest);

            System.out.println(clientRequest.getAction());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processClientRequest(Request request) throws IOException {
        System.out.println("Processing client request");

        if(request.getAction().equals("createRaidRoom")) {
            RaidRoom raidRoom = new RaidRoom(request.getParameter()[0], request.getParameter()[1]);
            raidRoom.addParticipant(new User(request.getEmail()));
            raidRoom.setAdmin(raidRoom.userList.get(0));

            System.out.println("Room created by "+ raidRoom.userList.get(0) + " Room id:" + raidRoom.getId() + " Location: " + raidRoom.getLocation() + " Time: " + raidRoom.getTime());
            DataStore.getInstance().addRaidRoom(raidRoom);
//            objectOutputStream.writeObject(new Response("Raid room created", raidRoom));
        } else if(request.getAction().equals("getRaidRooms")) {

        }

        System.out.println(DataStore.getInstance().raidRooms.get(0).userList.get(0).getInfo());
    }
}

class RaidRoom implements Serializable {
    private static int count = 0;
    private final int id;
    private final String time;
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
}

class User implements Serializable {
    private final String email;
    private String latitude = "0";
    private String longitude = "0";
    private static final long serialVersionUID = 1L;

    User(String email) {
        this.email = email;
    }

    public void updateLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return this.email;
    }

    public String getInfo() {
        return this.email + " " + this.latitude + " " + this.longitude;
    }
}

class DataStore {
    static DataStore object = new DataStore();
    public ArrayList<RaidRoom> raidRooms = new ArrayList<>();

    private DataStore() {}
    public static DataStore getInstance() {
        return object;
    }

    public void addRaidRoom(RaidRoom room) {
        System.out.println("adding raid room to data store");
        raidRooms.add(room);
    }
}

class Response {
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
        return this.getObject();
    }
}