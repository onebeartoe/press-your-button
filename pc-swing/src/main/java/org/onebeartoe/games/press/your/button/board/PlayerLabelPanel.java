
package org.onebeartoe.games.press.your.button.board;

import java.awt.Color;

/**
 * @author rmarquez
 */
public class PlayerLabelPanel extends BoardPanel
{    
    private String label;
    
    public PlayerLabelPanel(String label)
    {
	super(Color.BLACK);
	
	this.label = label;
    }

    @Override
    public String getLabel() 
    {
	return label;
    }
    
}
