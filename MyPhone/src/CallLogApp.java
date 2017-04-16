import java.io.IOException;

import org.xml.sax.SAXException;

import myOS.application.MessageProcedure;
import myOS.application.MyPhoneApp;
import phoneLibrary.address.MyAddress;
import phoneLibrary.address.MyAddressBook;
import phoneLibrary.address.MyAddressBook.WrongFileFormatException;
import phoneLibrary.address.PhoneNumber;
import phoneLibrary.communication.Call;

public class CallLogApp extends MyPhoneApp {

	public static final String ADDRESS_LIST_FILE = "addressList.xml";
	public static final String MESSAGE_LIST_FILE = "messageList.xml";
	public static final String CALLLOG_LIST_FILE = "callLogList.xml";
	
	private MyAddressBook addressBook;
	private String fileDirectory;
	
	@Override
	public void initialize(String parameter) {
		// TODO Auto-generated method stub
		
		addressBook = new MyAddressBook();
		fileDirectory = getOS().getRootPath() + "/data/";
		
		// read address list
		try {
			addressBook.loadAddressListFromXML(fileDirectory + ADDRESS_LIST_FILE);
		} catch (IOException | SAXException | WrongFileFormatException e) {
			e.printStackTrace();
		}
		
		// read message list
		try {
			addressBook.loadCallLogListFromXML(fileDirectory + CALLLOG_LIST_FILE);
		} catch (IOException | SAXException | WrongFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		registerMessageProcedure("loadCallList", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				StringBuilder javascript = new StringBuilder("document.getElementById('callList').innerHTML = '<table>");
				
				for(int i=0; i<addressBook.getCallLogSize(); ++i)
				{
					Call call = addressBook.getCallLog(i);
					PhoneNumber phoneNumber;
					String name;
					
					phoneNumber = call.getReceiver();
					if(phoneNumber == null || phoneNumber.isEmpty())
						phoneNumber = call.getSender();
					
					MyAddress address = addressBook.getAddress(phoneNumber);
					
					if(address != null)
						name = address.getName();
					else name = phoneNumber.getPhoneNumber();
					
					javascript.append("<tr><td class=\"name\">");
					
					if(call.getReceiver() == null || call.getReceiver().isEmpty())
						javascript.append("FROM");
					else javascript.append("TO");
					
					javascript.append("</td><td><a href=\"#\" ");
					
					if(phoneNumber != null && !phoneNumber.isEmpty())
					{
						javascript.append("onclick=\"sendNSCommand(\\'modify\\', \\'" + phoneNumber.getPhoneNumber() + "\\')\"");
					}
					
					javascript.append(">");
					
					javascript.append(name);
					javascript.append("</a></td></tr>");
					
					
				}
				
				javascript.append("</table>';");
				executeJavascript(javascript.toString());
			}
		});
		
		registerMessageProcedure("modify", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				getOS().executeApp("PhoneAddressApp", (String)parameters[0]);
			}
		});
		
		requestDisplay("callLogsApp.html");
	}

	@Override
	public void exit() {
		try {
			addressBook.saveCallLogListToXML(fileDirectory + CALLLOG_LIST_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
