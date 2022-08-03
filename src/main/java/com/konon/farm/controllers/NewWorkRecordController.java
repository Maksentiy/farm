package com.konon.farm.controllers;

import com.konon.farm.app.App;
import com.konon.farm.db.Searches;
import com.konon.farm.employee.*;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static javafx.stage.Modality.WINDOW_MODAL;


public class NewWorkRecordController {

    private Work startingWork;

    private List<Product> allProducts;

    private List<Employee> allEmployees;

    @FXML
    private Button Ok;

    @FXML
    private Button cancel;

    private Integer id;

    @FXML
    private ChoiceBox<String> employees;

    @FXML
    private ChoiceBox<String> products;

    @FXML
    private DatePicker date;

    @FXML
    private TextField weight;

    @FXML
    private TextField salary;

    @FXML
    private void initialize() {
        ProductCRUD productCRUD = new ProductCRUD();
        allProducts = productCRUD.getAll();
        List<String> productChoiceBoxList = new ArrayList<>();
        for (Product product: allProducts) {
            productChoiceBoxList.add(product.getName());
        }
        ObservableList<String> productsBox = FXCollections.observableArrayList(productChoiceBoxList);
        products.setItems(productsBox);
        EmployeeCRUD employeeCRUD = new EmployeeCRUD();
        allEmployees = employeeCRUD.getAll();
        List<String> employeesChoiceBoxList = new ArrayList<>();
        for (Employee employee: allEmployees) {
            employeesChoiceBoxList.add(employee.getFirstName() + " " + employee.getSecondName());
        }
        ObservableList<String> employeesBox = FXCollections.observableArrayList(employeesChoiceBoxList);
        employees.setItems(employeesBox);
    }

    @FXML
    private void OnOkButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/workersRecords.fxml"));
        stage.setScene(new Scene(loader.load(), 400, 300));
        stage.setTitle("Employees work");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Harvest newHarvest = new Harvest();
        Work work = new Work();
        try {
            work.setWork_date(Date.valueOf(date.getValue()));
            work.setHarvest_weight(Double.valueOf(weight.getText()));
            work.setSalary(Double.valueOf(salary.getText()));
            newHarvest.setHarvest_date(Date.valueOf(date.getValue()));
            newHarvest.setWeight(Double.valueOf(weight.getText()));
            newHarvest.setExpenses(Double.valueOf(salary.getText()));
        } catch (NumberFormatException e) {
            showAlert();
            return;
        }
        for (Employee employee:allEmployees) {
            if (employees.getValue().equals(employee.getFirstName() + " " + employee.getSecondName())) {
                work.setEmployee(employee);
            }
        }
        for (Product product:allProducts) {
            if (products.getValue().equals(product.getName())) {
                work.setProduct(product);
                newHarvest.setProduct(product);
            }
        }
        Searches searches = new Searches();
        if (fieldsCheck() && id == null) {
            new Thread(new WorkThread("insert", work)).start();
            new Thread(new TextFileThread<Work>("added new harvesting", work)).start();
            Harvest findedHarvest = searches.searchHarvestEqual(newHarvest);
            if (findedHarvest == null) {
                new Thread(new HarvestThread("insert", newHarvest)).start();
                new Thread(new TextFileThread<Harvest>("added new harvest", newHarvest)).start();
            } else {
                findedHarvest.setWeight(findedHarvest.getWeight() + newHarvest.getWeight());
                findedHarvest.setExpenses(findedHarvest.getExpenses() + newHarvest.getExpenses());
                new Thread(new HarvestThread("update", findedHarvest)).start();
                new Thread(new TextFileThread<Harvest>("updated harvest", findedHarvest)).start();
            }
        } else if (fieldsCheck()) {
            work.setId(id);
            new Thread(new WorkThread("update", work)).start();
            new Thread(new TextFileThread<Work>("updated new harvesting", work)).start();
            Harvest findedHarvest = searches.searchHarvestEqual(newHarvest);
            if (findedHarvest == null) {
                new Thread(new HarvestThread("insert", newHarvest)).start();
                new Thread(new TextFileThread<Harvest>("added new harvest", newHarvest)).start();
            } else {
                harvestSolution(findedHarvest, newHarvest, work);
            }
        } else {
            showAnotherAlert();
            return;
        }
        Stage thisStage = (Stage) Ok.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    @FXML
    private void OnCancelButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/workersRecords.fxml"));
        stage.setScene(new Scene(loader.load(), 400, 300));
        stage.setTitle("Employees work");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage thisStage = (Stage) cancel.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    private boolean fieldsCheck() {
        if (employees.getValue().equals("")) {
            return false;
        }
        if (products.getValue().equals("")) {
            return false;
        }
        if (date.getValue().toString().equals("")) {
            return false;
        }
        if (weight.getText().equals("")) {
            return false;
        }
        if (salary.getText().equals("")) {
            return false;
        }

        return true;
    }

    @FXML
    protected void OnTransferData(Work work){
        products.setValue(work.getProduct().getName());
        employees.setValue(work.getEmployee().toString());
        date.setUserData(work.getWork_date());
        weight.setText(work.getHarvest_weight().toString());
        salary.setText(work.getSalary().toString());
        id = work.getId();
        startingWork = work;
    }

    private void harvestSolution(Harvest oldHarvest, Harvest harvest, Work work) {
        if (oldHarvest == null) {
            new Thread(new HarvestThread("insert", harvest)).start();
            new Thread(new TextFileThread<Harvest>("added new harvest", harvest)).start();
        } else {
            if (oldHarvest.getHarvest_date().equals(startingWork.getWork_date()) && startingWork.getProduct().equals(oldHarvest.getProduct())) {
                harvest.setWeight(harvest.getWeight() - (harvest.getWeight() - startingWork.getHarvest_weight()));
                harvest.setExpenses(harvest.getExpenses() - (harvest.getExpenses() - startingWork.getSalary()));
                harvest.setId(oldHarvest.getId());

                if (work.getWork_date().equals(oldHarvest.getHarvest_date()) && harvest.getWeight().equals(0.0)) {
                    new Thread(new HarvestThread("delete", oldHarvest)).start();
                    new Thread(new TextFileThread<Harvest>("deleted harvest", oldHarvest)).start();
                    harvest.setWeight(Double.valueOf(weight.getText()));
                    harvest.setExpenses(Double.valueOf(salary.getText()));
                    new Thread(new HarvestThread("insert", harvest)).start();
                    new Thread(new TextFileThread<Harvest>("added new harvest", harvest)).start();
                } else {
                    new Thread(new HarvestThread("update", harvest)).start();
                    new Thread(new TextFileThread<Harvest>("updated harvest", harvest)).start();
                }
            }
        }
    }

    public static void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ahtung!!!!");
        alert.setHeaderText("Not correct data in fields salary or weight");
        alert.showAndWait();
    }

    public static void showAnotherAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ahtung!!!!");
        alert.setHeaderText("Not all fields filled");
        alert.showAndWait();
    }
}
