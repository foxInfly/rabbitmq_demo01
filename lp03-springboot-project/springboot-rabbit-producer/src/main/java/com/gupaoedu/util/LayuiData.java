package com.gupaoedu.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 同意返回实体类封装
 *
 * @author lp
 * @since 2021/2/5 10:06
 **/
@Data
@Accessors(chain = true)
public class LayuiData {

    private Integer count;//数量
    private Integer code;//返回码
    private String msg;//返回msg
    private List data;//数据集合

    public LayuiData() {
    }

    public LayuiData(Integer count, Integer code, String msg, List data) {
        this.count = count;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
