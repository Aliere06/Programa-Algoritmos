package com.aliere;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
//import javafx.scene.control.TableView;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

//ALGORITHM CONTROLLER
/**<p> Controller class for the algorithm FXML GUI.
 * 
 * <p> This class handles building the GUI from the {@code Algorithm} objects, 
 * and all GUI interactions.
 */
public class AlgorithmController{

    //FXML COMPONENTS
    /*Reference to all the relevant components in the FXML GUI*/

    //General Containers
    @FXML private VBox mainVBox;
    @FXML private VBox parameterVBox;
    
    //Display Elements
    @FXML private Label titleLabel;
    @FXML private GridPane gridTableHeader;
    @FXML private GridPane gridTableData;

    //Notification Components
    @FXML private HBox notificationBar;
    @FXML private Label notificationLabel;
    @FXML private Hyperlink notificationClose;

    //@FXML private TableView<RandomNumber> tableView;


    //ATRIBUTES
    private Algorithm currentAlgorithm;
    private LinkedHashMap<String, ParameterInput<?>> parameterInputs = new LinkedHashMap<>();

    //SETS
    public void setCurrentAlgorithm(Algorithm currentAlgorithm) {
        this.currentAlgorithm = currentAlgorithm;
    }
    public void setTitle(String text) {
        titleLabel.setText(text);
    }

    //GETS
    protected GridPane getGridTableData() {
        return gridTableData;
    }
    public Algorithm getCurrentAlgorithm() {
        return currentAlgorithm;
    }
    public LinkedHashMap<String, ParameterInput<?>> getParameterInputs() {
        return parameterInputs;
    }
    //public TableView<RandomNumber> getTableView() { return tableView; }


    //ALGRITHM LOADING METHOD
    /**Takes an {@code Algorithm} object and configures the GUI for it.
     * @param newAlgorithm the algorithm to be loaded.
     */
    protected void loadAlgorithm(Algorithm newAlgorithm) {
        setCurrentAlgorithm(newAlgorithm); //Update current algorithm
        setTitle(newAlgorithm.getName()); //Update title label
        parameterInputs.clear(); //Clear parameter input map
        parameterVBox.getChildren().clear(); //Clear parameter inputs container

        /*Import parameters from the algorithm object and create new parameter
         *inputs for each of them. */
        for (Parameter<?> p : newAlgorithm.getParameters().values()) {
            parameterInputs.put(p.getName(), new ParameterInput<>(p));
        }

        //Add new parameter inputs to the parameter inputs container
        parameterVBox.getChildren().addAll(parameterInputs.values());

        buildTable(newAlgorithm.getColumns()); //Build GUI table
        clearNotification(); //Clear last notification
    }

    //GRID TABLE METHODS

    //Build Table
    /**<p> Builds the GUI table for displaying the random number generation process.
     * 
     * <p> Clears the previous table and builds a new one based on the.
     * {@code currentAlgorithm}'s columns headers array.
     */
    public void buildTable(String[] columns) {
        //Clear all the row and column constraints from the table GridPanes
        cleanGridPane(gridTableHeader);
        cleanGridPane(gridTableData);
        
        int i = 0;
        for (String s : columns) {
            //Create new column constraints for each column header in columns
            ColumnConstraints CC = new ColumnConstraints(
                -1, -1, -1, Priority.SOMETIMES, HPos.CENTER, true
            );

            //Add the new constrains to the table GridPanes
            gridTableHeader.getColumnConstraints().add(CC);
            gridTableData.getColumnConstraints().add(CC);

            //Add header labels
            gridTableHeader.add(makeHeaderLabel(s), i, 0);
            i++;
        }
    }

    //Populate Table
    /**Populates the table's data GridPane from a {@code RandomNumber} ArrayList.
     * 
     * @param data the ArrayList of {@code RandomNumber} objects used to populate
     * the table.
     */
    public void populateTable(LinkedHashSet<RandomNumber> data) {
        //Clear the previous contents of the data GridPane
        gridTableData.getChildren().clear();
        gridTableData.getRowConstraints().clear();

        //Recover the table's header labels
        Label[] columns = gridTableHeader.getChildren().toArray(new Label[]{});

        //Define the row constraints for the data GridPane
        gridTableData.getRowConstraints().add(new RowConstraints(
                -1, -1, -1, Priority.NEVER, VPos.BASELINE, false
        ));

        //Obtain the first RandomNumber from the list
        RandomNumber r1 = data.removeFirst();
        int c = 0;
        for (Label l : columns) {
            /*Create a data label for the table's column from the corresponding
             *RandomNumber's component. */
            Label dataLabel = makeDataLabel(r1.getComponent(l.getText()));
            //Recover the column's header label
            Label lc = (Label)gridTableHeader.getChildren().get(c);
            //Bind the header's label width to the first's row corresponding label
            lc.prefWidthProperty().bind(dataLabel.widthProperty());
            //Add data label to the table
            gridTableData.add(dataLabel, c, 0);
            c++;
        }

        //Create and add the rest of the rows from the RandomNumber list
        int i = 1;
        for (RandomNumber r : data) {
            gridTableData.getRowConstraints().add(new RowConstraints(
                -1, -1, -1, Priority.NEVER, VPos.BASELINE, false
            ));
            int j = 0;
            for (Label l : columns) {
                Label dataLabel = makeDataLabel(r.getComponent(l.getText()));
                gridTableData.add(dataLabel, j, i);
                j++;
            }
            i++;
        }
    }

    //Clean GridPane
    /**Auxiliary method used to clear a GridPane's children, 
     * rown and cloumn constraints.
     * @param gridPane the GridPane to be cleaned
     */
    protected void cleanGridPane(GridPane gridPane) {
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
    }

    //Make Header Label
    /**Auxiliary method used to create a Label with the appropiate layout configurations
     * and style for the table's header.
     * @param text the string to be used for the header label's text.
     * @return a Label with the appropiate attributes.
     */
    protected Label makeHeaderLabel(String text) {
        Label headerLabel = new Label(text);
        headerLabel.setMaxWidth(Double.MAX_VALUE);
        headerLabel.alignmentProperty().set(Pos.CENTER);
        headerLabel.setBackground(Background.fill(Color.rgb(0, 29, 68)));
        headerLabel.setTextFill(Color.WHITE);
        headerLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        return headerLabel;
    }

    //Make Data Label
    /**Auxiliary method used to create a Label with the appropiate layout configurations
     * and style for the table's data rows.
     * @param text the string to be used for the data label's text.
     * @return a Label with the appropiate attributes.
     */
    protected Label makeDataLabel(String text) {
        Label dataLabel = new Label(text);
        dataLabel.setMaxWidth(Double.MAX_VALUE);
        dataLabel.alignmentProperty().set(Pos.CENTER_RIGHT);
        dataLabel.setFont(Font.font("Consolas", 12));
        return dataLabel;
    }


    //BUTTON INPUTS

    //Back Button Press
    /**Switches screens back to the main menu.*/
    @FXML
    public void backBtnPress() {
        Program.setScreen(Program.Screen.MENU);
    }

    //Export Button Press
    /**<p> Calls the algorithm's {@link Algorithm#exportNumbers(File) exportNumbers()} method to export
     * the generated random numbers to a file.
     * 
     * <p> Show a save dialog to obtain the output File.
    */
    @FXML
    public void exportBtnPress() {
        //Return and show a notification if no numbers have been generated
        if (currentAlgorithm.getRandomNumbers().isEmpty()) {
            showNotification("No hay números para exportar. Por favor genere algunos números para empezar");
            return;
        }
    
        //Set up the save dialog
        FileChooser fileChooser = new FileChooser();
        //Filter for .txt files
        //TODO: offer different export options (.txt, .csv, .pdf)
        fileChooser.getExtensionFilters().add(new ExtensionFilter("txt files", ".txt"));
        //Set default name based on the algorithm's name and the seed used for generation
        fileChooser.setInitialFileName(String.format("%s(%d).txt", 
            titleLabel.getText(), currentAlgorithm.getParameters().get("Semilla").getValue()
        ));
        //Show save dialog and revocer or create the selected file
        File numbersFile = fileChooser.showSaveDialog(mainVBox.getScene().getWindow());
        //Call export method to write the RandomNumber values to the file
        try {
            currentAlgorithm.exportNumbers(numbersFile);
            showNotification("Archivo exportado exitosamente");
        } catch (Exception e) {
            //Handle exceptions
            showNotification("Error al exportar el archivo: " + e.getMessage());
        }
    }

    //Notification Button Press
    /**Clears the notification.*/
    @FXML
    public void notifBtnPress() {
        clearNotification();
    }


    //NOTIFICATIONS

    //Show Notification
    /**Shows a notification under the title bar. Only one notification can be 
     * displayed at a time.
     * @param notification the string text of the notification
     */
    public void showNotification(String notification) {
        clearNotification();
        notificationLabel.setText(notification);
        notificationBar.getChildren().addAll(notificationLabel, notificationClose);
        //TODO: clear notifications automatically after a delay (~5s)
    }

    //Clear Notification
    /**Clears the current notification.*/
    public void clearNotification() {
        notificationBar.getChildren().clear();
    }
}
