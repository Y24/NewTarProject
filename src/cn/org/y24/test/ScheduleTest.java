/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScheduleTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(final Stage stage) {
        final Label statusLabel = new Label("Status");
        final Button runButton = new Button("Run");
        final ListView<String> peopleView = new ListView<>();
        peopleView.setPrefSize(220, 162);
        final ProgressBar progressBar = new ProgressBar();
        progressBar.prefWidthProperty().bind(peopleView.prefWidthProperty());

        runButton.setOnAction(actionEvent -> {
            final Task<ObservableList<String>> task = new Task<>() {
                @Override
                protected ObservableList<String> call() throws InterruptedException {
                    updateMessage("Finding friends . . .");
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(200);
                        updateProgress(i + 1, 10);
                    }
                    updateMessage("Finished.");
                    return FXCollections.observableArrayList("John", "Jim", "Geoff", "Jill", "Suki");
                }
//          @Override protected void done() {
//            super.done();
//            System.out.println("This is bad, do not do this, this thread " + Thread.currentThread() + " is not the FXApplication thread.");
//            runButton.setText("Voila!");
//          }
            };

            statusLabel.textProperty().bind(task.messageProperty());
            runButton.disableProperty().bind(task.runningProperty());
            peopleView.itemsProperty().bind(task.valueProperty());
            progressBar.progressProperty().bind(task.progressProperty());
            task.stateProperty().addListener((observableValue, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    System.out.println("This is ok, this thread " + Thread.currentThread() + " is the JavaFX Application thread.");
                    runButton.setText("Voila!");
                }
            });

            new Thread(task).start();
        });
        final VBox layout = new VBox(runButton, statusLabel, peopleView);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding:10; -fx-font-size: 16;");
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();
    }
}