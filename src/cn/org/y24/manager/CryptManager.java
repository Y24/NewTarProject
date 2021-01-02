package cn.org.y24.manager;

import cn.org.y24.actions.CryptAction;
import cn.org.y24.entity.CryptEntity;
import cn.org.y24.enums.CryptActionType;
import cn.org.y24.exception.InvalidPasswordException;
import cn.org.y24.interfaces.ICryptProcess;
import cn.org.y24.interfaces.IManager;
import cn.org.y24.interfaces.IType;

// do encryption and decryption task.
public class CryptManager implements IManager<CryptAction> {
    IType actionType;

    public CryptManager(ICryptProcess processor) {
        this.processor = processor;
    }

    private final ICryptProcess processor;

    @Override
    public boolean execute(CryptAction cryptAction) {
        final var entity = (CryptEntity) cryptAction.getEntity();
        actionType = cryptAction.getType();
        try {
            entity.setOutput(processor.process((CryptActionType) actionType, entity.getInput()));
        } catch (InvalidPasswordException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}
