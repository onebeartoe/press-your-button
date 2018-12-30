
package org.onebeartoe.games.press.your.button;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.onebeartoe.games.press.your.button.board.BoardPanel;
import org.onebeartoe.games.press.your.button.board.DefaultBoardPanels;

/**
 * @author Roberto Marquez
 */
public class PressYourButtonGame 
{
    public final static int BOARD_PANEL_COUNT = 16;
    
    public GameStates gameState = GameStates.NEW_GAME_CONFIG;

// TODO: MAKE THESE instance members NOT VISIBLE outside of the CLASS!            
    public List<Player> players;
    
    public int currentPlayer;
    
// TODO: MAKE THESE instance members NOT VISIBLE outside of the CLASS!                
    public int targetScore;
    
    public AudioClip boardSound;
    
    
    int curentPointPanelIndex;
            
    @Deprecated
    /**
     * use the index!
    */
    volatile BoardPanel curentPointPanel;
    
    public AudioClip whammySound;
    
    public AudioClip winnerSound;
    
    private List<BoardPanel> boardPanels;
    
    private Random locationRandom;
    
    public PressYourButtonGame()
    {
        locationRandom = new Random();

        loadSounds();
        
        setupBoardPanels();
    }
    
    public void createNewGame(List<Player> players, int targetScore)
    {
        winnerSound.stop();
        
        gameState = GameStates.NEW_GAME_CONFIG;
        
        if (curentPointPanel != null) 
        {
            curentPointPanel.amount = 0;
        }        
        
	this.players = players;
	this.targetScore = targetScore;
        
        gameState = GameStates.PLAYERS_TURN;	
	boardSound.loop();
    }
    
    public EndCurrentPlayersTurnResponses endCurrentPlayersTurn()
    {
        EndCurrentPlayersTurnResponses response = EndCurrentPlayersTurnResponses.UNKNOWN_RESPONSE;
        
        boardSound.stop();
        
        gameState = GameStates.END_OF_TURN;

        Player player = players.get(currentPlayer);

        if (curentPointPanel != null) 
        {
            if (curentPointPanel.amount < 0) 
            {
                player.score = player.score / 2;
                whammySound.play();
            } 
            else 
            {
                player.score += curentPointPanel.amount;
            }
        }

        if (player.score >= targetScore)
        {
            gameState = GameStates.END_OF_GAME;

            winnerSound.loop();

            response = EndCurrentPlayersTurnResponses.END_OF_GAME;

        } 
        else 
        {
            gameState = GameStates.END_OF_TURN;
            
            response = EndCurrentPlayersTurnResponses.END_OF_TURN;
        }
        
        if (curentPointPanel != null) 
        {
            System.out.println("current panel score: " + curentPointPanel.amount);
        }        
        
        return response;
    }

    public int getCurentPointPanelIndex()
    {
        return curentPointPanelIndex;
    }
    
    public boolean inProgress()
    {
        boolean inProgress = gameState == GameStates.PLAYERS_TURN ||
                             gameState == GameStates.END_OF_TURN ||
                             gameState == GameStates.SHOW_SCORE;
        
        return inProgress;
    }
    
    private void loadSounds()
    { 
        String path = "/big_board.wav";
        
        URL url = getClass().getResource(path);
        boardSound = Applet.newAudioClip(url);
       
        // http://www.zedge.net/ringtones/0-1-1-press%20your%20luck/
        path = "/whammy.wav";
        url = getClass().getResource(path);
        whammySound = Applet.newAudioClip(url);

        path = "/winner.wav";
        url = getClass().getResource(path);
        winnerSound = Applet.newAudioClip(url);
    }

    private void setupBoardPanels() 
    {
        boardPanels = DefaultBoardPanels.get();
    }    
    
    public void shuffleBoardPanels()
    {
        Collections.shuffle(boardPanels);
        
        curentPointPanelIndex = locationRandom.nextInt(BOARD_PANEL_COUNT);
        
        BoardPanel panel = boardPanels.get(curentPointPanelIndex);
        
        curentPointPanel = panel;
    }
    
    public boolean targetReached()
    {
	Player player = players.get(currentPlayer);
	
	boolean reached = player.score > targetScore;
	
	return reached;
    }
    
    @Override
    public String toString()
    {
	StringBuilder sb = new StringBuilder();
	
	sb.append("target score: ").append(targetScore).append(" - ");
	sb.append("current player: ").append(currentPlayer).append(" - ");
	
	for(int i=0; i < players.size(); i++)    
	{
	    Player p = players.get(i);
	    sb.append("P").append(i+1).append(": ").append(p.score).append("\t");
	}
	
	return sb.toString();
    }    
    
    public NextPlayerResponses nextPlayer()
    {
        NextPlayerResponses response = NextPlayerResponses.UNKNOWN_RESPONSE;
        
        if(gameState == GameStates.END_OF_GAME)
        {
            response = NextPlayerResponses.GAME_IS_OVER_CLICK_NEW_GAME_BUTTON;
        }
        else if(gameState == GameStates.PLAYERS_TURN)
        {
            response = NextPlayerResponses.CURRENT_PLAYER_CANNOT_BE_SKIPPED_TRY_NEW_GAME;
        }
        else if(gameState == GameStates.END_OF_TURN ||
                    (gameState == GameStates.SHOW_SCORE && !targetReached() )
                )
        {
            currentPlayer++;

            if(currentPlayer == players.size() )
            {
                currentPlayer = 0;
            }

            gameState = GameStates.PLAYERS_TURN;

            response = NextPlayerResponses.NEXT_PLAYERS_TURN;
            
            boardSound.loop();
        }
        
        return response;
    }

    public List<BoardPanel> getBoardPanels()
    {
        // I dont' like this return.  we should make a deep copy of the list!
        return boardPanels;
    }
}