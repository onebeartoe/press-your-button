    
package org.onebeartoe.games.press.your.button.fx;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

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
import javafx.application.Platform;

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
import org.onebeartoe.games.press.your.button.NextPlayerResponses;
import static org.onebeartoe.games.press.your.button.NextPlayerResponses.CURRENT_PLAYER_CANNOT_BE_SKIPPED_TRY_NEW_GAME;
import static org.onebeartoe.games.press.your.button.NextPlayerResponses.GAME_IS_OVER_CLICK_NEW_GAME_BUTTON;
import static org.onebeartoe.games.press.your.button.NextPlayerResponses.NEXT_PLAYERS_TURN;
import org.onebeartoe.games.press.your.button.Player;
import org.onebeartoe.games.press.your.button.PressYourButtonConstants;
import org.onebeartoe.games.press.your.button.PressYourButtonGame;
import org.onebeartoe.games.press.your.button.board.BoardPanel;

public class FXMLController implements Initializable 
{
    @FXML
    private Label currentPlayerLabel;
    
    @FXML
    private Label player1Label;
    
    @FXML
    private Label player2Label;
    
    @FXML
    private Label player3Label;
    
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
    @FXML
    private Label board05;
    @FXML
    private Label board06;
    @FXML
    private Label board07;
    @FXML
    private Label board08;
    @FXML
    private Label board09;
    @FXML
    private Label board10;
    @FXML
    private Label board11;
    @FXML
    private Label board12;
    @FXML
    private Label board13;
    @FXML
    private Label board14;
    @FXML
    private Label board15;
    
    private List<Label> boards;
    
    private TimerTask animationTask;

    private Timer timer;
    
    volatile public PressYourButtonGame game;
    
    private ChoiceBox playerCountDropdown;
    
    private ChoiceBox targetScoreDropdown;
    
    private Logger logger;
    
    private GpioController gpio;
    
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
        System.out.println("in conroller - game - created new game (1)");
        
        
        endCurrentPlayersTurn();
        System.out.println("in conroller - eneded current player's turn");
        
        game.createNewGame(players, targetScore);
        System.out.println("in conroller - game - created new game (2)");
    }

    public void endCurrentPlayersTurn()
    {        
        EndCurrentPlayersTurnResponses response = game.endCurrentPlayersTurn();

        if(animationTask != null)
        {
            animationTask.cancel();
        }
        
        // update score board
        updateScoreBoard();        
        
        switch(response)
        {
            case END_OF_GAME:
            {
                String title = "Winner";
                String message = "Player " + (game.currentPlayer + 1) + " is the winner of this game!";
                
                informationalAlert(title, message);
                
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
    private void handleNextPlayerButtonAction(ActionEvent event)
    {
        NextPlayerResponses commandRespnse = game.nextPlayer();
        
        String title = "Next Player";

        switch(commandRespnse)
        {
            case GAME_IS_OVER_CLICK_NEW_GAME_BUTTON:
            {
                String message = "This game is over, please click the 'New Game' button.";
                informationalAlert(title, message);

                break;
            }
            case CURRENT_PLAYER_CANNOT_BE_SKIPPED_TRY_NEW_GAME:
            {
                String message = "The current player cannot be skipped.  Try the 'New Game' button, if you are done with is game.";
                informationalAlert(title, message);

                break;
            }
            case NEXT_PLAYERS_TURN:
            {
                String label = "Player " + (game.currentPlayer + 1);
                currentPlayerLabel.setText(label);
                        
                startAnimation();
                System.out.println("it is the next player's turn");

                break;
            }
            default:
            {
                String message = "unknown enum: " + commandRespnse;
                logger.log(Level.SEVERE, message);
            }
        }

        System.out.println("in next listener, game state is " + game.gameState);
    }
    
    @FXML
    private void handleStopButtonAction(ActionEvent event) 
    {
        System.out.println("Stop button pressed");
        
        if(game.gameState == GameStates.PLAYERS_TURN)
        {
            System.out.println("ending the curent players turn");
            
            endCurrentPlayersTurn();
        }
        else
        {
            System.out.println("Not gonna stop the current player's turn because we are in state: " + game.gameState);
        }
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

    private void informationalAlert(String title, String message)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Information Alert");
        alert.setContentText(message);

        alert.show();
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
                
        initializeBoardPanels();
        
        initializeGpioButton();
    }

    private void initializeBoardPanels()
    {
        boards = new ArrayList();
        
        boards.add(board00);
        boards.add(board01);
        boards.add(board02);
        boards.add(board03);
        boards.add(board04);
        boards.add(board05);
        boards.add(board06);
        boards.add(board07);
        boards.add(board08);
        boards.add(board09);
        boards.add(board10);
        boards.add(board11);
        boards.add(board12);
        boards.add(board13);
        boards.add(board14);
        boards.add(board15);

        // can we use Streams here (not forEach() )?
        for(Label l : boards)
        {
            l.setText("");
        }
//        boards = boards.stream()
//                        .map( Label::setText("") )
//                        .collect( Collectors.toList() );
    }

    /**
     * Wiring: 
     * The arcade button (https://www.adafruit.com/products/1194)
     * used in this project is from Adafruit.
     *       Wiring the button for input into the Raspberry Pi is as follows:
     *
     *       The two connections on either of the long sides are for powering the 
     *       built-in LED.
     *
     *       The connection on the bottom is wired to Ground on the Raspberry Pi.
     *
     *       The button connection that sticks out of the housing, in the same direction 
     *       as the Ground connection, is connected to pin 13, on the Raspberry Pi 
     *       model B Revision 2, pin header.  It is also labeled #21 by the 
     *       Raspberry Pi foundation.
     */
    private void initializeGpioButton()
    {
        try
        {
            System.out.println("*Provisioning GPIO.");
            gpio = GpioFactory.getInstance();

            // This is pin 13 on the Raspberry Pi model B, revision 2 pin header.
            // It is also labeld #21 by the Raspberry Pi foundation.
            Pin buttonPin = RaspiPin.GPIO_02;

            GpioPinDigitalInput stopButton = gpio.provisionDigitalInputPin(buttonPin, 
                                                                             "press your button", 
                                                                             PinPullResistance.PULL_UP);   // works with the Adafruit massive arcade button
    //                                                                           PinPullResistance.PULL_DOWN); // works with a tactile/simple push button

            System.out.println("*GPIO provisioned :)");
            GpioHandler gpioHandler = new GpioHandler();
            stopButton.addListener(gpioHandler);            
        }
        catch(IllegalArgumentException | UnsatisfiedLinkError e)
        {
            e.printStackTrace();
        }
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
        Label label1 = new Label("Number of Players: ");
        Label label2 = new Label("Target Score: ");

        // Create layout and add to dialog
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 35, 20, 35));
        grid.add(label1, 1, 1); // col=1, row=1
        
        grid.add(playerCountDropdown, 2, 1);
        
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

        Optional<PartialGame> result = dialog.showAndWait();

        if (result.isPresent()) 
        {
            System.out.println("in conroller - Staring new game");
//            PartialGame game = result.get();
            
            createNewGame();
            System.out.println("in conroller - Created new game");
            
            startAnimation();
            System.out.println("in controller - Animation started");
        }        
    }
    
    private void startAnimation()
    {        
        Date firstTime = new Date();
        animationTask = new AnimationTask();

        // this is the value of the delay in milliseconds between each board refresh
        final long period = 1000;
        
        timer.schedule(animationTask, firstTime, period);
    }
    
    /**
     * This method is called to clean up any threads spawned by the application, 
     * as well as shutdown the GPIO interface.
     */
    public void stopThreads()
    {
        timer.cancel();

        if(gpio == null)
        {
            System.err.println("the GPIO interface was not initialized on app exit");
        }
        else
        {
            gpio.shutdown();
        }
    }
    
    private void updateScoreBoard()
    {
        int size = game.players.size();
                
        if(size > 0)
        {
            String text = "Player 1: " + game.players.get(0).score;
            player1Label.setText(text);
        }
        
        if(size > 1)
        {
            String text = "Player 2: " + game.players.get(1).score;
            player2Label.setText(text);
        }
        else
        {
            player2Label.setText("");
        }
        
        if(size > 2)
        {
            String text = "Player 3: " + game.players.get(2).score;
            player3Label.setText(text);
        }
        else
        {
            player3Label.setText("");
        }
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

            for (final Label location : boards) 
            {
                final BoardPanel panel = game.getBoardPanels().get(i);

                final Paint foreground;

                if (i == currentPanelIndex) 
                {
                    foreground = Color.RED;
                } 
                else 
                {
                    foreground = Color.WHITE;
                }

                Platform.runLater(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        location.setText(panel.getLabel());
                        location.setTextFill(foreground);

                        java.awt.Color awtColor = panel.getBackgroundColor();
                        Color backgroundColor = convert(awtColor);
                        Background background = new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY));
                        location.setBackground(background);                        
                    }
                });


                i++;
            }
        }
    }    
    
    private class GpioHandler implements GpioPinListenerDigital
    {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) 
        {
            PinState state = event.getState();
            if(state == PinState.LOW)
            {
                System.out.println("stop button pin state changed to LOW.");
         
                ActionEvent ae = null;
                
                Platform.runLater(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        handleStopButtonAction(ae);
                    }
                });
            }
            else
            {
                System.out.println("stop button pin state changed to HIGH.");
            }
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