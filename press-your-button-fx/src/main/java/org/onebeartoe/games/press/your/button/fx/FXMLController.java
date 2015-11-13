
package org.onebeartoe.games.press.your.button.fx;

//import java.awt.Color;
import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

import org.onebeartoe.games.press.your.button.EndCurrentPlayersTurnResponses;
import org.onebeartoe.games.press.your.button.GameStates;
import org.onebeartoe.games.press.your.button.Player;
import org.onebeartoe.games.press.your.button.PressYourButtonConstants;
import org.onebeartoe.games.press.your.button.PressYourButtonGame;
import org.onebeartoe.games.press.your.button.board.BoardPanel;
import org.onebeartoe.games.press.your.button.board.PlayerLabelPanel;

public class FXMLController implements Initializable 
{
    @FXML
    private Label playerLabel;
    
    @FXML
    private Label board00;
    @FXML
    private Label board01;
    @FXML
    private Label board02;
    @FXML
    private Label board03;
    @FXML
    private Label board04;
    
    private List<Label> boards;
    
    private TimerTask clickTask;
    
//TODO: don't forget to canel this when tapp exists    
    private Timer timer;
    
    volatile public PressYourButtonGame game;
    
    private ChoiceBox playerCountDropdown;
    
    private ChoiceBox targetScoreDropdown;
    
    private Logger logger;
    
    private void createNewGame()
    {
        

        PartialGame partialGame = partialGame(playerCountDropdown, targetScoreDropdown);
        Integer count = partialGame.numberOfPlayers;
	Integer targetScore = partialGame.targetScore;

	
	List<Player> players = new ArrayList();
	
	for(int p=0; p<count; p++)
	{
	    Player player = new Player();
	    players.add(player);
	}
        
        game.createNewGame(players, targetScore);
        
        endCurrentPlayersTurn();
        
        game.createNewGame(players, targetScore);
    }

    public void endCurrentPlayersTurn()
    {
        EndCurrentPlayersTurnResponses response = game.endCurrentPlayersTurn();
        
        // update score board
        updateScoreBoard();        
        
        switch(response)
        {
            case END_OF_GAME:
            {
                String message = "Player " + (game.currentPlayer + 1) + " is the winner of this game!";
                
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Winner");
                alert.setHeaderText("Information Alert");
                alert.setContentText(message);

                alert.show();
                
                break;
            }
            default:
            {
                String message = "unknown enum: " + response;
                logger.log(Level.SEVERE, message);
            }
        }

        String message = "current player: " + game.currentPlayer + " - score: " + game.players.get(game.currentPlayer).score;
        System.out.println(message);
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) 
    {
        System.out.println("You clicked me!");
        playerLabel.setText("Hello World!");
    }
    
    @FXML
    private void handleNewGameButton(ActionEvent event)
    {
        String titleText = "New Game";
        
        System.out.println(titleText + " clicked");
                
        // Show confirm alert dialog			
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titleText);
        String message = PressYourButtonConstants.CONFIRM_END_CURRENT_GAME;
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();

        if ((result.isPresent()) && (result.get() == ButtonType.OK)) 
        {
            System.out.println("okay I believe you now");

            showNewGameDialog();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        logger = Logger.getLogger(getClass().getName());
        
        game = new PressYourButtonGame(){};
        game.gameState = GameStates.PLAYERS_TURN;
        
        playerCountDropdown = new ChoiceBox(
                FXCollections.observableArrayList("1", "2", "3") );
        playerCountDropdown.getSelectionModel().selectFirst();
        
        targetScoreDropdown = new ChoiceBox(
                FXCollections.observableArrayList("100", "200", "300", "400") );
        targetScoreDropdown.getSelectionModel().selectFirst();
        
        timer = new Timer();
//        timer.setDelay(PressYourButtonConstants.BOARD_REFRESH_DELAY);
        
        boards = new ArrayList();
        
        boards.add(board00);
        boards.add(board01);
        boards.add(board02);
        boards.add(board03);
        boards.add(board04);
    }

    private PartialGame partialGame(ChoiceBox playerCountDropdown, ChoiceBox targetScoreDropdown)
    {
        String nop = playerCountDropdown.getSelectionModel().getSelectedItem().toString();
        String ts = targetScoreDropdown.getSelectionModel().getSelectedItem().toString();
        
        int numberOfPlayers = Integer.valueOf(nop);
                
        int targetScore = Integer.valueOf(ts);
                
        final PartialGame partialGame = new PartialGame(numberOfPlayers, targetScore);
        
        return partialGame;
    }
    
    private void showNewGameDialog()
    {
        // Custom dialog
		
        Dialog<PartialGame> dialog = new Dialog<>();
        dialog.setTitle("New Game");
        dialog.setHeaderText("This is a dialog. Enter info and \n" +
                "press Okay (or click title bar 'X' for cancel).");
        dialog.setResizable(true);

        // Widgets
        Label label1 = new Label("Name: ");
        Label label2 = new Label("Phone: ");
//        final TextField text1 = new TextField();
        
//        final TextField text2 = new TextField();

        // Create layout and add to dialog
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 35, 20, 35));
        grid.add(label1, 1, 1); // col=1, row=1
        
        grid.add(playerCountDropdown, 2, 1);
//        grid.add(text1, 2, 1);
        
        grid.add(label2, 1, 2); // col=1, row=2
        grid.add(targetScoreDropdown, 2, 2);
        dialog.getDialogPane().setContent(grid);

        // Add button to dialog
        final ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk );

        // Result converter for dialog
        dialog.setResultConverter(new Callback<ButtonType, PartialGame>() 
        {
            @Override
            public PartialGame call(ButtonType b) 
            {
                if (b == buttonTypeOk) 
                {
                    PartialGame game = partialGame(playerCountDropdown, targetScoreDropdown);                    

                    return game;
                }

                return null;
            }
        });

        // Show dialog
        Optional<PartialGame> result = dialog.showAndWait();

        if (result.isPresent()) 
        {
            System.out.println("Staring new game");
            PartialGame game = result.get();
            
            createNewGame();
            
            startGame();
        }        
    }
    
    private void startGame()
    {        
        Date firstTime = new Date();
        clickTask = new AnimationTask();

        // this is the value of the delay in milliseconds between each board refresh
        final long period = 1000;
        
        timer.schedule(clickTask, firstTime, period);
    }
    
//TODO: call this from the overriden stop() method in teh MainApp class.    
    public void stopThreads()
    {
        timer.cancel();
    }
    
    private void updateScoreBoard()
    {
//TODO: acutally update the score board
    }
    
    /**
     * This class shuffles the panels and updates the on-screen panels.
     * Calling this method repeatedly animates the point panels and selected panel.
     */
    private class AnimationTask extends TimerTask
    {
        private Color convert(java.awt.Color awtColor)
        {
            int r = awtColor.getRed();
            int g = awtColor.getGreen();
            int b = awtColor.getBlue();
            
            int a = awtColor.getAlpha();
            
            double opacity = a / 255.0 ;
            
            Color fxColor = Color.rgb(r, g, b, opacity);
            
            return fxColor;
        }
        
        @Override
        public void run()
        {                
            System.out.println("we are animatiing!");
            
            game.shuffleBoardPanels();
        
            int currentPanelIndex = game.getCurentPointPanelIndex();

            int i = 0;

            for (Label location : boards) 
            {
                BoardPanel panel = game.getBoardPanels().get(i);

                Paint foreground;

                if (i == currentPanelIndex) 
                {
                    foreground = Color.RED;
                } 
                else 
                {
                    foreground = Color.WHITE;
                }

                location.setText(panel.getLabel());
                location.setTextFill(foreground);
                
                java.awt.Color awtColor = panel.getBackgroundColor();
                Color backgroundColor = convert(awtColor);
                Background background = new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY));
                location.setBackground(background);

                i++;
            }

            
//TODO: IMPLEMENT DRAWING THE PLAYER LABEL IN THE CENTER            
            String label = "P" + (game.currentPlayer + 1);
            BoardPanel playerLabel = new PlayerLabelPanel(label);
//            playerLabel.draw(g2d, labelLocation, Color.RED, gamePanelWidth);

        }
    }    
    
    private class PartialGame
    {
        public  int numberOfPlayers;
        
        public int targetScore;
                
        PartialGame(int numberOfPlayers, int targetScore) 
        {
            this.numberOfPlayers = numberOfPlayers;
            this.targetScore = targetScore;
        }
    }
}
