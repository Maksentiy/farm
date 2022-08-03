package com.konon.farm.controllers;

import com.konon.farm.app.App;
import com.konon.farm.employee.Work;
import com.konon.farm.employee.WorkCRUD;
import com.konon.farm.files.TextFileThread;
import com.konon.farm.product.*;
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

import java.io.IOException;
import java.util.List;

import static javafx.stage.Modality.WINDOW_MODAL;

public class ProductListController {


    @FXML
    private Button add;

    @FXML
    private Button back;

    @FXML
    private Button update;

    @FXML
    private Button delete;

    @FXML
    TableView<Product> table;

    @FXML
    TableColumn<Product, String> name;

    @FXML
    TableColumn<Product, String> type;

    @FXML
    private void initialize() {
        ProductCRUD productCRUD = new ProductCRUD();
        ObservableList<Product> products = FXCollections.observableList(productCRUD.getAll());
        if (!products.isEmpty()) {
            initialData(products);
        }
    }

    @FXML
    private void OnAddButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/newProduct.fxml"));
        stage.setScene(new Scene(loader.load(), 250, 200));
        stage.setTitle("New product");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage thisStage = (Stage) back.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    @FXML
    private void OnBackButtonClick(ActionEvent event) throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/harvesting.fxml"));
        newStage.setScene(new Scene(loader.load(), 557, 400));
        newStage.setTitle("Harvesting");
        newStage.initModality(WINDOW_MODAL);
        newStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage thisStage = (Stage) back.getScene().getWindow();
        thisStage.close();
        newStage.show();
    }

    @FXML
    private void OnUpdateButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/newProduct.fxml"));
        stage.setScene(new Scene(loader.load(),250, 200));
        stage.setTitle("Product Update");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        NewProductController newProductController = loader.getController();
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        ProductCRUD productCRUD = new ProductCRUD();
        List<Product> all = productCRUD.getAll();
        for (Product  product:all) {
            if (product.equals(selectedProduct)) {
                newProductController.OnTransferData(product);
            }
        }
        Stage thisStage = (Stage) back.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    @FXML
    private void OnDeleteButtonClick(ActionEvent event) throws IOException{
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        ProductCRUD productCRUD = new ProductCRUD();
        List<Product> all = productCRUD.getAll();
        for (Product  product:all) {
            if (product.equals(selectedProduct)) {
                if (searchUsage(product)) {
                    productCRUD.delete(product.getId());
                    new Thread(new ProductThread("delete", product)).start();
                    new Thread(new TextFileThread<Product>("deleted product", product)).start();
                } else {
                    showAlert(product.getName());
                }
            }
        }
    }

    @FXML
    private void OnReloadButtonClick(ActionEvent event) throws IOException {
        ProductCRUD productCRUD = new ProductCRUD();
        ObservableList<Product> products = FXCollections.observableList(productCRUD.getAll());
        if (!products.isEmpty()) {
            initialData(products);
        }
    }

    private void initialData(ObservableList<Product> products) {
        name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        type.setCellValueFactory(new PropertyValueFactory<Product, String>("type"));

        name.setCellFactory(t -> {
            TableCell<Product, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(name.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        table.setItems(products);
    }

    private boolean searchUsage(Product product) {
        HarvestCRUD harvestCRUD = new HarvestCRUD();
        WorkCRUD workCRUD = new WorkCRUD();
        List<Harvest> harvestList = harvestCRUD.getAll();
        List<Work> workList = workCRUD.getAll();
        for (Harvest harvest:harvestList) {
            if (harvest.getProduct().getId().equals(product.getId())) {
                return false;
            }
        }

        for (Work work : workList) {
            if (work.getProduct().getId().equals(product.getId())) {
                return false;
            }
        }
        return true;
    }

    public static void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ahtung!!!!");
        alert.setHeaderText("Product " + msg + " using in another tables");
        alert.showAndWait();
    }
}
