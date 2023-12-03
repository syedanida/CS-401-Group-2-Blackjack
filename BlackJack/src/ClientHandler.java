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
        // Read and deserialize the message from the client
        String serializedMessage = input.readLine();
        Message receivedMessage = MessageDeserializer.deserialize(serializedMessage);

        // Process the received message
        processMessage(receivedMessage);
    }

    private void processMessage(Message message) throws IOException {
        switch (message.getType()) {
            case LOGIN:
                handleLogin(message);
                break;
            case FIND_TABLE:
                handleFindTable(message);
                break;
            case BANK_DETAILS:
                handleBankDetails(message);
                break;
            case SETTINGS:
                handleSettings(message);
                break;
            case LOGOUT:
                handleLogout(message);
                break;
            default:
                output.println("Invalid message type.");
        }
    }

    private void handleLogin(Message message) throws IOException {
        String userId = message.getUserId();
        String password = message.getPassword();

        if (server.verifyCredentials(userId, password)) {
            player = server.getPlayer(userId);
            output.println("Authentication successful. You are now connected.");

            // Handle user options
            handleUserOptions();
        } else {
            output.println("Invalid credentials. Connection terminated.");
        }
    }

    private void handleUserOptions() throws IOException {
        boolean loggedIn = true;

        while (loggedIn) {
            output.println("Select an option:");
            output.println("1. Find a table");
            output.println("2. View bank details");
            output.println("3. Change settings");
            output.println("4. Logout");

            // Read and deserialize the user's choice
            String serializedChoice = input.readLine();
            Message userChoiceMessage = MessageDeserializer.deserialize(serializedChoice);

            switch (userChoiceMessage.getType()) {
                case FIND_TABLE:
                    handleFindTable(userChoiceMessage);
                    break;
                case BANK_DETAILS:
                    handleBankDetails(userChoiceMessage);
                    break;
                case SETTINGS:
                    handleSettings(userChoiceMessage);
                    break;
                case LOGOUT:
                    handleLogout(userChoiceMessage);
                    loggedIn = false;
                    break;
                default:
                    output.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleFindTable(Message message) {
        // Implement logic to find or create a table
        output.println("Finding a Blackjack table...");
    }

    private void handleBankDetails(Message message) {
        // Implement logic to show bank details and options
        output.println("Viewing bank details...");
    }

    private void handleSettings(Message message) {
        // Implement logic to handle settings options
        output.println("Changing settings...");
    }

    private void handleLogout(Message message) {
        output.println("Logout successful. Goodbye!");
        closeResources();
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
