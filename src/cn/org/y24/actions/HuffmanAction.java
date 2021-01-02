/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.actions;

import cn.org.y24.entity.HuffmanEntity;
import cn.org.y24.enums.HuffmanActionType;
import cn.org.y24.interfaces.IAction;
import cn.org.y24.interfaces.IEntity;
import cn.org.y24.interfaces.IType;

public class HuffmanAction implements IAction {
    final private HuffmanActionType type;
    final private HuffmanEntity entity;

    public HuffmanAction(HuffmanActionType type, HuffmanEntity entity) {
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
