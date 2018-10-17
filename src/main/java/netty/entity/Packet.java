package netty.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 *  @JSONField 防止被序列化 -->协议对象 Packet 不会出现 version
 */
@Data
public abstract class Packet {
    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;


    @JSONField(serialize = false)
    public abstract Byte getCommand();

}
