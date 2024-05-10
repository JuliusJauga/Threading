package com.example.threading;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Platform;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DataThread extends Thread {
    private final Object monitor;
    private final String fileName;
    private final Controller controller;

    public DataThread(String fileName, Controller controller, Object monitor) {
        this.fileName = fileName;
        this.controller = controller;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        synchronized (monitor) {
            while (!IsThreadReady()) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        switch(fileName) {
            case "MOCK_DATA1.csv" -> Platform.runLater(() -> controller.addThread("Thread 1 begins work " + LocalTime.now()));
            case "MOCK_DATA2.csv" -> Platform.runLater(() -> controller.addThread("Thread 2 begins work " + LocalTime.now()));
            case "MOCK_DATA3.csv" -> Platform.runLater(() -> controller.addThread("Thread 3 begins work " + LocalTime.now()));
        }
        List<Data> dataList = new ArrayList<>();
        try {
            dataList = readCSV();
        } catch (IOException e) {
            switch(fileName) {
                case "MOCK_DATA1.csv" -> Platform.runLater(() -> controller.addError("Thread 1 failed to open file " + LocalTime.now()));
                case "MOCK_DATA2.csv" -> Platform.runLater(() -> controller.addError("Thread 2 failed to open file " + LocalTime.now()));
                case "MOCK_DATA3.csv" -> Platform.runLater(() -> controller.addError("Thread 3 failed to open file " + LocalTime.now()));
            }
        }
        List<Data> finalDataList = dataList;
        Platform.runLater(() -> controller.updateList(finalDataList));
        synchronized (monitor) {
            notifyNextThread();
        }
        System.out.println("thread finished work with " + fileName);
        switch(fileName) {
            case "MOCK_DATA1.csv" -> Platform.runLater(() -> controller.addThread("Thread 1 ends work " + LocalTime.now()));
            case "MOCK_DATA2.csv" -> Platform.runLater(() -> controller.addThread("Thread 2 ends work " + LocalTime.now()));
            case "MOCK_DATA3.csv" -> Platform.runLater(() -> controller.addThread("Thread 3 ends work " + LocalTime.now()));
        }
    }

    public List<Data> readCSV() throws IOException {
        List<Data> dataList = new ArrayList<>();
        String id;
        String first_name;
        String last_name;
        String email;
        String gender;
        String country;
        String domain_name;
        String birth_date;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            reader.readNext();
            String[] rowData;
            while ((rowData = reader.readNext()) != null) {
                id = rowData[0];
                first_name = rowData[1];
                last_name = rowData[2];
                email = rowData[3];
                gender = rowData[4];
                country = rowData[5];
                domain_name = rowData[6];
                birth_date = rowData[7];
                dataList.add(new Data(id, first_name, last_name, email, gender, country, domain_name, birth_date));
            }
        } catch (Exception e) {
            switch(fileName) {
                case "MOCK_DATA1.csv" -> Platform.runLater(() -> controller.addError("Thread 1 file format incorrect " + LocalTime.now()));
                case "MOCK_DATA2.csv" -> Platform.runLater(() -> controller.addError("Thread 2 file format incorrect " + LocalTime.now()));
                case "MOCK_DATA3.csv" -> Platform.runLater(() -> controller.addError("Thread 3 file format incorrect " + LocalTime.now()));
            }
        }
        return dataList;
    }
    private boolean IsThreadReady() {
        return switch (fileName) {
            case "MOCK_DATA1.csv" -> Controller.thread1Ready;
            case "MOCK_DATA2.csv" -> Controller.thread2Ready;
            case "MOCK_DATA3.csv" -> Controller.thread3Ready;
            default -> false;
        };
    }
    private void notifyNextThread() {
        if (fileName.equals("MOCK_DATA1.csv")) {
            Controller.thread1Ready = true;
            Controller.thread2Ready = true;
            Controller.thread3Ready = false;
        } else if (fileName.equals("MOCK_DATA2.csv")) {
            Controller.thread1Ready = true;
            Controller.thread2Ready = true;
            Controller.thread3Ready = true;
        } else if (fileName.equals("MOCK_DATA3.csv")) {
            Controller.thread1Ready = false;
            Controller.thread2Ready = false;
            Controller.thread3Ready = false;
        }
        monitor.notifyAll();
    }
}
