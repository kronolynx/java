package com.stampery;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	Stampery stampery = new Stampery("2d4cdee7-38b0-4a66-da87-c1ab05b43768");
    
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
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testHash()
    {
//    	b07eb9b2fc90792
    	String actual = stampery.hash("Hello, blockchain!");
        String expected = "2C18DF1F2BEC6F8C6FBE2AB26408EE60237B8F5A2A86576979B8F29AEAF25EB621BFFB95479CE67300D2BC50C422D2275207B809ACF64F4C4EEA167885000F89";
    	assertEquals(expected, actual);
    }
}
