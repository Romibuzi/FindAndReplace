package FindAndReplace;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Main Class
 * Used to define UI Interface, Elements and User interactions
 */
public class Main extends Application
{
    // The grid
    public static GridPane grid = new GridPane();
    // Label used to display the path of the File selected by the user
    public static Text chosenFilePath = new Text("");
    // Label used to display the path of File created by the programm (if user launched a replacement)
    public static Text createdFileInfo = new Text("");
    // Label used to display all different logs of the app
    public static Text infoText = new Text("");

    // Launch it, bitch !
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method used to initialize the scene and differents interface elements
     * @param stage the window of the application
     */
    @Override
    public void start(final Stage stage) {
        stage.setTitle("Find And Replace");

        // Scene Initialisation
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 20, 20, 20));
        Scene scene = new Scene(grid, 400, 350);
        stage.setScene(scene);

        // Text, Labels, Text Fields and Buttons initialization
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Helvetica", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        final FileChooser fileChooser = new FileChooser();
        Button choseButton = new Button();
        choseButton.setText("Choose A file");
        choseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent choseFileEvent) {
                configureFileChooser(fileChooser);
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    Controller.openFile(file);
                }
            }
        });
        grid.add(choseButton, 0, 1);
        grid.add(chosenFilePath, 1, 1);

        final Label findText = new Label("Text To find :");
        grid.add(findText, 0, 2);

        final TextField oldTextField = new TextField();
        grid.add(oldTextField, 1, 2);

        final Label replaceText = new Label("Text to replace :");
        grid.add(replaceText, 0, 3);

        final TextField newTextField = new TextField();
        grid.add(newTextField, 1, 3);

        // Find Button
        final Button findButton = new Button();
        findButton.setText("Find !");
        findButton.setTranslateZ(10);
        findButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent findEvent) {
                String oldText = oldTextField.getText();
                if (!oldText.isEmpty()) {
                    long startTime = System.currentTimeMillis();
                    Controller.searchInFile(oldText);
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    System.out.println("Execution time : " + duration + " ms !");
                } else {
                    // errors part
                    infoText.setFill(Color.RED);
                    if (Controller.chosenFile == null) {
                        infoText.setText("Select a file !");
                    } else {
                        infoText.setText("Enter a text to find !");
                    }
                }
            }
        });

        // Replace Button
        final Button replaceButton = new Button();
        replaceButton.setText("Replace !");
        replaceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent findEvent) {
                String oldText = oldTextField.getText();
                String newText = newTextField.getText();
                if (!oldText.isEmpty() && !newText.isEmpty() && Controller.chosenFile != null) {
                    long startTime = System.currentTimeMillis();
                    Controller.replaceInFile(oldText, newText);
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    System.out.println("Execution time : " + duration + " ms !");
                } else {
                    // errors part !
                    infoText.setFill(Color.RED);
                    if (Controller.chosenFile == null) {
                        infoText.setText("Select a file !");
                    } else if (oldText.isEmpty() && newText.isEmpty()) {
                        infoText.setText("Enter a text to find and a text to replace !");
                    } else if (oldText.isEmpty()) {
                        infoText.setText("Enter a text to find !");
                    } else if (newText.isEmpty()) {
                        infoText.setText("Enter a text to replace !");
                    }
                }
            }
        });

        // place Find and Replace Button into an HBox
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 15, 15, 15));
        hbox.setSpacing(10);
        hbox.getChildren().addAll(findButton, replaceButton);
        grid.add(hbox, 1, 4);

        grid.add(infoText, 1, 5);
        grid.add(createdFileInfo, 1, 6);

        stage.show();
    }

    /**
     * @param fileChooser the file selector
     */
    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Select A file");

        // Set extension filters
        // for the file selector
        FileChooser.ExtensionFilter sqlFilter  = new FileChooser.ExtensionFilter("SQL file (*.sql)", "*.sql");
        FileChooser.ExtensionFilter txtFilter  = new FileChooser.ExtensionFilter("TXT file (*.txt)", "*.txt");
        FileChooser.ExtensionFilter rtfFilter  = new FileChooser.ExtensionFilter("RTF file (*.rtf)", "*.rtf");
        FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON file (*.json)", "*.json");
        FileChooser.ExtensionFilter xmlFilter  = new FileChooser.ExtensionFilter("XML file (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(sqlFilter);
        fileChooser.getExtensionFilters().add(txtFilter);
        fileChooser.getExtensionFilters().add(rtfFilter);
        fileChooser.getExtensionFilters().add(jsonFilter);
        fileChooser.getExtensionFilters().add(xmlFilter);

        // set the home user directory
        // as the initial directory for the file selector
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        );
    }
}