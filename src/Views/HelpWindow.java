package Views;


import javafx.fxml.FXML;

import javafx.scene.control.*;


import java.io.*;


public class HelpWindow {

    /**
     * file object File whose data we are interested in
     */
    public File file;

    @FXML
    private TextArea textArea;

    /**
     * Initializes the help window which opens when the user clicks the help button in the home screen
     * @throws IOException: Handles the IOException thrown while reading the file in the BufferedReader
     */
    @FXML
    public void initialize() throws IOException {
        String everything;
        try(BufferedReader br = new BufferedReader(new FileReader("Message.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
            textArea.setText ( everything );
            textArea.setEditable(false);
        }

    }
}
