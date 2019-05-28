package io.sofastack.ar.biz.master.controller;

import com.alipay.sofa.runtime.api.annotation.SofaReference;
import io.sofastack.ark.biz.facade.JvmService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/5/27 3:48 PM
 * @since:
 **/
@RestController
public class SimpleController {

    @SofaReference
    private JvmService jvmService;

    @RequestMapping("jvm")
    public String jvmService() {
        return jvmService.HelloDynamicModule();
    }
}
