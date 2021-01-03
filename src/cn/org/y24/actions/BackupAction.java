/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.actions;

import cn.org.y24.entity.AccountEntity;
import cn.org.y24.entity.BackupEntity;
import cn.org.y24.enums.BackupActionType;

import java.util.List;

public class BackupAction {
    private final BackupActionType type;
    private List<BackupEntity> entityList;
    final private AccountEntity accountEntity;

    public BackupAction(BackupActionType type, AccountEntity accountEntity) {
        this.type = type;
        this.accountEntity = accountEntity;
    }

    public BackupAction(BackupActionType type, List<BackupEntity> entityList, AccountEntity accountEntity) {
        this.type = type;
        this.entityList = entityList;
        this.accountEntity = accountEntity;
    }

    public AccountEntity getAccount() {
        return accountEntity;
    }

    public BackupActionType getType() {
        return type;
    }

    public List<BackupEntity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<BackupEntity> entityList) {
        this.entityList = entityList;
    }
}
