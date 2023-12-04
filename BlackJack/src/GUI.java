import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

import javax.swing.*;
import javax.swing.border.*;

public class GUI 
{	
	private JFrame contentFrame = new JFrame("BlackJack");
	private JPanel contentPanel;
	private CardLayout cl = new CardLayout();
	private GridBagLayout gbl = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	private GUIListener listener;
	
	private JPanel loginPanel = new JPanel();
	private String loginUserText;
	private String loginPassText;
	private String loginInfoText;
	
	private JPanel testPanel = new JPanel();
	private JPanel menuPanel = new JPanel();
	
	private JPanel tablePanel = new JPanel();
	private JPanel tableTop = new JPanel();
	private JPanel tableButtons = new JPanel();
	private int cardDistance = 15;

	public GUI(Client listener)
	{
		// Creates black/green gradient background
		contentPanel = new JPanel() {
			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                Point2D center = new Point2D.Float(getWidth() / 2, getHeight() / 2);
                float radius = Math.max(getWidth(), getHeight());
                float[] dist = {0.0f, 1.0f};
                Color[] colors = {new Color(2, 107, 14), Color.BLACK};
                RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
		};
		this.listener = listener;
		
		// Sets up different pages of panel
		contentPanel.setLayout(cl);
		this.contentPanel.add(testPanel, "test");
		this.contentPanel.add(loginPanel, "welcome");
		this.contentPanel.add(menuPanel, "menu");
		contentPanel.add(tablePanel, "table");
		cl.show(contentPanel, "welcome");
		
		initializeTestPanel();
		initializeLoginPanel();
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
	
//	public JButton imageButton(String ) {
//		return JButton;
//	}
	
	public void initializeLoginPanel() 
	{
		JLabel msgLabel = new JLabel("test");
		loginPanel.setBackground(new Color(0, 0, 0, 0));
		loginPanel.setOpaque(false);
		loginPanel.setLayout(gbl);
		
		/// Blackjack logo image ///
		JPanel imagePanel = new JPanel();
		ImageIcon originalIcon = new ImageIcon("data/welcomeimage.png");
		JLabel welcomeImage = new JLabel(originalIcon);
		imagePanel.setBackground(new Color(0, 0, 0, 0));
		imagePanel.setOpaque(false);
		imagePanel.setPreferredSize(new Dimension(800, 120));
		imagePanel.addComponentListener(new ComponentAdapter() {
		    @Override	// Resizes image while keeping ratio
		    public void componentResized(ComponentEvent e) {
		        int currWidth = imagePanel.getWidth();
		        int currHeight = imagePanel.getHeight();
		        double ratio = 800.0/400.0;
		        int newWidth;
		        int newHeight;
		        if (currWidth / (double)currHeight > ratio) {
		            newHeight = currHeight;
		            newWidth = (int)(newHeight * ratio);
		        } else {
		            newWidth = currWidth;
		            newHeight = (int)(newWidth / ratio);
		        }
		        Image resizedImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		        welcomeImage.setIcon(new ImageIcon(resizedImage));
		    }
		});
		imagePanel.add(welcomeImage);
		
		/// Input panel ///
		JPanel inputPanel = new JPanel();
		JPanel inputA = new JPanel();
		JPanel inputB = new JPanel();
		inputPanel.setLayout(cl);
		inputPanel.add(inputA, "play");
		inputPanel.add(inputB, "login");
		cl.show(inputPanel, "play");
		inputPanel.setBackground(new Color(0, 0, 0, 0));
		inputPanel.setOpaque(false);
		inputPanel.setPreferredSize(new Dimension(600, 100));
		
		/// Play & Exit button panel ///
		inputA.setLayout(gbl);
		inputA.setBackground(new Color(0, 0, 0, 0));
		inputA.setOpaque(false);
		JPanel playPanel = new JPanel();
		JPanel exitPanel = new JPanel();
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		
		/// Play button ///
		Image playImage = new ImageIcon("data/play_button.png").getImage();
		JButton playButton = new JButton(new ImageIcon(playImage)) {
            @Override
            public Dimension getPreferredSize() {
                int height = playPanel.getHeight();
                int width = (int) (height * (5.0 / 2.0));
                return new Dimension(width, height);
            }
        };
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(inputPanel, "login");
			}
		});
		playPanel.addComponentListener(new ComponentAdapter() {
			@Override
            public void componentResized(ComponentEvent e) {
				int buttonWidth = playButton.getWidth();
				int buttonHeight = playButton.getHeight();
				Image scaledImage = playImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
				playButton.setIcon(new ImageIcon(scaledImage));
				playPanel.revalidate();
				playPanel.repaint();
            }
		});
		playButton.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent evt) {
		    	playPanel.revalidate();
				playPanel.repaint();
				inputPanel.revalidate();
				inputPanel.repaint();
		    }

		    public void mouseExited(MouseEvent evt) {
		    	playPanel.revalidate();
				playPanel.repaint();
				inputPanel.revalidate();
				inputPanel.repaint();
		    }
		});
		playButton.setBackground(new Color(0, 0, 0, 0));
		playButton.setOpaque(false);
		playButton.setBorderPainted(false);
		playButton.setFocusPainted(false);
		playButton.setPreferredSize(new Dimension(500, 200));
		playPanel.add(playButton);
		
	    /// Exit button ///
		Image exitImage = new ImageIcon("data/exit_button.png").getImage();
		JButton exitButton = new JButton(new ImageIcon(exitImage)) {
            @Override
            public Dimension getPreferredSize() {
                int height = exitPanel.getHeight();
                int width = (int) (height * (5.0 / 2.0));
                return new Dimension(width, height);
            }
        };
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitPanel.addComponentListener(new ComponentAdapter() {
			@Override
            public void componentResized(ComponentEvent e) {
				int buttonWidth = exitButton.getWidth();
				int buttonHeight = exitButton.getHeight();
				Image scaledImage = exitImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
				exitButton.setIcon(new ImageIcon(scaledImage));
				exitPanel.revalidate();
				exitPanel.repaint();
				inputPanel.revalidate();
				inputPanel.repaint();
            }
		});
		exitButton.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent evt) {
		    	exitPanel.revalidate();
		    	exitPanel.repaint();
				inputPanel.revalidate();
				inputPanel.repaint();
		    }

		    public void mouseExited(MouseEvent evt) {
		    	exitPanel.revalidate();
		    	exitPanel.repaint();
				inputPanel.revalidate();
				inputPanel.repaint();
		    }
		});
		exitButton.setBackground(new Color(0, 0, 0, 0));
		exitButton.setOpaque(false);
		exitButton.setBorderPainted(false);
		exitButton.setFocusPainted(false);
		exitButton.setPreferredSize(new Dimension(500, 200));
		exitPanel.add(exitButton);
		gbcPanel(inputA, playPanel, 0, 0, 1, 1, 1.0, 1.0);
		gbcPanel(inputA, exitPanel, 0, 1, 1, 1, 1.0, 1.0);
		gbcButton(playPanel, playButton, 0, 0, 1, 1, 1.0, 1.0);
		gbcButton(exitPanel, exitButton, 0, 0, 1, 1, 1.0, 1.0);
		
		/// Login Panel ///
		inputB.setLayout(gbl);
		inputB.setBackground(new Color(0, 0, 0, 0));
		inputB.setOpaque(false);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		
		/// Top row ///
		JPanel fieldPanel = new JPanel();
		Font fieldFont = new Font("Arial", Font.PLAIN, 45);
		JLabel userLabel = new JLabel("Username  ");
		userLabel.setHorizontalAlignment(JLabel.RIGHT);
		userLabel.setForeground(Color.YELLOW);
		userLabel.setFont(new Font("Times New Roman", Font.BOLD, 45));
		JLabel passLabel = new JLabel("Password  ");
		passLabel.setHorizontalAlignment(JLabel.RIGHT);
		passLabel.setForeground(Color.YELLOW);
		passLabel.setFont(new Font("Times New Roman", Font.BOLD, 45));
		JTextField userField = new JTextField(20);
		userField.setFont(fieldFont);
		JPasswordField passField = new JPasswordField(20);
		passField.setFont(fieldFont);
		fieldPanel.setLayout(gbl);
		gbc.insets = new Insets(10, 0, 10, 0);
		gbcLabel(fieldPanel, userLabel, 0, 0, 1, 1, 0.0, 0.0);
		gbcLabel(fieldPanel, passLabel, 0, 1, 1, 1, 0.0, 0.0);
		gbcTextField(fieldPanel, userField, 1, 0, 1, 1, 1.0, 1.0);
		gbcTextField(fieldPanel, passField, 1, 1, 1, 1, 1.0, 1.0);
		gbc.insets = new Insets(0, 0, 0, 0);
		
		/// Bottom Row ///
		JPanel infoPanel = new JPanel();
		JPanel backPanel = new JPanel();
		JPanel signinPanel = new JPanel();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		
		/// Back button ///
		Image backImage = new ImageIcon("data/back_button.png").getImage();
		JButton backButton = new JButton(new ImageIcon(backImage));
        backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(inputPanel, "play");
			}
		});
        backPanel.addComponentListener(new ComponentAdapter() {
			@Override
            public void componentResized(ComponentEvent e) {
				int buttonWidth = backButton.getWidth();
				int buttonHeight = backButton.getHeight();
				Image scaledImage = backImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
				backButton.setIcon(new ImageIcon(scaledImage));
				backPanel.revalidate();
				backPanel.repaint();
				inputPanel.revalidate();
				inputPanel.repaint();
            }
		});
        backButton.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent evt) {
		    	backPanel.revalidate();
		    	backPanel.repaint();
				inputPanel.revalidate();
				inputPanel.repaint();
		    }

		    public void mouseExited(MouseEvent evt) {
		    	backPanel.revalidate();
		    	backPanel.repaint();
				inputPanel.revalidate();
				inputPanel.repaint();
		    }
		});
        backButton.setBackground(new Color(0, 0, 0, 0));
        backButton.setOpaque(false);
        backButton.setBorderPainted(false);
		backButton.setFocusPainted(false);
		backButton.setPreferredSize(new Dimension(125, 50));
		backPanel.add(backButton);
		
		/// Sign in button ///
		Image signinImage = new ImageIcon("data/login_button.png").getImage();
		JButton signinButton = new JButton(new ImageIcon(signinImage));
        signinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
        signinPanel.addComponentListener(new ComponentAdapter() {
			@Override
            public void componentResized(ComponentEvent e) {
				int buttonWidth = signinButton.getWidth();
				int buttonHeight = signinButton.getHeight();
				Image scaledImage = signinImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
				signinButton.setIcon(new ImageIcon(scaledImage));
				signinPanel.revalidate();
				signinPanel.repaint();
				inputPanel.revalidate();
				inputPanel.repaint();
            }
		});
        signinButton.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent evt) {
		    	signinPanel.revalidate();
		    	signinPanel.repaint();
				inputPanel.revalidate();
				inputPanel.repaint();
		    }

		    public void mouseExited(MouseEvent evt) {
		    	signinPanel.revalidate();
		    	signinPanel.repaint();
				inputPanel.revalidate();
				inputPanel.repaint();
		    }
		});
		signinButton.setBackground(new Color(0, 0, 0, 0));
		signinButton.setOpaque(false);
		signinButton.setBorderPainted(false);
		signinButton.setFocusPainted(false);
		signinButton.setPreferredSize(new Dimension(125, 50));
		signinPanel.add(signinButton);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbcPanel(inputB, infoPanel, 0, 0, 2, 1, 1.0, 0.0);
		gbcPanel(inputB, fieldPanel, 0, 1, 2, 1, 1.0, 1.0);
		gbcPanel(inputB, backPanel, 0, 2, 1, 1, 1.0, 0.0);
		gbcPanel(inputB, signinPanel, 1, 2, 1, 1, 1.0, 0.0);
		
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbcPanel(loginPanel, imagePanel, 0, 0, 1, 1, 1.0, 1.0);
		gbc.fill = GridBagConstraints.VERTICAL;
		gbcPanel(loginPanel, inputPanel, 0, 1, 1, 1, 1.0, 1.0);
		System.out.println("Login panel created.");
	}
	
	public void initializeMenuPanel() 
	{
		JPanel buttonPanel = new JPanel();
		JPanel titlePanel = new JPanel();
		JPanel exitPanel = new JPanel();
		JPanel imagePanel = new JPanel();
		
		menuPanel.setBackground(new Color(0, 100, 0));
		menuPanel.setLayout(new GridBagLayout());
		
//		gbcPanel(menuPanel, titlePanel, 0, 0, 1, 1);
//		gbcPanel(menuPanel, buttonPanel, 0, 1, 1, 2);
//		gbcPanel(menuPanel, new JPanel(), 0, 3, 1, 1);
//		gbcPanel(menuPanel, exitPanel, 0, 4, 1, 1);
//		gbcPanel(menuPanel, imagePanel, 1, 0, 2, 5);
		
	}
	
	public void initializeTablePanel() 
	{
		JPanel player1 = new JPanel();		// Client player
		JPanel player2 = new JPanel();
		JPanel player3 = new JPanel();
		JPanel player4 = new JPanel();
		JPanel player5 = new JPanel();
		JPanel player6 = new JPanel();
		JPanel player7 = new JPanel();
		JPanel dealer = new JPanel();
		
		JButton hitButton = new JButton("Hit");
		JButton standButton = new JButton("Stand");
		
		GridBagLayout layout = new GridBagLayout();
		tablePanel.setLayout(layout);
		tablePanel.setPreferredSize(new Dimension(1280 , 720));
		tableTop.setLayout(layout);
		
		player1.setLayout(layout);
		player2.setLayout(layout);
		player3.setLayout(layout);
		player4.setLayout(layout);
		player5.setLayout(layout);
		player6.setLayout(layout);
		player7.setLayout(layout);
		dealer.setLayout(layout);
		
		ArrayList<Card> cards = new ArrayList<>();
		cards.add(new Card("5", Suit.DIAMONDS, 5));
		cards.add(new Card("9", Suit.HEARTS, 9));
		cards.add(new Card("Queen", Suit.HEARTS, 12));
		cards.add(new Card("Ace", Suit.CLUBS, 1));
		cards.add(new Card("2", Suit.SPADES, 2));
		cards.add(new Card("Jack", Suit.CLUBS, 11));
		cards.add(new Card("7", Suit.DIAMONDS, 7));
		
		gbc.insets = new Insets(0, 0, 0, 0);
//		gbcPanel(tablePanel, player1, 3, 4, 1, 2);
//		gbcPanel(tablePanel, player2, 2, 4, 1, 2);
//		gbcPanel(tablePanel, player3, 4, 4, 1, 2);
//		gbcPanel(tablePanel, player4, 1, 3, 1, 2);
//		gbcPanel(tablePanel, player5, 5, 3, 1, 2);
//		gbcPanel(tablePanel, player6, 0, 2, 1, 2);
//		gbcPanel(tablePanel, player7, 7, 2, 1, 2);
//		gbcPanel(tablePanel, dealer, 3, 1, 1, 3);
//		gbcPanel(tablePanel, tableButtons, 0, 6, 8, 2);
//		gbcPanel(tablePanel, tableTop, 0, 0, 8, 1);
		tableButtons.add(hitButton);
		tableButtons.add(standButton);
		
		gbcLayeredPane(player1, playerCardsGUI(cards), 0, 0, 1, 1);
		gbcLayeredPane(player2, playerCardsGUI(cards), 0, 0, 1, 1);
		gbcLayeredPane(player3, playerCardsGUI(cards), 0, 0, 1, 1);
		gbcLayeredPane(player4, playerCardsGUI(cards), 0, 0, 1, 1);
		gbcLayeredPane(player5, playerCardsGUI(cards), 0, 0, 1, 1);
		gbcLayeredPane(player6, playerCardsGUI(cards), 0, 0, 1, 1);
		gbcLayeredPane(player7, playerCardsGUI(cards), 0, 0, 1, 1);
		gbcLayeredPane(dealer, playerCardsGUI(cards), 0, 0, 1, 1);
	}
	
	public void playerGUI() {
		
	}
	
	// Method which creates visualization of hand of cards
	public JLayeredPane playerCardsGUI(ArrayList<Card> cards) 
	{
		JLayeredPane layeredPane = new JLayeredPane();
		JPanel panel = new JPanel();
		layeredPane.setOpaque(true);
//		layeredPane.setLayout(new GridBagLayout());
		// bounds = width: cardWidth + (cardDistance 
		int width = cards.get(0).getWidth() + ((cards.size()-1)*cardDistance);
		int height = cards.get(0).getHeight() + ((cards.size()-1)*cardDistance);
		layeredPane.setBounds(0, 0, width, height);
		layeredPane.setPreferredSize(new Dimension(width, height));
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
	
	private void gbcPanel(JPanel container, JPanel component, 
			int x, int y, int w, int h, double weightx, double weighty) 
	{
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		component.setBorder(new TitledBorder("(" + x + ", " + y + ")"));
		component.setBackground(new Color(0, 0, 0, 0));
		component.setOpaque(false);
		container.add(component, gbc);
	}
	
	private void gbcButton(JPanel container, JButton component, 
			int x, int y, int w, int h, double weightx, double weighty) 
	{
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		component.setBorder(new TitledBorder("(" + x + ", " + y + ")"));
		container.add(component, gbc);
	}
	
	private void gbcTextField(JPanel container, JTextField component, 
			int x, int y, int w, int h, double weightx, double weighty) 
	{
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		component.setBorder(new TitledBorder("(" + x + ", " + y + ")"));
		container.add(component, gbc);
	}
	
	private void gbcLabel(JPanel container, JLabel component, 
			int x, int y, int w, int h, double weightx, double weighty) 
	{
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		component.setBorder(new TitledBorder("(" + x + ", " + y + ")"));
		container.add(component, gbc);
	}
	
	private void gbcLayeredPane(JPanel container, JLayeredPane component, int x, int y, int w, int h) 
	{
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		component.setBorder(new TitledBorder("(" + x + ", " + y + ")"));
		container.add(component, gbc);
	}
	
	public void closeConnection() throws IOException 
	{
		listener.guiExit();
		System.exit(0);
	}
	
	public void initializeTestPanel() 
	{
		
		
//		JLabel layerA = new JLabel();
//		layerA.setBackground(Color.red);
//		layerA.setOpaque(true);
//		layerA.setBounds(0, 0, 400, 400);
//		
//		JLabel layerB = new JLabel();
//		layerB.setBackground(Color.green);
//		layerB.setOpaque(true);
//		layerB.setBounds(100, 100, 400, 400);
//		
//		JLabel layerC = new JLabel();
//		layerC.setBackground(Color.blue);
//		layerC.setOpaque(true);
//		layerC.setBounds(200, 200, 400, 400);
//
//		JLayeredPane layeredPane = new JLayeredPane();
//		layeredPane.setBounds(0,0, 800, 800);
//		
//		layeredPane.add(layerA, JLayeredPane.DEFAULT_LAYER);
//		layeredPane.add(layerB, JLayeredPane.DEFAULT_LAYER);
//		layeredPane.add(layerC, JLayeredPane.DEFAULT_LAYER);
//		
//		testPanel.add(layeredPane);
		
		testPanel.setLayout(new GridBagLayout());
//		ArrayList<Card> cards = new ArrayList<>();
//		cards.add(new Card("5", Suit.DIAMONDS, 5));
//		cards.add(new Card("9", Suit.HEARTS, 9));
//		cards.add(new Card("Queen", Suit.HEARTS, 12));
//		gbcLayeredPane(testPanel, playerCardsGUI(cards), 0, 0, 1, 1);
		
//		Card card = new Card("5", Suit.DIAMONDS, 5);
//		ImageIcon originalIcon = card.getCardFront();
//		JLabel image = new JLabel(card.getCardFront());
//		gbcLabel(testPanel, image, 0, 0, 1, 1);
//		testPanel.addComponentListener(new ComponentAdapter() {
//		    @Override
//		    public void componentResized(ComponentEvent e) {
//		        int width = testPanel.getWidth();
//		        int height = testPanel.getHeight();
//		        Image resizedImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
//		        image.setIcon(new ImageIcon(resizedImage));
//		        
//		        
//		    }
//		});
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridBagLayout());
		JPanel panel2 = new JPanel();
//		gbcPanel(testPanel, panel1, 0, 0, 1, 1);
//		gbcPanel(testPanel, panel2, 0, 1, 1, 1);
		
		ArrayList<Card> cards = new ArrayList<>();
		cards.add(new Card("5", Suit.DIAMONDS, 5));
		cards.add(new Card("9", Suit.HEARTS, 9));
		cards.add(new Card("Queen", Suit.HEARTS, 12));
		cards.add(new Card("Ace", Suit.CLUBS, 1));
		cards.add(new Card("2", Suit.SPADES, 2));
		cards.add(new Card("Jack", Suit.CLUBS, 11));
		cards.add(new Card("7", Suit.DIAMONDS, 7));
		
		//testPanel.add(playerCardsGUI(cards));
		gbcLayeredPane(panel1, playerCardsGUItest(cards, panel1), 0, 0, 1, 1);
		contentPanel.repaint();
		contentPanel.revalidate();
	}
	
	public JLayeredPane playerCardsGUItest(ArrayList<Card> cards, JPanel parent) 
	{
		JLayeredPane layeredPane = new JLayeredPane();
		JPanel panel = new JPanel();
		layeredPane.setBackground(Color.red);
		layeredPane.setOpaque(true);
//		layeredPane.setLayout(new GridBagLayout());
		// bounds = width: cardWidth + (cardDistance 
		int width = cards.get(0).getWidth() + ((cards.size()-1)*cardDistance);
		int height = cards.get(0).getHeight() + ((cards.size()-1)*cardDistance);
		layeredPane.setBounds(0, 0, width, height);
		layeredPane.setPreferredSize(new Dimension(width, height));
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
		parent.addComponentListener(new ComponentAdapter() {
			 public void componentResized(ComponentEvent e) {
		            // Calculate new card sizes and positions based on parentPanel size
		            int newWidth = parent.getWidth();
		            int newHeight = parent.getHeight();
		            System.out.println("Width:" + newWidth);
		            System.out.println("Height:" + newHeight);
		            // ... (code to calculate new sizes and positions of cards)

		            // Update card sizes and positions
		            for (int i = 0; i < cards.size(); i++) {
		                JLabel cardLabel = (JLabel) layeredPane.getComponent(i);
		                // Update cardLabel size and position
		                // cardLabel.setBounds(...); // Set new bounds
		            }

		            // Refresh the display
		            layeredPane.repaint();
		        }
		});
		return layeredPane;
	}
	
}