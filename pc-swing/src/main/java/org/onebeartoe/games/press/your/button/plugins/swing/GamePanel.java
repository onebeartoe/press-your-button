
package org.onebeartoe.games.press.your.button.swing;

import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.onebeartoe.games.press.your.button.plugins.GamePlugin;

/**
 * @author Administrator
 */
public abstract class GamePanel extends JPanel implements GamePlugin 
{

    
 
    
    public ImageIcon getTabIcon()
    {
	System.out.println("\n\n\nusing the default tab\n");
	
	String path = "/tab_icons/apple_small.png";
	URL url = getClass().getResource(path);
        ImageIcon imagesTabIcon = new ImageIcon(url);
	
	return imagesTabIcon;
    }
    
    public String getTabTitle()
    {
	return "Default Plugin Title";
    }
    
}
