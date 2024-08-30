package controller;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainWindowControllerForm implements Initializable {

    @FXML
    private JFXListView<String> completedList;

    @FXML
    private JFXListView<String> myToDoList;

    TaskService taskService = new TaskController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activeTaskView();
        completedView();
        myToDoList.setCellFactory(lv -> new ListCell<String>(){
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item,empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                }else{
                    checkBox.setText(item);
                    setGraphic(checkBox);

                    checkBox.setOnAction(event -> {
                        if (checkBox.isSelected()) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                            alert.setContentText("Are you completed the task?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.YES) {
                                for (int i = 0; i < taskService.getAll().size(); i++) {
                                    if (taskService.getAll().get(i).getDescription().equalsIgnoreCase(item)) {
                                        taskService.completedTasks(taskService.getAll().get(i));
                                        checkBox.setSelected(false);
                                        myToDoList.getItems().remove(item);
                                    }
                                }
                                completedView();
                            }else{
                                checkBox.setSelected(false);
                                alert.close();
                            }
                        }else{
                            checkBox.setSelected(false);
                        }
                    });
                }
            }
        });
    }

    private void completedView() {
        ObservableList<String> completeItems = FXCollections.observableArrayList();
        for (int i = 0; i < taskService.getAllCompletedTasks().size(); i++) {
            completeItems.add(taskService.getAllCompletedTasks().get(i).getDescription());
        }
        completedList.setItems(completeItems);
        completedList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

        });
    }

    public void activeTaskView(){
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < taskService.getAll().size(); i++) {
            items.add(taskService.getAll().get(i).getDescription());
        }
        myToDoList.setItems(items);
        myToDoList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

        });
    }

    @FXML
    void btnAddTaskOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/add_to_do_task_form.fxml"))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnHistoryOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/view_history_form.fxml"))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
