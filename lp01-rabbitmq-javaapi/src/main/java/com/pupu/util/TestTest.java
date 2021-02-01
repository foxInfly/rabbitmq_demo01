package com.pupu.util;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author lp
 * @since 2021/2/1 14:10
 **/
public class TestTest {
    public static void main(String[] args) {
        Set<Integer> set1 = new HashSet<>();

//        for(int i=0; i<100; i++){
//            set1.add(i);
//            set1.remove(i-1);
//        }
//        System.out.println(set1.size());
        System.out.println(ResourceUtil.getKey("rabbitmq.uri"));
    }

}
