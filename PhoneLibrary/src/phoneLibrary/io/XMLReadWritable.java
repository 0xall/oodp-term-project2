package phoneLibrary.io;

import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Node;

/*******************************************************************************
 * 
 *  This interface represents the object is readable and writable to the XML
 *  files as an element. (not the whole XML file, just the part of the XML file)
 * 
 * @author Kang Seung Won, Lee Ho Jun
 * 
 ******************************************************************************/

public interface XMLReadWritable {

	////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Write the object to the XML file. It can be loaded again by ReadFromXML() 
	 * methods.
	 * @param writer the XMLStreamWriter object for writing to XML.
	 * @throws Exception exceptions for writing to XML.
	 */
	public void writeToXML(XMLStreamWriter writer) throws Exception;
	
	/**
	 * Reads the object data from XML file.
	 * @param xmlAddressElement the element in the XML.
	 * @throws Exception exceptions for loading from XML.
	 */
	public void readFromXML(Node xmlAddressElement) throws Exception;
	
	////////////////////////////////////////////////////////////////////////////
}
