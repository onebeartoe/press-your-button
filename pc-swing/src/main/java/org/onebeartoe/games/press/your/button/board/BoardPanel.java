
package org.onebeartoe.games.press.your.button.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import org.onebeartoe.games.press.your.button.tabs.PressYourButton;

public abstract class BoardPanel
{
    protected Color backgroundColor;

    protected final String fontFamily = "Arial";

    protected Point location;
    
    public int amount;
    
    protected Font font;

    public BoardPanel(Color backgroundColor)
    {
	this.backgroundColor = backgroundColor;
	            
	font = new Font(fontFamily, Font.PLAIN, 32);
    }

    public void draw(Graphics2D g2d, Point location, Color foreground)
    {
	g2d.setColor(backgroundColor);
	g2d.fillRect(location.x, location.y, PressYourButton.gamePanelWidth, PressYourButton.gamePanelWidth);	
	g2d.setColor(foreground);

	String text = getLabel();
	
        Point textLocation = getTextLocation(location);
        
	float x = textLocation.x;
	float y = textLocation.y;
	
	g2d.setFont(font);	    
	g2d.drawString(text, x, y);
    }
    
    protected Point getTextLocation(Point origin)
    {        
        int x = origin.x + 25;
	int y = origin.y + 55;
     
        Point l = new Point(x, y);
        
        return l;
    }
    
    public abstract String getLabel();
}
