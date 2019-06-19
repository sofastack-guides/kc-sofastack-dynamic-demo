package io.sofastack.stockmng.controller;

import io.sofastack.stockmng.facade.SortedStrategyFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/6/19 5:44 PM
 * @since:
 **/
@Controller
public class SortedController {

    @Autowired
    private SortedStrategyFacade sortedStrategyFacade;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("productList", sortedStrategyFacade.getSorted());
        return "index";
    }
}
