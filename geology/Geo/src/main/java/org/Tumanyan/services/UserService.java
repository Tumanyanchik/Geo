package org.Tumanyan.services;

import org.Tumanyan.dao.UserDao;
import org.Tumanyan.entity.File;
import org.Tumanyan.entity.Role;
import org.Tumanyan.entity.User;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;

@Component
public class UserService {

    private final UserDao userDao;

    UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    //Получение списка должностей сотрудников
    public List<Role> showRoles() {
        return userDao.showRoles();
    }

    //Получение файлов в статусе "Принят"
    public List<File> showAcceptFiles() {
        return userDao.showAcceptFiles();
    }

    //Получение файлов в статусе "Обрабатывается" или "Не принят"
    public List<File> showProcessingFiles() {
        return userDao.showProcessingFiles();
    }

    //Обновление статуса файла
    public void updateStatus(File file) {
        userDao.updateStatus(file);
    }

    //Добавление файла в БД
    public void uploadFile(File file) {
        userDao.uploadFile(file);
    }

    //Занесение данных о созданном сотруднике в БД
    public void save(User user) {
        userDao.save(user);
    }

    // Проверка присутствия файла в БД
    public boolean checkFileInDb(File file){
        return userDao.checkFileInDb(file);
    }
    // Проверка присутствия пользователя в БД
    public boolean checkUserInDb(User user) {
        return userDao.checkUserInDb(user);
    }

    //Проверка верность пароля
    public boolean checkPassword(User user) {
        return userDao.checkPassword(user);
    }

    //Выбор файла
    public void chooseFile(){
        JFileChooser fileChooser = new JFileChooser();
        JFrame frame = new JFrame("Выбор");
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            // save to file
        }
    }
}
