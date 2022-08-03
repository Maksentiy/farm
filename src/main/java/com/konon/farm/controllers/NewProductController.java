package com.konon.farm.controllers;

import com.konon.farm.app.App;
import com.konon.farm.files.TextFileThread;
import com.konon.farm.product.Product;
import com.konon.farm.product.ProductThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.stage.Modality.WINDOW_MODAL;

public class NewProductController {

    private Integer id;

    @FXML
    private Button ok;

    @FXML
    private Button cancel;

    @FXML
    private TextField name;

    @FXML
    private TextField type;

    @FXML
    private void initialize() {

    }

    @FXML
    private void OnOkButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/productsList.fxml"));
        stage.setScene(new Scene(loader.load(), 250, 350));
        stage.setTitle("Product List");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        if (fieldsCheck() && id == null) {
            Product product = new Product(name.getText(), type.getText());
            new Thread(new ProductThread("insert", product)).start();
            new Thread(new TextFileThread<Product>("added new product", product)).start();
        } else if (fieldsCheck()) {
            Product product = new Product(name.getText(), type.getText());
            product.setId(id);
            new Thread(new ProductThread("update", product)).start();
            new Thread(new TextFileThread<Product>("updated product", product)).start();
        } else {
            showAlert();
            return;
        }
        Stage thisStage = (Stage) ok.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    @FXML
    private void OnCancelButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/productsList.fxml"));
        stage.setScene(new Scene(loader.load(), 250, 350));
        stage.setTitle("Product List");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage thisStage = (Stage) cancel.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    private boolean fieldsCheck() {
        if (name.getText().equals("")) {
            return false;
        }
        if (type.getText().equals("")) {
            return false;
        }

        return true;
    }

    @FXML
    protected void OnTransferData(Product product){
        name.setText(product.getName());
        type.setText(product.getType());
        id = product.getId();
    }

    public static void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ahtung!!!!");
        alert.setHeaderText("Not all fields filled");
        alert.showAndWait();
    }
}
