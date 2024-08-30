package controller;

import javafx.collections.ObservableList;
import model.Task;

import java.util.List;

public interface TaskService {
    boolean addTask(Task task);

    ObservableList<Task> getAll();

    void completedTasks(Task task);

    ObservableList<Task> getAllCompletedTasks();

    List<Task> getAllCompletedTasks(String columnName);
}
