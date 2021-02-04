package com.gupaoedu.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class LayuiData {

    private Integer count;
    private Integer code;
    private String msg;
    private List data;

    public LayuiData() {
    }

    public LayuiData(Integer count, Integer code, String msg, List data) {
        this.count = count;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
