import java.util.ArrayList;
import java.util.Date;

/*******************************************************************************
 * 
 * MyAddress class represents an address saved in the Phone address book. 
 * 
 * @author Lee Ho Jun
 *
 ******************************************************************************/

public class MyAddress {

	////////////////////////////////////////////////////////////////////////////
	/* MEMBER Variables */
	
	private String 					name;
	private ArrayList<PhoneNumber> 	phoneNumberList;
	private String					homeAddress, officeAddress;
	private String					group;
	private String					email;
	private String					url;
	private Date					birthday;
	
	
	/////////////////////////////////////////////////////////////////////////////
	/* CONSTRUCTORS */
	
	/**
	 * Constructs an address with empty value.
	 */
	MyAddress()
	{
		name = "";
		phoneNumberList = new ArrayList<PhoneNumber>();
		homeAddress = ""; officeAddress = "";
		group = "";
		email = "";
		url = "";
		birthday = new Date();
	}
	
	/////////////////////////////////////////////////////////////////////////////
	/* METHODS */
	
	/**
	 * Adds a phone number to the address.
	 * @param phoneNumber the phone number to add.
	 */
	void AddPhoneNumber(PhoneNumber phoneNumber)
	{
		phoneNumberList.add(phoneNumber);
	}
	
	/**
	 * Deletes a phone number by the phone number array index.
	 * @param index the phone number array index to delete.
	 */
	void DeletePhoneNumber(int index)
	{
		phoneNumberList.remove(index);
	}
	
	/**
	 * Deletes a phone number by the PhoneNumber instance.
	 * @param phoneNumber the phone number to remove.
	 */
	void DeletePhoneNumber(PhoneNumber phoneNumber)
	{
		phoneNumberList.remove(phoneNumber);
	}
	
	/**
	 * Modifies a phone number.
	 * @param index the index of the phone number to modify.
	 * @param phoneNumber the modifying phone number value.
	 */
	void ModifyPhoneNumber(int index, PhoneNumber phoneNumber)
	{
		PhoneNumber modPhoneNumber = phoneNumberList.get(index);
		modPhoneNumber.setPhoneNumber(phoneNumber);
	}
}
