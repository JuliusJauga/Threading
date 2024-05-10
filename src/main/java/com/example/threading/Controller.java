package com.example.threading;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    @FXML
    private DatePicker date_picker_end;

    @FXML
    private DatePicker date_picker_start;
    @FXML
    private ListView<String> error_view;
    @FXML
    private ListView<String> thread_view;
    private static final Object monitor = new Object();
    static boolean thread1Ready = false;
    static boolean thread2Ready = false;
    static boolean thread3Ready = false;

    @FXML
    private TableView<Data> table_view;

    private ObservableList<Data> dataBackup = FXCollections.observableArrayList();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table_view.getColumns().clear();
        TableColumn<Data, String> id_column = new TableColumn<>("ID");
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Data, String> first_name_column = new TableColumn<>("First name");
        first_name_column.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        TableColumn<Data, String> last_name_column = new TableColumn<>("Last name");
        last_name_column.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        TableColumn<Data, String> email_column = new TableColumn<>("Email");
        email_column.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Data, String> gender_column = new TableColumn<>("Gender");
        gender_column.setCellValueFactory(new PropertyValueFactory<>("gender"));
        TableColumn<Data, String> country_column = new TableColumn<>("Country");
        country_column.setCellValueFactory(new PropertyValueFactory<>("country"));
        TableColumn<Data, String> domain_name_column = new TableColumn<>("Domain name");
        domain_name_column.setCellValueFactory(new PropertyValueFactory<>("domain_name"));
        TableColumn<Data, String> birth_date_column = new TableColumn<>("Birth date");
        birth_date_column.setCellValueFactory(new PropertyValueFactory<>("birth_date"));
        table_view.getColumns().add(id_column);
        table_view.getColumns().add(first_name_column);
        table_view.getColumns().add(last_name_column);
        table_view.getColumns().add(email_column);
        table_view.getColumns().add(gender_column);
        table_view.getColumns().add(country_column);
        table_view.getColumns().add(domain_name_column);
        table_view.getColumns().add(birth_date_column);
        table_view.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        id_column.sortTypeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == TableColumn.SortType.ASCENDING) {
                List<Data> listSorted = table_view.getItems().stream().sorted(Comparator.comparingInt(Data::getId_int)).toList();
                table_view.getItems().clear();
                table_view.getItems().addAll(listSorted);
            } else if (newValue == TableColumn.SortType.DESCENDING) {
                List<Data> listSorted = table_view.getItems().stream().sorted(Comparator.comparingInt(Data::getId_int)).collect(Collectors.toList());
                Collections.reverse(listSorted);
                table_view.getItems().clear();
                table_view.getItems().addAll(listSorted);
            } else {
                List<Data> listSorted = table_view.getItems().stream().sorted(Comparator.comparingInt(Data::getId_int)).toList();
                table_view.getItems().clear();
                table_view.getItems().addAll(listSorted);
            }
        });
        sortColumProperty(first_name_column, Comparator.comparing(Data::getFirst_name));
        sortColumProperty(last_name_column, Comparator.comparing(Data::getLast_name));
        sortColumProperty(email_column, Comparator.comparing(Data::getEmail));
        sortColumProperty(gender_column, Comparator.comparing(Data::getGender));
        sortColumProperty(country_column, Comparator.comparing(Data::getCountry));
        sortColumProperty(domain_name_column, Comparator.comparing(Data::getDomain_name));
        sortColumProperty(birth_date_column, Comparator.comparing(Data::getBirth_date));
        filterDate(date_picker_start);
        filterDate(date_picker_end);
    }

    private void sortColumProperty(TableColumn<Data, String> birth_date_column, Comparator<Data> comparing) {
        birth_date_column.sortTypeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == TableColumn.SortType.ASCENDING) {
                List<Data> listSorted = table_view.getItems().stream().sorted(comparing).toList();
                table_view.getItems().clear();
                table_view.getItems().addAll(listSorted);
            } else if (newValue == TableColumn.SortType.DESCENDING) {
                List<Data> listSorted = table_view.getItems().stream().sorted(comparing).collect(Collectors.toList());
                Collections.reverse(listSorted);
                table_view.getItems().clear();
                table_view.getItems().addAll(listSorted);
            } else {
                List<Data> listSorted = table_view.getItems().stream().sorted(comparing).toList();
                table_view.getItems().clear();
                table_view.getItems().addAll(listSorted);
            }
        });
    }

    private void filterDate(DatePicker datePickerEnd) {
        datePickerEnd.setOnAction(event -> {
            if (date_picker_start.getValue() != null && date_picker_end.getValue() != null) {
                table_view.getItems().clear();
                for (Data data : dataBackup) {
                    LocalDate date = LocalDate.parse(data.getBirth_date(), dateTimeFormatter);
                    if (date.isAfter(date_picker_start.getValue()) && date.isBefore(date_picker_end.getValue())) {
                        table_view.getItems().add(data);
                    }
                    else if (date.isEqual(date_picker_start.getValue()) || date.isEqual(date_picker_end.getValue())) {
                        table_view.getItems().add(data);
                    }
                }
            }
        });
    }

    @FXML
    void clicked() throws InterruptedException {
        table_view.getItems().clear();
        DataThread thread1 = new DataThread("MOCK_DATA1.csv", this, monitor);
        DataThread thread2 = new DataThread("MOCK_DATA2.csv", this, monitor);
        DataThread thread3 = new DataThread("MOCK_DATA3.csv", this, monitor);
        synchronized (monitor) {
            thread1.start();
            thread1Ready = true;
            while (!thread2Ready) {
                monitor.wait();
            }
            thread2.start();
            while (!thread3Ready) {
                monitor.wait();
            }
            thread3.start();
        }
    }

    public void updateList(List<Data> dataList) {
        table_view.getItems().addAll(dataList);
        dataBackup.addAll(dataList);
    }
    public void addError(String message) {
        error_view.getItems().add(message);
    }
    public void addThread(String message) {
        thread_view.getItems().add(message);
    }
}