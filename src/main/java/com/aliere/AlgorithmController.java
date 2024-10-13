package com.aliere;

import java.io.File;
import java.util.ArrayList;
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

public class AlgorithmController{

    @FXML private VBox mainVBox;
    @FXML private Label titleLabel;
    @FXML private HBox notificationBar;
    @FXML private Label notificationLabel;
    @FXML private Hyperlink notificationClose;
    @FXML private VBox parameterVBox;
    @FXML private GridPane gridTableHeader;
    @FXML private GridPane gridTableData;
    //@FXML private TableView<RandomNumber> tableView;

    private Algorithm currentAlgorithm;
    private LinkedHashMap<String, ParameterInput<?>> parameterInputs = new LinkedHashMap<>();

    //SETS
    public void setCurrentAlgorithm(Algorithm currentAlgorithm) {
        this.currentAlgorithm = currentAlgorithm;
    }
    public void setTitle(String text) {
        titleLabel.setText(text);
    }
    public void addParameterInput(ParameterInput<?>... parameterInputs) {
        parameterVBox.getChildren().clear();
        for (ParameterInput<?> parameterInput : parameterInputs) {
            this.parameterInputs.put(parameterInput.getName(), parameterInput);
            parameterVBox.getChildren().add(parameterInput);
        }
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

    //ALGRITHM LOADING
    protected void loadAlgorithm(Algorithm newAlgorithm) {
        setCurrentAlgorithm(newAlgorithm);
        setTitle(newAlgorithm.getName());
        parameterInputs.clear();
        parameterVBox.getChildren().clear();
        for (Parameter<?> p : newAlgorithm.getParameters().values()) {
            parameterInputs.put(p.getName(), new ParameterInput<>(p));
        }
        parameterVBox.getChildren().addAll(parameterInputs.values());
        buildTable(newAlgorithm.getColumns());
        clearNotification();
    }

    //GRID TABLE
    public void buildTable(String[] columns) {
        cleanGridPane(gridTableHeader);
        cleanGridPane(gridTableData);
        
        int i = 0;
        for (String s : columns) {
            ColumnConstraints CC = new ColumnConstraints(
                -1, -1, -1, Priority.SOMETIMES, HPos.CENTER, true);
            gridTableHeader.getColumnConstraints().add(CC);
            gridTableData.getColumnConstraints().add(CC);
            gridTableHeader.add(makeHeaderLabel(s), i, 0);
            i++;
        }
    }

    public void populateTable(ArrayList<RandomNumber> data) {
        gridTableData.getChildren().clear();
        gridTableData.getRowConstraints().clear();

        Label[] columns = gridTableHeader.getChildren().toArray(new Label[]{});

        RandomNumber r1 = data.removeFirst();
        gridTableData.getRowConstraints().add(new RowConstraints(
                -1, -1, -1, Priority.NEVER, VPos.BASELINE, false));
        int c = 0;
        for (Label l : columns) {
            Label dataLabel = makeDataLabel(r1.getComponent(l.getText()));
            Label lc = (Label)gridTableHeader.getChildren().get(c);
            lc.prefWidthProperty().bind(dataLabel.widthProperty());
            gridTableData.add(dataLabel, c, 0);
            c++;
        }

        System.out.println(data.size());
        int i = 1;
        for (RandomNumber r : data) {
            gridTableData.getRowConstraints().add(new RowConstraints(
                -1, -1, -1, Priority.NEVER, VPos.BASELINE, false));
            int j = 0;
            for (Label l : columns) {
                Label dataLabel = makeDataLabel(r.getComponent(l.getText()));
                gridTableData.add(dataLabel, j, i);
                j++;
            }
            i++;
        }
    }

    protected void cleanGridPane(GridPane gridPane) {
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
    }

    protected Label makeHeaderLabel(String text) {
        Label headerLabel = new Label(text);
        headerLabel.setMaxWidth(Double.MAX_VALUE);
        headerLabel.alignmentProperty().set(Pos.CENTER);
        headerLabel.setBackground(Background.fill(Color.rgb(0, 29, 68)));
        headerLabel.setTextFill(Color.WHITE);
        headerLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        return headerLabel;
    }

    protected Label makeDataLabel(String text) {
        Label dataLabel = new Label(text);
        dataLabel.setMaxWidth(Double.MAX_VALUE);
        dataLabel.alignmentProperty().set(Pos.CENTER_LEFT);
        dataLabel.setFont(Font.font("Consolas", 12));
        return dataLabel;
    }

    //BUTTON INPUTS
    @FXML
    public void backBtnPress() {
        Main.setScreen(Main.Screen.MENU);
    }

    @FXML
    public void exportBtnPress() {
        //TODO: offer different export options (.txt, .csv, .pdf)
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("txt files", ".txt"));
        fileChooser.setInitialFileName(String.format("%s(%f).txt", 
        titleLabel.getText(), currentAlgorithm.getParameters().get("Seed").getValue()));
        File numbersFile = fileChooser.showSaveDialog(mainVBox.getScene().getWindow());
        try {
            currentAlgorithm.exportarNumeros(numbersFile);
            showNotification("Archivo exportado exitosamente");
        } catch (Exception e) {
            showNotification("Error al exportar el archivo: " + e.getMessage());
        }
    }

    @FXML
    public void notifBtnPress() {
        clearNotification();
    }

    //NOTIFICATIONS
    public void showNotification(String notification) {
        clearNotification();
        notificationLabel.setText(notification);
        notificationBar.getChildren().addAll(notificationLabel, notificationClose);
        //TODO: clear notifications automatically after a delay (~5s)
    }

    public void clearNotification() {
        notificationBar.getChildren().clear();
    }
}
