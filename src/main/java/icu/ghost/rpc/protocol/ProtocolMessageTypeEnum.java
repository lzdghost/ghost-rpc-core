package icu.ghost.rpc.protocol;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

@Getter
public enum ProtocolMessageTypeEnum {
    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);

    private int key;

    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    /**
     * 根据key获取枚举
     * @param key
     * @return
     */
    public static ProtocolMessageTypeEnum getByKey(int key) {
        if (ObjectUtil.isEmpty(key)) {
            return null;
        }
        for (ProtocolMessageTypeEnum e : ProtocolMessageTypeEnum.values()) {
            if (e.key == key) {
                return e;
            }
        }
        return null;
    }
}
