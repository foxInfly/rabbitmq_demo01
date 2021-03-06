package com.gupaoedu.mapper;
import com.gupaoedu.entity.Merchant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantMapper {

   Merchant getMerchantById(Integer sid);

    List<Merchant> getMerchantList(@Param("name") String name, @Param("page")int page, @Param("limit")int limit);

    int add(Merchant merchant);

    int update(Merchant merchant);

    int updateState(Merchant merchant);

    int delete(Integer sid);

    int getMerchantCount();
}