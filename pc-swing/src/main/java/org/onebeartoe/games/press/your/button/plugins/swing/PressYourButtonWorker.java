
package org.onebeartoe.games.press.your.button.plugins.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static org.onebeartoe.games.press.your.button.GameStates.END_OF_GAME;
import static org.onebeartoe.games.press.your.button.GameStates.END_OF_TURN;
import static org.onebeartoe.games.press.your.button.GameStates.PLAYERS_TURN;
import static org.onebeartoe.games.press.your.button.GameStates.SHOW_SCORE;
import org.onebeartoe.games.press.your.button.PressYourButtonGame;
import org.onebeartoe.games.press.your.button.tabs.PressYourButtonGamePanel;

/**
 * @author rmarquez
 */
public class PressYourButtonWorker implements ActionListener 
{
    private PressYourButtonGamePanel app;
    
    private volatile PressYourButtonGame game;

    public PressYourButtonWorker(final PressYourButtonGamePanel plugin, PressYourButtonGame game) 
    {
	this.app = plugin;
        
        this.game = game;
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
        System.out.println("in PressYourButtonWorker#ActionPerformed: " + game.gameState);
        
	switch (game.gameState) 
	{
	    case PLAYERS_TURN:
	    {
		app.drawBoardForPlayersTurn();
		break;
	    }
	    case END_OF_GAME:
	    {
		app.endOfGame();
		break;
	    }
	    case END_OF_TURN:
	    {
		app.endOfTurn();
		break;
	    }
	    case SHOW_SCORE:
	    {
		app.drawScoreBoardOnPanel();
		break;
	    }
	    default:
	    {
		app.newGameConfiguration();
	    }
	}
    }   
}