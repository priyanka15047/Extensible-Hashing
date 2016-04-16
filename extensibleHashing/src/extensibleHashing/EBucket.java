package extensibleHashing;

public class EBucket {
  int localDepth;
  int size;
  int array[];
  int noOfEmptySpace;
  int overflow;
  
  public EBucket() {
	
    size = 40;
    noOfEmptySpace = size;
    array = new int[size];
    overflow = -1;
    
  }
  public void print()
  {   
  	  for(int i=0;i<size-noOfEmptySpace;i++)
	  {   
		  System.out.print(array[i]+" ");
	  }
  	/*  if(overflow!=null)
  	  {
  		  for(int j=0;j<overflow.size-overflow.noOfEmptySpace;j++)
  		  {
  			  System.out.print(overflow.array[j]+" ");
  		  }
  	  }*/
  }
}
