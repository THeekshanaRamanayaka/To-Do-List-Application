package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskController implements TaskService {
    @Override
    public boolean addTask(Task task) {
        try{
            String SQL = "INSERT INTO ActiveTasks VALUES(?,?,?)";
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setObject(1,task.getId());
            pstm.setObject(2,task.getDescription());
            pstm.setObject(3, Date.valueOf(task.getDate()));
            return pstm.executeUpdate()>0;
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Error : "+e.getMessage()).show();
        }
        return false;
    }

    @Override
    public ObservableList<Task> getAll() {
        ObservableList<Task> taskObservableList = FXCollections.observableArrayList();
        try{
            String SQL = "SELECT * FROM ActiveTasks ORDER BY TaskID ASC";
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                Task task = new Task(
                        resultSet.getString("TaskID"),
                        resultSet.getString("TaskDescription"),
                        resultSet.getDate("TaskCreateDate").toLocalDate()
                );
                taskObservableList.add(task);
            }
            return taskObservableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void completedTasks(Task task) {
        String SQL = "INSERT INTO CompletedTasks (TaskID, TaskDescription, TaskCompleteDate) VALUES (?,?,?)";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setObject(1,task.getId());
            pstm.setObject(2,task.getDescription());
            pstm.setObject(3,Date.valueOf(task.getDate()));
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        deleteCompletedTasks(task.getId());
    }

    private void deleteCompletedTasks(String id) {
        String SQL = "DELETE FROM ActiveTasks WHERE TaskID = ?";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setObject(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<Task> getAllCompletedTasks() {
        ObservableList<Task> taskObservableList = FXCollections.observableArrayList();
        try{
            String SQL = "SELECT * FROM CompletedTasks ORDER BY TaskID ASC";
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                Task task = new Task(
                        resultSet.getString("TaskID"),
                        resultSet.getString("TaskDescription"),
                        resultSet.getDate("TaskCompleteDate").toLocalDate()
                );
                taskObservableList.add(task);
            }
            return taskObservableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Task> getAllCompletedTasks(String columnName) {
        String SQL = "SELECT * FROM CompletedTasks ORDER BY '" + columnName + "' ASC";
        List<Task> taskList = new ArrayList<>();
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                Task task = new Task(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDate(3).toLocalDate()
                );
                taskList.add(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return taskList;
    }
}
