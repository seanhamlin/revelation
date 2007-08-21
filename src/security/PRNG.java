package security;

/**
 * @author Sean Hamlin
 * @since 2.0
 *
 */
public interface PRNG {
    /**
     * @param password a string to set up the seed for the PRNG
     */
	public void setPassword(String password);
	
    /**
     * @param length the total size of the array
     */
	public void setLength(int length);
	
    /**
     * Initialises the PRNG
     */
	public void init();
	
    /**
     * Gets the next PRN from the generator
     */
	public int getNextPsuedo();
}
