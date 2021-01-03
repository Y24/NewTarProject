/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import cn.org.y24.Main;
import cn.org.y24.ui.framework.StageManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class PageControllerUtil {
    public static void getPath(StageManager stageManager, StringProperty destination) {
        final DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        final File result = chooser.showDialog(stageManager.get(Main.primarySceneManagerName).getOwnerStage());
        if (result != null) {
            destination.setValue(result.getPath());
        }
    }

    public static boolean checkEncryption(CheckBox encryptionStatusId, StringProperty encryption, StringProperty keyPrompt, Label keyPromptId, BooleanProperty isKeyPromptShowing) {
        if (encryptionStatusId.isSelected()) {
            final var value = encryption.getValue();
            if (value == null || value.length() != 32) {
                keyPrompt.setValue("Key must be 32 long!");
                keyPromptId.textFillProperty().setValue(Paint.valueOf("red"));
                isKeyPromptShowing.setValue(true);
                return false;
            } else {
                keyPrompt.setValue("Good Choose!");
                keyPromptId.textFillProperty().setValue(Paint.valueOf("green"));
                isKeyPromptShowing.setValue(true);
                return true;
            }
        } else {
            isKeyPromptShowing.setValue(false);
            return true;
        }
    }
}
