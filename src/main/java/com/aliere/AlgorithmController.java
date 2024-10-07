package com.aliere;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
//import javafx.scene.control.TableView;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AlgorithmController{

    @FXML private Label titleLabel;
    @FXML private VBox parameterVBox;
    @FXML private GridPane gridTableHeader;
    @FXML private GridPane gridTableData;
    //@FXML private TableView<RandomNumber> tableView;

    public void setTitle(String text) {
        titleLabel.setText(text);
    }
    public void addParameterInput(ParameterInput<?>... parameterInputs) {
        parameterVBox.getChildren().addAll(parameterInputs);
    }

    protected GridPane getGridTableData() {
        return gridTableData;
    }
    //public TableView<RandomNumber> getTableView() { return tableView; }

    public void buildTable(String[] columns) {
        cleanGridPane(gridTableHeader);
        cleanGridPane(gridTableData);

        int i = 0;
        for (String s : columns) {
            ColumnConstraints CC = new ColumnConstraints(-1, -1, -1, Priority.SOMETIMES, HPos.CENTER, true);
            gridTableHeader.getColumnConstraints().add(CC);
            gridTableData.getColumnConstraints().add(CC);
            gridTableHeader.add(makeHeaderLabel(s), i, 0);
            i++;
        }
    }

    public void populateTable(ArrayList<RandomNumber> data) {
        System.out.println("clearing data");
        gridTableData.getChildren().clear();
        gridTableData.getRowConstraints().clear();

        Label[] columns = gridTableHeader.getChildren().toArray(new Label[]{});
        int i = 0;
        for (RandomNumber r : data) {
            gridTableData.getRowConstraints().add(new RowConstraints(-1, -1, -1, Priority.NEVER, VPos.BASELINE, false));
            int j = 0;
            for (Label c : columns) {
                gridTableData.add(makeDataLabel(r.getComponent(c.getText())), j, i);
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

    @FXML
    public void backBtnPress() {
        Main.setScreen(Main.Screen.MENU);
    }
}
