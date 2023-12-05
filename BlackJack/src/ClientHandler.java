import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private Server server;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Player player;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;

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
        	while (!pass) {
        		Message receivedMessage = (Message) inputStream.readObject();
            	pass = handleLogin(receivedMessage);
        	}
        	
        	// Continuously handle client messages
            while (true) {
            	
            	Message clientMessage = (Message) inputStream.readObject();
            	// Check for log out message
            	if(clientMessage.getType() == Message.MessageType.LOGOUT) {
                    outputStream.writeObject("Logout successful. Goodbye!");
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
                handleLogout(message);
                break;
            }
            default:
            	outputStream.writeObject("Invalid message type.");
        }
    }

    private boolean handleLogin(Message message) throws IOException {
        String userId = message.getUserId();
        String password = message.getPassword();
        boolean verified = false;

        if (server.verifyCredentials(userId, password)) {
            player = server.getPlayer(userId);
            outputStream.writeObject("Authentication successful. You are now connected.");
            verified = true;
            return verified;
        } else {
        	outputStream.writeObject("Invalid credentials. Try again!");
        	return verified;
        	//closeResources();
        }
    }

    private void handleFindTable(Message message) {
        // Implement logic to find or create a table
			server.findSeat(player);
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
                    outputStream.writeObject("Invalid option. Please try again.");
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
                    outputStream.writeObject("Invalid option. Please try again.");
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
            
            // Save changes to data file
            server.savePlayerDataToFile();

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
            
            // Save changes to data file
            server.savePlayerDataToFile();

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
            
            // Save changes to data file
            server.savePlayerDataToFile();

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
            
            // Save changes to data file
            server.savePlayerDataToFile();

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
