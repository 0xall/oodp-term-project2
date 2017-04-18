import java.io.IOException;
import java.util.LinkedList;

import org.xml.sax.SAXException;

import myOS.application.MessageProcedure;
import myOS.application.MyPhoneApp;
import phoneLibrary.address.MyAddress;
import phoneLibrary.address.MyAddressBook;
import phoneLibrary.address.MyAddressBook.WrongFileFormatException;
import phoneLibrary.address.PhoneNumber;
import phoneLibrary.address.PhoneNumber.WrongSyntaxException;
import phoneLibrary.communication.Message;

public class MessageApp extends MyPhoneApp {

	public static final String ADDRESS_LIST_FILE = "addressList.xml";
	public static final String MESSAGE_LIST_FILE = "messageList.xml";
	public static final String CALLLOG_LIST_FILE = "callLogList.xml";
	
	private MyAddressBook addressBook;
	private String fileDirectory;
	private PhoneNumber sendingPhoneNumber;
	
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
			addressBook.loadMessageListFromXML(fileDirectory + MESSAGE_LIST_FILE);
		} catch (IOException | SAXException | WrongFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		registerMessageProcedure("loadMessageList", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				LinkedList<PhoneNumber> phoneNumberList = new LinkedList<PhoneNumber>();
				StringBuilder javascript = new StringBuilder("document.getElementById('messageList')"
						+ ".innerHTML = '<table>");
				
				for(int i=0; i<addressBook.getMessageSize(); ++i)
				{
					Message message = addressBook.getMessage(i);
					PhoneNumber phoneNumber = message.getSender();
					if(phoneNumber.isEmpty())
						phoneNumber = message.getReceiver();
					
					if(!phoneNumberList.contains(message.getSender()) &&
							!phoneNumberList.contains(message.getReceiver()))
					{
						javascript.append("<tr class=\"name\"><td><a href=\"#\" onclick=\"sendNSCommand(\\'send\\',"
								+ "\\'" + phoneNumber.getPhoneNumber() + "\\')\">");
						
						MyAddress address = addressBook.getAddress(phoneNumber);
						
						if(address == null)
							javascript.append(phoneNumber.getPhoneNumber());
						
						else
							javascript.append(address.getName());
						
						javascript.append("</a></td></tr><tr class=\"lastMessage\"><td>");
						javascript.append(message.getMessage());
						javascript.append("</td></tr>");
						
						phoneNumberList.add(phoneNumber);
					}
				}
				
				javascript.append("</table>';");
				executeJavascript(javascript.toString());
			}
		});
		
		registerMessageProcedure("send", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				
				if(parameters == null || parameters.length == 0 ||
						parameters[0] == null || ((String) parameters[0]).trim().equals(""))
					showSendMessageDialog(null);
				else
					try {
					showSendMessageDialog(new PhoneNumber((String)parameters[0]));
				} catch (WrongSyntaxException e) {
					// TODO Auto-generated catch block
					return;
				}
			}
		});
		
		registerMessageProcedure("loadMessagesOfAddress", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				
				String name;
				MyAddress address = addressBook.getAddress(sendingPhoneNumber);
				
				if(address == null) name = sendingPhoneNumber.getPhoneNumber();
				else name = address.getName();
				
				StringBuilder javascript = new StringBuilder("document.getElementById('name').innerHTML = '");
				javascript.append(name);
				javascript.append("';");
				
				executeJavascript(javascript.toString());
				
				javascript = new StringBuilder("document.getElementById('messageList').innerHTML = '"
						+ "<table>");
				
				LinkedList<Message> messageList = addressBook.getMessages(sendingPhoneNumber);
				
				for(Message message : messageList)
				{
					boolean sending = true;
					if(!message.getSender().isEmpty()) sending = false;
					
					javascript.append("<tr><td class=\"content ");
					
					if(sending) javascript.append("send");
					else javascript.append("receive");
					
					javascript.append("\">" + message.getMessage() + "</td></tr><tr><td class=\"time ");
					
					if(sending) javascript.append("send");
					else javascript.append("receive");
					
					javascript.append("\">");
					javascript.append(message.getCommunicationTimeToString());
					javascript.append("</td></tr>");
				}
				
				javascript.append("</table>';");
				
				executeJavascript(javascript.toString());
			}
		});
		
		registerMessageProcedure("sendMessage", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				String message = (String)parameters[0];
				
				if(message == null || message.trim().equals(""))
					return;
				
				try {
					Message msg = new Message(new PhoneNumber(""), sendingPhoneNumber, message);
					addressBook.addMessage(msg);
				} catch (WrongSyntaxException e) {
				}
				
			}
		});
		
		registerMessageProcedure("goMessageList", new MessageProcedure() {
			
			@Override
			public void process(String command, Object[] parameters) {
				requestDisplay("messagesApp.html");
				
			}
		});

		if(parameter == "" || parameter == null)
			requestDisplay("messagesApp.html");
		else
			try {
				showSendMessageDialog(new PhoneNumber(parameter));
			} catch (WrongSyntaxException e) {
				requestDisplay("messagesApp.html");
			}
	}

	@Override
	public void exit() {

		try {
			addressBook.saveMessageListToXML(fileDirectory + MESSAGE_LIST_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void showSendMessageDialog(PhoneNumber phoneNumber)
	{
		sendingPhoneNumber = phoneNumber;
		requestDisplay("sendMessage.html");
	}

}
