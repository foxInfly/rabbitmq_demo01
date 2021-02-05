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

/**
 * 商户管理
 *
 * @author lp
 * @since 2021/2/5 10:12
 **/
@Controller
public class MerchantController {
    @Resource
    private MerchantService merchantService;

    /**
     * 1. 去新增商户界面
     */
    @RequestMapping("/toMerchant")
    public String toMerchant() {
        return "merchantAdd";
    }

    /**
     * 2. 新增商户
     */
    @RequestMapping("/merchantAdd")
    @Transactional
    @ResponseBody
    public Integer merchantAdd(String name, String address, String accountNo, String accountName) {
        return merchantService.add(new Merchant()
                .setAccountNo(accountNo)
                .setName(name)
                .setAddress(address)
                .setAccountName(accountName)
                .setState(Constant.MERCHANT_STATE.ACITVE));
    }

    /**
     * 3. 去商户列表页面
     */
    @RequestMapping("/merchantList")
    public String merchantList() {
        return "merchantListPage";
    }

    /**
     * 4. 分页查询商户列表
     */
    @RequestMapping("/getMerchantList")
    @ResponseBody
    public LayuiData getMerchantList(HttpServletRequest request) {
        String name = request.getParameter("name");
        if (name == null) name = "";

        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        if (page >= 1) page = (page - 1) * limit;

        LayuiData layuiData = new LayuiData();

        List<Merchant> merchantList = merchantService.getMerchantList(name, page, limit);
        int count = merchantService.getMerchantCount();

        return layuiData
                .setCode(0)
                .setCount(count)
                .setMsg("数据请求成功")
                .setData(merchantList);
    }

    /**
     * 5. 去详情查看界面
     */
    @RequestMapping("/toDetail")
    public String toDetail(Integer id, Model model) {
        model.addAttribute("merchant", merchantService.getMerchantById(id));
        return "merchantDetail";
    }

    /**
     * 6. 去修改界面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id, Model model) {
        model.addAttribute("merchant", merchantService.getMerchantById(id));
        return "merchantUpdate";
    }

    /**
     * 7. 根据id修改商户信息
     */
    @RequestMapping("/merchantUpdate")
    @Transactional
    @ResponseBody
    public Integer merchantUpdate(Integer id, String name, String address, String accountNo, String accountName) {
        return merchantService.update(new Merchant()
                .setId(id)
                .setName(name)
                .setAddress(address)
                .setAccountNo(accountNo)
                .setAccountName(accountName));
    }

    /**
     * 8. 变更商户状态
     */
    @GetMapping(value = "/changeState")
    @ResponseBody
    public String changeState(@RequestParam(value = "id") String idStr) {
        String errmsg = "";
        if (null == idStr || "".equals(idStr)) return "商户号不能为空";

        int id = Integer.parseInt(idStr);

        // 校验
        Merchant result = merchantService.getMerchantById(id);
        if (null == result) return "编号为" + id + "的商户不存在！";

        Merchant updateBean = new Merchant()
                .setId(id);

        //如果是现在是启用，则停用
        if (Constant.MERCHANT_STATE.ACITVE.equals(result.getState())) {
            updateBean.setState(Constant.MERCHANT_STATE.CLOSE);
        } else {
            updateBean.setState(Constant.MERCHANT_STATE.ACITVE);
        }

        int num = merchantService.updateState(updateBean);
        // 1表示成功
        if (num == 1) {
            return "1";
        } else {
            return "更新商户状态失败";
        }

    }


    /**
     * 9. 根据id删除商户
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Integer delete(Integer id) {
        return merchantService.delete(id);
    }
}
