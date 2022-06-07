package org.Tumanyan.dao;


import org.Tumanyan.entity.File;
import org.Tumanyan.entity.Role;
import org.Tumanyan.entity.User;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Класс отвечает за логику взаимодействия пользователя с БД
@Component
public class UserDao {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String USERNAME = "DEMO";
    private static final String PASSWORD = "12345";

    private static Connection connection;

    //Создание соединения с БД
    private void openConnection() {
        //Загружаем реализацию JDBC
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //Подключение к БД
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Закрытие соединения с БД
    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Получение списка должностей сотрудников
    public List<Role> showRoles() {

        List<Role> roles = new ArrayList<>();

        try {
            openConnection();
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM EMPLOYEES_ROLES";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Role role = new Role();
                role.setRoleName(resultSet.getString("NAME_ROLE"));
                roles.add(role);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return roles;
    }

    //Получение файлов в статусе "Принят"
    public List<File> showAcceptFiles() {
        List<File> fileList = new ArrayList<>();

        try {
            openConnection();
            Statement statement = connection.createStatement();
            String SQL = "SELECT ID_FILE,c.ID_CONTRACT,STATUS,f.ID_EMPLOYEE,FIO,LATITUDE,LONGITUDE,FLINK,c.YEAR FROM FILES f JOIN CONTRACT c " +
                    "ON f.id_contract=c.id_contract JOIN EMPLOYEE e ON f.id_employee =e.id_employee WHERE STATUS = 'Принят'";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                File file = new File();
                file.setId_file(resultSet.getInt("ID_FILE"));
                file.setId_contract(Integer.toString(resultSet.getInt("ID_CONTRACT")));
                file.setId_employee(resultSet.getInt("ID_EMPLOYEE"));
                file.setEmployeeName(resultSet.getString("FIO"));
                file.setLatitude(resultSet.getString("LATITUDE"));
                file.setLongitude(resultSet.getString("LONGITUDE"));
                file.setfLink(resultSet.getString("FLINK"));
                file.setYear(resultSet.getInt("YEAR"));
                fileList.add(file);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return fileList;
    }

    //Получение файлов в статусе "Обрабатывается"
    public List<File> showProcessingFiles() {
        List<File> fileList = new ArrayList<>();
        try {
            openConnection();
            Statement statement = connection.createStatement();
            String SQL = "SELECT ID_FILE,f.ID_EMPLOYEE,FIO,FLINK,STATUS FROM FILES f JOIN CONTRACT c " +
                    "ON f.id_contract=c.id_contract JOIN EMPLOYEE e ON f.id_employee =e.id_employee WHERE STATUS = 'Обрабатывается'";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                File file = new File();
                file.setId_file(resultSet.getInt("ID_FILE"));
                file.setId_employee(resultSet.getInt("ID_EMPLOYEE"));
                file.setEmployeeName(resultSet.getString("FIO"));
                file.setfLink(resultSet.getString("FLINK"));
                file.setStatus(resultSet.getString("STATUS"));
                fileList.add(file);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return fileList;
    }


    //Получение файлов в статусе "Обрабатывается" или "Не принят"
    public List<File> showProcessingRejectFiles() {
        List<File> fileList = new ArrayList<>();
        try {
            openConnection();
            Statement statement = connection.createStatement();
            String SQL = "" +
                    "SELECT ID_FILE,f.ID_EMPLOYEE,FIO,FLINK,STATUS " +
                    "FROM FILES f " +
                    "   JOIN CONTRACT c " +
                    "       ON f.id_contract=c.id_contract " +
                    "   JOIN EMPLOYEE e" +
                    "       ON f.id_employee =e.id_employee " +
                    "WHERE (STATUS = 'Обрабатывается'" +
                    "   OR STATUS = 'Не принят') " +
                    "   AND e.ISACTIVE = 1";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                File file = new File();
                file.setId_file(resultSet.getInt("ID_FILE"));
                file.setId_employee(resultSet.getInt("ID_EMPLOYEE"));
                file.setEmployeeName(resultSet.getString("FIO"));
                file.setfLink(resultSet.getString("FLINK"));
                file.setStatus(resultSet.getString("STATUS"));
                fileList.add(file);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return fileList;
    }


    //Обновление статуса файла
    public void updateStatus(File file) {
        try {
            openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE DEMO.FILES SET STATUS = ? WHERE ID_FILE = ?");
            preparedStatement.setString(1, file.getStatus());
            preparedStatement.setInt(2, file.getId_file());

            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    //Добавление файла в БД
    public void uploadFile(File file) {
        try {
            openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CONTRACT (ID_CONTRACT,CUSTOMER,TITLEWORK,YEAR,LONGITUDE,LATITUDE) " +
                    "VALUES (?,?,?,?,?,?)");
            preparedStatement.setInt(1, file.getId_contract());
            preparedStatement.setString(2, file.getCustomerName());
            preparedStatement.setString(3, file.getTitleWork());
            preparedStatement.setInt(4, file.getYear());
            preparedStatement.setString(5, file.getLongitude());
            preparedStatement.setString(6, file.getLatitude());
            preparedStatement.executeUpdate();


            preparedStatement = connection.prepareStatement("SELECT ID_EMPLOYEE FROM EMPLOYEE  WHERE ISACTIVE = 1");
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            int id = resultSet.getInt("ID_EMPLOYEE");

            preparedStatement = connection.prepareStatement("INSERT INTO FILES (ID_CONTRACT,ID_TYPE,ID_FORMAT,STATUS,FLINK,ID_EMPLOYEE) VALUES (?,1,1,'Обрабатывается',?,?)");
            preparedStatement.setInt(1, file.getId_contract());
            preparedStatement.setString(2, file.getfLink());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    //Занесение данных о созданном сотруднике в БД
    public void save(User user) {
        try {
            openConnection();
            // Вложенный Select ищет id должности по названию
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO EMPLOYEE (FIO,ID_Role,Login,Password) " +
                    "VALUES (?,(SELECT ID_ROLE FROM EMPLOYEES_ROLES WHERE NAME_ROLE =?),?,?)");

            preparedStatement.setString(1, user.getFio());
            preparedStatement.setString(2, user.getRole().getRoleName());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    // Проверка присутствия файла в БД
    public boolean checkFileInDb(File file) {
        try {
            openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) from CONTRACT WHERE ID_CONTRACT = ?");
            preparedStatement.setInt(1, file.getId_contract());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getInt(1) == 1) {
                    return true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection);
        }

        return false;
    }

    //Установка активного пользователя
    public void setActiveUser(User user) {
        try {
            openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE DEMO.EMPLOYEE Set ISACTIVE = " + 0);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("UPDATE DEMO.EMPLOYEE Set ISACTIVE= " + 1 + " WHERE " +
                    "LOGIN = ? AND ID_ROLE=(SELECT ID_ROLE FROM EMPLOYEES_ROLES WHERE NAME_ROLE = ?)");
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getRole().getRoleName());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    // Проверка присутствия пользователя в БД
    public boolean checkUserInDb(User user) {
        try {
            openConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) from EMPLOYEE WHERE " +
                    "LOGIN = ? AND ID_ROLE=(SELECT ID_ROLE FROM EMPLOYEES_ROLES WHERE NAME_ROLE = ?)");

            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getRole().getRoleName());
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                if (resultSet.getInt(1) == 1) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return false;
    }


    //Проверка верность пароля
    public boolean checkPassword(User user) {
        try {
            openConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT PASSWORD from EMPLOYEE WHERE " +
                    "LOGIN = ? AND ID_ROLE=(SELECT ID_ROLE FROM EMPLOYEES_ROLES WHERE NAME_ROLE = ?)");

            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getRole().getRoleName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getString(1).equals(user.getPassword())) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return false;
    }

}
