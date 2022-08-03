package com.konon.farm.employee;

public class EmployeeThread implements Runnable{

    private String msg;

    private Employee employee;

    public EmployeeThread(String msg, Employee employee) {
        this.msg = msg;
        this.employee = employee;
    }

    @Override
    public void run() {
        EmployeeCRUD employeeCRUD = new EmployeeCRUD();
        switch (msg) {
            case "insert" -> {
                employeeCRUD.insert(employee);
            }
            case "update" -> {
                employeeCRUD.update(employee);
            }
            case "delete" -> {
                employeeCRUD.delete(employee.getId());
            }
        }
    }
}
