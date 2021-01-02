/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.actions;

import cn.org.y24.entity.FileEntity;
import cn.org.y24.enums.FileActionType;
import cn.org.y24.interfaces.IAction;
import cn.org.y24.interfaces.IEntity;
import cn.org.y24.interfaces.IType;

public class FileAction implements IAction {
    public FileAction(FileActionType type, FileEntity entity) {
        this.entity = entity;
        this.type = type;
    }


    public void setEntity(FileEntity entity) {
        this.entity = entity;
    }

    private FileEntity entity;
    private final FileActionType type;

    @Override
    public IType getType() {
        return type;
    }

    @Override
    public IEntity getEntity() {
        return entity;
    }
}
