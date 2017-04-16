package phoneLibrary.address;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import phoneLibrary.communication.Call;
import phoneLibrary.communication.Message;

/*******************************************************************************
 * 
 * MyAddressBook represents the address book in the phone. It saves the 
 * addresses, call logs, and messages in the phone.
 * 
 * @author Kang Seung Won, Lee Ho Jun
 *
 ******************************************************************************/
public class MyAddressBook {

	////////////////////////////////////////////////////////////////////////////
	/* CONSTANT Variables */
	
	////////////////////////////////////////////////////////////////////////////
	/* MEMBER Variables */
	
	private ArrayList<MyAddress>			addressList;
	private HashMap<PhoneNumber, MyAddress>	phoneDirectory;
	private ArrayList<Call>					callList;
	private ArrayList<Message>				messageList;
	
	////////////////////////////////////////////////////////////////////////////
	/* STATIC METHODS */
	
	
	////////////////////////////////////////////////////////////////////////////
	/* CONSTRUCTORS */
	
	/**
	 * Constructs an address book with empty data.
	 */
	public MyAddressBook()
	{
		addressList = new ArrayList<MyAddress>();
		phoneDirectory = new HashMap<PhoneNumber, MyAddress>();
		callList = new ArrayList<Call>();
		messageList = new ArrayList<Message>();
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	/* METHODS */
	
	/**
	 * Tests if the address can be added to the address book. If the address has
	 * phone numbers that exists in the address book, it cannot be added to the
	 * address book.
	 * @param address the address to test.
	 * @return true if the address can be added to the address book, or false if
	 * it can't be added to it.
	 */
	private boolean isAddressAddible(MyAddress address)
	{
		for(PhoneNumber phoneNumber : address)
		{
			// if the phone number already exists in the list, it cannot be 
			// addible.
			if(phoneDirectory.containsKey(phoneNumber)) return false;
		}
		
		// if the phone number does not exist in the list, return true
		return true;
	}
	
	/**
	 * Inserts an address in the list to the position sorted by the name of
	 * the address. This can be operated correctly only when the list is 
	 * sorted excluding the index.
	 * @param index the index to insert.
	 * @throws IndexOutOfBoundsException if the index is out of range.
	 * (index < 0 || index >= size())
	 */
	private void InsertAddress(int index) throws IndexOutOfBoundsException
	{		
		// try to insert left position.
		while(index > 0 && addressList.get(index - 1).getName().
				compareToIgnoreCase(addressList.get(index).getName()) > 0)
		{
			Collections.swap(addressList, index - 1, index);
			--index;
		}
		
		// try to insert right position.
		while(index < addressList.size() - 1 && addressList.get(index).getName().
				compareToIgnoreCase(addressList.get(index + 1).getName()) > 0)
		{
			Collections.swap(addressList, index, index + 1);
			++index;
		}
	}
	
	/**
	 * Inserts an call log in the list to the position sorted by the time of
	 * the call log. This can be operated correctly only when the list is
	 * sorted excluding the index.
	 * @param index the index to insert.
	 * @throws IndexOutOfBoundsException if the index is out of range.
	 * (index < 0 || index >= size())
	 */
	private void InsertCall(int index) throws IndexOutOfBoundsException
	{
		// try to insert left position.
		while(index > 0 && callList.get(index - 1).
				compareTo(callList.get(index)) < 0)
		{
			Collections.swap(callList,  index - 1, index);
			--index;
		}
		
		// try to insert right position.
		while(index < callList.size() - 1 && callList.get(index).
				compareTo(callList.get(index + 1)) < 0)
		{
			Collections.swap(callList, index, index + 1);
			++index;
		}
	}
	
	private void InsertMessage(int index) throws IndexOutOfBoundsException
	{
		// try to insert left position.
		while(index > 0 && messageList.get(index - 1).
				compareTo(messageList.get(index)) < 0)
		{
			Collections.swap(messageList, index - 1, index);
			--index;
		}
		
		// try to insert right position.
		while(index < messageList.size() - 1 && messageList.get(index).
				compareTo(messageList.get(index + 1)) < 0)
		{
			Collections.swap(messageList, index, index + 1);
			++index;
		}
	}
	
	/**
	 * Adds an address to the address book.
	 * @param address the address to add.
	 * @return true if succeeding to add, or false if failed to add the address.
	 */
	public boolean addAddress(MyAddress address)
	{
		if(!isAddressAddible(address))
		{
			// if the address cannot be addible to the list, return false without
			// adding to the list.
			return false;
		}
		
		for(PhoneNumber phoneNumber : address)
		{
			// add the name value with the phone numbers to the hash table.
			phoneDirectory.put(phoneNumber, address);
		}
		
		// add the address to the list.
		addressList.add(address);
		
		// insert the last address to the sorted position
		InsertAddress(addressList.size() - 1);
		
		return true;
	}
	
	/**
	 * Deletes an address in the address book.
	 * @param index the index of the address to remove in the list.
	 * @throws IndexOutOfBoundsException if the index is out of range. 
	 * (index < 0 || index >= size())
	 */
	public void deleteAddress(int index) throws IndexOutOfBoundsException
	{
		for(PhoneNumber phoneNumber : addressList.get(index))
		{
			// delete the name value with phone numbers in the hash table.
			phoneDirectory.remove(phoneNumber);
		}
		
		// delete the address in the list.
		addressList.remove(index);
	}
	
	/**
	 * Modifies an address in the address book.
	 * @param index the index of the address to modify in the list.
	 * @param address the address value to change.
	 * @throws IndexOutOfBoundsException if the index is out of range.
	 * (index < 0 || index >= size())
	 * @return true if succeeding to add, or false if failed to add
	 * because the address cannot be addible.
	 */
	public boolean modifyAddress(int index, MyAddress address) 
			throws IndexOutOfBoundsException
	{
		MyAddress previousAddress = addressList.get(index);
		
		if(isAddressAddible(address)) return false;
		
		for(PhoneNumber phoneNumber : previousAddress)
		{
			// remove all previous phone numbers in the hash table
			phoneDirectory.remove(phoneNumber);
		}
		
		for(PhoneNumber phoneNumber : address)
		{
			// add all modified phone numbers to the hash table
			phoneDirectory.put(phoneNumber, address);
		}
		
		// modify the address
		addressList.set(index, address);
		
		// insert the address into the sorted position
		InsertAddress(index);
		
		return true;
	}
	
	/**
	 * Returns the address at the specified position in the address list.
	 * @param index the index of the address to return.
	 * @return the address at the specified position in the address list.
	 * @throws IndexOutOfBoundsException the index is out of bounds.
	 * (index < 0 || index >= size())
	 */
	public MyAddress getAddress(int index) throws IndexOutOfBoundsException
	{
		return addressList.get(index);
	}
	
	/**
	 * Returns the first address index at the specified name in the address 
	 * list. If the name does not exist, returns -1.
	 * @param name the name of the address to return.
	 * @return the first address index at the specified name in the address
	 * list or -1 if the name does not exist.
	 */
	public int getFirstAddressIndexByName(String name)
	{
		MyAddress searchInfo = new MyAddress();
		searchInfo.setName(name);
		
		int index = Collections.binarySearch(addressList, searchInfo,
				new Comparator<MyAddress>(){

					@Override
					public int compare(MyAddress o1, MyAddress o2) {
						return o1.getName().compareTo(o2.getName());
					}
			
		});
		
		if(index < 0) return -1;
		
		while(index > 0 && addressList.get(index - 1).getName().equals(name))
			index--;
		
		return index;
	}
	
	/**
	 * Returns the address at the specified position in the address list.
	 * @param phoneNumber the phone number of the address to return.
	 * @return the address at the specified position in the list or null if the
	 * phone number does not exist.
	 */
	public MyAddress getAddress(PhoneNumber phoneNumber)
	{
		return phoneDirectory.get(phoneNumber);
	}
	
	/**
	 * Returns the address that contains the specified string.
	 * @param stringContained the string to contain.
	 * @return the linked list of the addresses that contains the specified
	 * string.
	 */
	public LinkedList<MyAddress> getAddressListByName(String stringContained)
	{
		LinkedList<MyAddress> list = new LinkedList<MyAddress>();
		
		for(MyAddress address : addressList)
		{
			if(address.getName().contains(stringContained))
			{
				list.add(address);
			}
		}
		
		return list;
	}
	
	/**
	 * Returns the number of addresses in the book.
	 * @return the number of addresses in the book.
	 */
	public int getAddressListSize()
	{
		return addressList.size();
	}
	
	/**
	 * Saves the address list as an XML file.
	 * @param fileName the XML file name.
	 * @throws IOException signals if some I/O exception at the file occurs.
	 */
	public void saveAddressListToXML(String fileName) throws IOException
	{
		XMLOutputFactory xml = XMLOutputFactory.newInstance();
		FileOutputStream file;
		XMLStreamWriter writer;
		
		try
		{
			file = new FileOutputStream(fileName);
			writer = xml.createXMLStreamWriter(file, "UTF-8");
			
			// starts to write document
			writer.writeStartDocument();
			writer.writeCharacters(System.getProperty("line.separator"));
			
			// writes root element
			writer.writeStartElement("address-list");
			writer.writeCharacters(System.getProperty("line.separator"));
			
			
			// writes address elements
			for(MyAddress address : addressList)
				address.writeToXML(writer);
			
			// closes root element
			writer.writeEndElement();
			
			// finishes to write document
			writer.writeEndDocument();
			writer.flush();
			writer.close();
			
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the address list from an XML file.
	 * @param fileName the XML file to load.
	 * @return the number of addresses that are unloaded because it has
	 * wrong syntax. 0 if there are no unloaded addresses.
	 * @throws IOException signals if some I/O exception at the file occurs.
	 * @throws SAXException
	 * @throws WrongFileFormatException signals if the XML file is wrong
	 * file format for loading addresses.
	 */
	public int loadAddressListFromXML(String fileName) 
			throws IOException, SAXException, WrongFileFormatException
	{
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;
		int errorCount = 0;
		
		try {
			
			// initialize variable for reading from XML
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.parse(new File(fileName));
			
			Element element = document.getDocumentElement();
			
			// if the the name of root node is not address-list, it is wrong
			// file format, so throw a signal
			if(!element.getNodeName().equals("address-list"))
			{
				throw new MyAddressBook.WrongFileFormatException();
			}
			
			// get node list that the tag name is address
			NodeList addressNodeList = element.getElementsByTagName("address");
			
			for(int i=0; i<addressNodeList.getLength(); ++i)
			{
				// read nodes in the node list
				try
				{
					MyAddress address = new MyAddress();
					address.readFromXML(addressNodeList.item(i));
					addAddress(address);
				} catch(MyAddress.WrongSyntaxException e)
				{
					// if the node has wrong syntax, plus errorCount to 1
					++errorCount;
				}
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} 
		
		// return the number of unloaded addresses
		return errorCount;
	}
	
	/**
	 * Adds an call log to the call log list.
	 * @param call call log to add.
	 */
	public void addCallLog(Call call)
	{
		// add the call log to the list
		callList.add(call);
		
		// insert the call log to be sorted position
		InsertCall(callList.size() - 1);
	}
	
	/**
	 * Deletes an call log in the call log list.
	 * @param index the index of the call log to remove in the list.
	 * @throws IndexOutOfBoundsException if the index is out of range.
	 * (index < 0 || index >= size())
	 */
	public void deleteCallLog(int index)
	{
		callList.remove(index);
	}
	
	/**
	 * Returns the call log at the specified position in the call log list.
	 * @param index the index of the call log to return.
	 * @return the call log at the specified position in the call log list.
	 * @throws IndexOutOfBoundsException the index is out of bounds.
	 * (index < 0 || index >= size())
	 */
	public Call getCallLog(int index)
	{
		return callList.get(index);
	}
	
	/**
	 * Returns call logs that the receiver's or the sender's phone number is
	 * the specified one. The nodes of the list must be sorted in descending 
	 * order by time.
	 * @param phoneNumber the phone number to search.
	 * @return linked list with call logs nodes sorted in descending order by
	 * time.
	 */
	public LinkedList<Call> getCallLogs(PhoneNumber phoneNumber)
	{
		LinkedList<Call> callLogs = new LinkedList<Call>();
		
		for(Call call : callList)
		{
			// if the call is associated with the phone number,
			// add to the linked list
			if(call.getReceiver().equals(phoneNumber) || 
					call.getSender().equals(phoneNumber))
			{
				callLogs.add(call);
			}
		}
		
		// return the linked list
		return callLogs;
	}
	
	/**
	 * Saves the call logs list as an XML file.
	 * @param fileName the XML file name.
	 * @throws IOException signals if some I/O exception at the file occurs.
	 */
	public void saveCallLogListToXML(String fileName) throws IOException
	{
		XMLOutputFactory xml = XMLOutputFactory.newInstance();
		FileOutputStream file;
		XMLStreamWriter writer;
		
		try
		{
			file = new FileOutputStream(fileName);
			writer = xml.createXMLStreamWriter(file, "UTF-8");
			
			// starts to write document
			writer.writeStartDocument();
			writer.writeCharacters(System.getProperty("line.separator"));
			
			// writes root element
			writer.writeStartElement("call-list");
			writer.writeCharacters(System.getProperty("line.separator"));
			
			// writes call elements
			for(Call call : callList)
				call.writeToXML(writer);
			
			// closes root element
			writer.writeEndElement();
			
			// finishes to write document
			writer.writeEndDocument();
			writer.flush();
			writer.close();
			
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the call log list from an XML file.
	 * @param fileName the XML file to load.
	 * @return the number of call logs that are unloaded because it has
	 * wrong syntax. 0 if there are no unloaded calls.
	 * @throws IOException signals if some I/O exception at the file occurs.
	 * @throws SAXException
	 * @throws WrongFileFormatException signals if the XML file is wrong
	 * file format for loading call logs.
	 */
	public int loadCallLogListFromXML(String fileName)
			throws IOException, SAXException, WrongFileFormatException
	{
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;
		int errorCount = 0;
		
		try {
			
			// initialize variable for reading from XML
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.parse(new File(fileName));
			
			Element element = document.getDocumentElement();
			
			// if the the name of root node is not call-list, it is wrong
			// file format, so throw a signal
			if(!element.getNodeName().equals("call-list"))
			{
				throw new MyAddressBook.WrongFileFormatException();
			}
			
			// get node list that the tag name is call
			NodeList callNodeList = element.getElementsByTagName("call");
			
			for(int i=0; i<callNodeList.getLength(); ++i)
			{
				// read nodes in the node list
				try
				{
					Call call = new Call();
					call.readFromXML(callNodeList.item(i));
					addCallLog(call);
				} catch(Call.WrongSyntaxException e)
				{
					// if the node has wrong syntax, plus errorCount to 1
					++errorCount;
				}
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} 
		
		// return the number of unloaded calls
		return errorCount;
	}
	
	/**
	 * Returns the number of call logs in the book.
	 * @return the number of call logs in the book.
	 */
	public int getCallLogSize()
	{
		return callList.size();
	}
	
	/**
	 * Adds an message to the message list.
	 * @param message message to add.
	 */
	public void addMessage(Message message)
	{
		// add the message to the list
		messageList.add(message);
		
		// insert the message to be sorted position
		InsertMessage(messageList.size() - 1);
	}

	/**
	 * Deletes an message in the message list.
	 * @param index the index of the message to remove in the list.
	 * @throws IndexOutOfBoundsException if the index is out of range.
	 * (index < 0 || index >= size())
	 */
	public void deleteMessage(int index)
	{
		messageList.remove(index);
	}
	
	/**
	 * Returns the message at the specified position in the message list.
	 * @param index the index of the message to return.
	 * @return the message at the specified position in the message list.
	 * @throws IndexOutOfBoundsException the index is out of bounds.
	 * (index < 0 || index >= size())
	 */
	public Message getMessage(int index)
	{
		return messageList.get(index);
	}

	/**
	 * Returns messages that the receiver's or the sender's phone number is
	 * the specified one. The node of the list must be sorted in descending
	 * order by time.
	 * @param phoneNumber the phone number to search.
	 * @return linked list with messages node sorted in descending order by
	 * time.
	 */
	public LinkedList<Message> getMessages(PhoneNumber phoneNumber)
	{
		LinkedList<Message> messages = new LinkedList<Message>();
		
		for(Message message : messageList)
		{
			// if the message is associated with the phone number,
			// add to the linked list
			if(message.getReceiver().equals(phoneNumber) ||
					message.getSender().equals(phoneNumber))
			{
				messages.add(message);
			}
		}
		
		// return the linked list
		return messages;
	}
	
	/**
	 * Saves the message list as an XML file.
	 * @param fileName the XML file name.
	 * @throws IOException signals if some I/O exception at the file occurs.
	 */
	public void saveMessageListToXML(String fileName) throws IOException
	{
		XMLOutputFactory xml = XMLOutputFactory.newInstance();
		FileOutputStream file;
		XMLStreamWriter writer;
		
		try
		{
			file = new FileOutputStream(fileName);
			writer = xml.createXMLStreamWriter(file, "UTF-8");
			
			// starts to write document
			writer.writeStartDocument();
			writer.writeCharacters(System.getProperty("line.separator"));
			
			// writes root element
			writer.writeStartElement("message-list");
			writer.writeCharacters(System.getProperty("line.separator"));
			
			// writes message elements
			for(Message message : messageList)
				message.writeToXML(writer);
			
			// closes root element
			writer.writeEndElement();
			
			// finishes to write document
			writer.writeEndDocument();
			writer.flush();
			writer.close();
			
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the message list from an XML file.
	 * @param fileName the XML file to load.
	 * @return the number of messages that are unloaded because it has
	 * wrong syntax. 0 if there are no unloaded messages.
	 * @throws IOException signals if some I/O exception at the file occurs.
	 * @throws SAXException
	 * @throws WrongFileFormatException signals if the XML file is wrong
	 * file format for loading messages.
	 * @throws FileNotFoundException signals if the file does not exist.
	 */
	public int loadMessageListFromXML(String fileName)
			throws IOException, SAXException, WrongFileFormatException
	{
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;
		int errorCount = 0;
		
		try {
			
			// initialize variable for reading from XML
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.parse(new File(fileName));
			
			Element element = document.getDocumentElement();
			
			// if the the name of root node is not message-list, it is wrong
			// file format, so throw a signal
			if(!element.getNodeName().equals("message-list"))
			{
				throw new MyAddressBook.WrongFileFormatException();
			}
			
			// get node list that the tag name is message
			NodeList messageNodeList = element.getElementsByTagName("message");
			
			for(int i=0; i<messageNodeList.getLength(); ++i)
			{
				// read nodes in the node list
				try
				{
					Message message = new Message();
					message.readFromXML(messageNodeList.item(i));
					addMessage(message);
				} catch(Message.WrongSyntaxException e)
				{
					// if the node has wrong syntax, plus errorCount to 1
					++errorCount;
				}
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} 
		
		// return the number of unloaded calls
		return errorCount;
	}
	
	/**
	 * Returns the number of messages in the book.
	 * @return the number of messages in the book.
	 */
	public int getMessageSize()
	{
		return messageList.size();
	}
	
	////////////////////////////////////////////////////////////////////////////
	/* EXCEPTIONS */
	
	/**
	* This exception is thrown by MyAddressBook object if failed to load data
	* from the XML files because it has wrong file format or not XML file.
	* 
	* @author Kang Seung Won 
	*/
	@SuppressWarnings("serial")
	public static class WrongFileFormatException extends Exception 
		{
		
		/**
		* Constructs a exception representing the number is wrong syntax.
		*/
		public WrongFileFormatException()
		{
		super("Wrong File Format!");
		
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	
}


















