package cn.org.y24.enums;

import cn.org.y24.interfaces.IType;

public enum FileActionType implements IType {
    readLocalUnTarFile,
    readLocalTarFile,
    writeLocalUnTarFile,
    writeLocalTarFile,
    readRemoteUnTarFile,
    readRemoteTarFile,
    writeRemoteUnTarFile,
    writeRemoteTarFile,
}
