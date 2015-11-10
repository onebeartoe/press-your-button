
package org.onebeartoe.games.press.your.button.service;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.onebeartoe.games.press.your.button.board.BoardPanel;
import org.onebeartoe.games.press.your.button.board.PointPanel;
import org.onebeartoe.games.press.your.button.board.WhammyPanel;

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
        BoardPanel p1 = new PointPanel(Color.YELLOW, 10);
        BoardPanel p2 = new PointPanel(Color.BLUE, 20);
        BoardPanel p3 = new PointPanel(Color.GREEN, 30);
        BoardPanel p4 = new PointPanel(Color.LIGHT_GRAY, 40);
        BoardPanel p5 = new PointPanel(Color.CYAN, 50);
        BoardPanel p6 = new PointPanel(Color.DARK_GRAY, 60);
        BoardPanel p7 = new PointPanel(Color.MAGENTA, 70);
        BoardPanel p8 = new PointPanel(Color.ORANGE, 80);
        BoardPanel p9 = new PointPanel(Color.PINK, 90);
        BoardPanel p10 = new PointPanel(Color.YELLOW, 10);
        BoardPanel p11 = new PointPanel(Color.orange, 20);
        BoardPanel p12 = new PointPanel(Color.BLUE, 30); 
        
        BoardPanel p13 = new PointPanel(Color.GREEN, 30);
        BoardPanel p14 = new PointPanel(Color.LIGHT_GRAY, 40);
        BoardPanel p15 = new PointPanel(Color.CYAN, 50);
        BoardPanel p16 = new PointPanel(Color.DARK_GRAY, 60);
        BoardPanel p17 = new PointPanel(Color.MAGENTA, 70);
        BoardPanel p18 = new PointPanel(Color.ORANGE, 80);
        BoardPanel p19 = new PointPanel(Color.PINK, 90);
        BoardPanel p20 = new PointPanel(Color.YELLOW, 10);
        BoardPanel p21 = new PointPanel(Color.orange, 20);
        BoardPanel p22 = new PointPanel(Color.BLUE, 30);

        BoardPanel w1 = new WhammyPanel();
        BoardPanel w2 = new WhammyPanel();
        BoardPanel w3 = new WhammyPanel();
        BoardPanel w4 = new WhammyPanel();

        List<BoardPanel> boardPanels = new ArrayList();
        boardPanels.add(p1);
        boardPanels.add(p2);
        boardPanels.add(p3);
        boardPanels.add(p4);
        boardPanels.add(p5);
        boardPanels.add(p6);
        boardPanels.add(p7);
        boardPanels.add(p8);
        boardPanels.add(p9);
        boardPanels.add(p10);
        boardPanels.add(p11);
        boardPanels.add(p12);
        boardPanels.add(p13);
        boardPanels.add(p14);
        boardPanels.add(p15);
        boardPanels.add(p16);
        boardPanels.add(p17);
        boardPanels.add(p18);
        boardPanels.add(p19);
        boardPanels.add(p20);
        boardPanels.add(p21);
        boardPanels.add(p22);

        boardPanels.add(w1);
        boardPanels.add(w2);
        boardPanels.add(w3);
        boardPanels.add(w4);
        
        return boardPanels;
    }
}
