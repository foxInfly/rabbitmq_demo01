package com.gupaoedu.controller;

import com.gupaoedu.entity.Merchant;
import com.gupaoedu.service.MerchantService;
import com.gupaoedu.util.Constant;
import com.gupaoedu.util.LayuiData;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MerchantController {
    @Resource
    private MerchantService merchantService;

    /**
     * 查询商户列表
     */
    @RequestMapping("/getMerchantList")
    @ResponseBody
    public LayuiData getMerchantList (HttpServletRequest request){
        String name = request.getParameter("name");
        if(name==null){
            name="";
        }
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        if(page>=1){
            page = (page-1)*limit;
        }
        LayuiData layuiData = new LayuiData();
        List<Merchant> merchantList = merchantService.getMerchantList(name,page,limit);
        int count = merchantService.getMerchantCount();
        layuiData.setCode(0);
        layuiData.setCount(count);
        layuiData.setMsg("数据请求成功");
        layuiData.setData(merchantList);
       return layuiData;
    }

    /**
     * 去新增商户界面
     */
    @RequestMapping("/toMerchant")
    public String toMerchant (){

        return "merchantAdd";
    }

    /**
     * 新增商户
     */
    @RequestMapping("/merchantAdd")
    @Transactional
    @ResponseBody
    public Integer merchantAdd (String name,String address,String accountNo, String accountName){
        Merchant merchant = new Merchant();
        merchant.setAccountNo(accountNo);
        merchant.setName(name);
        merchant.setAddress(address);
        merchant.setAccountName(accountName);
        merchant.setState(Constant.MERCHANT_STATE.ACITVE);
        return merchantService.add(merchant);
    }

    @RequestMapping("/merchantList")
    public String merchantList1() {
        return "merchantListPage";
    }

    /**
     * 根据id删除商户
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Integer delete(Integer id) {

        return merchantService.delete(id);
    }

    /**
     * 去查看界面
     */
    @RequestMapping("/toDetail")
    public String toDetail(Integer id, Model model) {

        Merchant merchant = merchantService.getMerchantById(id);
        model.addAttribute("merchant",merchant);
        return "merchantDetail";
    }
    /**
     * 去修改界面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id, Model model) {

        Merchant merchant = merchantService.getMerchantById(id);
        model.addAttribute("merchant",merchant);
        return "merchantUpdate";
    }
    /**
     * 根据id修改商户信息
     */
    @RequestMapping("/merchantUpdate")
    @Transactional
    @ResponseBody
    public Integer merchantUpdate (Integer id, String name,String address,String accountNo, String accountName){
        Merchant merchant = new Merchant();
        merchant.setId(id);
        merchant.setName(name);
        merchant.setAddress(address);
        merchant.setAccountNo(accountNo);
        merchant.setAccountName(accountName);
        return merchantService.update(merchant);
    }

    /**
     * 变更商户状态
     */
    @GetMapping(value = "/changeState")
    @ResponseBody
    public String changeState(@RequestParam(value = "id") String idStr){
        String errmsg="";
        if( null == idStr || "".equals(idStr))
        return "商户号不能为空";
        int id = Integer.parseInt(idStr);

        // 校验
        Merchant result = merchantService.getMerchantById(id);
        if (null == result) {
            return "编号为" + id + "的商户不存在！";
        }

        Merchant updateBean = new Merchant();
        updateBean.setId(id);
        //如果是现在是启用，则停用
        if(Constant.MERCHANT_STATE.ACITVE.equals(result.getState())){
            updateBean.setState("0");
        }else {
            updateBean.setState("1");
        }

        int num = merchantService.updateState(updateBean);
        // 1表示成功
        if( num == 1){
            return "1";
        }else{
            return "更新商户状态失败";
        }

    }
}
