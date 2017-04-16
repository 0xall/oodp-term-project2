
package myOS;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserCommandEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserListener;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowOpeningEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowWillOpenEvent;
import myOS.application.MyPhoneApp;

public class MyOS implements WebBrowserListener {

	////////////////////////////////////////////////////////////////////////////
	/* CONSTANT Variables */
	
	
	////////////////////////////////////////////////////////////////////////////
	/* MEMBER Variables */
	
	private JWebBrowser displayManager;
	private MyPhoneApp executingApp;
	private ArrayList<Class<? extends MyPhoneApp> > applications;
	private ArrayList<String> applicationName;
	private String rootPath;
	private String currentPath;
	
	////////////////////////////////////////////////////////////////////////////
	/* CONSTRUCTORS */
	
	public MyOS(JWebBrowser display, Class<? extends MyPhoneApp> shellApp)
	{
		// set display
		displayManager = display;
		
		// register shell application
		applications = new ArrayList<Class<? extends MyPhoneApp> >();
		applicationName = new ArrayList<String>();
		applications.add(shellApp);
		applicationName.add("Main App");
		
		// set the root path of the operating system
		rootPath = this.getClass().getProtectionDomain().getCodeSource().
				getLocation().getPath();
		int startIndex = 0;
		if(rootPath.charAt(0) == '\\' || rootPath.charAt(0) == '/') startIndex = 1;
		rootPath = rootPath.substring(startIndex, rootPath.lastIndexOf("/"));
		displayManager.addWebBrowserListener(this);
		
		executeMainApp();
	}
	
	////////////////////////////////////////////////////////////////////////////
	/* METHODS */
	
	// change the display in the operating system
	public void Display(String path)
	{
		String absolutePath;
		
		if(path.trim().charAt(0) == '/')
			absolutePath = rootPath.substring(0, rootPath.length() - 1) + path;
		
		else absolutePath = currentPath + path;
		
		displayManager.navigate(absolutePath);
		
	}
	
	public void executeApp(String appName, String parameter)
	{
		try
		{
			for(Class<? extends MyPhoneApp> c : applications)
			{
				if(c.getName().equals(appName))
				{
					if(executingApp != null && executingApp.getStatus() == MyPhoneApp.APP_STATUS_EXECUTED)
						executingApp.exit();
					executingApp = c.newInstance();
					executingApp.SetOS(this);
					currentPath = rootPath + "/apps/" + executingApp.getClass().getName() + "/";
					executingApp.execute(parameter);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadApp(Class<? extends MyPhoneApp> app, String appName)
	{
		applications.add(app);
		applicationName.add(appName);
	}

	@Override
	public void commandReceived(WebBrowserCommandEvent e) {
		String command = e.getCommand();
		Object[] parameters = e.getParameters();
		
		// if the app is executed, send message to the app
		if(executingApp.getStatus() == MyPhoneApp.APP_STATUS_EXECUTED)
		{
			executingApp.processMessage(command, parameters);
		}
	}

	@Override
	public void loadingProgressChanged(WebBrowserEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void locationChangeCanceled(WebBrowserNavigationEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void locationChanged(WebBrowserNavigationEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void locationChanging(WebBrowserNavigationEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void statusChanged(WebBrowserEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void titleChanged(WebBrowserEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WebBrowserEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowOpening(WebBrowserWindowOpeningEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowWillOpen(WebBrowserWindowWillOpenEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public JWebBrowser getDisplayManager()
	{
		return displayManager;
	}
	
	public ArrayList<Class<? extends MyPhoneApp> > getApplications()
	{
		return applications;
	}
	
	public ImageIcon getApplicationImageIcon(String applicationName)
	{
		return new ImageIcon(rootPath + "/apps/" + applicationName + "/icon.gif");
	}
	
	public String getApplicationName(int index)
	{
		return applicationName.get(index);
	}
	
	public void executeMainApp()
	{
		executeApp(applications.get(0).getName(), "");
	}
	
	public String getRootPath()
	{
		return rootPath;
	}
	
	public void shutdown()
	{
		executingApp.exit();
		displayManager.setEnabled(false);
	}
}
