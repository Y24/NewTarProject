/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */
package cn.org.y24.manager;

import cn.org.y24.actions.BackupAction;
import cn.org.y24.entity.AccountEntity;
import cn.org.y24.entity.BackupEntity;
import cn.org.y24.enums.BackupActionType;
import cn.org.y24.interfaces.IManager;
import cn.org.y24.interfaces.IUrlProvider;
import cn.org.y24.utils.UrlHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BackupManager implements IManager<BackupAction>, IUrlProvider {
    private BackupActionType actionType;
    private final List<BackupEntity> cloudList = new ArrayList<>();
    private final List<BackupEntity> localList = new ArrayList<>();

    @Override
    public String getUrl() {
        return baseUrl + actionType;
    }


    @Override
    public boolean execute(BackupAction action) {
        actionType = action.getType();
        final AccountEntity account = action.getAccount();
        final var handler = new UrlHandler();
        switch (actionType) {
            case fetch -> {
                final List<BackupEntity> backupList = new ArrayList<>();
                try {
                    if (!handler.handle(getUrl(), account.toMap())) {
                        handler.dispose();
                        action.setEntityList(backupList);
                        return false;
                    }
                    final var reader = handler.getReader();
                    String line = reader.readLine();
                    if (!line.startsWith("OK")) {
                        action.setEntityList(backupList);
                        handler.dispose();
                        return false;
                    }

                    while ((line = reader.readLine()) != null) {
                        backupList.add(new BackupEntity(line));
                    }
                    action.setEntityList(backupList);
                } catch (IOException e) {
                    e.printStackTrace();
                    action.setEntityList(backupList);
                    return false;
                } finally {
                    handler.dispose();
                }
            }
            case push -> {
                final List<BackupEntity> historyList = action.getEntityList();
                try {
                    Map<String, String> options = new LinkedHashMap<>(account.toMap());
                    options.put("count", historyList.size() + "");
                    for (int i = 0; i < historyList.size(); i++) {
                        options.put("content" + i, historyList.get(i).toString());
                    }
                    if (!handler.handle(getUrl(), options)) {
                        handler.dispose();
                        return false;
                    }
                    final var reader = handler.getReader();
                    String line = reader.readLine();
                    handler.dispose();
                    return line.startsWith("OK");
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;

                } finally {
                    handler.dispose();
                }
            }
            case clear -> action.getEntityList().clear();
        }
        return true;
    }
}