package com.gupaoedu.service;


import com.gupaoedu.entity.Merchant;

import java.util.List;

/**
 * 商户service
 *
 * @author lp
 * @since 2021/2/5 10:37
 **/
public interface MerchantService {

    /**查询商户列表*/
    List<Merchant> getMerchantList(String name, int page, int limit);

    /**根据Id查询商户信息*/
    Merchant getMerchantById(Integer id);

    /**新增商户*/
    int add(Merchant merchant);

    /**修改商户*/
    int update(Merchant merchant);

    /**修改商户状态*/
    int updateState(Merchant merchant);

    /**根据id删除商户*/
    int delete(Integer id);

    /**获取商户数量*/
    int getMerchantCount();
}
