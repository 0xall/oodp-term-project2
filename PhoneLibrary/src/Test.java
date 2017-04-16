
import java.io.IOException;

import phoneLibrary.address.MyAddress;
import phoneLibrary.address.MyAddressBook;
import phoneLibrary.address.PhoneNumber;
import phoneLibrary.address.PhoneNumber.WrongSyntaxException;

public class Test {
	public static void main(String[] args) throws WrongSyntaxException, IOException
	{
		MyAddressBook addressBook = new MyAddressBook();
		
		MyAddress address = new MyAddress();
		address.setName("°­½Â¿ø");
		address.addPhoneNumber(new PhoneNumber("01038940662"));
		
		addressBook.addAddress(address);
		addressBook.saveAddressListToXML("test.xml");
	}
}
