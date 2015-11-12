package org.onebeartoe.games.press.your.button.fx;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.onebeartoe.games.press.your.button.PressYourButtonConstants;

public class FXMLController implements Initializable 
{
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) 
    {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
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
        // TODO
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
        final TextField text1 = new TextField();
        final TextField text2 = new TextField();

        // Create layout and add to dialog
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 35, 20, 35));
        grid.add(label1, 1, 1); // col=1, row=1
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2); // col=1, row=2
        grid.add(text2, 2, 2);
        dialog.getDialogPane().setContent(grid);

        // Add button to dialog
        final ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk );

        // Result converter for dialog
        dialog.setResultConverter(new Callback<ButtonType, PartialGame>() {
                @Override
                public PartialGame call(ButtonType b) {

                        if (b == buttonTypeOk) {

                                return new PartialGame(text1.getText(), text2.getText());
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
            
            // start a new game
        }        
    }
    
    private class PartialGame
    {
        private String name;
        private String phone;

        PartialGame(String s1, String s2) 
        {
                name = s1;
                phone = s2;
        }
    }
}
