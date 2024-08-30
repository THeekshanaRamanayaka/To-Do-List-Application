package controller;

import com.jfoenix.controls.JFXTextArea;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Task;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddToDoTaskFormController implements Initializable {

    @FXML
    private DatePicker date;

    @FXML
    private JFXTextArea txtAreaAddDescription;

    @FXML
    private Label lblTaskId;

    TaskService service = new TaskController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String id = idGenerator();
        lblTaskId.setText(id);
        date.setValue(LocalDate.now());
    }

    private String idGenerator() {
        int count = 0;
        try {
            String SQL = "Select * from ActiveTasks";
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery(SQL);
            while(rst.next()){
                count++;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        if (count == 0){
            return "T0001";
        }
        return String.format("T%04d",count+1);
    }

    @FXML
    void btnAddTaskOnAction(ActionEvent event) {
        String description = txtAreaAddDescription.getText();
        if(description.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Description required !").show();
        }

        Task task = new Task(
                lblTaskId.getText(),
                txtAreaAddDescription.getText(),
                date.getValue()
        );
        if(service.addTask(task)){
            new Alert(Alert.AlertType.INFORMATION,"Task Added Successfully !!").show();
            lblTaskId.setText(idGenerator());
            txtAreaAddDescription.setText("");
            date.setValue(LocalDate.now());
        }else{
            new Alert(Alert.AlertType.ERROR,"Task Not Added :(").show();
        }
    }

    @FXML
    void btnBackToHomeOnAction(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
