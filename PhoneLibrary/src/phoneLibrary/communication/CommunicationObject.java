package phoneLibrary.communication;
import java.util.Calendar;

import phoneLibrary.address.PhoneNumber;

/*******************************************************************************
 * 
 *  The CommunicationObject class represents all objects for communicating with
 * each other in the phone. This class is the super class of all communication 
 * objects in the phone such as messages and calls.
 * 
 * @author Lee Ho Jun
 * 
 ******************************************************************************/

public abstract class CommunicationObject implements Comparable<CommunicationObject> {
	
	////////////////////////////////////////////////////////////////////////////
	/* MEMBER Variables */
	
	private PhoneNumber 	sender, receiver;
	private Calendar		communicationTime;
	
	
	////////////////////////////////////////////////////////////////////////////
	/* CONSTRUCTORS */
	
	/**
	 * Constructs an object with no sender, no receiver and current time.
	 */
	public CommunicationObject()
	{
		receiver = new PhoneNumber();
		sender = new PhoneNumber();
		communicationTime = Calendar.getInstance();
	}
	
	/**
	 * Constructs an object with given sender, receiver, and current time.
	 * @param sender phone number of the sender
	 * @param receiver phone number of the receiver
	 */
	public CommunicationObject(PhoneNumber sender, PhoneNumber receiver)
	{
		this.sender =  sender;
		this.receiver = receiver;
		communicationTime = Calendar.getInstance();
	}
	
	/**
	 * Constructs an object with given sender, receiver, and current time.
	 * @param sender phone number of the sender
	 * @param receiver phone number of the receiver
	 * @throws PhoneNumber.WrongSyntaxException signal if the phone number 
	 * string has syntax errors.
	 */
	public CommunicationObject(String sender, String receiver) 
			throws PhoneNumber.WrongSyntaxException
	{
		this.sender = new PhoneNumber(sender);
		this.receiver = new PhoneNumber(receiver);
		communicationTime = Calendar.getInstance();
	}
	
	/**
	 * Constructs an object with given sender, receiver, and time.
	 * @param sender phone number of the sender
	 * @param receiver phone number of the receiver
	 * @param communicationTime the time when the object is created.
	 */
	public CommunicationObject(PhoneNumber sender, PhoneNumber receiver, Calendar communicationTime)
	{
		this.sender = sender;
		this.receiver = receiver;
		this.communicationTime = communicationTime;
	}
	
	/**
	 * Constructs an object with given sender, receiver, and time.
	 * @param sender phone number string of the sender
	 * @param receiver phone number string of the receiver
	 * @param communicationTime the time when the object is created.
	 * @throws PhoneNumber.WrongSyntaxException signal if the phone number string 
	 * has syntax errors.
	 */
	public CommunicationObject(String sender, String receiver, Calendar communicationTime) 
			throws PhoneNumber.WrongSyntaxException
	{
		this.sender = new PhoneNumber(sender);
		this.receiver = new PhoneNumber(receiver);
		this.communicationTime = communicationTime;
	}
	
	/**
	 * Initializes a newly created CommunicationObject object so that it 
	 * represents the same values as the argument.
	 * @param communicationObject CommunicationObject object to copy.
	 */
	public CommunicationObject(CommunicationObject communicationObject)
	{
		this.sender = new PhoneNumber(communicationObject.getSender());
		this.receiver = new PhoneNumber(communicationObject.getReceiver());
		this.communicationTime = (Calendar) communicationObject.getCommunicationTime().clone();
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	/* METHODS */
	
	/**
	 * Sets the sender of the communication.
	 * @param sender the phone number of the sender
	 */
	public void setSender(PhoneNumber sender)
	{
		this.sender = sender;
	}
	
	/**
	 * Sets the receiver of the communication.
	 * @param receiver the phone number of the receiver
	 */
	public void setReceiver(PhoneNumber receiver)
	{
		this.receiver = receiver;
	}
	
	/**
	 * Sets the created time of the communication.
	 * @param communicationTime the time when the communication is created.
	 */
	public void setCommunicationTime(Calendar communicationTime)
	{
		this.communicationTime = communicationTime;
	}
	
	/**
	 * Sets the communication time to current time.
	 */
	public void setCommunicationTimeToCurrentTime()
	{
		this.communicationTime = Calendar.getInstance();
	}
	
	/**
	 * Gets the phone number of the sender.
	 * @return the phone number of the sender
	 */
	public PhoneNumber getSender()
	{
		return sender;
	}
	
	/**
	 * Gets the phone number of the receiver.
	 * @return the phone number of the receiver
	 */
	public PhoneNumber getReceiver()
	{
		return receiver;
	}
	
	/**
	 * Gets the time of the communication.
	 * @return the time of the communication
	 */
	public Calendar getCommunicationTime()
	{
		return communicationTime;
	}
	
	public String getCommunicationTimeToString()
	{
		return String.format("%04d/%02d/%02d %02d:%02d:%02d", 
				communicationTime.get(Calendar.YEAR), 
				communicationTime.get(Calendar.MONTH) + 1,
				communicationTime.get(Calendar.DATE), 
				communicationTime.get(Calendar.HOUR),
				communicationTime.get(Calendar.MINUTE), 
				communicationTime.get(Calendar.SECOND));
	}
	
	/**
	 * Compares two communication object for ordering.
	 * @param object the object to be compared.
	 * @return the value 0 if the communication time of the argument object 
	 * is equal to the communication time of this object; a value less than
	 * 0 if the communication time of this object is before the communication
	 * time of the argument object; a value greater than 0 if the communication
	 * time of this object is after the communication time of the argument
	 * object. 
	 */
	public int compareTo(CommunicationObject object)
	{
		return communicationTime.compareTo(object.communicationTime);
	}
	
	


	////////////////////////////////////////////////////////////////////////////
	
}
