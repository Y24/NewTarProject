package cn.org.y24.actions;

import cn.org.y24.entity.ScheduleEntity;
import cn.org.y24.enums.ScheduleActionType;
import cn.org.y24.interfaces.IAction;
import cn.org.y24.interfaces.IEntity;
import cn.org.y24.interfaces.IType;

public class ScheduleAction implements IAction {
    final private ScheduleActionType type;
    final private ScheduleEntity entity;

    public ScheduleAction(ScheduleActionType type, ScheduleEntity entity) {
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
