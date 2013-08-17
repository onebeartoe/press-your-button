
package org.onebeartoe.games.press.your.button;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.onebeartoe.games.press.your.button.board.PreviewPanel;
import org.onebeartoe.games.press.your.button.tabs.PressYourButton;

/**
 * @author rmarquez
 */
public class GameControlPanel extends JPanel
{
    
    private final PressYourButton plugin;

    public GameControlPanel(final PressYourButton plugin, PreviewPanel gameBoardPanel, PreviewPanel scoreBoardPanel) 
    {
	this.plugin = plugin;
	
	JButton stopButton = new JButton("Stop");
	stopButton.addActionListener( new StopButtonListener() );	
	
	JPanel panels = new JPanel( new FlowLayout(FlowLayout.CENTER) );
	panels.add(scoreBoardPanel);
	panels.add(gameBoardPanel);
	panels.add(stopButton);
	
	Box box = new Box(BoxLayout.Y_AXIS);
        box.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        box.add(Box.createVerticalGlue());
        box.add(panels);        
	box.add(Box.createVerticalGlue());
	
	JButton newGameButton = new JButton("New Game");
	newGameButton.addActionListener( new NewGameListener() );
	
	JButton showScoreButton = new JButton("Show Score");
	showScoreButton.addActionListener( new ShowScoreListener() );
	
	JButton nextPlayerButton = new JButton("Next Player");
	nextPlayerButton.addActionListener( new NextPlayerListener() );
	
	JPanel buttonPanel = new JPanel( new GridLayout(1, 3, 10, 10) );
	buttonPanel.add(newGameButton);
	buttonPanel.add(showScoreButton);
	buttonPanel.add(nextPlayerButton);
	
	setLayout( new BorderLayout() );
	
	add(box, BorderLayout.CENTER);
	add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void newGame()
    {
	plugin.winnerSound.stop();
		    
	plugin.remove(plugin.endOfTurnPanel);
	plugin.add(plugin.newGamePanel, BorderLayout.CENTER);
	plugin.newGame();
	plugin.gameState = GameStates.NEW_GAME_CONFIG;
    }
    
    private class ShowScoreListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e) 
	{
	    if(plugin.gameState == GameStates.END_OF_TURN || 
		    plugin.gameState == GameStates.SHOW_SCORE ||
			plugin.gameState == GameStates.END_OF_GAME)
	    {
		plugin.switchToScoreView(true);
		plugin.gameState = GameStates.SHOW_SCORE;
	    }
	    else
	    {
		String message = "The score cannot be shown during a players turn.";
		System.out.println(message + "  state: " + plugin.gameState);
		JOptionPane.showMessageDialog(plugin, message);
	    }
	}	
    }
    
    private class NewGameListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e) 
	{	    
            System.out.println("in new game listener, game state is " + plugin.gameState);

	    if(plugin.gameState == GameStates.PLAYERS_TURN ||
		    plugin.gameState == GameStates.END_OF_TURN ||
			plugin.gameState == GameStates.SHOW_SCORE)
	    {
		String message = "Are you sure you want to end the current game?";
		int result = JOptionPane.showConfirmDialog(GameControlPanel.this, message);
		if(result == JOptionPane.OK_OPTION)
		{
		    newGame();		    
		}
	    }
	    else if(plugin.gameState == GameStates.END_OF_GAME)
	    {
		newGame();
		
	    }
        }
    }
    
    private class NextPlayerListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e) 
	{
            System.out.println("in next listener, game state is " + plugin.gameState);	    
	    if(plugin.gameState == GameStates.END_OF_GAME)
	    {
		String message = "This game is over, please click the 'New Game' button.";
		JOptionPane.showMessageDialog(GameControlPanel.this, message);
	    }
	    else if(plugin.gameState == GameStates.PLAYERS_TURN)
	    {
		String message = "The current player cannot be skipped.  Try the 'New Game' button, if you are done with is game.";
		JOptionPane.showMessageDialog(GameControlPanel.this, message);
	    }
	    else if(plugin.gameState == GameStates.END_OF_TURN ||
                        (plugin.gameState == GameStates.SHOW_SCORE && !plugin.currentGame.targetReached() )
		    )
	    {
		plugin.currentGame.currentPlayer++;
		    
		if(plugin.currentGame.currentPlayer == plugin.currentGame.players.size() )
		{
		    plugin.currentGame.currentPlayer = 0;
		}

		plugin.gameState = GameStates.PLAYERS_TURN;

		plugin.boardSound.loop();
	    }	    
	}	
    }
    
    private class StopButtonListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e) 
	{
	    if(plugin.gameState == GameStates.PLAYERS_TURN)
	    {
		plugin.endCurrentPlayersTurn();
	    }
	}
    }
    
}
