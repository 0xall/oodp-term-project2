import java.util.Date;

/*******************************************************************************
 * 
 *  The CommunicationObject class represents all objects for communicating with
 * each other in the phone. This class is the super class of all communication 
 * objects in the phone such as messages and calls.
 * 
 * @author Lee Ho Jun
 * 
 ******************************************************************************/

public class CommunicationObject {
	
	////////////////////////////////////////////////////////////////////////////
	/* MEMBER Variables */
	
	private PhoneNumber sender, receiver;
	private Date communicationTime;
	
	
	////////////////////////////////////////////////////////////////////////////
	/* CONSTRUCTORS */
	
	/**
	 * Constructs an object with no sender, receiver and current time.
	 */
	CommunicationObject()
	{
		receiver = new PhoneNumber();
		sender = new PhoneNumber();
		communicationTime = new Date();
	}
	
	/**
	 * Constructs an object with given sender, receiver, and current time.
	 * @param sender phone number of the sender
	 * @param receiver phone number of the receiver
	 */
	CommunicationObject(PhoneNumber sender, PhoneNumber receiver)
	{
		this.sender = sender;
		this.receiver = receiver;
		communicationTime = new Date();
	}
	
	/**
	 * Constructs an object with given sender, receiver, and time.
	 * @param sender phone number of the sender
	 * @param receiver phone number of the receiver
	 * @param communicationTime the time when the object is created.
	 */
	CommunicationObject(PhoneNumber sender, PhoneNumber receiver, Date communicationTime)
	{
		this.sender = sender;
		this.receiver = receiver;
		this.communicationTime = communicationTime;
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	/* METHODS */
	
	/**
	 * Sets the sender of the communication.
	 * @param sender the phone number of the sender
	 */
	public void SetSender(PhoneNumber sender)
	{
		this.sender = sender;
	}
	
	/**
	 * Sets the receiver of the communication.
	 * @param receiver the phone number of the receiver
	 */
	public void SetReceiver(PhoneNumber receiver)
	{
		this.receiver = receiver;
	}
	
	/**
	 * Sets the created time of the communication.
	 * @param communicationTime the time when the communication is created.
	 */
	public void SetCommunicationTime(Date communicationTime)
	{
		this.communicationTime = communicationTime;
	}
	
	/**
	 * Sets the communication time to current time.
	 */
	public void SetCommunicationTimeToCurrentTime()
	{
		this.communicationTime = new Date();
	}
	
	/**
	 * Gets the phone number of the sender.
	 * @return the phone number of the sender
	 */
	public PhoneNumber GetSender()
	{
		return sender;
	}
	
	/**
	 * Gets the phone number of the receiver.
	 * @return the phone number of the receiver
	 */
	public PhoneNumber GetReceiver()
	{
		return receiver;
	}
	
	/**
	 * Gets the time of the communication.
	 * @return the time of the communication
	 */
	public Date GetCommunicationTime()
	{
		return communicationTime;
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
}















