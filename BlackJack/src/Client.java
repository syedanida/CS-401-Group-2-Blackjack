import java.io.*;
import java.net.*;

public class Client {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6000;

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private BufferedReader scanner;

    public Client() {
        scanner = new BufferedReader(new InputStreamReader(System.in));
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
        try {
        	for (int i = 0;i < 5; i++) {
                String response = (String) inputStream.readObject();
                System.out.println(response);
        	}
        	String answer = scanner.readLine();
        	if (answer.equals("1")) {
        		System.out.println("calling Deposit");
        		handleDeposit();
        	}
        	if (answer.equals("2")) {
        		System.out.println("calling Withdraw");
        		handleWithdraw();
        	}
        	if (answer.equals("3"))
        		handleUserOptions();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void handleDeposit() {
    	sendMessage(new Message(Message.MessageType.DEPOSIT, null, null));
		String response;
		try {
			System.out.println("Inside handle deposit");
			response = (String) inputStream.readObject();
			System.out.println(response);
			String depositAmount = scanner.readLine();
			int intNumber = Integer.parseInt(depositAmount);
			sendMessage(new Message(Message.MessageType.DEPOSIT, null, null, intNumber));
			response = (String) inputStream.readObject();
			System.out.println(response);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void handleWithdraw() {
    	sendMessage(new Message(Message.MessageType.WITHDRAW, null, null));
		String response;
		try {
			System.out.println("Inside handle withdraw");
			response = (String) inputStream.readObject();
			System.out.println(response);
			String withdrawAmount = scanner.readLine();
			int intNumber = Integer.parseInt(withdrawAmount);
			sendMessage(new Message(Message.MessageType.DEPOSIT, null, null, intNumber, true));
			response = (String) inputStream.readObject();
			System.out.println(response);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void handleChangeSettings() {
    	System.out.println("calling ChangeSettings");
        sendMessage(new Message(Message.MessageType.SETTINGS, null, null));
    }

    private void handleLogout() {
        try {
            sendMessage(new Message(Message.MessageType.LOGOUT, null, null));
            if (!socket.isClosed()) {
                String loginResponse = (String) inputStream.readObject();
                System.out.println(loginResponse);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
}
