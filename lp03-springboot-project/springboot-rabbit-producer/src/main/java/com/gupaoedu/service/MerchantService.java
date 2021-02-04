package com.gupaoedu.service;


import com.gupaoedu.entity.Merchant;

import java.util.List;

public interface MerchantService {

    List<Merchant> getMerchantList(String name, int page, int limit);

    Merchant getMerchantById(Integer id);

    int add(Merchant merchant);

    int update(Merchant merchant);

    int updateState(Merchant merchant);

    int delete(Integer id);

    int getMerchantCount();
}
