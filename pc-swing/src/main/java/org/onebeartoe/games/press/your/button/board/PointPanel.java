
package org.onebeartoe.games.press.your.button.board;

import java.awt.Color;

public class PointPanel extends BoardPanel
{
//    public int amount;

    public PointPanel(Color backgroundColor, int amount) 
    {
	super(backgroundColor);

	this.amount = amount;
    }

    @Override
    public String getLabel() 
    {
	return String.valueOf(amount);
    }

}