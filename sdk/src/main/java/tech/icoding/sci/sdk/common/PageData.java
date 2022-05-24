package tech.icoding.sci.sdk.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Joe
 * @date : 2022/5/11
 */
@Data
@NoArgsConstructor
public class PageData<T> implements Serializable {


    private static final long serialVersionUID = -9081782848153708161L;
    private long total;
    private int pageNumber;
    private int pageSize;
    private List<T> content = new ArrayList<>();


    public PageData(long total, int pageNumber, int pageSize, List<T> content){
        this.content = content;
        this.total = total;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public boolean isEmpty(){
        return this.total < 1;
    }
}
