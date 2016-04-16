package extensibleHashing;

public class Hasher {

	public int hash(int value, int globalDepth) {
		
		String str = bitsOf(value,globalDepth);
		return Integer.parseInt(str,2);
	}
	
	public String bitsOf(int value,int globalDepth) {
	    String binary="";
	    String bits="";
		binary = binaryOf(value);
		int zeroToAppend=20-binary.length();
		
		if(binary.length()<20)
		{
			for(int i=0;i<zeroToAppend;i++)
			{  
				binary = '0'+binary;
			}
			bits = binary;
		}
		else
		{
			bits = binary;
		}
		
		String str ="";
		for(int i=0;i<globalDepth;i++)
		{   
			str=str+bits.charAt(i);
		}
		
		return str;
		
	}

	public String binaryOf(int value) 
	{
		String str = "";
		int a = value;
		str = a%2 + str;
		a = a / 2;
		while(a>0)
			{   
				str =  a%2 + str;
		    	a = a/2;
			}
	    return str;
	}
}
