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
            
            verifyUser();
            handleUserOptions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void verifyUser() {
        try {
        	boolean authenticated = false;
        	while (!authenticated){
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
            
           if(authenticationResponse.equals("Authentication successful. You are now connected.")) {
        	   authenticated = true;
        	   return;
           	}
        	}
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

private void handleUserOptions() {
	
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
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (IOException e) {
                e.printStackTrace();
        }
    }

    private void handleFindTable() {
        sendMessage(new Message(Message.MessageType.FIND_TABLE, null, null));
    }

    private void handleBankDetails() {
        sendMessage(new Message(Message.MessageType.BANK_DETAILS, null, null));
        try {
        	for (int i = 0;i < 5; i++) {
                String response = (String) inputStream.readObject();
                System.out.println(response);
        	}
        	String answer = scanner.readLine();
        	if (answer.equals("1")) {
        		handleDeposit();
        	}
        	if (answer.equals("2")) {
        		handleWithdraw();
        	}
        	if (answer.equals("3"))
        		sendMessage(new Message(Message.MessageType.MAIN_MENU, null, null));
        		handleUserOptions();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void handleDeposit() {
    	sendMessage(new Message(Message.MessageType.DEPOSIT, null, null));
		String response;
		try {
			response = (String) inputStream.readObject();
			System.out.println(response);
			String depositAmount = scanner.readLine();
			int intNumber = Integer.parseInt(depositAmount);
			sendMessage(new Message(Message.MessageType.DEPOSIT, null, null, intNumber));
			response = (String) inputStream.readObject();
			System.out.println(response);
			handleUserOptions();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void handleWithdraw() {
    	sendMessage(new Message(Message.MessageType.WITHDRAW, null, null));
		String response;
		try {
			response = (String) inputStream.readObject();
			System.out.println(response);
			String withdrawAmount = scanner.readLine();
			int intNumber = Integer.parseInt(withdrawAmount);
			sendMessage(new Message(Message.MessageType.WITHDRAW, null, null, intNumber, true));
			response = (String) inputStream.readObject();
			System.out.println(response);
			handleUserOptions();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void handleChangeSettings() {
        sendMessage(new Message(Message.MessageType.SETTINGS, null, null));
        try {
        	for (int i = 0;i < 5; i++) {
                String response = (String) inputStream.readObject();
                System.out.println(response);
        	}
        	String answer = scanner.readLine();
        	if (answer.equals("1")) {
        		changeDisplayName();
        	}
        	if (answer.equals("2")) {
        		changePassword();
        	}
        	if (answer.equals("3"))
        		sendMessage(new Message(Message.MessageType.MAIN_MENU, null, null));
        		handleUserOptions();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void changeDisplayName() {
        sendMessage(new Message(Message.MessageType.CHANGE_NAME, null, null));
        String response;
		try {
			response = (String) inputStream.readObject();
			System.out.println(response);
			String newDisplayName = scanner.readLine();
			sendMessage(new Message(Message.MessageType.CHANGE_NAME, null, null, newDisplayName, null));
			response = (String) inputStream.readObject();
			System.out.println(response);
			handleUserOptions();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void changePassword() {
        sendMessage(new Message(Message.MessageType.CHANGE_PASSWORD, null, null));
        String response;
		try {
			response = (String) inputStream.readObject();
			System.out.println(response);
			String newPassword = scanner.readLine();
			sendMessage(new Message(Message.MessageType.CHANGE_PASSWORD, null, null, null, newPassword));
			response = (String) inputStream.readObject();
			System.out.println(response);
			handleUserOptions();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void handleLogout() {
        try {
            outputStream.writeObject(new Message(Message.MessageType.LOGOUT, null, null));
            // Check if the server has responded or handle accordingly
            String logoutResponse = (String) inputStream.readObject();
            System.out.println(logoutResponse);
        } catch (IOException | ClassNotFoundException e) {
            // Handle IOException and ClassNotFoundException
            e.printStackTrace();
        } finally {
            // Ensure resources are properly closed, even in case of exceptions
            closeResources();
        }
    }
    
    private void closeResources() {
        try {
            // Close the outputStream, inputStream, and socket
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
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
