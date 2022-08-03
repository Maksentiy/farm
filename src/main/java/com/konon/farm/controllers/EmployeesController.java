package com.konon.farm.controllers;

import com.konon.farm.app.App;
import com.konon.farm.employee.*;
import com.konon.farm.files.TextFileThread;
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

public class EmployeesController {

    @FXML
    private Button add;

    @FXML
    private Button back;

    @FXML
    private TableView<Employee> table;

    @FXML
    private TableColumn<Employee, String> firstName;

    @FXML
    private TableColumn<Employee, String> secondName;

    @FXML
    private TableColumn<Employee, String> position;

    @FXML
    private TableColumn<Employee, String> pasport_id;


    @FXML
    private void initialize() {
        EmployeeCRUD employeeCRUD = new EmployeeCRUD();
        ObservableList<Employee> employees = FXCollections.observableList(employeeCRUD.getAll());
        if (!employees.isEmpty()) {
            initialData(employees);
        }

    }

    @FXML
    private void OnAddButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/newEmployee.fxml"));
        stage.setScene(new Scene(loader.load(), 400, 200));
        stage.setTitle("New Employee");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage thisStage = (Stage) add.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    @FXML
    private void OnBackButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/workersRecords.fxml"));
        stage.setScene(new Scene(loader.load(), 400, 300));
        stage.setTitle("Employees work");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage thisStage = (Stage) back.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    @FXML
    private void OnUpdateButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/newEmployee.fxml"));
        stage.setScene(new Scene(loader.load(), 400, 200));
        stage.setTitle("Employee Update");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        NewEmployeeController newEmployeeController = loader.getController();
        Employee selectedEmployee = table.getSelectionModel().getSelectedItem();
        EmployeeCRUD employeeCRUD = new EmployeeCRUD();
        List<Employee> all = employeeCRUD.getAll();
        for (Employee employee:all) {
            if (employee.equals(selectedEmployee)) {
                newEmployeeController.OnTransferData(selectedEmployee);
                break;
            }
        }
        Stage thisStage = (Stage) add.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    @FXML
    private void OnDeleteButtonClick(ActionEvent event) throws IOException{
        Employee selectedEmployee = table.getSelectionModel().getSelectedItem();
        EmployeeCRUD employeeCRUD = new EmployeeCRUD();
        List<Employee> all = employeeCRUD.getAll();
        for (Employee employee:all) {
            if (employee.equals(selectedEmployee)) {
                if (searchUsage(employee)) {
                    new Thread(new EmployeeThread("delete", employee)).start();
                    new Thread(new TextFileThread<Employee>("deleted", employee)).start();
                } else {
                    showAlert(employee.getFirstName() + " " + employee.getSecondName());
                }
                break;
            }
        }
    }

    @FXML
    private void OnReloadButtonClick(ActionEvent event) throws IOException {
        EmployeeCRUD employeeCRUD = new EmployeeCRUD();
        ObservableList<Employee> employees = FXCollections.observableList(employeeCRUD.getAll());
        if (!employees.isEmpty()) {
            initialData(employees);
        }
    }

    private void initialData(ObservableList<Employee> employees) {
        firstName.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        secondName.setCellValueFactory(new PropertyValueFactory<Employee, String>("secondName"));
        position.setCellValueFactory(new PropertyValueFactory<Employee, String>("position"));
        pasport_id.setCellValueFactory(new PropertyValueFactory<Employee, String>("pasport_id"));

        firstName.setCellFactory(t -> {
            TableCell<Employee, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(firstName.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        table.setItems(employees);
    }
    private boolean searchUsage(Employee employee) {
        WorkCRUD workCRUD = new WorkCRUD();
        List<Work> workList = workCRUD.getAll();
        for (Work work : workList) {
            if (work.getProduct().getId().equals(employee.getId())) {
                return false;
            }
        }
        return true;
    }

    public static void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ahtung!!!!");
        alert.setHeaderText("Employee " + msg + " using in another tables");
        alert.showAndWait();
    }
}
