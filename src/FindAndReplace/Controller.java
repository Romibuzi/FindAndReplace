package FindAndReplace;

import javafx.scene.paint.Color;
import java.io.*;

/**
 * Controller Class
 * Used to contain Programm Logic
 */
public class Controller
{
    public static File chosenFile;
    public static File createdFile;

    /**
     * Display the path
     * of the selected file if it's a valid file
     *
     * @param file : file chosen by the user
     */
    public static void openFile(File file) {
        if (isValidFile(file)) {
            chosenFile = file;
            Main.chosenFilePath.setText(chosenFile.getAbsolutePath());
        }  else {
            Main.infoText.setFill(Color.RED);
            Main.infoText.setText("This File seems invalid !");
        }
    }

    /**
     * Check if the file
     * chosen by the user is valid
     * @param file : the file chosen by the user
     * @return bool
     */
    public static boolean isValidFile(File file) {
        try {
            file.getCanonicalPath();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    /**
     * Method used to Find a specific text in the File
     *
     * @param textToFind : the text to find in the file
     */
    public static void searchInFile(String textToFind) {
        try {
            // initializion of the file reader
            FileReader fr       = new FileReader(chosenFile.getAbsolutePath());
            BufferedReader in   = new BufferedReader(fr);
            String currentLine;
            Integer occurences = 0;
            while ((currentLine = in.readLine()) != null) {
                // increments the number of occurence each time a line contains the text
                if (currentLine.contains(textToFind)) {
                    ++occurences;
                }
            }
            if (occurences == 0) {
                Main.infoText.setFill(Color.RED);
                Main.infoText.setText("No occurences found in the file !");
            } else {
                Main.infoText.setFill(Color.GREEN);
                Main.infoText.setText(occurences + " occurences found in the file !");
            }
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * Method used to Replace a specific text by another text in the File
     *
     * @param oldText : the replaced text in the file
     * @param newText : the text to replace in the file
     */
    public static void replaceInFile(String oldText, String newText) {
        // Create the new File in the same directory of the selected File
        // with the same name but prefixed by "NEW_"
        String chosenFileParent = chosenFile.getParent();
        String chosenFileName   = chosenFile.getName();
        String createdFileName  = chosenFileParent
                                  .concat(File.separator)
                                  .concat("NEW_")
                                  .concat(chosenFileName);
        createdFile = new File(createdFileName);
        //System.out.println(createdFileName);

        try {
            // initializion of the file reader and the file writer
            FileReader fr      = new FileReader(chosenFile.getAbsolutePath());
            BufferedReader in  = new BufferedReader(fr);
            FileWriter fw      = new FileWriter(createdFile);
            BufferedWriter out = new BufferedWriter(fw);
            String currentLine;
            String newLine;
            Integer replacements = 0;

            while ((currentLine = in.readLine()) != null) {
                if (currentLine.contains(oldText)) {
                    newLine = currentLine.replace(oldText, newText);
                    ++replacements;
                } else {
                    newLine = currentLine;
                }
                out.write(newLine);
                out.newLine();
            }
            out.flush();
            out.close();

            if (replacements > 0) {
                Main.infoText.setFill(Color.GREEN);
                Main.infoText.setText(replacements + " replacements made in the file !");
                Main.createdFileInfo.setText("Created file is located at : \n" + createdFileName);
            } else {
                // delete the created file because there was no replacements
                createdFile.delete();
                Main.infoText.setFill(Color.RED);
                Main.infoText.setText("No replacements made !");
            }
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
}
