import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;

public class MyPhone extends JFrame {
	
	////////////////////////////////////////////////////////////////////////////
	/* CONSTANT Variables */
	
	public static final int MY_PHONE_FRAME_WIDTH = 381;
	public static final int MY_PHONE_FRAME_HEIGHT = 702;
	
	////////////////////////////////////////////////////////////////////////////
	/* MEMBER Variables */
	
	private JPanel phonePanel;
	
	private String frameTitle;
	
	////////////////////////////////////////////////////////////////////////////
	/**/
	
	public static void main(String[] args)
	{
		UIUtils.setPreferredLookAndFeel();
		NativeInterface.open();
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				JFrame frame = new MyPhone();
				frame.setVisible(true);
				frame.addWindowListener(new WindowListener() {
					
					@Override
					public void windowOpened(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void windowIconified(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void windowDeiconified(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void windowDeactivated(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void windowClosing(WindowEvent e) {
						// TODO Auto-generated method stub
						((MyPhone) frame).exit();
					}
					
					@Override
					public void windowClosed(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void windowActivated(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		
		NativeInterface.runEventPump();
	}
	

	
	////////////////////////////////////////////////////////////////////////////
	/* CONSTRUCTORS */
	
	public MyPhone()
	{
		
		frameTitle = "MyPhone";
		setTitle(frameTitle);
		setSize(MY_PHONE_FRAME_WIDTH, MY_PHONE_FRAME_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		phonePanel = new MyPhonePanel();
		getContentPane().add(phonePanel, BorderLayout.CENTER);
		
	}
	
	////////////////////////////////////////////////////////////////////////////
	/* METHODS */
	
	public void exit()
	{
		((MyPhonePanel) phonePanel).exit();
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	
}
