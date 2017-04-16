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
 *  The Call class represents the call log in the phone.
 * 
 * @author Lee Ho Jun
 * 
 ******************************************************************************/

public class Call extends CommunicationObject implements XMLReadWritable {

	/**
	 * Constructs an Call object with no sender, no receiver, and current time.
	 */
	public Call()
	{
		super();
	}
	
	/**
	 * Constructs an Call object with given sender, receiver, and current time.
	 * @param sender phone number of the sender
	 * @param receiver phone number of the receiver
	 */
	public Call(PhoneNumber sender, PhoneNumber receiver)
	{
		super(sender, receiver);
	}
	
	/**
	 * Constructs an call object with given sender, receiver, and current time.
	 * @param sender phone number of the sender
	 * @param receiver phone number of the receiver
	 * @throws PhoneNumber.WrongSyntaxException signal if the phone number 
	 * string has syntax errors.
	 */
	public Call(String sender, String receiver) 
			throws PhoneNumber.WrongSyntaxException
	{
		super(sender, receiver);
	}
	
	/**
	 * Constructs an Call object with given sender, receiver, and time.
	 * @param sender phone number of the sender
	 * @param receiver phone number of the receiver
	 * @param communicationTime the time when the object is created.
	 */
	public Call(PhoneNumber sender, PhoneNumber receiver, Calendar communicationTime)
	{
		super(sender, receiver, communicationTime);
	}
	
	/**
	 * Constructs an Call object with given sender, receiver, and time.
	 * @param sender phone number string of the sender
	 * @param receiver phone number string of the receiver
	 * @param communicationTime the time when the object is created.
	 * @throws PhoneNumber.WrongSyntaxException signal if the phone number string 
	 * has syntax errors.
	 */
	public Call(String sender, String receiver, Calendar communicationTime) 
			throws PhoneNumber.WrongSyntaxException
	{
		super(sender, receiver, communicationTime);
	}
	
	/**
	 * Initializes a newly created Call object so that it 
	 * represents the same values as the argument.
	 * @param call Call object to copy.
	 */
	public Call(Call call)
	{
		super(call);
	}

	////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void writeToXML(XMLStreamWriter writer) throws XMLStreamException {
		
		writer.writeStartElement("call");
		
		// write attributes to the XML
		writer.writeAttribute("sender", getSender().getPhoneNumber());
		writer.writeAttribute("receiver", getReceiver().getPhoneNumber());
		writer.writeAttribute("time", getCommunicationTimeToString());		
		
		writer.writeEndElement();
		writer.writeCharacters(System.getProperty("line.separator"));
	}

	@Override
	public void readFromXML(Node xmlAddressElement) throws WrongSyntaxException {
		
		if(!xmlAddressElement.getNodeName().equals("call"))
		{
			throw new Call.WrongSyntaxException();
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
			
			
		} catch(PhoneNumber.WrongSyntaxException e) {
			throw new Call.WrongSyntaxException();
		}
		
	}
	
	////////////////////////////////////////////////////////////////////////////
	/* EXCEPTIONS */
	
	/**
	* This exception is thrown by Call object if failed to load data from
	* the XML files.
	* 
	* @author Kang Seung Won 
	*/
	@SuppressWarnings("serial")
	public static class WrongSyntaxException extends Exception 
	{
	
		/**
		* Constructs a exception representing the call log is wrong syntax.
		*/
		public WrongSyntaxException()
		{
			super("XML Load Failed - Wrong Call Element syntax!");
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////
}
