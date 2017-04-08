
public class LibraryTester {
	
	public static void main(String[] args) throws Exception
	{
		String test = "abcdefg";
		
		String n = test.toString();
		Object x = n;
		
		if(x instanceof String)
			System.out.println(x);
	}
}
