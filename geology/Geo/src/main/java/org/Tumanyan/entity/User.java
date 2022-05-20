package org.Tumanyan.entity;



import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;

public class User {
    @NotEmpty(message = "Поле Login не должно быть пустым")
    @Email(message = "Поле Login должно быть действительным")
    private String login;
    @NotEmpty (message = "Поле Password не должно быть пустым")
    @Size(min=4,max=30,message = "Поле Password должно содержать от 4 до 30 символов")
    private String password;

    @NotEmpty (message = "Поле FIO не должно быть пустым")
    @Size(min=4,max=30,message = "Поле FIO должно содержать от 4 до 30 символов")
    private String fio;
    private Role role;


    public User(){

    }

    public User(String login, String password, String fio, Role role) {
        this.login = login;
        this.password = password;
        this.fio = fio;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
