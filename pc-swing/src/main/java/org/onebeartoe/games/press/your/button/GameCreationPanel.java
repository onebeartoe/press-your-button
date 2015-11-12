
package org.onebeartoe.games.press.your.button;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.onebeartoe.games.press.your.button.tabs.PressYourButton;

/**
 * @author rmarquez
 */
public class GameCreationPanel extends JPanel implements ActionListener
{
    private JComboBox<Integer> playerCountDropdown;
    
    private JComboBox<Integer> targetScoreDropdown;
    
    private JButton startButton;
    
    private PressYourButton app;
    
    private PressYourButtonGame game;
    
    public GameCreationPanel(PressYourButton parent, PressYourButtonGame game)
    {
	this.app = parent;
        
        this.game = game;
	
	Integer [] values = {1,2,3};
	playerCountDropdown = new JComboBox(values);
	String title = "Number of Players";
	TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
	playerCountDropdown.setBorder(titledBorder);
	
	Integer [] scores = {100, 200, 300, 400};
	title = "Target Score";
	titledBorder = BorderFactory.createTitledBorder(title);
	targetScoreDropdown = new JComboBox(scores);
	targetScoreDropdown.setBorder(titledBorder);
	
	startButton = new JButton("Start");
	startButton.addActionListener(this);
	
	LayoutManager layout = new GridLayout(3,1, 10,10);
	setLayout(layout);
	
	add(playerCountDropdown);
	add(targetScoreDropdown);
	add(startButton);
    }

    public void actionPerformed(ActionEvent e) 
    {
	app.invalidate();
	app.updateUI();
	    
	createNewGame();
	
	app.game = game;
	
	app.remove(app.newGamePanel);
	app.add(app.endOfTurnPanel, BorderLayout.CENTER);
	
        app.startGame();
//	app.newGame();
	
// duechlandia        
// put this back if it does not work        
//	game.gameState = GameStates.PLAYERS_TURN;	
//	app.boardSound.loop();
//        
    }
    
    public void createNewGame()
    {
	Integer count = (Integer) playerCountDropdown.getSelectedItem();
	Integer targetScore = (Integer) targetScoreDropdown.getSelectedItem();
	
	List<Player> players = new ArrayList();
	
	for(int p=0; p<count; p++)
	{
	    Player player = new Player();
	    players.add(player);
	}

        game.createNewGame(players, targetScore);
    }   
}