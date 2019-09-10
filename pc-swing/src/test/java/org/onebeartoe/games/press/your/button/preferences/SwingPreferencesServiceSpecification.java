/*
 */
package org.onebeartoe.games.press.your.button.preferences;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;



/**
 *
 * @author Roberto Marquez
 */
public class SwingPreferencesServiceSpecification
{
    SwingPreferencesService implementation;
    
    @BeforeMethod
    public void setUpMethod() throws Exception
    {
        implementation = new SwingPreferencesService();
    }
    
    /**
     * This method asserts that the loadPlugin specification throws an error 
     * when bad paths are supplied.
     */
    @Test(expectedExceptions = {ClassNotFoundException.class})
    public void loadPlugin_fails_invalidPath() throws Exception
    {
        String jarPath = "bogus-12455654.jar";
        
        String className = "SomeFakeClass";
        
        implementation.loadPlugin(jarPath, className);
    }
}
