
package org.onebeartoe.games.press.your.button;

import java.util.List;

/**
 * @author rmarquez
 */
public class Game 
{
    public List<Player> players;
    
    public volatile int currentPlayer;
    
    public int targetScore;
    
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
	
	sb.append("target score: " + targetScore + " - ");
	sb.append("current player: " + currentPlayer + " - ");
	
	for(int i=0; i < players.size(); i++)    
	{
	    Player p = players.get(i);
	    sb.append("P" + (i+1) + ": " + p.score + "\t");
	}
	
	return sb.toString();
    }
}
