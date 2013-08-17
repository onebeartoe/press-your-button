
package org.onebeartoe.games.press.your.button.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

/**
 * @author rmarquez
 */
public class PlayerLabelPanel extends BoardPanel
{    
    private String label;
    
    public PlayerLabelPanel(String label)
    {
	super(Color.LIGHT_GRAY);
	
	this.label = label;
        
        font = new Font(fontFamily, Font.PLAIN, 99);
    }

    @Override
    public String getLabel() 
    {
	return label;
    }
    
    @Override
        protected Point getTextLocation(Point origin)
    {        
        int x = origin.x + 100;
	int y = origin.y + 190;
     
        Point l = new Point(x, y);
        
        return l;
    }
    
    
}
