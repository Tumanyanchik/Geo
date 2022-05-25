package org.Tumanyan.services;

import org.Tumanyan.dao.UserDao;
import org.Tumanyan.entity.File;
import org.Tumanyan.entity.Role;
import org.Tumanyan.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserService {

    private final UserDao userDao;
    private final DiskService diskService;

    UserService(UserDao userDao, DiskService diskService) {
        this.diskService = diskService;
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

    //Получение файлов в статусе "Обрабатывается"
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
    public boolean checkFileInDb(File file) {
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

    public void setActiveUser(User user) {
        userDao.setActiveUser(user);
    }

    //Получение файлов в статусе "Обрабатывается" или "Не принят"
    public List<File> showProcessingRejectFiles() {
        return userDao.showProcessingRejectFiles();
    }

    // Скачивание файла (Обрабатывающиеся)
    public void downloadProccessedFile(File file) {
        List<File> fileList = showProcessingFiles();
        Optional<File> optionalFile = fileList.stream()
                .filter(f -> f.getId_file() == file.getId_file())
                .findFirst();
        if (!optionalFile.isPresent())
            return;
        diskService.getFile("C:\\Users\\Artem\\downloads\\" + optionalFile.get().getId_file() + ".doc", optionalFile.get().getfLink());
    }

    public void downloadSFile(File file) {
        List<File> fileList = showAcceptFiles();
        Optional<File> optionalFile = fileList.stream()
                .filter(f -> f.getId_file() == file.getId_file())
                .findFirst();
        if (!optionalFile.isPresent())
            return;
        diskService.getFile("C:\\Users\\Artem\\downloads\\" + optionalFile.get().getId_file() + ".doc", optionalFile.get().getfLink());
    }

    // Скачивание файла (Обрабатывающиеся и непринятые)
    public void downloadProcessingRejectFile(File file) {
        List<File> fileList = showProcessingRejectFiles();
        Optional<File> optionalFile = fileList.stream()
                .filter(f -> f.getId_file() == file.getId_file())
                .findFirst();
        if (!optionalFile.isPresent())
            return;
        diskService.getFile("C:\\Users\\Artem\\downloads\\" + optionalFile.get().getId_file() + ".doc", optionalFile.get().getfLink());
    }

    //Выбор файла
    public void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        JFrame frame = new JFrame("Выбор");
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            // save to file
        }
    }
}
