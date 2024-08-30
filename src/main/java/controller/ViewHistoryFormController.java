package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Task;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewHistoryFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colCompletedDate;

    @FXML
    private TableColumn<?, ?> colTaskDescription;

    @FXML
    private TableColumn<?, ?> colTaskId;

    @FXML
    private TableView<Task> tblTskHistory;

    @FXML
    private JFXTextField txtSearchByDate;

    TaskService taskService = new TaskController();

    @FXML
    void btnBackToHomeOnAction(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        List<Task> taskList = taskService.getAllCompletedTasks("TaskCompleteDate");
        ObservableList<Task> taskObservableList = FXCollections.observableArrayList();
        taskObservableList.addAll(taskList);
        tblTskHistory.setItems(taskObservableList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colTaskId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTaskDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCompletedDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        loadTable();
    }

    private void loadTable() {
        List<Task> taskList = taskService.getAllCompletedTasks("TaskID");
        ObservableList<Task> taskObservableList = FXCollections.observableArrayList();
        taskObservableList.addAll(taskList);
        tblTskHistory.setItems(taskObservableList);
    }
}
