package extensibleHashing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Random;
import java.util.Scanner;

public class Hashing 
{ 

	public static void main(String[] args)
	{   // to initialize the secondary memory
		
		Hashing h=new Hashing();
		for(int i=0;i<=600000;i++)
		{
		    SecondaryMem.getInstance().buckets.add(new EBucket());
		}
		
	    Directory directory = new Directory();
	   
	    BufferedReader br;
	//    float Utilization=0.0f;
	/*    int bc=10;
	    int B;
	*/	try { 
			FileInputStream fstream = new FileInputStream("Dataset_HighBit.txt");
		    br = new BufferedReader(new InputStreamReader(fstream));
		//	Writer out = new FileWriter("eh_storage_10_H.txt",true);
		//	Writer out1 = new FileWriter("eh_search_10_H.txt",true);
			Writer out3 = new FileWriter("eh_splitingcost_40H.txt",true);
			String str;
			int a,count=0;
			// record from dataset 
			while((str=br.readLine())!=null && count<100000)
				{   count++;
					a = Integer.parseInt(str);
					//System.out.print("entry "+count+" ")
					Directory.splitCost=0;
					directory.insertValue(a);
			//		B=directory.calcualteNoOfBucket();
					
				//	Utilization = (float)count/(bc*B);
				//	out.write(String.valueOf(count)+","+String.valueOf(Utilization)+System.getProperty("line.separator"));
					
				/*	if(count % 5000 == 0){
						out1.write(String.valueOf(count) + "," + String.valueOf(directory.metrictwo(count)) + System.getProperty("line.separator") );
						}
					*/
					/*if(SecondaryMem.count==600000)
					{
						break;
					}*/
					
					out3.write(String.valueOf(count)+","+String.valueOf(Directory.splitCost)+System.getProperty("line.separator"));
					
				}
			//out1.close();
		//	out.close();
			out3.close();
		
		//	directory.printBucket();
		//	System.out.println();
		
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
		
	}
		
	
}
