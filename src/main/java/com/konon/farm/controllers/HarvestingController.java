package com.konon.farm.controllers;

import com.konon.farm.app.App;
import com.konon.farm.db.Searches;
import com.konon.farm.files.TextFileSupport;
import com.konon.farm.product.Harvest;
import com.konon.farm.product.HarvestCRUD;
import com.konon.farm.product.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Date;


import static javafx.stage.Modality.WINDOW_MODAL;

public class HarvestingController {

    @FXML
    public Button products;

    @FXML
    public Button new_record;

    @FXML
    private Button employees;

    @FXML
    private TableView<Harvest> table;

    @FXML
    private TableColumn<Harvest, Integer> id;

    @FXML
    private TableColumn<Product, String> product;

    @FXML
    private TableColumn<Harvest, Double> weight;

    @FXML
    private TableColumn<Harvest, Double>  price;

    @FXML
    private TableColumn<Harvest, Date> harvest_date;

    @FXML
    private TableColumn<Harvest, Double> expenses;

    @FXML
    private TableColumn<Harvest, Double> sold;

    @FXML
    private TableColumn<Harvest, Double> profit;

    @FXML
    private void initialize() throws IOException{
        HarvestCRUD harvestCRUD = new HarvestCRUD();
        ObservableList<Harvest> harvests = FXCollections.observableList(harvestCRUD.getAll());
        if (!harvests.isEmpty()) {
            initialData(harvests);
        }
        File file = TextFileSupport.findFile();
    }

    @FXML
    private void OnUpdateButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/newRecord.fxml"));
        stage.setScene(new Scene(loader.load(), 300, 300));
        stage.setTitle("Harvest record Update");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        NewRecordController newRecordController = loader.getController();
        Harvest selectedHarvest = table.getSelectionModel().getSelectedItem();
        Searches searches = new Searches();
        newRecordController.OnTransferData(searches.searchHarvestEqual(selectedHarvest));
        Stage thisStage = (Stage) products.getScene().getWindow();
        thisStage.close();
        stage.show();

    }

    @FXML
    private void OnEmployeesButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/workersRecords.fxml"));
        stage.setScene(new Scene(loader.load(), 400, 300));
        stage.setTitle("Employees work");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage thisStage = (Stage) products.getScene().getWindow();
        thisStage.close();
        stage.show();

    }

    @FXML
    private void OnProductsButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/productsList.fxml"));
        stage.setScene(new Scene(loader.load(), 250, 350));
        stage.setTitle("Product List");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage thisStage = (Stage) products.getScene().getWindow();
        thisStage.close();
        stage.show();
    }
    
    @FXML
    private void OnLogsButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/logs.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Logs");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        stage.show();
    }

    @FXML
    private void OnReloadButtonClick(ActionEvent event) throws IOException {
        HarvestCRUD harvestCRUD = new HarvestCRUD();
        ObservableList<Harvest> harvests = FXCollections.observableList(harvestCRUD.getAll());
        if (!harvests.isEmpty()) {
            initialData(harvests);
        }
    }

    @FXML
    private void OnReportButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/report.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Report");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        stage.show();
    }

    private void initialData(ObservableList<Harvest> harvests) {
        id.setCellValueFactory(new PropertyValueFactory<Harvest, Integer>("id"));
        price.setCellValueFactory(new PropertyValueFactory<Harvest, Double>("price"));
        product.setCellValueFactory(new PropertyValueFactory<Product, String>("product"));
        harvest_date.setCellValueFactory(new PropertyValueFactory<Harvest, Date>("harvest_date"));
        weight.setCellValueFactory(new PropertyValueFactory<Harvest, Double>("weight"));
        expenses.setCellValueFactory(new PropertyValueFactory<Harvest, Double>("expenses"));
        sold.setCellValueFactory(new PropertyValueFactory<Harvest, Double>("sold"));
        profit.setCellValueFactory(new PropertyValueFactory<Harvest, Double>("profit"));

        id.setCellFactory(t -> {
            TableCell<Harvest, Integer> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(id.widthProperty());
            text.textProperty().bind(cell.textProperty());
            return cell;
        });

        table.setItems(harvests);
    }
}
