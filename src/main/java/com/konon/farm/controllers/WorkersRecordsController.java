package com.konon.farm.controllers;

import com.konon.farm.app.App;
import com.konon.farm.db.Searches;
import com.konon.farm.employee.*;
import com.konon.farm.files.TextFileThread;
import com.konon.farm.product.Harvest;
import com.konon.farm.product.HarvestThread;
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

import java.io.IOException;
import java.sql.Date;

import static javafx.stage.Modality.WINDOW_MODAL;

public class WorkersRecordsController {

    @FXML
    private Button new_record;

    @FXML
    private Button employees;

    @FXML
    private Button update;

    @FXML
    private Button delete;

    @FXML
    private Button products;

    @FXML
    private TableView<Work> table;

    @FXML
    private TableColumn<Work, Integer> id;

    @FXML
    private TableColumn<Employee, String> employee;

    @FXML
    private TableColumn<Product, String> product;

    @FXML
    private TableColumn<Work, Date> work_date;

    @FXML
    private TableColumn<Work, Double> harvest_weight;

    @FXML
    private TableColumn<Work, Double> salary;


    @FXML
    private void initialize() {
        WorkCRUD workCRUD = new WorkCRUD();
        ObservableList<Work> works = FXCollections.observableList(workCRUD.getAll());
        if (!works.isEmpty()) {
            initialData(works);
        }

    }

    @FXML
    private void OnNewRecordButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/newWorkRecord.fxml"));
        stage.setScene(new Scene(loader.load(), 650, 200));
        stage.setTitle("New Record");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage thisStage = (Stage) products.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    @FXML
    private void OnUpdateButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/newWorkRecord.fxml"));
        stage.setScene(new Scene(loader.load(), 650, 200));
        stage.setTitle("Work record Update");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        NewWorkRecordController newWorkRecordController = loader.getController();

        Work selectedWork = table.getSelectionModel().getSelectedItem();
        Searches searches = new Searches();
        newWorkRecordController.OnTransferData(searches.searchWorkEqual(selectedWork));
        Stage thisStage = (Stage) products.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    @FXML
    private void OnDeleteButtonClick(ActionEvent event) throws IOException{
        Work selectedWork = table.getSelectionModel().getSelectedItem();
        WorkCRUD workCRUD = new WorkCRUD();
        Searches searches = new Searches();
        Work work = searches.searchWorkEqual(selectedWork);
        workCRUD.delete(work.getId());
        new Thread(new WorkThread("delete", work)).start();
        new Thread(new TextFileThread<Work>("deleted harvesting", work)).start();
        updateHarvest(work);
    }

    @FXML
    private void OnProductsButtonClick(ActionEvent event) throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/harvesting.fxml"));
        newStage.setScene(new Scene(loader.load(), 557, 400));
        newStage.setTitle("Harvesting");
        newStage.initModality(WINDOW_MODAL);
        newStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage thisStage = (Stage) products.getScene().getWindow();
        thisStage.close();
        newStage.show();
    }

    @FXML
    private void OnEmployeesButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/employees.fxml"));
        stage.setScene(new Scene(loader.load(), 310, 400));
        stage.setTitle("Employees");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage thisStage = (Stage) products.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    @FXML
    private void OnReloadButtonClick(ActionEvent event) throws IOException {
        WorkCRUD workCRUD = new WorkCRUD();
        ObservableList<Work> works = FXCollections.observableList(workCRUD.getAll());
        if (!works.isEmpty()) {
            initialData(works);
        }
    }

    private void initialData(ObservableList<Work> works) {
        id.setCellValueFactory(new PropertyValueFactory<Work, Integer>("id"));
        employee.setCellValueFactory(new PropertyValueFactory<Employee, String>("employee"));
        product.setCellValueFactory(new PropertyValueFactory<Product, String>("product"));
        work_date.setCellValueFactory(new PropertyValueFactory<Work, Date>("work_date"));
        harvest_weight.setCellValueFactory(new PropertyValueFactory<Work, Double>("harvest_weight"));
        salary.setCellValueFactory(new PropertyValueFactory<Work, Double>("salary"));

        id.setCellFactory(t -> {
            TableCell<Work, Integer> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(id.widthProperty());
            text.textProperty().bind(cell.textProperty());
            return cell;
        });

        table.setItems(works);
    }

    private void updateHarvest(Work work) {
        Harvest newHarvest = new Harvest();
        newHarvest.setHarvest_date(work.getWork_date());
        newHarvest.setProduct(work.getProduct());
        Searches searches = new Searches();
        Harvest harvest = searches.searchHarvestEqual(newHarvest);
        if (harvest.getHarvest_date().equals(work.getWork_date()) && harvest.getProduct().equals(work.getProduct())) {
            harvest.setWeight(harvest.getWeight() - work.getHarvest_weight());
            harvest.setExpenses(harvest.getExpenses() - work.getSalary());
            if (harvest.getWeight().equals(0.0)) {
                new Thread(new HarvestThread("delete", harvest)).start();
                new Thread(new TextFileThread<Harvest>("deleted harvest", harvest)).start();
            } else {
                new Thread(new HarvestThread("update", harvest)).start();
                new Thread(new TextFileThread<Harvest>("updated harvest", harvest)).start();
            }
        }
    }
}
