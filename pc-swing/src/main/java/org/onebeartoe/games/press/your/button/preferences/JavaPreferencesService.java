
package org.onebeartoe.games.press.your.button.preferences;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import org.onebeartoe.games.press.your.button.App;
import org.onebeartoe.games.press.your.button.plugins.PluginConfigEntry;
import org.onebeartoe.games.press.your.button.swing.GamePanel;

/**
 * @author rmarquez
 */
public class JavaPreferencesService implements PreferencesService
{
    
    private Preferences preferences;
    
    public JavaPreferencesService()
    {
	preferences = Preferences.userNodeForPackage(App.class);
    }

    public String get(String key, String defaultValue) 
    {	
	String value = preferences.get(key, defaultValue);
	
	return value;
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
        
        int count = preferences.getInt(GamePreferencesKeys.userPluginCount, 0);
        
        for(int i=0; i<count; i++)
        {
            String key = GamePreferencesKeys.userPlugin + i;
            String s = preferences.get(key, null);
            if(s == null)
            {
                System.out.println("No class names for " + key);
            }
            else
            {
                String [] strs = s.split(PluginConfigEntry.JAR_CLASS_SEPARATOR);
                if(strs.length > 1)
                {
                    String jarPath = strs[0];
                    String classes = strs[1];
                    String [] classNames = classes.split(PluginConfigEntry.JAR_CLASS_SEPARATOR);
                    for(String name : classNames)
                    {
                        PluginConfigEntry entry = new PluginConfigEntry();
                        entry.jarPath = jarPath;
                        entry.qualifiedClassName = name;
                        userPluginConfiguration.add(entry);

                        GamePanel plugin = loadPlugin(jarPath, name);
                        plugins.add(plugin);
                    }
                }
            }
        }
        
	return plugins;
    }
	
    public Dimension restoreWindowDimension() 
    {	
	int defaultValue = 450;
	String key = GamePreferencesKeys.windowWidth;
	int width = preferences.getInt(key, defaultValue);
	
	key = GamePreferencesKeys.windowHeight;
	int height = preferences.getInt(key, App.DEFAULT_HEIGHT);
	
	Dimension demension = new Dimension(width, height);
	
	return demension;
    }
    
    @Override
    public Point restoreWindowLocation() throws Exception
    {
	int errorValue = -1;
	String key = GamePreferencesKeys.windowX;
	int x = preferences.getInt(key, errorValue);

	key = GamePreferencesKeys.windowY;
	int y = preferences.getInt(key, errorValue);
	
	if(x == errorValue || y == errorValue)
	{
	    // The window location hasn't been saved, yet.
	    
	    throw new Exception();
	}
	
	Point point = new Point(x,y);
	
	return point;
    }
    
   

    public void saveUserPluginPreferences(List<PluginConfigEntry> userPluginConfigurations)
    {
        Map<String, String> jarsToClasses = new HashMap();

	for(PluginConfigEntry entry : userPluginConfigurations)
        {
            String classes = jarsToClasses.get(entry.jarPath);
            
            if(classes == null)
            {
                classes = entry.qualifiedClassName;
            }
            else
            {
                classes += PluginConfigEntry.CLASS_SEPARATOR + entry.qualifiedClassName;
            }
            
            jarsToClasses.put(entry.jarPath, classes);
        }                

        int i = 0;
        Set<String> keys = jarsToClasses.keySet();
        for(String jarKey : keys)            
        {
            String classes = jarsToClasses.get(jarKey);
            String entry = jarKey + PluginConfigEntry.JAR_CLASS_SEPARATOR + classes;
            String key = GamePreferencesKeys.userPlugin + i;
            preferences.put(key, entry);
            
            i++;
        }
        
        int count = jarsToClasses.size();        
        preferences.putInt(GamePreferencesKeys.userPluginCount, count);
    }
    
    @Override
    public void saveWindowPreferences(JFrame window)
    {
	int x = window.getX();
	String key = GamePreferencesKeys.windowX;
	preferences.putInt(key, x);
	
	int y = window.getY();
	key = GamePreferencesKeys.windowY;
	preferences.putInt(key, y);
	
	int width = window.getWidth();	
	key = GamePreferencesKeys.windowWidth;
	preferences.putInt(key, width);
	
	int height = window.getHeight();
	key = GamePreferencesKeys.windowHeight;
	preferences.putInt(key, height);
    }


    
}
