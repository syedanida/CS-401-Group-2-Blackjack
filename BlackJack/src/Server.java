import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private Map<String, Player> playerMap;
    private List<Table> tables;
    private static int SERVER_PORT = 6000;

    public Server() {
        loadPlayerDataFromFile();
        tables = new ArrayList<>();
        createTable(); // Initial table creation
    }

    private void loadPlayerDataFromFile() {
    	// Hashmap <userUUID, playerObject>
        playerMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/userdata.txt"))) {
            String line;
            System.out.println("Contents of userdata.txt:");
            while ((line = reader.readLine()) != null) {
            	System.out.println(line); // for testing purposes
                String[] userDataArray = line.split(",");
                // Separates data values into array
                if (userDataArray.length == 4) {
                    String userID = userDataArray[0];
                    String userPassword = userDataArray[1];
                    String userName = userDataArray[2];
                    int userBalance = Integer.parseInt(userDataArray[3]);
                    
                    // Sets player data from data values, inserts into Map
                    Player player = new Player(userID, userPassword, userName, userBalance);
                    playerMap.put(userID, player);
                } else {
                    System.out.println("Invalid data format in line: " + line);
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Player getPlayer(String userId) {
        return playerMap.get(userId);
    }

    public boolean verifyCredentials(String userId, String password) {
        Player player = playerMap.get(userId);
        return player != null && player.getPassword().equals(password);
    }
    
    private void createTable() {
    	Table table = new Table();
        tables.add(table);
    }

    public Table findSeat(Player player) {
        for (Table table : tables) {
            if (!table.isFull()) {
                table.addPlayer(player);
                return table;
            }
        }

        // If no table has an available seat, create a new table
        Table newTable = new Table();
        newTable.addPlayer(player);
        tables.add(newTable);
        return newTable;
    }

    public static void main(String[] args) {
    	Server server = new Server();

    	try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket, server);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}