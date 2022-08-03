package com.konon.farm.controllers;

import com.konon.farm.files.TextFileSupport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LogsController {

    @FXML
    private ComboBox<String> dateBox;

    @FXML
    private Button show;

    @FXML
    private TextArea logsArea;

    @FXML
    private void initialize() throws IOException {
        List<String> allLogFilesName = TextFileSupport.getAllLogFilesName();
        if (allLogFilesName != null) {
            ObservableList<String> allDates = FXCollections.observableList(allLogFilesName);
            dateBox.setItems(allDates);
        }
    }

    @FXML
    private void OnShowButtonClick(ActionEvent event) throws IOException {
        String value = dateBox.getValue();
        String fileName = "D:\\java-stuff\\Last-one\\farm\\logs\\" + value;
        File log = new File(fileName);
        FileReader reader = new FileReader(log);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String s = "";
        String line = bufferedReader.readLine();
        while (line != null) {
            s += line + "\n";
            line = bufferedReader.readLine();
        }
        logsArea.setText(s);
    }
}
