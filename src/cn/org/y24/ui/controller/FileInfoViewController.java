/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.ui.controller;

import cn.org.y24.Main;
import cn.org.y24.actions.AccountAction;
import cn.org.y24.entity.AccountEntity;
import cn.org.y24.entity.FileEntity;
import cn.org.y24.entity.TarFileInfoEntity;
import cn.org.y24.interfaces.IManager;
import cn.org.y24.manager.AccountManager;
import cn.org.y24.ui.framework.BaseStageController;
import cn.org.y24.ui.framework.Deliverer;
import cn.org.y24.ui.framework.StageManager;
import com.sun.media.jfxmediaimpl.platform.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class FileInfoViewController extends BaseStageController implements Initializable {
    private final IManager<AccountAction> accountManager = new AccountManager();
    private TarFileInfoEntity tarFileInfoEntity;
    private StringProperty fileName;
    private StringProperty integrity;
    private StringProperty encryption;
    private StringProperty fileCount;
    private StringProperty fileBlocks;
    @FXML
    private Label fileNameLabelId;
    @FXML
    private Label fileNameId;
    @FXML
    private Label integrityId;
    @FXML
    private Label encryptionId;
    @FXML
    private Label fileCountId;
    @FXML
    private Label fileBlocksId;
    private StageManager stageManager;

    @Override
    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void receiveMessage() {
        receiveEntity();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("FileInfoViewController initialize!");
        fileName = new SimpleStringProperty();
        integrity = new SimpleStringProperty();
        encryption = new SimpleStringProperty();
        fileCount = new SimpleStringProperty();
        fileBlocks = new SimpleStringProperty();
        fileNameId.textProperty().bind(fileName);
        fileNameLabelId.textProperty().bind(fileName);
        integrityId.textProperty().bind(integrity);
        encryptionId.textProperty().bind(encryption);
        fileCountId.textProperty().bind(fileCount);
        fileBlocksId.textProperty().bind(fileBlocks);
    }

    private void receiveEntity() {
        if (tarFileInfoEntity == null) {
            tarFileInfoEntity = (TarFileInfoEntity) ((Deliverer) stageManager.receiveSingleCastMessage(1).toArray()[0]).getMessage();
            fileName.setValue(tarFileInfoEntity.name);
            integrity.setValue(tarFileInfoEntity.integrity);
            encryption.setValue(tarFileInfoEntity.encryption);
            fileBlocks.setValue(tarFileInfoEntity.fileBlocks);
            fileCount.setValue(tarFileInfoEntity.fileCount);
        }
    }
}
