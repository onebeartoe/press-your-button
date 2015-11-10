
package org.onebeartoe.games.press.your.button.preferences;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import javax.swing.JFrame;
import org.onebeartoe.games.press.your.button.plugins.PluginConfigEntry;
import org.onebeartoe.games.press.your.button.swing.GamePanel;

/**
 * @author rmarquez
 */
public interface PreferencesService
{
    String get(String key, String defaultValue);
    
    GamePanel loadPlugin(String jarPath, String className) throws Exception;
    
    List<GamePanel> restoreUserPluginPreferences(List<PluginConfigEntry> userPluginConfiguration) throws Exception;
    
    Dimension restoreWindowDimension() throws Exception;
    
    Point restoreWindowLocation() throws Exception;
    
//    void saveBuiltInPluginsPreferences(UserProvidedPanel localImagesPanel);
    
    void saveUserPluginPreferences(List<PluginConfigEntry> userPluginConfiguration);
    
    void saveWindowPreferences(JFrame window);
}