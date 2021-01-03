/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.entity;

import cn.org.y24.interfaces.IEntity;
import cn.org.y24.utils.NewTarFileSpec;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BackupEntity implements IEntity {
    public final static BackupEntity nullEntity = new BackupEntity("", null);
    private final String target;
    private final Date date;

    public BackupEntity(String target, Date date) {
        this.target = target;
        this.date = date;
    }

    public String getTarget() {
        return target;
    }

    public Date getDate() {
        return date;
    }

    public BackupEntity(String format) {
        Date tmpDate;
        final String[] strings = format.split("~", 2);
        target = strings[0];
        try {
            tmpDate = NewTarFileSpec.defaultDateFormat.parse(strings[1]);
        } catch (ParseException e) {
            tmpDate = null;
            e.printStackTrace();
        }
        date = tmpDate;
    }

    public String toString() {
        return target + "~" + NewTarFileSpec.defaultDateFormat.format(date);
    }

    @Override
    public Map<String, String> toMap() {
        return new HashMap<>() {{
            this.put("target", target);
            this.put("date", NewTarFileSpec.defaultDateFormat.format(date));
        }};
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BackupEntity
                && target.equals(((BackupEntity) obj).getTarget())
                && date.equals(((BackupEntity) obj).getDate());
    }
}
