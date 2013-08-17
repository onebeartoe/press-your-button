
package org.onebeartoe.press.your.button;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
	File pwd = new File(".");		
	try 
	{
	    String resourcesPath = "src/main/resources";
	    
	    System.out.println("current working directory: " + pwd.getCanonicalPath() );    	    
	    
	    
	} 
	catch (IOException ex) 
	{
	    Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
	}
		
        assertTrue( true );
    }
}
