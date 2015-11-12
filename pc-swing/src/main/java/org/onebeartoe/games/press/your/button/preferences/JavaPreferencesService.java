
package org.onebeartoe.games.press.your.button.preferences;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.onebeartoe.games.press.your.button.plugins.PluginConfigEntry;
import org.onebeartoe.games.press.your.button.plugins.swing.GamePanel;

/**
 * @deprecated Use the version at https://github.com/onebeartoe/java-libraries/tree/master/onebeartoe-application/src/main/java/org/onebeartoe/application
 * @author Roberto Marquez
 */
public class JavaPreferencesService implements PreferencesService
{
    
//    private Preferences preferences;
    
    public JavaPreferencesService()
    {
//	preferences = Preferences.userNodeForPackage(App.class);
    }

    public String get(String key, String defaultValue) 
    {	
//	String value = preferences.get(key, defaultValue);
	
	return "";
    }
    
    @Override
    public GamePanel loadPlugin(String jarPath, String className) throws Exception
    {	
        File jar = new File(jarPath);
	if( !jar.exists() || !jar.canRead() )
	{
	    System.out.println("\n\nThere is a problem with the specified JAR.");
	    System.out.println("The jar exists: " + jar.exists() );
	    System.out.println("The jar is readable: " + jar.canRead() );
	}
	
	
	URL url = jar.toURI().toURL();
	URL [] urls = new URL[1];
	urls[0] = url;
	URLClassLoader classLoader = new URLClassLoader(urls);

	Class<?> clazz = classLoader.loadClass(className);

	Constructor<?> constructor = clazz.getConstructor();
	Object o = constructor.newInstance();
	GamePanel plugin = (GamePanel) o;
	    
	return plugin;
    }
    
    @Override
    public List<GamePanel> restoreUserPluginPreferences(List<PluginConfigEntry> userPluginConfiguration) throws Exception 
    {
        List<GamePanel> plugins = new ArrayList();
        

        
	return plugins;
    }
	
    public Dimension restoreWindowDimension() 
    {	
	
	
	return new Dimension(750, 850);
    }
    
    @Override
    public Point restoreWindowLocation() throws Exception
    {
	
	Point point = new Point(1, 2);
	
	return point;
    }
    
   

    public void saveUserPluginPreferences(List<PluginConfigEntry> userPluginConfigurations)
    {
        
    }
    
    @Override
    public void saveWindowPreferences(JFrame window)
    {
	
    }


    
}
