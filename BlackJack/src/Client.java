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
            Message loginMessage = new Message(MessageType.LOGIN, userId, password);
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
        sendMessage(new Message(MessageType.FIND_TABLE, null, null));
    }

    private void handleBankDetails() {
    	System.out.println("calling BankDetails");
        sendMessage(new Message(MessageType.BANK_DETAILS, null, null));
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
    	sendMessage(new Message(MessageType.DEPOSIT, null, null));
		String response;
		try {
			System.out.println("Inside handle deposit");
			response = (String) inputStream.readObject();
			System.out.println(response);
			String depositAmount = scanner.readLine();
			int intNumber = Integer.parseInt(depositAmount);
			sendMessage(new Message(MessageType.DEPOSIT, null, null, intNumber));
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
    	sendMessage(new Message(MessageType.WITHDRAW, null, null));
		String response;
		try {
			System.out.println("Inside handle withdraw");
			response = (String) inputStream.readObject();
			System.out.println(response);
			String withdrawAmount = scanner.readLine();
			int intNumber = Integer.parseInt(withdrawAmount);
			sendMessage(new Message(MessageType.WITHDRAW, null, null, intNumber, true));
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
        sendMessage(new Message(MessageType.SETTINGS, null, null));
        try {
        	for (int i = 0;i < 5; i++) {
                String response = (String) inputStream.readObject();
                System.out.println(response);
        	}
        	String answer = scanner.readLine();
        	if (answer.equals("1")) {
        		System.out.println("calling change displayname");
        		changeDisplayName();
        	}
        	if (answer.equals("2")) {
        		System.out.println("calling change password");
        		changePassword();
        	}
        	if (answer.equals("3"))
        		handleUserOptions();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void changeDisplayName() {
        sendMessage(new Message(MessageType.CHANGE_NAME, null, null));
        String response;
		try {
			System.out.println("inside change displayname");
			response = (String) inputStream.readObject();
			System.out.println(response);
			String newDisplayName = scanner.readLine();
			sendMessage(new Message(MessageType.WITHDRAW, null, null, newDisplayName, null));
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
    
    private void changePassword() {
    	System.out.println("inside changepassword");
        sendMessage(new Message(MessageType.CHANGE_PASSWORD, null, null));
        try {
        	for (int i = 0;i < 4; i++) {
                String response = (String) inputStream.readObject();
                System.out.println(response);
        	}
        	String answer = scanner.readLine();
        	if (answer.equals("1")) {
        		System.out.println("calling change displayname");
        		changeDisplayName();
        	}
        	if (answer.equals("2")) {
        		System.out.println("calling change password");
        		changePassword();
        	}
        	if (answer.equals("3"))
        		handleUserOptions();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        try {
            sendMessage(new Message(MessageType.LOGOUT, null, null));
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


//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class Client implements GUIListener{
//
//    private static final String SERVER_ADDRESS = "localhost"; //Change during presentation
//    private static final int SERVER_PORT = 6000; 
//
//    private Socket socket;
//    private BufferedReader input;
//    private PrintWriter output;
//    private Scanner scanner;
//    private GUI gui;
//    
//    private String verifyLogin;
//    private String verifyPass;
//
//    public Client() {
//        scanner = new Scanner(System.in);
//        gui = new GUI(this);
//        
//    }
//
//    public void connectToServer() {
//        try {
//            // Connect to the server
//            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
//            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            output = new PrintWriter(socket.getOutputStream(), true);
//
//            // Start a new thread to listen for server messages
//            new Thread(this::listenForServerMessages).start();
//
//            // Send user credentials to the server for verification
//            //authenticateUser();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void authenticateUser() {
//        try {
//            System.out.println("Enter your user ID:");
//            String userId = scanner.nextLine();
//
//            System.out.println("Enter your password:");
//            String password = scanner.nextLine();
//
//            // Send credentials to the server for verification
//            output.println(userId + "," + password);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void listenForServerMessages() {
//        try {
//            String message;
//            while ((message = input.readLine()) != null) {
//                System.out.println("Server says: " + message);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//	public void guiVerifyLogin(String username, String password) {
//		System.out.println(username);
//		System.out.println(password);
//	}
//	
//	public void guiExit() throws IOException {
//		System.out.println("Closing client");
////		socket.close();
//		System.exit(0);
//	}
//	
//	public static void main(String[] args) {
//    	Client client = new Client();
//        client.connectToServer();
//    }
//}