package com.konon.farm.controllers;

import com.konon.farm.app.App;
import com.konon.farm.employee.Employee;
import com.konon.farm.employee.EmployeeCRUD;
import com.konon.farm.employee.EmployeeThread;
import com.konon.farm.files.TextFileThread;
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

public class NewEmployeeController {

    private Integer id;

    @FXML
    private Button ok;

    @FXML
    private Button cancel;

    @FXML
    private TextField firstName;

    @FXML
    private TextField secondName;

    @FXML
    private TextField position;

    @FXML
    private TextField pasport_id;

    @FXML
    private void initialize() {

    }

    @FXML
    private void OnOkButtonClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/employees.fxml"));
        stage.setScene(new Scene(loader.load(), 310, 400));
        stage.setTitle("Employees");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Employee employee = new Employee(firstName.getText(), secondName.getText(), position.getText(), pasport_id.getText());
        EmployeeCRUD employeeCRUD = new EmployeeCRUD();
        if (id == null && employeeCRUD.search(employee.getPasport_id())) {
            showIdAlert();
            return;
        }
        if (fieldsCheck() && id == null) {
            new Thread(new EmployeeThread("insert", employee)).start();
            new Thread(new TextFileThread<Employee>("added new employee", employee)).start();
        } else if (fieldsCheck()) {
            employee.setId(id);
            new Thread(new EmployeeThread("update", employee)).start();
            new Thread(new TextFileThread<Employee>("updated employee", employee)).start();
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
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/konon/farm/employees.fxml"));
        stage.setScene(new Scene(loader.load(), 310, 400));
        stage.setTitle("Employees");
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        Stage thisStage = (Stage) cancel.getScene().getWindow();
        thisStage.close();
        stage.show();
    }

    private boolean fieldsCheck() {
        if (firstName.getText().equals("")) {
            return false;
        }
        if (secondName.getText().equals("")) {
            return false;
        }
        if (position.getText().equals("")) {
            return false;
        }
        if (pasport_id.getText().equals("")) {
            return false;
        }

        return true;
    }

    @FXML
    protected void OnTransferData(Employee employee){
        firstName.setText(employee.getFirstName());
        secondName.setText(employee.getSecondName());
        position.setText(employee.getPosition());
        pasport_id.setText(employee.getPasport_id());
        id = employee.getId();
    }

    public static void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ahtung!!!!");
        alert.setHeaderText("Not all fields filled");
        alert.showAndWait();
    }

    public static void showIdAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ahtung!!!!");
        alert.setHeaderText("Employee with same pasport id already exist");
        alert.showAndWait();
    }
}
