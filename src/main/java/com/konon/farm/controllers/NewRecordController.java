package com.konon.farm.controllers;

import com.konon.farm.app.App;
import com.konon.farm.files.TextFileThread;
import com.konon.farm.product.Harvest;
import com.konon.farm.product.HarvestThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.stage.Modality.WINDOW_MODAL;

public class NewRecordController {

    private Harvest startHarvest;

    @FXML
    private Button ok;

    @FXML
    private Button cancel;

    @FXML
    private Label product;

    @FXML
    private Label weight;

    @FXML
    private Label date;

    @FXML
    private Label expenses;

    @FXML
    private TextField price;

    @FXML
    private TextField sold;

    @FXML
    private void initialize() {

    }

    @FXML
    private void OnOkButtonClick(ActionEvent event) throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/harvesting.fxml"));
        newStage.setScene(new Scene(loader.load()));
        newStage.setTitle("Harvesting");
        newStage.initModality(WINDOW_MODAL);
        newStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        try {
            startHarvest.setPrice(Double.valueOf(price.getText()));
            startHarvest.setSold(Double.valueOf(sold.getText()));
        } catch (NumberFormatException e) {
            showNumberAlert();
            return;
        }
        startHarvest.setProfit(startHarvest.getPrice() * startHarvest.getSold() - startHarvest.getExpenses());
        if (startHarvest.getSold() > startHarvest.getWeight()) {
            showAlert();
            return;
        }
        new Thread(new HarvestThread("update", startHarvest)).start();
        new Thread(new TextFileThread<Harvest>("added new harvest", startHarvest)).start();
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
        newStage.show();
    }

    @FXML
    private void OnCancelButtonClick(ActionEvent event) throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/harvesting.fxml"));
        newStage.setScene(new Scene(loader.load()));
        newStage.setTitle("Harvesting");
        newStage.initModality(WINDOW_MODAL);
        newStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
        newStage.show();
    }

    @FXML
    protected void OnTransferData(Harvest harvest){
        product.setText(harvest.getProduct().getName());
        weight.setText(harvest.getWeight().toString());
        date.setText(harvest.getHarvest_date().toString());
        expenses.setText(harvest.getExpenses().toString());
        price.setText(harvest.getPrice().toString());
        sold.setText(harvest.getSold().toString());
        startHarvest = harvest;
    }

    public static void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ahtung!!!!");
        alert.setHeaderText("the sold weight of the product cannot exceed the weight of the assembled");
        alert.showAndWait();
    }

    public static void showNumberAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ahtung!!!!");
        alert.setHeaderText("Typos in fields sold or price");
        alert.showAndWait();
    }
}
