package extensibleHashing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Directory {
  
  int dir[] = new int[1];
  int globalDepth;
  int maxDepth=10;
  SecondaryMem mem = SecondaryMem.getInstance();
  Hasher h = new Hasher();
  int bucketDir = 600001;
  static int splitCost=0;
  public Directory()
	{   
	    globalDepth = 0;
	    dir[0]= mem.getBucket(0);
	}
    
  public int insert(int value)
  {   
  	if(globalDepth == 0)
  	{   
  	    return 0;
  	}
  	else
  	{
  		return h.hash(value,globalDepth);
  	}
  }
  
  	public void insertValue(int value) 
  	{   
  	//	System.out.print(value+ " "+insert(value) );
  		System.out.println();
  	    int bucketPos = insert(value);
  	    EBucket eB = null;
  	    int bucketIndex ;
  	    if(bucketPos<1024)
  	    {  
  	    	bucketIndex = dir[bucketPos];
  	    	eB = mem.buckets.get(bucketIndex);
  	    }
  	    else
  	    {   
  	        int index = searchKey(bucketPos);
  	        bucketIndex = index;
  	        eB = mem.buckets.get(bucketIndex);
  	    }
  		
  		if(eB.noOfEmptySpace==0 && eB.localDepth==globalDepth)
  	    {   
  			split(bucketPos,bucketIndex,value);
  	    }
  		
  	    else if(eB.noOfEmptySpace==0 && eB.localDepth<globalDepth)
  	    {   System.out.println("hello");
  	    	int pos = findPossition(eB,bucketIndex,bucketPos);
  	    	
  	    	int diff = globalDepth-eB.localDepth;
  	    	
	    	int x=(int)Math.pow(2,diff);
	    	int index = 0;
  	    	eB.localDepth+=1;
  	    	int lastDirBucket=mem.buckets.indexOf(mem.buckets.lastElement());
  	    	if(pos>=(int)Math.pow(2, maxDepth))
  	        {   splitCost=splitCost+(x/2);
  	        	for(int i=0;i<x/2;i++)
  	  	    	{   putBucketIndex((pos+i),bucketIndex,lastDirBucket);
  	        		
  	  	    	}
  	        	for(int i=x/2;i<x;i++)
  	  	    	{    index= mem.getBucket(eB.localDepth);
  	  	            putBucketIndex((pos+i),index,lastDirBucket);	
  	  	    	}
  	        }
  	        else
  	        {
  	        	for(int i=0;i<x/2;i++)
  	        	{   if((pos+i)<1024)
  	    			{  
  	        			dir[pos+i]= bucketIndex;
  	    			}
  	        		else
  	        		{   splitCost=splitCost+(x/2);
  	        			putBucketIndex((pos+i),bucketIndex,lastDirBucket);	
  	        		}
  	        	}
  	        	for(int i=x/2;i<x;i++)
  	        	{	if((pos+i)<1024)
  	    			{   System.out.println(pos+i);
  	        			dir[pos+i]=mem.getBucket(eB.localDepth);
  	    		
  	    			}
  	        		else
  	        		{   splitCost=splitCost+(x/2);
  	        			index= mem.getBucket(eB.localDepth);
  	        		    putBucketIndex((pos+i),index,lastDirBucket);
  	        		}
  	        	}
  	        }
   	    	rehash(bucketIndex,value);
   	    	if(mem.buckets.get(index).noOfEmptySpace<mem.buckets.get(index).size && index!=0)
   	    	{
   	    		splitCost=splitCost+1;
   	    	}
   	    	if(mem.buckets.get(bucketIndex).noOfEmptySpace<mem.buckets.get(bucketIndex).size)
   	    	{
   	    		splitCost=splitCost+1;
   	    	}
   	    		
   	        System.out.println(splitCost+"splitcost");
  	    }
  	    else
  	    {  
  	    	eB.array[eB.size-eB.noOfEmptySpace]=value;
  	    	eB.noOfEmptySpace--;
  	    	
  	    }
  		
  	} 	
    
    private void putBucketIndex(int pos,int bucketIndex,int lastDirBucket) {
    	
    	for(int p=bucketDir;p<=lastDirBucket;p++)
  		{   
  			int j=0;
  			while(j<mem.buckets.get(p).size)
  			{   
  				if(mem.buckets.get(p).array[j]==pos)
  				{  
  					mem.buckets.get(p).array[j+1]=bucketIndex;
  					
  				}
  				j=j+2;
  			}
  		}
		
	}

	private int findPossition(EBucket eB, int bucketIndex,int bucketPos) {
    	int diff = globalDepth-eB.localDepth;
	    	int pos = bucketPos;
	    	int x=(int)Math.pow(2,diff);
	    	for(int i=0;i<x;i++)
	        {   if(pos==0)
	        	{
	        	   pos = pos;
	        	}
	        	else
	        	{
	        		pos=pos-1;
	        		if(pos<(int)Math.pow(2,maxDepth))
	        		{
	        			if(dir[pos]!=bucketIndex)
	  	        		{
	  	        			pos=pos+1;
	  	        			break;
	  	        		}
	        		}
	        		else
	        		{   
	        			if(searchKey(pos)!=bucketIndex)
	        			{   
	        				pos=pos+1;
	        				break;
	        			}
	        		}
	      		}
	        }
	    	return pos;
	}

	private void rehash(int bucketIndex, int value) 
    {  
    	ArrayList<Integer> elements = new ArrayList<Integer>();
		elements = getBucketElement(bucketIndex);
        System.out.println(elements);
        System.out.println(elements.size());
		for(int j=0;j<elements.size();j++)
		{  
			insertValue(elements.get(j));
		}
		
		EBucket b = mem.buckets.get(bucketIndex);  
		if(b.noOfEmptySpace ==0)
		  {
			  b.overflow = mem.getBucket(0);
			  mem.buckets.get(b.overflow).array[mem.buckets.get(b.overflow).size-mem.buckets.get(b.overflow).noOfEmptySpace]=value;
			  mem.buckets.get(b.overflow).noOfEmptySpace--;
		  }
		  else
		  {
			  b.array[b.size-b.noOfEmptySpace]=value;
			  b.noOfEmptySpace--;
		  }
	}

	private int searchKey(int bucketNo) {
		
    	int lastDirBucket=mem.buckets.indexOf(mem.buckets.lastElement());
    	for(int i=bucketDir;i<=lastDirBucket;i++)
    	{   
    		int j=0;
    		while(j<mem.buckets.get(i).size)
    		{   
    			if(mem.buckets.get(i).array[j]==bucketNo)
    			{  
    			   return (mem.buckets.get(i).array[j+1]);
    			}
    			j=j+2;
    		}
    	}
    	return -1;
		
	}
    
    public void split(int bucketPos,int bucketNumber, int value)
	{   int no = 0;
		ArrayList<Integer> oldDir = new ArrayList<Integer>();
		ArrayList<Integer> newDir = new ArrayList<Integer>();
		ArrayList<Integer> SecDir = new ArrayList<Integer>();
		
		// taking the elemens of bucket to split
		ArrayList<Integer> elements = new ArrayList<Integer>();
		elements = getBucketElement(bucketNumber);
		
	    globalDepth+=1;
		
	    // clear the bucket to be split
	    mem.buckets.get(bucketNumber).noOfEmptySpace=mem.buckets.get(bucketNumber).size;
	    if( mem.buckets.get(bucketNumber).overflow!=-1)
	    {
	    	mem.buckets.get(mem.buckets.get(bucketNumber).overflow).noOfEmptySpace=mem.buckets.get(bucketNumber).size;
	    }
	    mem.buckets.get(bucketNumber).overflow=-1;
		mem.buckets.get(bucketNumber).localDepth+=1;
    	
        for(int i=0;i<dir.length;i++)
        {
        	oldDir.add(dir[i]);	
        }
       
        for(int i=0;i<2*oldDir.size();i++)
		{   
        	newDir.add(oldDir.get((int)Math.ceil(i/2)));
		}
        
        
        if(globalDepth<=10)
	  	{ 
        	dir = new int[newDir.size()];
	        for(int i=0;i<newDir.size();i++)
	        {
	        	dir[i]=newDir.get(i);
	        }
	        no=mem.getBucket(mem.buckets.get(dir[2*bucketPos]).localDepth);
	        dir[2*bucketPos+1]= no;
	        
	  	}
        
	   else
		{   System.out.println("poiiut"); 
		    int lastDirBucket=mem.buckets.indexOf(mem.buckets.lastElement());
		     if(globalDepth==11)
			{ 
			splitCost=(int)Math.ceil(((double)1024/(mem.buckets.get(0).size/2)));
			}
		    if(lastDirBucket>=bucketDir)
		    {   
		    	for(int j=bucketDir;j<=lastDirBucket;j++)
		    	{
		    		int l=0;
		    		while(l<mem.buckets.get(j).size)
		    		{
		    			 SecDir.add(mem.buckets.get(j).array[l+1]);
		    			 l=l+2;
		    		}
		    		mem.buckets.get(j).noOfEmptySpace=mem.buckets.get(j).size;
		    		
		    	}
		    	
		    	
			   
			      splitCost=2*(int)Math.ceil((double)SecDir.size()/(mem.buckets.get(0).size/2));
			      
			    
               
                for(int m=0;m<2*SecDir.size();m++)
				{  
				   newDir.add(SecDir.get((int)Math.ceil(m/2)));
				   if(newDir.size()==(int)Math.pow(2,globalDepth))
				   {
					break;   
				   }
				}	 
					
		    }
		   
       no=mem.getBucket(mem.buckets.get(newDir.get(2*bucketPos)).localDepth);
    
       newDir.set(2*bucketPos+1,no);
    
		dir = new int[1024];
		for(int l=0;l<1024;l++)
		{
		    dir[l]=newDir.get(l);
		}
		
		int size=0;
		if(lastDirBucket>=bucketDir)
		{   int totalEntries=(int)Math.pow(2,globalDepth);
		     size=mem.buckets.get(0).size/2;
		    int len = totalEntries-((lastDirBucket-(bucketDir-1))*size)-1024;  
			for(int i=0;i<=(int)Math.ceil(len/size);i++)
			{  
				mem.buckets.add(lastDirBucket+1+i,new EBucket() );
			}
		}
		else
		{    size=mem.buckets.get(0).size/2;
			for(int i=0;i<(int)Math.ceil(Math.pow(2,maxDepth)/size);i++)
			{   
				mem.buckets.add(new EBucket());
				
			}
		}
		
		lastDirBucket = mem.buckets.indexOf(mem.buckets.lastElement());
		
		int bucket=bucketDir,i=1024;
		
		while(i<newDir.size())
			{  
		    	while(bucket<=lastDirBucket)
		    	{  
		    		int j=0;
		    		while(j<mem.buckets.get(bucket).size)
		    		{  
		    			if(i<newDir.size())
		    			{	mem.buckets.get(bucket).array[j]=i;
		    				mem.buckets.get(bucket).array[j+1]=newDir.get(i);
		    				j=j+2;
		    				i++;
		    			}
		    			else
		    			{   
		    				break;
		    			}
		    		}
		    		bucket++;
		    		
		    		
		    	}
		    	
		    }
		    
		}
		
		for(int j=0;j<elements.size();j++)
		{   
			insertValue(elements.get(j));
		}
		
		EBucket b = mem.buckets.get(bucketNumber);  
		
		if(b.noOfEmptySpace==0)
		  {   
			  b.overflow = mem.getBucket(0);
			  mem.buckets.get(b.overflow).array[mem.buckets.get(b.overflow).size-mem.buckets.get(b.overflow).noOfEmptySpace]=value;
			  mem.buckets.get(b.overflow).noOfEmptySpace--;
		  }
		  else
		  {  
			  b.array[b.size-b.noOfEmptySpace]=value;
			  b.noOfEmptySpace--;
		  }
		if(no!=0)
		{
		EBucket bm=mem.buckets.get(no);
		
		if(bm.noOfEmptySpace<mem.buckets.get(0).size)
		{	
			splitCost=splitCost+1;
		
		}
		if(b.noOfEmptySpace<mem.buckets.get(0).size)
		{  
			splitCost=splitCost+1;
			
		}
		
		System.out.println(splitCost+"splitcost");
	}
	}
	
    
    private ArrayList<Integer> getBucketElement(int bucketNumber)
	 {
		    ArrayList<Integer> elements = new ArrayList<Integer>();
		    for(int i = 0;i<mem.buckets.get(bucketNumber).size;i++)
			{
				elements.add(mem.buckets.get(bucketNumber).array[i]);
			}
	    
		    if(mem.buckets.get(bucketNumber).overflow!=-1)
		    {
		    	int len = mem.buckets.get(mem.buckets.get(bucketNumber).overflow).size- mem.buckets.get(mem.buckets.get(bucketNumber).overflow).noOfEmptySpace;
				   
		    	for(int i=0; i<len;i++)
		    	{   
		    	    elements.add(mem.buckets.get(mem.buckets.get(bucketNumber).overflow).array[i]);
		    		
		    	}
		    	
		    }	
		    mem.buckets.get(bucketNumber).noOfEmptySpace=mem.buckets.get(bucketNumber).size;
			mem.buckets.get(bucketNumber).overflow=-1;
		    return elements;	
	}
	 
	 public int calcualteNoOfBucket()
		{   int noOfBuckets=1;
			int bucket=0;
		    
			if(globalDepth<=10)
		    {
		    	for(int i=0;i<(int)Math.pow(2,globalDepth);i++)
		    	{
		    		if(dir[i]!=bucket)
		    		{
		    			bucket=dir[i];
		    			noOfBuckets++;
		    		}
		    		
		    	}
		    }
			else
			{
				for(int i=0;i<(int)Math.pow(2,maxDepth);i++)
		    	{
		    		if(dir[i]!=bucket)
		    		{
		    			bucket=dir[i];
		    			noOfBuckets++;
		    		}
		    	}
				
				int lastDirBucket=mem.buckets.indexOf(mem.buckets.lastElement());
                int count=0;		
		    	for(int i=bucketDir;i<=lastDirBucket;i++)
		    	{  
		    		int j=0;
		    		while(j<mem.buckets.get(i).size)
		    		{   if(mem.buckets.get(i).array[j+1]!=bucket && mem.buckets.get(i).array[j+1]!=0)
		    			{   count++;
		    			   bucket=mem.buckets.get(i).array[j+1];
		    			   noOfBuckets++;
		    			}
		    			j=j+2;
		    		}
		    		
		    	}
		   
				
				
			}
			
			return noOfBuckets;
			
		}
		 

	public void printBucket()
		{ 
		try {
			Writer out = new FileWriter("directorys.txt",true);
			
						
		
		
		 System.out.println(dir.length);
		 
		 for(int j=0;j<dir.length;j++)
		 {   System.out.print(j+"-> ");
		 	 out.write(String.valueOf(j)+"-> ");	
		     System.out.print(dir[j]+"-> ");
		     out.write(dir[j]+"-> ");
			 mem.buckets.get(dir[j]).print();
			 System.out.println("");
		 }
		
		 int last=mem.buckets.indexOf(mem.buckets.lastElement());
		 System.out.println(last+"last");
		 if(last>=bucketDir)
		 {
			 for(int i=bucketDir;i<=last;i++)
			 {   int j=0;
				 while(j<mem.buckets.get(i).size)
				 {
					 System.out.print(mem.buckets.get(i).array[j]+"->");
					 out.write(mem.buckets.get(i).array[j]+"->");
					 System.out.print(mem.buckets.get(i).array[j+1]+"->");
					 out.write(mem.buckets.get(i).array[j+1]+"->");
					 mem.buckets.get(mem.buckets.get(i).array[j+1]).print();
					 System.out.println();
					 j=j+2;
				 }
			 }
			 
		 }
		 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		}

       public int search(int value)
       {   int count=0;
    	   int dirPoss=insert(value);
    	   System.out.println(dirPoss);
    	   if(dirPoss<1024)
    	   {
    		 count++;
    		 if(mem.buckets.get(dir[dirPoss]).overflow!=-1)
    		 {
    			 count++;
    		 }
    	   }
    	   else
    	   {  count++;
    	      int size=mem.buckets.get(0).size/2;
    	   	  int access= (int)Math.ceil((double)(dirPoss-1024)/size);
    	   	 
    	   	  int bucket = bucketDir+access-1;
    	   	 
    	   	  int j=0;
    	   	  while(j<mem.buckets.get(bucket).size)
    	   	  {   
    	   		  if(mem.buckets.get(bucket).array[j]==dirPoss)
    	   	  	{   
    	   		    count++;
    	   		    if(mem.buckets.get(mem.buckets.get(bucket).array[j+1]).overflow!=-1)
    	   		    {
    	   		    	count++;
    	   		    }
    	   	  	}
    	   		  j=j+2;
    	   	  }
    		   
    	   }
    	   return count;
       }
    
   	public static String choose(File f, int linenumber) throws FileNotFoundException
   	{
   	    String result = null;
   	    Random rand = new Random();
   	    //System.out.println(linenumber);
   	    int n = 1 + rand.nextInt(linenumber);
   	    Scanner sc = new Scanner(f);
   	    
   	    for(int i = 0 ;i < n;i++)
   	    {
   	   	 sc.nextLine();
   	   	 
   	    }
   	    result = sc.nextLine();

   	    return result;      
   	 }

   	public  double metrictwo(int linenumber){
   		double bs = 0;
   		try {
   			for(int i = 0;i<50;i++){
   				String s = choose(new File("Dataset-HighBit.txt"),linenumber);
   				int cost = search(Integer.parseInt(s));
   				if(cost>0){
   					bs += cost;
   				}
   				System.out.println(s);
   				
   				//System.out.println(bs/50);
   			}
   			
   			
   		} catch (FileNotFoundException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
   		
   		return bs/50;
   		
   		
   	}
   	
   	
   	
}
