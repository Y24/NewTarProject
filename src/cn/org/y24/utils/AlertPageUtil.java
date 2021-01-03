/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import javafx.scene.control.Alert;

public class AlertPageUtil {
    public static void showErrorPage(String message) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setHeaderText("Error!");
        alert.showAndWait();
    }

    public static void showSuccessPage(String message) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setHeaderText("Good!");
        alert.showAndWait();
    }
}
