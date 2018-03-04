
import java.io.*;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {
		Naive_splay sptree = new Naive_splay();

		// read from standard input
	    Scanner sc = new Scanner(System.in);
		String line = "";
		int finds = 0;
		// store the size
		int size = 0;
		int count = 0;
		int total_counts = 0;
        
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			if (line.contains("#")) {
				if (finds != 0) {
					System.out.println(size + " " + ((double) total_counts) / finds);
					total_counts = 0;
					count = 0;
					finds = 0;
				}
				sptree = new Naive_splay();
				String[] sizes = line.split(" ");
				String sizes2 = sizes[1];
				size = Integer.parseInt(sizes2);
				
			}
			if (line.contains("I")) {
				String[] inserts = line.split(" ");
				String insert2 = inserts[1];
				int result = Integer.parseInt(insert2);
				sptree.insert(result);
			
			} else if (line.contains("F")) {
				finds++;
				String[] parts = line.split(" ");
				String part2 = parts[1];
				int result2 = Integer.parseInt(part2);
			    count = sptree.findNode(result2);
			    if (count > 0) {
			    	  total_counts += count;
			    }

			}

	}
			
		// print the last tree
		System.out.println(size + " " + ((double) total_counts) / finds);
		sc.close();
	} 
}