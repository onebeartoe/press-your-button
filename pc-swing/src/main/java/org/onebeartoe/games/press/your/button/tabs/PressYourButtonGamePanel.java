
package org.onebeartoe.games.press.your.button.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.onebeartoe.games.press.your.button.EndCurrentPlayersTurnResponses;
import org.onebeartoe.games.press.your.button.GameControllerPanel;
import org.onebeartoe.games.press.your.button.GameCreationPanel;
import org.onebeartoe.games.press.your.button.GameStates;
import org.onebeartoe.games.press.your.button.Player;
import org.onebeartoe.games.press.your.button.PressYourButtonConstants;
import org.onebeartoe.games.press.your.button.PressYourButtonGame;
import org.onebeartoe.games.press.your.button.board.BoardPanel;
import org.onebeartoe.games.press.your.button.board.PlayerLabelPanel;
import org.onebeartoe.games.press.your.button.board.PreviewPanel;
import org.onebeartoe.games.press.your.button.plugins.swing.PressYourButtonWorker;
import org.onebeartoe.games.press.your.button.plugins.swing.SingleThreadedGamePanel;

/**
 * This is a plugin for the Press Your Luck PC app. It show a board with moving
 * point and whammy panels. A button will stop the board. If the user lands on a
 * point panel the points are added the users score. Landing on a whammy will
 * zero out their score.
 */
public class PressYourButtonGamePanel extends SingleThreadedGamePanel 
{
    private final ActionListener worker;
    
    private Thread bigButtonThread;
    
    protected List<Point> boardPanelLocations;
    
    private final PreviewPanel gameBoardPanel;
    
    private final PreviewPanel scoreBoardPanel;
    
    public GameControllerPanel endOfTurnPanel;
    
    public GameCreationPanel newGamePanel;
    
    volatile public PressYourButtonGame game;
        
    public static final int BOARD_WIDTH = 512;
    
    public static final int scaleFactor = 1;
    
    public static final int columnCount = 5;
    
    public final int gamePanelWidth = BOARD_WIDTH / columnCount;
    
    private Logger logger;

    public PressYourButtonGamePanel() 
    {
        logger = Logger.getLogger(getClass().getName());
        
        game = new PressYourButtonGame(){};
        game.gameState = GameStates.PLAYERS_TURN;

        worker = new PressYourButtonWorker(this, game);
        
        setupBoardPanelLocations();

        setLayout(new BorderLayout());

        newGamePanel = new GameCreationPanel(this, game);

        Dimension scoreBoardDimension = new Dimension(128, 128);
        scoreBoardPanel = new PreviewPanel(this, scoreBoardDimension);
        scoreBoardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension boardDimension = new Dimension(BOARD_WIDTH, BOARD_WIDTH);
        gameBoardPanel = new PreviewPanel(this, boardDimension);
        gameBoardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        endOfTurnPanel = new GameControllerPanel(this, gameBoardPanel, scoreBoardPanel, game);

        add(newGamePanel, BorderLayout.CENTER);
    }

    /**
     * This method shuffles the panels and updates the on-screen panels.
     * Calling this method repeatedly animates the point panels and selected panel.
     */
    public void drawBoardForPlayersTurn() 
    {
//        Duration.of
        timer.setDelay(PressYourButtonConstants.BOARD_REFRESH_DELAY);  // milliseconds 

        BufferedImage img = new BufferedImage(BOARD_WIDTH, BOARD_WIDTH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = img.createGraphics();

        g2d.setPaint(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, BOARD_WIDTH, BOARD_WIDTH);

        Color textColor = Color.GREEN;
        g2d.setPaint(textColor);

        String fontFamily = "Arial";
        Font font = new Font(fontFamily, Font.PLAIN, 32);

        g2d.setFont(font);
        
        game.shuffleBoardPanels();
        
        int currentPanelIndex = game.getCurentPointPanelIndex();

        int i = 0;
        
        for (Point location : boardPanelLocations) 
        {
            BoardPanel panel = game.getBoardPanels().get(i);

            Color foreground;
            
            if (i == currentPanelIndex) 
            {
                foreground = Color.RED;
            } 
            else 
            {
                foreground = Color.WHITE;
            }

            panel.draw(g2d, location, foreground, gamePanelWidth);

            i++;
        }

        Point labelLocation = new Point(gamePanelWidth, gamePanelWidth);
        String label = "P" + (game.currentPlayer + 1);
        BoardPanel playerLabel = new PlayerLabelPanel(label);
        
        // temporarily don't show the user       
        playerLabel.draw(g2d, labelLocation, Color.RED, gamePanelWidth);

        g2d.dispose();

        gameBoardPanel.setImage(img);

        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                gameBoardPanel.invalidate();
                gameBoardPanel.updateUI();
            }
        });
    }    

    private BufferedImage drawScoreBoard() 
    {
        int width = 128;
        int height = 128;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = img.createGraphics();
        
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        Color textColor = Color.WHITE;
        g2d.setPaint(textColor);

        String fontFamily = "Arial";
        Font font = new Font(fontFamily, Font.PLAIN, 34);

        g2d.setFont(font);

        System.out.println(game.toString());

        int verticalGap = 40;
        int i = 0;
        for (Player p : game.players) {
            String s = "P" + (i + 1) + " " + p.score;
            int x = 5;
            int y = 30 + i * verticalGap;
            System.out.println("drawing " + s + " at " + x + ", " + y + " at " + new Date());
            g2d.drawString(s, x, y);
            i++;
        }

        g2d.dispose();

        return img;
    }

    public void drawScoreBoardOnPanel() 
    {
        BufferedImage before = drawScoreBoard();

        scoreBoardPanel.setImage(before);

        updateScoreBoardPane();

        timer.setDelay(5000);  // milliseconds 	
    }

    public void endCurrentPlayersTurn()
    {
        EndCurrentPlayersTurnResponses response = game.endCurrentPlayersTurn();
        
        switch(response)
        {
            case END_OF_GAME:
            {
                String message = "Player " + (game.currentPlayer + 1) + " is the winner of this game!";
                JOptionPane.showMessageDialog(this, message);
                
                break;
            }
            default:
            {
                String message = "unknown enum: " + response;
                logger.log(Level.SEVERE, message);
            }
        }

        BufferedImage scoreBoard = drawScoreBoard();
        scoreBoardPanel.setImage(scoreBoard);
        updateScoreBoardPane();

        String message = "current player: " + game.currentPlayer + " - score: " + game.players.get(game.currentPlayer).score;
        System.out.println(message);
    }
    
    /**
     * draw method
     */
    public void endOfGame() 
    {
        System.out.println("End of run reached");
    }

    /**
     * draw method
     */
    public void endOfTurn() {
    }

    @Override
    public ActionListener getActionListener() 
    {
        return worker;
    }

    @Override
    public ImageIcon getTabIcon() {
        String path = "tab-icon.png";
        URL url = getClass().getResource(path);
        ImageIcon imagesTabIcon = new ImageIcon(url);

        return imagesTabIcon;
    }

    @Override
    public String getTabTitle() 
    {
        return "Press Your Button";
    }

    public void newGame() 
    {
        invalidate();
        updateUI();
        
        newGamePanel.createNewGame();
        
        endCurrentPlayersTurn();
    }
    
    public void startGame() 
    {
        invalidate();
        updateUI();

        endCurrentPlayersTurn();
        
        newGamePanel.createNewGame();
    }    

    /**
     * draw method for a new game
     */
    public void newGameConfiguration() 
    {
        timer.setDelay(1900);  // milliseconds 
    }

    /**
     * currently 5 by 5, with a 3x3 center setup is used.
     */
    private void setupBoardPanelLocations() 
    {
        boardPanelLocations = new ArrayList();

        // top row
        Point p1 = new Point(0, 0);
        Point p2 = new Point(gamePanelWidth, 0);
        Point p3 = new Point(gamePanelWidth * 2, 0);
        Point p4 = new Point(gamePanelWidth * 3, 0);
        Point p5 = new Point(gamePanelWidth * 4, 0);

        // second row
        Point p6 = new Point(0, gamePanelWidth);
        Point p7 = new Point(gamePanelWidth * 4, gamePanelWidth);

        // third row
        Point p8 = new Point(0, gamePanelWidth * 2);
        Point p9 = new Point(gamePanelWidth * 4, gamePanelWidth * 2);
        
        // fourth row
        Point p10 = new Point(0, gamePanelWidth * 3);
        Point p11 = new Point(gamePanelWidth * 4, gamePanelWidth * 3);
        
        // bottom row
        Point p12 = new Point(0, gamePanelWidth * 4);
        Point p13 = new Point(gamePanelWidth, gamePanelWidth * 4);
        Point p14 = new Point(gamePanelWidth * 2, gamePanelWidth * 4);
        Point p15 = new Point(gamePanelWidth * 3, gamePanelWidth * 4);
        Point p16 = new Point(gamePanelWidth *4, gamePanelWidth * 4);

        // first row
        boardPanelLocations.add(p1);
        boardPanelLocations.add(p2);
        boardPanelLocations.add(p3);
        boardPanelLocations.add(p4);
        boardPanelLocations.add(p5);
        boardPanelLocations.add(p6);
        boardPanelLocations.add(p7);
        
        // second row
        boardPanelLocations.add(p8);        
        boardPanelLocations.add(p9);
        
        // third row
        boardPanelLocations.add(p10);
        boardPanelLocations.add(p11);
        
        // bottom row
        boardPanelLocations.add(p12);
        boardPanelLocations.add(p13);
        boardPanelLocations.add(p14);
        boardPanelLocations.add(p15);
        boardPanelLocations.add(p16);
    }

    @Override
    public void startTabActivity() 
    {
        super.startTabActivity();
    }

    @Override
    public void stopTabActivity() 
    {
        super.stopTabActivity();

        if (bigButtonThread != null) {
            bigButtonThread.stop();
        }
    }

    public void switchToScoreView(boolean haveWinner) 
    {
        System.out.println("switching to score view");
    }

    public void updateScoreBoardPane() 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                System.out.println("updating the score board pane");
                scoreBoardPanel.invalidate();
                scoreBoardPanel.updateUI();
            }
        });
    }
}