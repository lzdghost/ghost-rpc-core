package icu.ghost.rpc.protocol;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

@Getter
public enum ProtocolMessageStatusEnum {

    OK("ok",20),
    BAD_REQUEST("badRequest", 40),
    BAD_RESPONSE("badResponse",50);

    private final String text;

    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举值
     * @param value
     * @return
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (ProtocolMessageStatusEnum e : ProtocolMessageStatusEnum.values()) {
            if (e.value == value) {
                return e;
            }
        }
        return null;
    }
}
