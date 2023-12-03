import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

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
	private int cardDistance = 15;

	public GUI(Client listener)
	{
		this.listener = listener;
		// Sets up main panel content
		contentPanel.setLayout(cl);
//		this.contentPanel.add(testPanel, "test");
//		this.contentPanel.add(loginPanel, "welcome");
//		this.contentPanel.add(menuPanel, "menu");
		contentPanel.add(tablePanel, "table");
		cl.show(contentPanel, "table");
		
		// Sets up pages (row, col)
//		initializeTestPanel();
		System.out.println("test page created");
		initializeLoginPanel(2, 1);
		System.out.println("login page created");
		initializeTablePanel();
		initializeMenuPanel();
		
		contentPanel.repaint();
		contentPanel.revalidate();
		contentPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	contentPanel.repaint();
        		contentPanel.revalidate();
            }
        });
		// Sets up main frame
		contentFrame.add(contentPanel);
		//this.contentFrame.setPreferredSize(new Dimension(1000, 800));
		contentFrame.setVisible(true);
		contentFrame.pack();
		contentFrame.addWindowListener(new WindowAdapter() {
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
	
	public void initializeTablePanel() 
	{
		tablePanel.setLayout(new GridBagLayout());
		
		ArrayList<Card> cards = new ArrayList<>();
		cards.add(new Card("5", Suit.DIAMONDS, 5));
		cards.add(new Card("9", Suit.HEARTS, 9));
		cards.add(new Card("Queen", Suit.HEARTS, 12));
		
		
		gbcLayeredPane(tablePanel, createCardsGUI(cards), 0, 0, 1, 1);
	}
	
	public JLayeredPane createCardsGUI(ArrayList<Card> cards) 
	{
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, 75+(cardDistance*cards.size()), 100+(cardDistance*cards.size()));
		int x = 0;
		int y = 0;
		int layer = 1;
		for(int i = 0; i < cards.size(); i++) {
			ImageIcon icon = cards.get(i).getCardFront();
			JLabel image = new JLabel(icon);
			image.setOpaque(true);
			image.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
			x += cardDistance;
			y += cardDistance;
			layeredPane.add(image, Integer.valueOf(layer++));
		}
		return layeredPane;
	}
	
	
	private void gbcPanel(JPanel container, JPanel component, int x, int y, int w, int h) 
	{
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		component.setBorder(new TitledBorder("(" + x + ", " + y + ")"));
//		component.add(new JTextField("(" + w + ", " + h + ")"));
		container.add(component, gbc);
	}
	
	private void gbcButton(JPanel container, JButton component, int x, int y, int w, int h) 
	{
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		component.setBorder(new TitledBorder("(" + x + ", " + y + ")"));
//		component.add(new JTextField("(" + w + ", " + h + ")"));
		container.add(component, gbc);
	}
	
	private void gbcLayeredPane(JPanel container, JLayeredPane component, int x, int y, int w, int h) 
	{
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		component.setBorder(new TitledBorder("(" + x + ", " + y + ")"));
//		component.add(new JTextField("(" + w + ", " + h + ")"));
		container.add(component, gbc);
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
		
		contentPanel.repaint();
		contentPanel.revalidate();
		
		
	}
	
}