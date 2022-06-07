package org.Tumanyan.entity;

import org.Tumanyan.Annotations.CheckInt;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;

public class File {
    @Size(min=4,max=30,message = "Поле Долгота должно содержать от 4 до 30 символов")
    private String longitude;
    @Size(min=4,max=30,message = "Поле Широта должно содержать от 4 до 30 символов")
    private String latitude;
    @Size(min=4,max=30,message = "Поле Сотрудник должно содержать от 4 до 30 символов")
    private String employeeName;
    @Size(min=4,max=30,message = "Поле Заказчик должно содержать от 4 до 30 символов")
    private String customerName;
    private String fLink;
    private String status;
    @Size(min=4,max=30,message = "Поле Тип работ должно содержать от 4 до 30 символов")
    private String titleWork;
    private String location;
    private String district;


    private int id_file;
    @NotNull(message = "Поле №дела не должно быть пустым")
    @Positive(message = "Поле №дела должно быть положительным")
    @Min(value = 999,message = "Поле №дела должно быть больше 999")
    @Max(value = 9999999,message = "Поле №дела должно быть меньше 9999999")
    private int id_contract;
    private int id_employee;
    @NotNull(message = "Поле Год не должно быть пустым")
    @Digits(message = "Поле Год работ должно содержать 4 символа", integer = 4, fraction = 0)
    @Min(value = 1920,message = "Поле Год должно быть больше 1920")
    @Max(value = 2022,message = "Поле Год должно быть меньше 2023")
    private int year;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitleWork() {
        return titleWork;
    }

    public void setTitleWork(String titleWork) {
        this.titleWork = titleWork;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getfLink() {
        return fLink;
    }

    public void setfLink(String fLink) {
        this.fLink = fLink;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getId_file() {
        return id_file;
    }

    public void setId_file(int id_file) {
        this.id_file = id_file;
    }

    public int getId_employee() {
        return id_employee;
    }

    public void setId_employee(int employee) {
        this.id_employee = employee;
    }

    public int getId_contract() {
        return id_contract;
    }

    public void setId_contract(String id_contract) {
        try{
            this.id_contract = Integer.parseInt(id_contract);
       //     this.id_contract =id_contract;
        }catch (Exception ex){

        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
