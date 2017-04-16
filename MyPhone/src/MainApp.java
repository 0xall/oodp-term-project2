

import java.util.ArrayList;

import myOS.application.MessageProcedure;
import myOS.application.MyPhoneApp;

public class MainApp extends MyPhoneApp {
	
	public MainApp()
	{
		super();
		
	}

	@Override
	public void initialize(String parameter) {
			registerMessageProcedure("load", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				
				ArrayList<Class<? extends MyPhoneApp> > applications = getOS().getApplications();
				
				StringBuilder javascript = 
						new StringBuilder("document.getElementById('applicationTable').innerHTML = '<table>"
								+ "<tr> <td></td> <td></td> <td></td> <td></td> </tr><tr>");
				
				
				for(int i = 1; i < applications.size(); ++i)
				{
					Class<? extends MyPhoneApp> app = applications.get(i);

					javascript.append("<td>"
							+ "<a href=\"#\" onclick=\"sendNSCommand(\\'execute\\', \\'" + app.getName() + "\\');\">" 
							+ "<img src=\"" + getOS().getApplicationImageIcon(app.getName()) + "\"></a></td>");
					
					if((i % 4 == 0) || i == applications.size() - 1)
					{
						javascript.append("</tr><tr>");
						
						for(int j = i - 4; j <= i; ++j)
						{
							if(j < 0) j = 1;
							if(j < 0 || j >= applications.size())
								javascript.append("<td></td>");
							else 
								javascript.append("<td>" + getOS().getApplicationName(j) + "</td>");
						}
						
						javascript.append("</tr><tr>");
					}
				}
				
				javascript.append("</tr></table>';"); 
				
				executeJavascript(javascript.toString());
			}
		});
			
		registerMessageProcedure("execute", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] id) {
				getOS().executeApp((String)id[0], null);
			}
		});
		
		requestDisplay("mainapp.html");
	}

	@Override
	public void exit() {
		
	}

}
