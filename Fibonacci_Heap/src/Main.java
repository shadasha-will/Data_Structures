//Data Structures 
// Homework #2
// Fibonacci Heaps

import java.io.*;
import java.util.Scanner;

public class Main {
	public static  void main(String[] args) throws IOException {
		 //naive_fibonacci heap = null;
		 Fibonacci_classic heap = null;
         long total_steps = 0;
         int extracts = 0;
         int result = 0;
	    Scanner sc = new Scanner(System.in);
		String strLine = "";
		    while(sc.hasNextLine()) {
		    	strLine = sc.nextLine();
	        	String[] commands = strLine.split(" ");
	        	String command = commands[0];
	        	switch(command) {
	        	case "#":
	        		if (heap!= null) {
    	    				System.out.println(result + " " + ((double) total_steps) / extracts);
    	    				total_steps = 0;
    	    				extracts = 0; }
    	    			String num = strLine.substring(1).replaceAll("\\s", "");
    	    			result = Integer.parseInt(num);
    	    			heap = new Fibonacci_classic(result);
    	    			//heap = new naive_fibonacci(result);
    	    			break;
	        	case "INS":
	        		String[] inserts = strLine.split(" ");
            		String first = inserts[1];
            		String second = inserts[2];
            		int element = Integer.parseInt(first);
            		int key = Integer.parseInt(second);
            		heap.insert(element, key);
            		break;
            		
	        	case "DEL":
	        		total_steps += heap.extract_Min();
	        		extracts++;
	        		break;
	        		
	        	case "DEC":
	        		String[] dec = strLine.split(" ");
            		String first_dec = dec[1];
            		String second_dec = dec[2];
            		int element_dec = Integer.parseInt(first_dec);
            		int key_dec = Integer.parseInt(second_dec);
            		heap.decrease_key(element_dec, key_dec);
            		break;
            	default:
            		continue;
	        		
	        	}
	        	  
	        }
	        System.out.println(result + " " + ((double) total_steps) / extracts);
	        sc.close();
	    } 
	   
		
	}

	

