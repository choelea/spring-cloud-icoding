package tech.icoding.sci.core.common;

import lombok.Data;
import lombok.ToString;

/***
 * 返回码和返回信息父类
 * @author ray
 */
@Data
@ToString
public class CodeAndMessage {
    /**
     * 返回码
     */
    private int code;
    /***
     * 返回信息
     */
    private String message;

    public CodeAndMessage() {

    }

    public CodeAndMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CodeAndMessage create(int code, String message) {
        return new CodeAndMessage(code, message);
    }
}