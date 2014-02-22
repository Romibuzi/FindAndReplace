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

    /**
     * Display the path
     * of the selected file if it's a valid file
     *
     * @param file : file chosen by the user
     */
    public static void openFile(File file)
    {
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
     *
     * @param file : the file chosen by the user
     *
     * @return bool
     */
    private static boolean isValidFile(File file)
    {
        try {
            file.getCanonicalPath();
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Method used to create the new File
     * in the same directory of the selected File
     * with the same name but prefixed by "NEW_"
     *
     * @return File : the new file
     */
    private static File createNewFile()
    {
        String chosenFileParent = chosenFile.getParent();
        String chosenFileName   = chosenFile.getName();
        String newFileName      = chosenFileParent
                                  .concat(File.separator)
                                  .concat("NEW_")
                                  .concat(chosenFileName);

        return new File(newFileName);
    }

    /**
     * Method used to Find a specific text in the File
     *
     * @param textToFind : the text to find in the file
     */
    public static void searchInFile(String textToFind)
    {
        try {
            // initializion of the file reader
            FileReader fr       = new FileReader(chosenFile.getAbsolutePath());
            BufferedReader in   = new BufferedReader(fr);
            String currentLine;
            Integer occurences = 0;
            while ((currentLine = in.readLine()) != null) {
                // increments the number of occurence each time a line contains the text
                if (currentLine.contains(textToFind)) {
                    int count   = countSubstring(textToFind, currentLine);
                    occurences += count;
                }
            }
            if (occurences == 0) {
                Main.infoText.setFill(Color.RED);
                Main.infoText.setText("No occurences found in the file !");
            } else {
                Main.infoText.setFill(Color.GREEN);
                Main.infoText.setText(occurences + " occurences found in the file !");
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * Method used to Replace a specific text by another text in the File
     *
     * @param oldText : the replaced text in the file
     * @param newText : the text to replace in the file
     */
    public static void replaceInFile(String oldText, String newText, boolean inSameFile)
    {
        File newFile = createNewFile();

        try {
            // initializion of the file reader and the file writer
            FileReader fr      = new FileReader(chosenFile.getAbsolutePath());
            BufferedReader in  = new BufferedReader(fr);
            FileWriter fw      = new FileWriter(newFile);
            BufferedWriter out = new BufferedWriter(fw);
            String currentLine;
            String newLine;
            Integer replacements = 0;

            while ((currentLine = in.readLine()) != null) {
                if (currentLine.contains(oldText)) {
                    int count     = countSubstring(oldText, currentLine);
                    newLine       = currentLine.replace(oldText, newText);
                    replacements += count;
                } else {
                    newLine = currentLine;
                }
                out.write(newLine);
                out.newLine();
            }
            // flush and close reader and writter
            in.close();
            out.flush();
            out.close();

            if (replacements > 0) {
                Main.infoText.setFill(Color.GREEN);
                Main.infoText.setText(replacements + " replacements made in the file !");
                if (inSameFile == true) {
                    // Delete the old file and rename the created one
                    if (renameFile(newFile)) {
                        Main.createdFileInfo.setText("Created file is located at : \n" + chosenFile.getAbsolutePath());
                    }
                } else {
                    Main.createdFileInfo.setText("Created file is located at : \n" + newFile.getAbsolutePath());
                }
            } else {
                // delete the created file because there weren't replacements
                newFile.delete();
                Main.infoText.setFill(Color.RED);
                Main.infoText.setText("No replacements made !");
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * @param newFile
     *
     * @return boolean
     */
    private static boolean renameFile(File newFile)
    {
        if (chosenFile.delete()) {
            if (newFile.renameTo(chosenFile)) {
                System.out.println("Rename succesful");
                return true;
            } else {
                System.out.println("Rename unsuccessfull");
                return false;
            }
        } else {
            System.out.println("Delete operation is failed.");
            return false;
        }
    }

    /**
     *
     * @param subStr
     * @param str
     *
     * @return int
     */
    private static int countSubstring(String subStr, String str)
    {
        return (str.length() - str.replace(subStr, "").length()) / subStr.length();
    }
}
