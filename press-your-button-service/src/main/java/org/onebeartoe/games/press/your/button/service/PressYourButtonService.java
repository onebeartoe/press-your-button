
package org.onebeartoe.games.press.your.button.service;

import java.util.List;
import org.onebeartoe.games.press.your.button.board.BoardPanel;
import org.onebeartoe.games.press.your.button.board.DefaultBoardPanels;

/**
 * @author Roberto Marquez
 */
public interface PressYourButtonService 
{
    /**
     * This method demonstrates how to use a default method implementatino on an
     * interface.  (https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html)
     * 
     * @return A list of the default panel board values.  Most are point panels and 
     * the list also contains 3 Whammys.
     */
    default List<BoardPanel> getGameBoardPanels()
    {
        List<BoardPanel> boardPanels = DefaultBoardPanels.get();
        
        return boardPanels;
    }
}
