package phoneLibrary.communication;

import java.util.Calendar;
import java.util.Scanner;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import phoneLibrary.address.PhoneNumber;
import phoneLibrary.io.XMLReadWritable;

/*******************************************************************************
 * 
 *  The Message class represents SMS messages in the phone.
 * 
 * @author Lee Ho Jun
 * 
 ******************************************************************************/

public class Message extends CommunicationObject implements XMLReadWritable {

	////////////////////////////////////////////////////////////////////////////
	/* MEMBER Variables */

	private String message;
	
	
	////////////////////////////////////////////////////////////////////////////
	/* CONSTRUCTORS */
	
	/**
	 * Constructs an message with no sender, no receiver, no message and 
	 * current time.
	 */
	public Message()
	{
		super();
		setMessage("");
	}
	
	/**
	 * Constructs an object with given sender, receiver, message and current 
	 * time.
	 * @param sender phone number of the sender
	 * @param receiver phone number of the receiver
	 * @param message message of the sender.
	 */
	public Message(PhoneNumber sender, PhoneNumber receiver, String message)
	{
		super(sender, receiver);
		setMessage(message);
	}
	
	/**
	 * Constructs an object with given sender, receiver, message and current 
	 * time.
	 * @param sender phone number of the sender
	 * @param receiver phone number of the receiver
	 * @param message of the sender.
	 * @throws PhoneNumber.WrongSyntaxException signal if the phone number 
	 * string has syntax errors.
	 */
	public Message(String sender, String receiver, String message) 
			throws PhoneNumber.WrongSyntaxException
	{
		super(sender, receiver);
		setMessage(message);
	}
	
	/**
	 * Constructs an object with given sender, receiver, message, and time.
	 * @param sender phone number of the sender
	 * @param receiver phone number of the receiver
	 * @param message message of the sender.
	 * @param communicationTime the time when the object is created.
	 */
	public Message(PhoneNumber sender, PhoneNumber receiver, 
			Calendar communicationTime, String message)
	{
		super(sender, receiver, communicationTime);
		setMessage(message);
	}
	
	/**
	 * Constructs an message with given sender, receiver, and time.
	 * @param sender phone number string of the sender
	 * @param receiver phone number string of the receiver
	 * @param communicationTime the time when the object is created.
	 * @param message message of the sender.
	 * @throws PhoneNumber.WrongSyntaxException signal if the phone number string 
	 * has syntax errors.
	 */
	public Message(String sender, String receiver, Calendar communicationTime,
			String message) 
			throws PhoneNumber.WrongSyntaxException
	{
		super(sender, receiver, communicationTime);
		setMessage(message);
	}
	
	/**
	 * Initializes a newly created message object so that it represents the 
	 * same values as the argument.
	 * @param message Message object to copy.
	 */
	public Message(Message message)
	{
		super(message);
		setMessage(message.message);
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	/* METHODS */
	
	/**
	 * Sets the message of the sender.
	 * @param message the message string.
	 */
	public void setMessage(String message)
	{
		this.message = message.trim();
	}
	
	/**
	 * Gets the message of the sender.
	 * @return message of the sender.
	 */
	public String getMessage()
	{
		return message;
	}

	@Override
	public void writeToXML(XMLStreamWriter writer) throws XMLStreamException {
		
		writer.writeStartElement("message");
		
		// write attributes to the XML
		writer.writeAttribute("sender", getSender().getPhoneNumber());
		writer.writeAttribute("receiver", getReceiver().getPhoneNumber());
		writer.writeAttribute("time", getCommunicationTimeToString());
		
		writer.writeCharacters(System.getProperty("line.separator"));
		writer.writeCharacters(message);
		writer.writeCharacters(System.getProperty("line.separator"));
		
		writer.writeEndElement();
		writer.writeCharacters(System.getProperty("line.separator"));
		
	}

	@Override
	public void readFromXML(Node xmlAddressElement) throws WrongSyntaxException {

		if(!xmlAddressElement.getNodeName().equals("message"))
		{
			throw new Message.WrongSyntaxException();
		}
		
		NamedNodeMap attributes = xmlAddressElement.getAttributes();
		
		try
		{
			setSender(new PhoneNumber(attributes.getNamedItem("sender").
					getNodeValue()));
			
			setReceiver(new PhoneNumber(attributes.getNamedItem("receiver").
					getNodeValue()));
			
			// set time
			String time = attributes.getNamedItem("time").getNodeValue();
			time = time.replaceAll("[/:]+", " ");
			Scanner timeScanner = new Scanner(time);
			
			int year = timeScanner.nextInt();
			int month = timeScanner.nextInt() - 1;
			int day = timeScanner.nextInt();
			int hour = timeScanner.nextInt();
			int minute = timeScanner.nextInt();
			int second = timeScanner.nextInt();
			
			timeScanner.close();
			
			setCommunicationTime(new Calendar.Builder().
					setDate(year, month, day).
					setTimeOfDay(hour, minute, second).build());
			
			
			setMessage(xmlAddressElement.getTextContent());
			
		} catch(PhoneNumber.WrongSyntaxException e) {
			throw new Message.WrongSyntaxException();
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	/* EXCEPTIONS */
	
	/**
	* This exception is thrown by Message object if failed to load data from
	* the XML files.
	* 
	* @author Kang Seung Won 
	*/
	@SuppressWarnings("serial")
	public static class WrongSyntaxException extends Exception 
	{
	
		/**
		* Constructs a exception representing the message is wrong syntax.
		*/
		public WrongSyntaxException()
		{
		super("XML Load Failed - Wrong Message Element syntax!");
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	
}





















