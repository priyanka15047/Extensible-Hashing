package extensibleHashing;

import java.util.Vector;

public class SecondaryMem {
Vector<EBucket> buckets = new Vector<EBucket>(700000);
static SecondaryMem mem = null;
static int count=0;

public static SecondaryMem getInstance()
{
  if(mem==null)
  {
	  mem = new SecondaryMem();
	  return mem;
  }
  else
  {
	  return mem;
  }
}
 
  public int getBucket(int localDepth)
  { 
	EBucket br = buckets.get(count);
	br.localDepth=localDepth;
	count++;
    return (buckets.indexOf(br));
  }
  
  public void printBucket()
	{ 
	 for(int j=0;j<buckets.size();j++)
	 {  // buckets.get(j).print();
		 System.out.println("");
	 }
	}
}
