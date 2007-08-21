/*
 * Created on 2/07/2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package security;

/**
 * 
 * @author Sean Hamlin
 * @since version 2.0
 * 
 * Dedicated class that created a psuedo random number generator, based on a given seed
 * 
 */
public class pseudoRandom implements PRNG{
       private static long REVELATION_SEED;
       private static int REVELATION_BITMAP_LENGTH;
       
       private MersenneTwisterFast revelationWheel;
       private int[] startArray;
       private int pointer;
       
       /**
        * Testing method, 
        * @param args
        */
       public static void main(String args[]) {
    	   // TODO remove this later on
           test();
       }
      
       /**
        * @param password a string to set up the seed for the PRNG
        */
       public void setPassword(String password) {
    	   // TODO improve security here, maybe add salt, maybe use SHA-1 etc
    	   // String.hashCode() creates an int, only has an output of 2^32 numbers
    	   REVELATION_SEED = (long) (password.hashCode()); 
       }
       
       /**
        * @param length the total size of the array
        */
       public void setLength(int length) {
    	   REVELATION_BITMAP_LENGTH = length;
       }
       
       /**
        * Initialises the array
        */
       public void init() {
    	   //create the fast PRNG with initial seed
    	   revelationWheel = new MersenneTwisterFast( REVELATION_SEED );
    	   
    	   //create the array of ints, from 0 - length
           startArray = new int[REVELATION_BITMAP_LENGTH];
           //initialise with default values
           for (int i = 0 ; i < REVELATION_BITMAP_LENGTH ; i++ ) {
        	   startArray[i] = i;
           }
           //generate random number
           int m = revelationWheel.nextInt( REVELATION_BITMAP_LENGTH );
           
           //swap indexes
           for (int i = 0 ; i < REVELATION_BITMAP_LENGTH ; i++ ) {
        	   int temp = startArray[i];
        	   startArray[i] = startArray[m];
        	   startArray[m] = temp;
        	   m = revelationWheel.nextInt( REVELATION_BITMAP_LENGTH );
           }
           
           //reset pointer to start of array ready for getNextPsuedo()
           pointer = 0;
       }

       /**
        * External (public method) that gets the next PRN from the generator
        * , also increments the pointer after finished
        */
       public int getNextPsuedo () {
    	   //System.out.println(pointer + ": " + startArray[pointer]);
    	   return startArray[pointer++];
       }


       /**
        * tests the pseudo random number generator for uniqueness
        */
       private static void test () {
    	   //default size of array
           final int TOTAL_POSSIBLE_NUMBERS = 1000;
           final int TOTAL_NUMBERS_USED = 50;
           
           
           final long PRNG_SEED = (long) (new String("password")).hashCode(); 
           
           System.out.println("Pseudo Random Number generator");
           System.out.println("------------------------------");

           MersenneTwisterFast wheel = new MersenneTwisterFast( PRNG_SEED ); //takes a LONG as a seed

           System.out.println("\n" + "Starting seed = " + PRNG_SEED );
           System.out.println("Total Possible numbers = " + TOTAL_POSSIBLE_NUMBERS);
           System.out.println("Total length of test = " + TOTAL_NUMBERS_USED + "\n\n");
           long total = 0;
           
           int startArray[] = new int[TOTAL_POSSIBLE_NUMBERS];
           //init
           for (int i = 0 ; i < TOTAL_POSSIBLE_NUMBERS ; i++ ) {
        	   startArray[i] = i;
           }
           
           int m = wheel.nextInt( TOTAL_POSSIBLE_NUMBERS );
           
           //swap indexes
           for (int i = 0 ; i < TOTAL_NUMBERS_USED ; i++ ) {
        	   int temp = startArray[i];
        	   startArray[i] = startArray[m];
        	   startArray[m] = temp;
        	   m = wheel.nextInt( TOTAL_POSSIBLE_NUMBERS );
           }
           
           //print it out
           for (int i = 0 ; i < TOTAL_NUMBERS_USED ; i++ ) {
        	   //System.out.print(startArray[i] + ", ");
        	   total += startArray[i];
           }
           //total
           System.out.println("\ntotal = " + total);
           //average
           double average = (double)(total/TOTAL_NUMBERS_USED);
           System.out.println("average = " + average);
           
           //standard deviation
           double var = Double.NaN;
           boolean isBiasCorrected = true;
		   double accum = 0.0;
           double accum2 = 0.0;
           for (int i = 0; i < TOTAL_NUMBERS_USED; i++) {
               accum += Math.pow((startArray[i] - average), 2.0);
               accum2 += (startArray[i] - average);
           }
           if (isBiasCorrected) {
               var = (accum - (Math.pow(accum2, 2) / ((double) TOTAL_NUMBERS_USED))) /
               (double) (TOTAL_NUMBERS_USED - 1);
           } else {
               var = (accum - (Math.pow(accum2, 2) / ((double) TOTAL_NUMBERS_USED))) /
               (double) TOTAL_NUMBERS_USED;
           }
           var = Math.sqrt(var);
           System.out.println("standard deviation = " + var);
       }


}
