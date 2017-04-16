import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import myOS.MyOS;


public class MyPhonePanel extends JPanel implements ActionListener {
	
	////////////////////////////////////////////////////////////////////////////
	/* CONSTANT Variables */
	
	public static final int MY_PHONE_PANEL_WIDTH = 375;
	public static final int MY_PHONE_PANEL_HEIGHT = 667;
	
	public static final int PHONE_BUTTON_POSITION_X = 153;
	public static final int PHONE_BUTTON_POSITION_Y = 580;
	
	public static final String PHONE_IMAGE_PATH =
			"." + File.separator + "res" + File.separator + "iphone7.gif";
	
	public static final String PHONE_BUTTON_IMAGE_PATH = 
			"." + File.separator + "res" + File.separator + "iphone-button.gif";
	
	////////////////////////////////////////////////////////////////////////////
	/* MEMBER Variables */
	
	private Image phoneImage;
	private ImageIcon phoneButtonImage;
	private JButton phoneButton;
	private JWebBrowser browser;
	
	MyOS os;

	
	////////////////////////////////////////////////////////////////////////////
	/* CONSTRUCTORS */
	
	/**
	 * Constructs a panel that represents a phone. 
	 */
	public MyPhonePanel()
	{
		
		// loads images for iPhone
		ImageIcon phoneImageIcon = new ImageIcon(PHONE_IMAGE_PATH);
		phoneImage = phoneImageIcon.getImage();
		phoneButtonImage = new ImageIcon(PHONE_BUTTON_IMAGE_PATH);
		
		// make phone button 
		phoneButton = new JButton(phoneButtonImage);
		phoneButton.setBorder(BorderFactory.createEmptyBorder());
		phoneButton.setContentAreaFilled(false);
		phoneButton.addActionListener(this);
		
		// add phone button
		setLayout(null);
		phoneButton.setBounds(	// set position of the button
				PHONE_BUTTON_POSITION_X, 
				PHONE_BUTTON_POSITION_Y, 
				phoneButtonImage.getIconWidth(), 
				phoneButtonImage.getIconHeight());
		
		add(phoneButton);
		
		browser = new JWebBrowser();
		browser.setBounds(15, 60, 345, 500);
		browser.setMenuBarVisible(false);
		browser.setStatusBarVisible(false);
		browser.setLocationBarVisible(false);
		browser.setButtonBarVisible(false);
		add(browser);
		
		os = new MyOS(browser, MainApp.class);
		
		os.loadApp(PhoneAddressApp.class, "Addrss Book");
		os.loadApp(MessageApp.class, "Messages");
		os.loadApp(CallLogApp.class, "Call logs");
		
		phoneButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				os.executeMainApp();
			}
		});
		
		setVisible(true);
	}
	
	@Override
	public void paint(Graphics g)
	{
		
		// draw background (phone image)
		g.drawImage(phoneImage, 0, 0, 
				MY_PHONE_PANEL_WIDTH, MY_PHONE_PANEL_HEIGHT, null);
		setOpaque(false);
		super.paint(g);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		
	}
	
	public void exit()
	{
		this.os.shutdown();
	}

	////////////////////////////////////////////////////////////////////////////
}
