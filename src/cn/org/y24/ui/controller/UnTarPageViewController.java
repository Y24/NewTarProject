/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.ui.controller;

import cn.org.y24.ui.framework.BaseStageController;
import cn.org.y24.ui.framework.Deliverer;
import cn.org.y24.ui.framework.StageManager;
import cn.org.y24.utils.AlertPageUtil;
import cn.org.y24.utils.NewTarUtils;
import cn.org.y24.utils.PageControllerUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class UnTarPageViewController extends BaseStageController implements Initializable {

    @FXML
    private TextField nameId;
    @FXML
    private TextField targetId;
    @FXML
    private Label namePromptId;
    @FXML
    private CheckBox encryptionStatusId;
    @FXML
    private PasswordField encryptionId;
    @FXML
    private Label keyPromptId;
    private StageManager stageManager;
    private String fileName;

    private StringProperty target;
    private StringProperty encryption;
    private StringProperty name;
    private StringProperty namePrompt;
    private BooleanProperty isNamePromptShowing;
    private StringProperty keyPrompt;
    private BooleanProperty isKeyPromptShowing;


    @Override
    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void receiveMessage() {
        if (fileName == null) {
            fileName = (String) ((Deliverer) stageManager.receiveSingleCastMessage(3).toArray()[0]).getMessage();
            target.setValue(fileName);
        }
    }

    @FXML
    private void doNewUnTar() {
        String path = name.getValue();
        if (path == null) {
            namePrompt.setValue("Please choose a destination firstly!");
            namePromptId.textFillProperty().setValue(Paint.valueOf("red"));
        } else {
            path = path + (path.endsWith(File.separator) ? "" : File.separator);
            final var paths = target.getValue().split(File.separator);
            final var dirName = paths[paths.length - 1];
            final int index = dirName.lastIndexOf('.');
            if (index == -1) {
                AlertPageUtil.showErrorPage(String.format("File %s is invalid", target.getValue()));
                return;
            }
            final File file = new File(path + dirName.substring(0, index));
            if (file.exists()) {
                namePrompt.setValue("Path already exists!");
                namePromptId.textFillProperty().setValue(Paint.valueOf("red"));
            } else {
                namePrompt.setValue("Good Choose!");
                namePromptId.textFillProperty().setValue(Paint.valueOf("green"));
                if (!checkEncryption())
                    return;
                namePrompt.setValue("Good Choose!");
                namePromptId.textFillProperty().setValue(Paint.valueOf("green"));
                final String key = encryption.getValue();
                final String actualKey = String.join("", Collections.nCopies(4, key));
                if (NewTarUtils.unTar(target.getValue(), file.getPath(), actualKey)) {
                    stageManager.closeNewest();
                    AlertPageUtil.showSuccessPage("Untar NewTar file successfully!");
                } else {
                    stageManager.closeNewest();
                    AlertPageUtil.showErrorPage(String.format("Key of File %s is invalid!", target.getValue()));
                    return;
                }
            }
        }
        isNamePromptShowing.setValue(true);
    }

    private boolean checkEncryption() {
        return PageControllerUtil.checkEncryption(encryptionStatusId, encryption, keyPrompt, keyPromptId, isKeyPromptShowing);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name = new SimpleStringProperty();
        namePrompt = new SimpleStringProperty();
        isNamePromptShowing = new SimpleBooleanProperty();
        encryption = new SimpleStringProperty();
        keyPrompt = new SimpleStringProperty();
        isKeyPromptShowing = new SimpleBooleanProperty();
        target = new SimpleStringProperty();
        targetId.textProperty().bind(target);
        nameId.textProperty().bindBidirectional(name);
        namePromptId.textProperty().bind(namePrompt);
        isKeyPromptShowing.setValue(false);
        namePromptId.visibleProperty().bind(isNamePromptShowing);
        encryptionId.editableProperty().bind(encryptionStatusId.selectedProperty());
        encryptionId.textProperty().bindBidirectional(encryption);
        keyPromptId.textProperty().bind(keyPrompt);
        keyPromptId.visibleProperty().bind(isKeyPromptShowing);
        isKeyPromptShowing.setValue(false);
    }

    public void choosePath() {
        PageControllerUtil.getPath(stageManager, name);
    }


}
