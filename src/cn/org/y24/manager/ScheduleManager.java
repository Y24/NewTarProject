package cn.org.y24.manager;

import cn.org.y24.actions.ScheduleAction;
import cn.org.y24.interfaces.IManager;

// do auto-backup task.
public class ScheduleManager implements IManager<ScheduleAction> {

    @Override
    public boolean execute(ScheduleAction type) {
        return false;
    }
}
