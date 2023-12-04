import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class GUI 
{
	private JFrame contentFrame = new JFrame("BlackJack");
	private JPanel contentPanel = new JPanel();
	private CardLayout cl = new CardLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	private GUIListener listener;
	
	private JPanel[][] loginGrid;
	private JPanel loginPanel = new JPanel();
	private JPanel loginSubPanel = new JPanel();
	private JPanel loginButtons = new JPanel();
	private JPanel loginInput = new JPanel();
	private String loginUserText;
	private String loginPassText;
	
	private JPanel testPanel = new JPanel();
	private JPanel menuPanel = new JPanel();
	private JPanel tablePanel = new JPanel();

	public GUI(Client listener) 
	{
		this.listener = listener; // ERROR I FORGOT ABOUT
		// Sets up main panel content
		this.contentPanel.setLayout(cl);
		this.contentPanel.add(testPanel, "test");
		this.contentPanel.add(loginPanel, "welcome");
		this.contentPanel.add(menuPanel, "menu");
		this.cl.show(contentPanel, "test");
		
		// Sets up pages (row, col)
		initializeTestPanel();
		System.out.println("test page created");
		initializeLoginPanel(2, 1);
		System.out.println("login page created");
		initializeMenuPanel();
		
		this.contentPanel.repaint();
		this.contentPanel.revalidate();
		// Sets up main frame
		this.contentFrame.add(contentPanel);
		this.contentFrame.setPreferredSize(new Dimension(1000, 800));
		this.contentFrame.setVisible(true);
		this.contentFrame.pack();
		this.contentFrame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	try {
					closeConnection();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		    }
		});
	}
	
	public void initializeLoginPanel(int row, int col) 
	{
		JButton playButton = new JButton("Play");
		JButton exitButton = new JButton("Exit");
		ImageIcon welcomeIcon = new ImageIcon("data/welcomeimage.png");
		JLabel welcomeImage = new JLabel();
		
		JLabel msgLabel = new JLabel("test");
		JLabel userLabel = new JLabel("Username");
		JLabel passLabel = new JLabel("Password");
		JTextField userField = new JTextField(20);
		JPasswordField passField = new JPasswordField(20);
		JButton loginButton = new JButton("Login");
		
		loginPanel.setBackground(new Color(0, 100, 0));
		loginPanel.setLayout(new GridBagLayout());
		
		// Sets up initial page
		Image img = welcomeIcon.getImage();
		Image welcomeImg = img.getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
		welcomeIcon = new ImageIcon(welcomeImg);
		welcomeImage.setIcon(welcomeIcon);
		gbc.insets = new Insets(10, 50, 10, 50);
		gbc.gridx = 0;
		gbc.gridy = 0;
		loginPanel.add(welcomeImage, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		loginPanel.add(loginSubPanel, gbc);
		
		
		// Sets up changing page
		loginSubPanel.setLayout(cl);
		loginSubPanel.add(loginButtons, "play");
		loginSubPanel.add(loginInput, "login");
		cl.show(loginSubPanel, "play");
		
		// Setting up buttons
		loginButtons.setOpaque(true);
		//loginButtons.setBackground(new Color(0,0,0,0));
		loginButtons.setLayout(new BoxLayout(loginButtons, BoxLayout.Y_AXIS));
		loginButtons.add(Box.createRigidArea(new Dimension(0, 30)));
		loginButtons.add(playButton);
		loginButtons.add(Box.createRigidArea(new Dimension(0, 10)));
		loginButtons.add(exitButton);
		
		// Sets up text fields
		loginInput.setOpaque(true);
		//loginInput.setBackground(new Color(0,0,0,0));
		loginInput.setLayout(new GridBagLayout());
		
		// Initial 'Play' button
		playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(loginSubPanel, "login");
			}
		});
		// Initial 'Exit' button
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					closeConnection();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		// User input fields
		gbc.insets = new Insets(5, 5, 5, 5);
		// First row //
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		loginInput.add(msgLabel, gbc);
		/// Second row ///
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		loginInput.add(userLabel, gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		loginInput.add(userField, gbc);
		/// Third row ///
		gbc.gridx = 0;
		gbc.gridy = 2;
		loginInput.add(passLabel, gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		loginInput.add(passField, gbc);
		/// Fourth row ///
		gbc.gridx = 1;
		gbc.gridy = 3;
		loginButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				// Verifies input fields then sends to client
				loginUserText = userField.getText();
				loginPassText = passField.getText();
				System.out.println("click");
				
				if(loginUserText.isBlank() || loginPassText.isBlank()) {
					msgLabel.setText("Please fill empty fields");
				} else {
					listener.guiVerifyLogin(loginUserText, loginPassText);
//					cl.show(contentPanel, "menu");
//					contentFrame.revalidate();
//					contentFrame.repaint();
				}
			}
		});
		loginInput.add(loginButton, gbc);
		
	}
	
	public void initializeMenuPanel() 
	{
		menuPanel.setBackground(new Color(0, 100, 0));
		menuPanel.setLayout(new GridBagLayout());
		JButton testButton = new JButton("Test");
		menuPanel.add(testButton);
		testButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(loginSubPanel, "welcome");
				contentFrame.revalidate();
				contentFrame.repaint();
			}
		});
	}
	
	public void closeConnection() throws IOException 
	{
		listener.guiExit();
		//System.exit(0);
	}
	
	public void initializeTestPanel() 
	{
		
		
		JLabel layerA = new JLabel();
		layerA.setBackground(Color.red);
		layerA.setOpaque(true);
		layerA.setBounds(0, 0, 400, 400);
		
		JLabel layerB = new JLabel();
		layerB.setBackground(Color.green);
		layerB.setOpaque(true);
		layerB.setBounds(100, 100, 400, 400);
		
		JLabel layerC = new JLabel();
		layerC.setBackground(Color.blue);
		layerC.setOpaque(true);
		layerC.setBounds(200, 200, 400, 400);

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(0,0, 800, 800);
		
		layeredPane.add(layerA, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(layerB, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(layerC, JLayeredPane.DEFAULT_LAYER);
		
		testPanel.add(layeredPane);
		testPanel.setLayout(null);
		
		
		
		this.contentPanel.repaint();
		this.contentPanel.revalidate();
		
		
	}
	
}