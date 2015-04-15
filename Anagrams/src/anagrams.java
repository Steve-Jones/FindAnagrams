/*
	Steven Jones
	CS311: PA2 - Anagrams
	
	1 CODE (See below)
	2 DESCRIPTION OF ALGORITHM:
		To solve find all the anagrams in the dictionary I first created a 
		static variable for a string of the letters in the alphabet and a 
		static variable for the the first 26 prime numbers starting with 3. 
		
		I then have the user input a 1 or 2 for which dictionary they would
		like to use. Based on their input I create the variables for the
		input and output file names.
		
		I count the number of lines of the input dictionary, then read in 
		all these lines and put each word into my array of words.
		
		I then take each word and calculate the product of the prime numbers
		associated with each letter in the word using a separate method.
		This method take in a word and goes through each letter and finds the
		index of the character in my alphabet string. It uses this index to
		find the prime number associate with that letting in my prime number
		array. It multiplies this prime number to the product of the prime
		numbers of the letters before it in the word. Then the method returns
		this product.
		
		I create an array and store all of the prime products in it. I then
		use a merge sort to sort this array so all of the duplicate products 
		are adjacent. When I sort the prime products I also include the array
		of words so that the index of the word array still matches the index
		of the prime products array. 
		
		I then go iterate through the indexes and I check if the previous
		prime product matches the current prime product. If it does that
		means they are in the same anagram class and I write the words
		associated with them to the same line of an output file. If not I
		write the word associated with it on its own line. After iterating
		through each word the file is complete and writes out.
		
		This algorithm produces the correct answer because I associate each
		letter in the alphabet with a unique prime number and find the
		product of these numbers associated with each letter in the word.
		Because I am using prime numbers no other product of other letters
		will be the same. The product of the numbers associated with the
		letters in a word will be the same as the product of any other word 
		that is an anagram because it does not matter the order of the
		letters in the word. This means I can add all of the words with
		the same products to an anagram class.
		
		RUN TIME:
			w 		- Count words in input dictionary
			w 		- Read in words and put in array
			l*w 	- Create product of primes from word and add each product to array
			wlogw 	- Merge sort
			w		- Add anagram classes to output file
			-------
			= wlogw + l*w + 3w
			wlogw + l*w
			
	3 RUN TIME
		MACHINE: MacBook Pro (Retina, 13-inch, Late 2013) 2.4 GHz Intel Core i5
			Dict1:
				real	0m3.522s
				user	0m0.610s
				sys		0m0.058s
			Dict2:
				real	0m17.373s
				user	0m34.135s
				sys		0m1.291s
		MACHINE: edlab
			Dict1:
				real	0m1.097s
				user	0m0.616s
				sys		0m0.076s
			Dict2
				real	0m29.025s
				user	2m22.877s
				sys		0m1.428s
	4 OUTPUT
		Anagram Classes:
			Dict1: 67604
			Dict2: 320722
		anagram1 Classes with over 5 words:
			elva lave leva vale veal vela 
			ardeb barde beard bread debar debra 
			aril lair liar lira rail rial 
			caret cater crate react recta trace 
			leapt lepta palet petal plate pleat 
			least setal slate stale steal stela teals 
			reins resin rinse risen serin siren 
			luster lustre result rustle sutler ulster 
		
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class anagrams {
	static int[] primes = {3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103};
	static String alphabet = "abcdefghijklmnopqrstuvwxyz";

	//DICT WORD --> SUM PRIME NUMS
	public static int wordToPrimeSum(String s) {
		int result=1;
		for(int i=0;i<s.length();++i){
			int index=alphabet.indexOf(s.charAt(i));
			if(index!=-1){//ignore unknown characters.
				result=result*primes[index];
			}
		}
		return result;
	}
	//MAIN METHOD
	public static void main (String[] args) throws IOException{
		Scanner scan = new Scanner(System.in);

		String[] wordArray; //ARRAY OF DICT WORDS
		int lineCount=0; //LINE NUMBER OF DICT
		
		System.out.print("Choose which dictionary to use (Enter 1 or 2): ");
		int dictNum = scan.nextInt();
		String dict="";
		String anaNum="";
		if(dictNum == 2){
			dict = "dict2";
			anaNum="anagram2";
		}
		else if(dictNum == 1){
			dict = "dict1";
			anaNum="anagram1";
		}
		else
			System.out.println("\nMust Enter 1 or 2\n\n");
		String inputFile = dict;
		//COUNT DICT LINES
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		try {
			while(br.readLine() != null)
				lineCount++;
		}
		finally {
			br.close();
		}

		//READ DICT WORDS --> ARRAY
		BufferedReader br2 = new BufferedReader(new FileReader(inputFile));
		try {
			//CREATE WORD ARRAY
			wordArray = new String[lineCount];

			//ADD LINES TO ARRAY
			String line = br2.readLine();
			int i=0;
			while (line != null) {
				wordArray[i] = line; //ADD WORD TO ARRAY
				line = br2.readLine(); //READ NEXT LINE
				i++;
			}
		}
		finally {
			br2.close();
		}

		int[] primeArray = new int[lineCount]; //ARRAY OF PRIME NUMS
		//WORD --> SUM OF PRIME CHARS
		for(int i=0; i < lineCount; i++){
			primeArray[i] = wordToPrimeSum(wordArray[i]);
			//System.out.println(wordArray[i]+" = "+primeArray[i]);
		}
		
		//SORT THAT REAL GOOD
		mergeClass m = new mergeClass();
		m.mSort(primeArray, wordArray);
		
		//MAKE OUTPUT FILE
		PrintWriter pw1 = new PrintWriter(new FileWriter(anaNum));
		int printLnCount=0;
		//ADD ANAGRAM CLASSES TO OUTPUT FILE
		for(int i=1; i < lineCount; i++){
			if(primeArray[i] == primeArray[i-1]){
				pw1.write(wordArray[i]+" ");
			}
			else{
				pw1.write("\n"+wordArray[i]+" ");
				printLnCount++;
			}	
			//System.out.println(primeArray[i]+" = "+wordArray[i]);
		}
		pw1.close();
		System.out.println("LINES="+printLnCount);
	}
}

class mergeClass {
	//MERGESORT
	private int[] numbers;
	private String[] words;
	private int[] helper;
	private String[] helper2;
	private int num;
	private int wrd;
	public void mSort(int[] unsortPrimeArray, String[] unsortWordArray) {
	    this.numbers = unsortPrimeArray;
	    this.words = unsortWordArray;
	    num = unsortPrimeArray.length;
	    wrd = unsortWordArray.length;
	    this.helper = new int[num];
	    this.helper2 = new String[num];
	    mergeSort(0, num - 1);
	}
	private void mergeSort(int low, int high) {
		//CHECK LOW < HIGH ... IF NOT SORT
		if (low < high) {
			//GET MIDDLE INDEX
			int middle = low + (high - low) / 2;
			//SORT LEFT SIDE OF ARRAY
			mergeSort(low, middle);
			//SORT RIGHT SIDE OF ARRAY
			mergeSort(middle + 1, high);
			//COMBINE SIDES
			merge(low, middle, high);
		}
	}
	//MERGE SIDES
	private void merge(int low, int middle, int high) {
		//COPY BOTH PARTS TO HELPER ARRAY
		for (int i = low; i <= high; i++){
			helper[i] = numbers[i];
			helper2[i] = words[i];
		}

		int i = low;
		int j = middle + 1;
		int k = low;
		// COPY SMALLEST VAL FROM RIGHT OR LEFT TO ORIGIONAL
		while (i <= middle && j <= high) {
			if (helper[i] <= helper[j]) {
				numbers[k] = helper[i];
				words[k] = helper2[i];
				i++;
			} else {
				numbers[k] = helper[j];
				words[k] = helper2[j];
				j++;
			}
			k++;
		}
		//COPY REST OF LEFT SIDE TO TARGET ARRAY
		while (i <= middle) {
			numbers[k] = helper[i];
			words[k] = helper2[i];
			k++;
			i++;
		}
	}
}
