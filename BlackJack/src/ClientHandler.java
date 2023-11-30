import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private Server server;
    private BufferedReader input;
    private PrintWriter output;
    private Player player;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;

        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Handle the client connection
            handleClient();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Ensure resources are properly closed
            closeResources();
        }
    }

    private void handleClient() throws IOException {
        // Verify credentials with the server
        String credentials = input.readLine();
        String[] credentialsArray = credentials.split(",");
        
        if (credentialsArray.length == 2) {
            String userId = credentialsArray[0];
            String password = credentialsArray[1];

            if (server.verifyCredentials(userId, password)) {
                player = server.getPlayer(userId);
                output.println("Authentication successful. You are now connected.");

                // Handle user options
                handleUserOptions();
            } else {
                output.println("Invalid credentials. Connection terminated.");
            }
        } else {
            output.println("Invalid credentials format. Connection terminated.");
        }
    }

    private void handleUserOptions() throws IOException {
        boolean loggedIn = true;

        while (loggedIn) {
            output.println("Select an option:");
            output.println("1. Find a Blackjack table");
            output.println("2. View bank details");
            output.println("3. Change settings");
            output.println("4. Logout");

            String userChoice = input.readLine();

            System.out.println("User choice: " + userChoice);  // Debugging output

            if (userChoice != null) {  // Check if userChoice is not null
                switch (userChoice) {
                    case "1":
                        // Handle finding a Blackjack table
                        System.out.println("Handling option 1");  // Debugging output
                        handleFindTable();
                        break;
                    case "2":
                        // Handle viewing bank details
                        System.out.println("Handling option 2");  // Debugging output
                        handleViewBankDetails();
                        break;
                    case "3":
                        // Handle changing settings
                        System.out.println("Handling option 3");  // Debugging output
                        handleChangeSettings();
                        break;
                    case "4":
                        // Handle logging out
                        System.out.println("Handling option 4");  // Debugging output
                        output.println("Logout successful. Goodbye!");
                        loggedIn = false;
                        closeResources(); // Close resources and terminate the connection
                        break;
                    default:
                        output.println("Invalid option. Please try again.");
                }
            }
        }
    }

    private void handleFindTable() {
        // Implement logic to find a Blackjack table
        output.println("Finding a Blackjack table...");
    }

    private void handleViewBankDetails() {
        // Implement logic to view bank details
        output.println("Viewing bank details...");
    }

    private void handleChangeSettings() {
        // Implement logic to change settings
        output.println("Changing settings...");
    }

    private void closeResources() {
        try {
            input.close();
            output.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}