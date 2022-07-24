package tech.icoding.sci.sdk.common;

import lombok.Data;

/**
 * @author : Joe
 * @date : 2021/2/5
 */
@Data
public class EnumData{
    private Integer code;
    private String desc;

    public EnumData(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
