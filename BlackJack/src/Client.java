import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements GUIListener{

    private static final String SERVER_ADDRESS = "localhost"; //Change during presentation
    private static final int SERVER_PORT = 6000; 

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Scanner scanner;
    private GUI gui;
    
    private String verifyLogin;
    private String verifyPass;

    public Client() {
        scanner = new Scanner(System.in);
        gui = new GUI(this);
        
    }

    public void connectToServer() {
        try {
            // Connect to the server
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            // Start a new thread to listen for server messages
            new Thread(this::listenForServerMessages).start();

            // Send user credentials to the server for verification
            //authenticateUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void authenticateUser() {
        try {
            System.out.println("Enter your user ID:");
            String userId = scanner.nextLine();

            System.out.println("Enter your password:");
            String password = scanner.nextLine();

            // Send credentials to the server for verification
            output.println(userId + "," + password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenForServerMessages() {
        try {
            String message;
            while ((message = input.readLine()) != null) {
                System.out.println("Server says: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void guiVerifyLogin(String username, String password) {
		System.out.println(username);
		System.out.println(password);
	}
	
	public void guiExit() throws IOException {
		System.out.println("Closing client");
		socket.close();
	}
	
	public static void main(String[] args) {
    	Client client = new Client();
        client.connectToServer();
    }
}