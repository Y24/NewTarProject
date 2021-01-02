/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.actions;

import cn.org.y24.entity.AccountEntity;
import cn.org.y24.enums.AccountActionType;
import cn.org.y24.interfaces.IAction;
import cn.org.y24.interfaces.IEntity;
import cn.org.y24.interfaces.IType;

public class AccountAction implements IAction {

    public AccountAction(AccountActionType type, AccountEntity entity) {
        this.type = type;
        this.entity = entity;
    }

    private final AccountActionType type;
    private final AccountEntity entity;

    @Override
    public IType getType() {
        return type;
    }

    @Override
    public IEntity getEntity() {
        return entity;
    }
}
