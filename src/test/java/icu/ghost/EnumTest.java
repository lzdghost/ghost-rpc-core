package icu.ghost;

import icu.ghost.rpc.model.ServiceMetaInfo;
import icu.ghost.rpc.protocol.ProtocolMessageStatusEnum;
import org.junit.Test;

/**
 * @author Ghost
 * @version 1.0
 * @className EnumTest
 * @date 2024/08/12 12:46
 * @since 1.0
 */
public class EnumTest {
    @Test
    public void testEnum() {
        System.out.println(ProtocolMessageStatusEnum.values());
        System.out.println(ProtocolMessageStatusEnum.OK);
        System.out.println(ProtocolMessageStatusEnum.OK.getText());
        System.out.println(ProtocolMessageStatusEnum.OK.getValue());
        System.out.println(ProtocolMessageStatusEnum.getEnumByValue(20));
    }
}
