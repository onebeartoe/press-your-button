
package org.onebeartoe.games.press.your.button.plugins.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.onebeartoe.games.press.your.button.tabs.PressYourButton;

/**
 * @author rmarquez
 */
public class PressYourButtonWorker implements ActionListener 
{
    private final PressYourButton plugin;

    public PressYourButtonWorker(final PressYourButton plugin) 
    {
	this.plugin = plugin;
    }

    /**
     * Every time the application communicates, this method 
     * is called.
     * 
     * The frequency can be adjusted with calls to ...
     * 
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
	switch (plugin.gameState) 
	{
	    case PLAYERS_TURN:
	    {
		plugin.drawBoardForPlayersTurn();
		break;
	    }
	    case END_OF_GAME:
	    {
		plugin.endOfGame();
		break;
	    }
	    case END_OF_TURN:
	    {
		plugin.endOfTurn();
		break;
	    }
	    case SHOW_SCORE:
	    {
		plugin.drawScoreBoardOnPanel();
		break;
	    }
	    default:
	    {
		plugin.newGameConfiguration();
	    }
	}
    }
    
}
