
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
public class GameControllerPanel extends JPanel
{
    private final PressYourButtonGame game;
    
    private final PressYourButton app;

//    public GameControllerPanel(final PressYourButtonGame app, PreviewPanel gameBoardPanel, PreviewPanel scoreBoardPanel)
    public GameControllerPanel(final PressYourButton app, PreviewPanel gameBoardPanel, PreviewPanel scoreBoardPanel, PressYourButtonGame game)
    {
	this.app = app;
        
        this.game = game;
	
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
	app.winnerSound.stop();
		    
	app.remove(app.endOfTurnPanel);
	app.add(app.newGamePanel, BorderLayout.CENTER);
	app.newGame();
	game.gameState = GameStates.NEW_GAME_CONFIG;
    }
    
    private class ShowScoreListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e) 
	{
	    if(game.gameState == GameStates.END_OF_TURN || 
		    game.gameState == GameStates.SHOW_SCORE ||
			game.gameState == GameStates.END_OF_GAME)
	    {
		app.switchToScoreView(true);
		game.gameState = GameStates.SHOW_SCORE;
	    }
	    else
	    {
		String message = "The score cannot be shown during a players turn.";
		System.out.println(message + "  state: " + game.gameState);
		JOptionPane.showMessageDialog(app, message);
	    }
	}	
    }
    
    private class NewGameListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e) 
	{	    
            System.out.println("in new game listener, game state is " + game.gameState);

	    if(game.gameState == GameStates.PLAYERS_TURN ||
		    game.gameState == GameStates.END_OF_TURN ||
			game.gameState == GameStates.SHOW_SCORE)
	    {
		String message = "Are you sure you want to end the current game?";
		int result = JOptionPane.showConfirmDialog(GameControllerPanel.this, message);
		if(result == JOptionPane.OK_OPTION)
		{
		    newGame();		    
		}
	    }
	    else if(game.gameState == GameStates.END_OF_GAME)
	    {
		newGame();
		
	    }
        }
    }
    
    private class NextPlayerListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e) 
	{
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!            
// use NextPlayerResponses                    
            System.out.println("in next listener, game state is " + game.gameState);	    
	    if(game.gameState == GameStates.END_OF_GAME)
	    {
		String message = "This game is over, please click the 'New Game' button.";
		JOptionPane.showMessageDialog(GameControllerPanel.this, message);
	    }
	    else if(game.gameState == GameStates.PLAYERS_TURN)
	    {
		String message = "The current player cannot be skipped.  Try the 'New Game' button, if you are done with is game.";
		JOptionPane.showMessageDialog(GameControllerPanel.this, message);
	    }
	    else if(game.gameState == GameStates.END_OF_TURN ||
                        (game.gameState == GameStates.SHOW_SCORE && !app.game.targetReached() )
		    )
	    {
		app.game.currentPlayer++;
		    
		if(app.game.currentPlayer == app.game.players.size() )
		{
		    app.game.currentPlayer = 0;
		}

		game.gameState = GameStates.PLAYERS_TURN;

		app.boardSound.loop();
	    }	    
	}	
    }
    
    private class StopButtonListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e) 
	{
	    if(game.gameState == GameStates.PLAYERS_TURN)
	    {
                System.out.println("ending the curent players turn");
		app.endCurrentPlayersTurn();
	    }
            else
            {
                System.out.println("not gonna stp player turn because we are in state: " + game.gameState);
            }
	}
    }
    
}
