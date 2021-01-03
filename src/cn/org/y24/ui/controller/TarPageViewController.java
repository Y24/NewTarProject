/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.ui.controller;

import cn.org.y24.enums.CryptAlgorithm;
import cn.org.y24.ui.framework.BaseStageController;
import cn.org.y24.ui.framework.Deliverer;
import cn.org.y24.ui.framework.StageManager;
import cn.org.y24.utils.AlertPageUtil;
import cn.org.y24.utils.NewTarFileSpec;
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
import java.util.ResourceBundle;

public class TarPageViewController extends BaseStageController implements Initializable {

    @FXML
    private TextField targetId;
    @FXML
    private TextField nameId;
    @FXML
    private Label namePromptId;
    @FXML
    private CheckBox encryptionStatusId;
    @FXML
    private PasswordField encryptionId;
    @FXML
    private Label keyPromptId;
    @FXML
    private TextField destinationId;
    @FXML
    private StageManager stageManager;
    private String fileName;

    private StringProperty target;
    private StringProperty name;
    private StringProperty encryption;
    private StringProperty destination;
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
            fileName = (String) ((Deliverer) stageManager.receiveSingleCastMessage(2).toArray()[0]).getMessage();
            target.setValue(fileName);
        }
    }

    @FXML
    private void doNewTar() {
        String path = destination.getValue();
        if (path == null) {
            namePrompt.setValue("Firstly choose a destination!");
            namePromptId.textFillProperty().setValue(Paint.valueOf("red"));
        } else {
            path = path + (path.endsWith(File.separator) ? "" : File.separator);
            final File file = new File(path + name.getValue() + NewTarFileSpec.suffix);
            if (file.exists()) {
                namePrompt.setValue("File already exists!");
                namePromptId.textFillProperty().setValue(Paint.valueOf("red"));

            } else {
                namePrompt.setValue("Good Choose!");
                namePromptId.textFillProperty().setValue(Paint.valueOf("green"));
                if (!checkEncryption())
                    return;
                if (NewTarUtils.tar(fileName, file.getPath(), encryptionStatusId.isSelected() ? CryptAlgorithm.defaultCrypt : CryptAlgorithm.noCrypt, encryption.getValue())) {
                    stageManager.closeNewest();
                    AlertPageUtil.showSuccessPage("Create NewTar file successfully!");
                } else {
                    stageManager.closeNewest();
                    AlertPageUtil.showErrorPage(String.format("Operation on file %s failed!", target.getValue()));
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
        target = new SimpleStringProperty();
        name = new SimpleStringProperty();
        namePrompt = new SimpleStringProperty();
        isNamePromptShowing = new SimpleBooleanProperty();
        encryption = new SimpleStringProperty();
        keyPrompt = new SimpleStringProperty();
        isKeyPromptShowing = new SimpleBooleanProperty();
        destination = new SimpleStringProperty();
        targetId.textProperty().bind(target);
        nameId.textProperty().bindBidirectional(name);
        namePromptId.textProperty().bind(namePrompt);
        isKeyPromptShowing.setValue(false);
        namePromptId.visibleProperty().bind(isNamePromptShowing);
        encryptionId.editableProperty().bind(encryptionStatusId.selectedProperty());
        encryptionId.textProperty().bindBidirectional(encryption);
        encryptionStatusId.selectedProperty().bindBidirectional(isKeyPromptShowing);
        keyPromptId.textProperty().bind(keyPrompt);
        keyPromptId.visibleProperty().bind(isKeyPromptShowing);
        isKeyPromptShowing.setValue(false);
        destinationId.textProperty().bind(destination);
    }

    public void choosePath() {
        PageControllerUtil.getPath(stageManager, destination);
    }
}
