import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static Map<String, Player> playerMap;
    private static Map<String, String> idMap;
    private List<Table> tables;
    private static int SERVER_PORT = 6000;

    public Server() {
        loadPlayerDataFromFile();
        tables = new ArrayList<>();
        createTable(); // Initial table creation
    }

    private void loadPlayerDataFromFile() 
    {
        playerMap = new HashMap<>();
        idMap = new HashMap<>();
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
                    idMap.put(userName, userID);
                } else {
                    System.out.println("Invalid data format in line: " + line);
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Player getPlayer(String username) {
    	String userId = idMap.get(username);
    	Player player = playerMap.get(userId);
    	System.out.println("Getting:");
    	System.out.println(player.getId());
    	System.out.println(player.getDisplayName());
    	System.out.println(player.getPassword());
        return player;
    }

    public static boolean verifyCredentials(String username, String password) {
    	String id = idMap.get(username);
    	System.out.println("Verifying: " + id);
        Player player = playerMap.get(id);
        return player != null && player.getPassword().equals(password);
    }
    
    private void createTable() {
    	Table table = new Table();
        tables.add(table);
    }

    public Table findSeat(Player player) {
        for (Table table : tables) {
            if (!table.isTableFull()) {
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

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private ObjectOutputStream outputStream;
        private ObjectInputStream inputStream;
        private Player player;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            
            try {
            	outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
            	boolean pass = false;
            	while(!pass) {
            		Message receivedMessage = (Message) inputStream.readObject();
                	pass = handleLogin(receivedMessage);
            	}
            	
            	
            	// Continuously handle client messages
                while (true) {
                	
                	Message clientMessage = (Message) inputStream.readObject();
                	// Check for log out message
                	if(clientMessage.getType() == MessageType.LOGOUT) {
                		break;
                	} else {
                		// Process the received message
                        processMessage(clientMessage);
                	}
                }
                // Handle the client connection
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
    			e.printStackTrace();
    		} finally {
                // Ensure resources are properly closed
                closeResources();
            }
        }

        private void processMessage(Message message) throws IOException {
        	 if (message == null) {
        		 outputStream.writeObject("Invalid message received.");
        	        return;
        	    }
        	 
            switch (message.getType()) {
                case FIND_TABLE:
                    handleFindTable(message);
                    break;
                case BANK_DETAILS:
                    handleBankDetails();
                    break;
                case SETTINGS:
                    handleSettings(message);
                    break;
                case LOGOUT: {
                	System.out.println("Client " + message.getDisplayName() + " initiating logout.");
                    handleLogout(message);
                    break;
                }
                default:
                	outputStream.writeObject("Invalid message type.");
            }
        }

        private boolean handleLogin(Message message) throws IOException 
        {
            String username = message.getDisplayName();
            String password = message.getPassword();
            boolean verified = false;
            
            System.out.println("Client login request: " + username + ", " + password);

            if (verifyCredentials(username, password)) {
			    player = getPlayer(username);
			    outputStream.writeObject("Authentication successful. You are now connected.");
			    outputStream.writeObject(new Message(MessageType.LOGIN, player.getId(), player.getPassword(), player.getDisplayName()));
			    System.out.println("Client login successful.");
			    verified = true;
			    return verified;
			} else {
				outputStream.writeObject("Invalid credentials. Try again!");
				System.out.println("Client login failure.");
				return verified;
				//closeResources();
			}
        }

        private void handleFindTable(Message message) {
            // Implement logic to find or create a table
//    			findSeat(player);
        }
        
        private void handleBankDetails() {
            try {
                // Display the client's balance and options
                outputStream.writeObject("Your current balance: " + player.getBalance());
                outputStream.writeObject("Options:");
                outputStream.writeObject("1. Deposit");
                outputStream.writeObject("2. Withdraw");
                outputStream.writeObject("3. Back to main menu");

                // Read and process the user's choice
                Message userChoice = (Message) inputStream.readObject();
                switch (userChoice.getType()) {
                    case DEPOSIT:
                        // Deposit
                        handleDeposit();
                        break;
                    case WITHDRAW:
                        // Withdraw
                        handleWithdraw();
                        break;
                    default:
                        // Invalid choice
                        outputStream.writeObject("Invalid option. Please try again. bankdetails");
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        private void handleSettings(Message message) {
            try {
            	outputStream.writeObject("Your current Display Name: " + player.getDisplayName());
                outputStream.writeObject("Options:");
                outputStream.writeObject("1. Change Display Name");
                outputStream.writeObject("2. Change Password");
                outputStream.writeObject("3. Back to Main Menu");
                
             // Read and process the user's choice
                Message userChoice = (Message) inputStream.readObject();
                switch (userChoice.getType()) {
                    case CHANGE_NAME:
                        // Deposit
                        changeDisplayName();
                        break;
                    case CHANGE_PASSWORD:
                        // Withdraw
                        changePassword();
                        break;
                    default:
                        // Invalid choice
                        outputStream.writeObject("Invalid option. Please try again. handlesettings");
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void handleDeposit() {
            try {
                // Ask the user for the deposit amount
                outputStream.writeObject("Enter the deposit amount:");
                
                // Read the deposit amount from the client
                Message receivedMessage = (Message) inputStream.readObject();

                // Perform the deposit
                player.deposit(receivedMessage.getDepositAmount());

                // Notify the user about the successful deposit
                outputStream.writeObject("Deposit successful. Your new balance: " + player.getBalance());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void handleWithdraw() {
            try {
                // Ask the user for the withdrawal amount
                outputStream.writeObject("Enter the withdrawal amount:");

                // Read the withdrawal amount from the client
                Message receivedMessage = (Message) inputStream.readObject();

                // Perform the withdrawal
                player.withdraw(receivedMessage.getWithdrawAmount());

                // Notify the user about the result of the withdrawal
                outputStream.writeObject("Withdrawal successful. Your new balance: " + player.getBalance());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        private void changePassword() {
        	try {
                // Ask the user for the new password
                outputStream.writeObject("Enter your new Password:");

                // Read the new password from the client
                Message receivedMessage = (Message) inputStream.readObject();

                // Perform password change
                player.setPassword(receivedMessage.getNewPassword());

                // Notify the user about the password change
                outputStream.writeObject("Your password has been changed successfully!");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
    		
    	}

    	private void changeDisplayName() {
    		try {
                // Ask the user for the new user display name
                outputStream.writeObject("Enter the new display name:");

                // Read the new display name from the client
                Message receivedMessage = (Message) inputStream.readObject();

                // Perform the display name change
                player.setDisplayName(receivedMessage.getNewUserName());

                // Notify the user about the result of the withdrawal
                outputStream.writeObject("Display name change succesful. Your new display name: " + player.getDisplayName());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void handleLogout(Message message) {
            try {
                if (!clientSocket.isClosed()) {
                    outputStream.writeObject("Logout successful. Goodbye!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeResources();
            }
        }

        private void closeResources() {
            try {
                // Close the outputStream and clientSocket; inputStream is closed automatically
                outputStream.close();
                if (!clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}