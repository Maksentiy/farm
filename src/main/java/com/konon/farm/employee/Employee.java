package com.konon.farm.employee;

import java.util.Objects;

public class Employee {

    private Integer id;

    private String firstName;

    private String secondName;

    private String position;

    private String pasport_id;

    public Employee() {
    }

    public Employee(String firstName, String secondName, String position, String pasport_id) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.position = position;
        this.pasport_id = pasport_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPasport_id() {
        return pasport_id;
    }

    public void setPasport_id(String pasport_id) {
        this.pasport_id = pasport_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id.equals(employee.id) && firstName.equals(employee.firstName) && secondName.equals(employee.secondName) && position.equals(employee.position) && pasport_id.equals(employee.pasport_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, secondName, position, pasport_id);
    }

    @Override
    public String toString() {
        return firstName + " " + secondName;
    }
}
