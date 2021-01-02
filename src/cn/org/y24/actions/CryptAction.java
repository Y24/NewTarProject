/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.actions;

import cn.org.y24.entity.CryptEntity;
import cn.org.y24.enums.CryptActionType;
import cn.org.y24.interfaces.IAction;
import cn.org.y24.interfaces.IEntity;
import cn.org.y24.interfaces.IType;

public class CryptAction implements IAction {
    private final CryptActionType type;


    private final CryptEntity entity;

    public CryptAction(CryptActionType type, CryptEntity entity) {
        this.type = type;
        this.entity = entity;
    }

    @Override
    public IType getType() {
        return type;
    }

    @Override
    public IEntity getEntity() {
        return entity;
    }
}
