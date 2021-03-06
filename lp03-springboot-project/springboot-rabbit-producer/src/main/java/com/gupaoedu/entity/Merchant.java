package com.gupaoedu.entity;

import com.gupaoedu.util.Constant;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 商户
 *
 * @author lp
 * @since 2021/2/4 22:31
 */
@Data
@Accessors(chain = true)
public class Merchant implements Serializable {
    int id; // 商户编号
    String name; // 商户名称
    String address; // 商户地址
    String accountNo; // 商户账号
    String accountName; // 户名
    String state; // 状态 1 激活 2 关闭
    String stateStr; // 状态中文

    public Merchant() {
    }

    public Merchant(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getStateStr() {
        if (null == state) {
            return "";
        } else if (Constant.MERCHANT_STATE.ACITVE.equals(state)) {
            return "激活";
        } else if (Constant.MERCHANT_STATE.CLOSE.equals(state)) {
            return "关闭";
        } else {
            return "未知";
        }
    }

}
