
import java.io.IOException;
import org.xml.sax.SAXException;
import myOS.application.MessageProcedure;
import myOS.application.MyPhoneApp;
import phoneLibrary.address.MyAddress;
import phoneLibrary.address.MyAddressBook;
import phoneLibrary.address.MyAddressBook.WrongFileFormatException;
import phoneLibrary.address.PhoneNumber;
import phoneLibrary.address.PhoneNumber.WrongSyntaxException;
import phoneLibrary.communication.Call;

public class PhoneAddressApp extends MyPhoneApp {
	
	public static final String ADDRESS_LIST_FILE = "addressList.xml";
	public static final String MESSAGE_LIST_FILE = "messageList.xml";
	public static final String CALLLOG_LIST_FILE = "callLogList.xml";
	
	private int modifyingIndex = 0;
	
	private String fileDirectory;
	
	MyAddressBook addressBook;
	
	public PhoneAddressApp()
	{
		super();
		addressBook = new MyAddressBook();
	}
	
	@Override
	public void initialize(String parameter) {
		
		fileDirectory = getOS().getRootPath() + "/data/";
		
		// load data
		try {
			addressBook.loadAddressListFromXML(fileDirectory + ADDRESS_LIST_FILE);
		} catch (IOException | SAXException | WrongFileFormatException e) { }
		
		try {
			addressBook.loadCallLogListFromXML(fileDirectory + CALLLOG_LIST_FILE);
		} catch (IOException | SAXException | WrongFileFormatException e) {}
		
		registerMessageProcedure("loadList", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				
				StringBuilder javascript = new StringBuilder( 
						"document.getElementById('addressList').innerHTML = '"
						+ "<table>");
				
				String containingString = (String) parameters[0];
				if(containingString == null) containingString = "";
				
				for(int i=0; i<addressBook.getAddressListSize(); ++i)
				{
					MyAddress address = addressBook.getAddress(i);
					
					if(!address.getName().toLowerCase().contains(containingString.toLowerCase())) continue; 
					javascript.append("<tr>");
					javascript.append("<td class=\"name\"><a href=\"#\" onclick=\"sendNSCommand(\\'change\\', \\'" + i
							+ "\\')\">" + address.getName() + "</a></td><td>" + address.getPhoneNumber(0) + "</td>");					
					
					javascript.append("</tr>");
				}
				
				javascript.append("</table>'");
				
				executeJavascript(javascript.toString());
			}
		});
		
		registerMessageProcedure("add", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				requestDisplay("addAddress.html");
				
			}
		});
		
		registerMessageProcedure("goList", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				requestDisplay("phoneAddressApp.html");
				
			}
		});
		
		registerMessageProcedure("addAddress", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				String name = (String) parameters[0];
				String phoneNumber1 = ((String) parameters[1]);
				String phoneNumber2 = ((String) parameters[2]);
				String phoneNumber3 = ((String) parameters[3]);
				String homeAddress = ((String) parameters[4]);
				String officeAddress = ((String) parameters[5]);
				String emailAddress = ((String) parameters[6]);
				String urlAddress = ((String) parameters[7]);
				
				MyAddress address = new MyAddress();
				
				try
				{
				if(name == null || phoneNumber1 == null)
				{
					executeJavascript("alert('You should input name and phone number 1.')");
					return;
				}
				address.setName(name.trim());
				address.addPhoneNumber(new PhoneNumber(phoneNumber1));
				if(phoneNumber2 != null) address.addPhoneNumber(new PhoneNumber(phoneNumber2));
				if(phoneNumber3 != null) address.addPhoneNumber(new PhoneNumber(phoneNumber3));
				if(homeAddress != null) address.setHomeAddress(homeAddress.trim());
				if(officeAddress != null) address.setOfficeAddress(officeAddress.trim());
				if(emailAddress != null && !address.setEmail(emailAddress.trim())) throw new MyAddress.WrongSyntaxException();
				if(urlAddress != null) address.setURL(urlAddress.trim());
				
				} catch(MyAddress.WrongSyntaxException | WrongSyntaxException e)
				{
					executeJavascript("alert('I cannot add address because it has some syntax errors.')");
					return;
				}
				
				if(!addressBook.addAddress(address))
				{
					executeJavascript("alert('I cannot add address because the number already exists in your"
							+ " book. The phone number cannot be duplicated.')");
					return;
				}
				requestDisplay("phoneAddressApp.html");
				
			}
		});
		
		registerMessageProcedure("change", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				modifyingIndex = Integer.parseInt((String)parameters[0]);
				requestDisplay("modifyAddress.html");
			}
		});
		
		registerMessageProcedure("loadChange", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				MyAddress modifiedAddress = addressBook.getAddress(modifyingIndex);
				
				String[] value = {
						modifiedAddress.getName(),
						modifiedAddress.getPhoneNumber(0).getPhoneNumber(),
						"", "",		// input later
						modifiedAddress.getHomeAddress(),
						modifiedAddress.getOfficeAddress(),
						modifiedAddress.getEmail(),
						modifiedAddress.getURL()
				};
				
				try
				{
					value[2] = modifiedAddress.getPhoneNumber(1).getPhoneNumber();
					value[3] = modifiedAddress.getPhoneNumber(2).getPhoneNumber();
				} catch(IndexOutOfBoundsException e){}	// if null, the string will be set to empty string
				
				String[] input = {
					"input_name", "input_phoneNumber1", "input_phoneNumber2", "input_phoneNumber3",
					"input_home_address", "input_office_address", "input_email", "input_url"
				};
				
				for(int i=0; i<input.length; ++i)
				{
					String javascript = "document.getElementById('" + input[i] + "').value = '"
							+ value[i] + "';";
					executeJavascript(javascript);
				}
				
			}
		});
		
		registerMessageProcedure("changeAddress", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				String name = (String) parameters[0];
				String phoneNumber1 = ((String) parameters[1]);
				String phoneNumber2 = ((String) parameters[2]);
				String phoneNumber3 = ((String) parameters[3]);
				String homeAddress = ((String) parameters[4]);
				String officeAddress = ((String) parameters[5]);
				String emailAddress = ((String) parameters[6]);
				String urlAddress = ((String) parameters[7]);
				
				MyAddress address = new MyAddress();
				
				try
				{
				if(name == null || phoneNumber1 == null)
				{
					executeJavascript("alert('You should input name and phone number 1.')");
					return;
				}
				address.setName(name.trim());
				address.addPhoneNumber(new PhoneNumber(phoneNumber1));
				if(phoneNumber2 != null) address.addPhoneNumber(new PhoneNumber(phoneNumber2));
				if(phoneNumber3 != null) address.addPhoneNumber(new PhoneNumber(phoneNumber3));
				if(homeAddress != null) address.setHomeAddress(homeAddress.trim());
				if(officeAddress != null) address.setOfficeAddress(officeAddress.trim());
				if(emailAddress != null && !address.setEmail(emailAddress.trim())) throw new MyAddress.WrongSyntaxException();
				if(urlAddress != null) address.setURL(urlAddress.trim());
				
				} catch(MyAddress.WrongSyntaxException | WrongSyntaxException e)
				{
					executeJavascript("alert('I cannot add address because it has some syntax errors.')");
					return;
				}
				
				if(!addressBook.modifyAddress(modifyingIndex, address))
				{
					executeJavascript("alert('I cannot add address because the number already exists in your"
							+ " book. The phone number cannot be duplicated.')");
					return;
				}
				
				
				requestDisplay("phoneAddressApp.html");
				
			}
		});
		
		registerMessageProcedure("delete", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				addressBook.deleteAddress(modifyingIndex);
				requestDisplay("phoneAddressApp.html");
			}
		});
		
		registerMessageProcedure("sendMessage", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				getOS().executeApp("MessageApp", addressBook.getAddress(modifyingIndex).getPhoneNumber(0).getPhoneNumber());
				
			}
		});
		
		registerMessageProcedure("call", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				MyAddress address = addressBook.getAddress(modifyingIndex);
				StringBuilder javascript = new StringBuilder();
				javascript.append("alert('You call " + address.getPhoneNumber(0) +". ");
				javascript.append("You can see the log in the call log app!');");
				executeJavascript(javascript.toString());
				
				Call call = new Call(new PhoneNumber(), address.getPhoneNumber(0));
				addressBook.addCallLog(call);
			}
		});
		
		if(parameter == null || parameter.equals(""))
			requestDisplay("phoneAddressApp.html");
		else
		{
			try {
				int i;
				PhoneNumber phoneNumber = new PhoneNumber(parameter);
				
				for(i=0; i<addressBook.getAddressListSize(); ++i)
				{
					if(addressBook.getAddress(i).containsPhoneNumber(phoneNumber))
					{
						modifyingIndex = i;
						break;
					}
				}
				
				if(i == addressBook.getAddressListSize()) requestDisplay("PhoneAddressApp.html");
				else requestDisplay("modifyAddress.html");
			} catch (WrongSyntaxException e) {
				requestDisplay("PhoneAddressApp.html");
			}
		}
		
	}
	
	@Override
	public void exit()
	{
		// save data
		
		try {
			addressBook.saveAddressListToXML(fileDirectory + ADDRESS_LIST_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			addressBook.saveCallLogListToXML(fileDirectory + CALLLOG_LIST_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
