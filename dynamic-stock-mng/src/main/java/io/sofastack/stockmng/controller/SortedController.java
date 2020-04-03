package io.sofastack.stockmng.controller;


import com.alipay.sofa.runtime.api.annotation.SofaReference;
import io.sofastack.dynamic.facade.StrategyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/6/19 5:44 PM
 * @since:
 **/
@Controller
public class SortedController {

    @SofaReference
    private StrategyService strategyService;

    @RequestMapping("/")
    public String index(Model model) {
        try {
            model.addAttribute("name", strategyService.strategy());
        }catch (Throwable ex){
            model.addAttribute("name", "DEFAULT BIZ");
        }
        return "index";
    }
}
