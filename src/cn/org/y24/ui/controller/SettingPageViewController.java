/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.ui.controller;

import cn.org.y24.actions.AccountAction;
import cn.org.y24.interfaces.IManager;
import cn.org.y24.manager.AccountManager;
import cn.org.y24.ui.framework.BaseStageController;
import cn.org.y24.ui.framework.StageManager;

public class SettingPageViewController extends BaseStageController {
    private StageManager stageManager;
    private final IManager<AccountAction> accountManager = new AccountManager();

    @Override
    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void receiveMessage() {

    }
}
