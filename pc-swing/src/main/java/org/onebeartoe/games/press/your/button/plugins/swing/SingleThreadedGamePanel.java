
package org.onebeartoe.games.press.your.button.plugins.swing;

import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.onebeartoe.games.press.your.button.swing.GamePanel;

/**
 * @author rmarquez
 */
public abstract class SingleThreadedGamePanel extends GamePanel
{
    volatile protected Timer timer;   
	
    @Override
    public void startTabActivity()
    {
	System.out.println("Starting activity in " + getClass().getSimpleName() + ".");		
	ActionListener listener = getActionListener();
	
	// set the IOIO loop delay to half a second, by default
	int delay = 500; // milliseconds
	timer = new Timer(delay, listener);
	
	timer.start();
    }
   
    @Override
    public void stopTabActivity()
    {	
	String className = getClass().getSimpleName();
	System.out.println("Preparing to stoping activity in " + className + ".");
        if(timer != null)// && timer.isRunning() )
        {            
	    System.out.println("Stoping activity in " + className + ".");
            timer.stop();
        }
    }
    
//TODO: this needs a better name
    protected abstract ActionListener getActionListener();
}
