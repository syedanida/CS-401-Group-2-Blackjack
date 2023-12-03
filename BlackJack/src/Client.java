import java.io.*;
import java.net.*;

public class Client implements GUIListener{

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6000;

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private BufferedReader scanner;
    private GUI gui;

    public Client() {
        scanner = new BufferedReader(new InputStreamReader(System.in));
        gui = new GUI(this);
    }

    public void connectToServer() {
        try {
            // Connect to the server
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            
            getUserCred();
            System.out.println("User log in successful");
            handleUserOptions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void getUserCred() {
        try {
            // Ask the user for user ID and password
            System.out.println("Enter your user ID:");
            String userId = scanner.readLine();
            System.out.println("Enter your password:");
            String password = scanner.readLine();

            // Create a LOGIN message with userId and password as payload
            Message loginMessage = new Message(Message.MessageType.LOGIN, userId, password);
            sendMessage(loginMessage);

            // Wait for the server response (authentication status)
            String authenticationResponse = (String) inputStream.readObject();
            System.out.println(authenticationResponse);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

private void handleUserOptions() {
        boolean loggedIn = true;

        while (loggedIn) {
            // Display menu options and get user input
            System.out.println("Select an option:");
            System.out.println("1. Find a table");
            System.out.println("2. View bank details");
            System.out.println("3. Change settings");
            System.out.println("4. Logout");

            try {
                String userChoice = scanner.readLine();

                switch (userChoice) {
                    case "1":
                        handleFindTable();
                        break;
                    case "2":
                        handleBankDetails();
                        break;
                    case "3":
                        handleChangeSettings();
                        break;
                    case "4":
                        handleLogout();
                        loggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleFindTable() {
    	System.out.println("calling FindTable");
        sendMessage(new Message(Message.MessageType.FIND_TABLE, null, null));
    }

    private void handleBankDetails() {
    	System.out.println("calling BankDetails");
        sendMessage(new Message(Message.MessageType.BANK_DETAILS, null, null));
    }

    private void handleChangeSettings() {
    	System.out.println("calling ChangeSettings");
        sendMessage(new Message(Message.MessageType.SETTINGS, null, null));
    }

    private void handleLogout() {
        sendMessage(new Message(Message.MessageType.LOGOUT, null, null));
    }

    private void sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connectToServer();
    }

	@Override
	public void guiVerifyLogin(String username, String password) {
		System.out.println(username);
		System.out.println(password);
	}

	@Override
	public void guiExit() throws IOException {
		System.out.println("Closing client");
//		socket.close();
		System.exit(0);
	}
}