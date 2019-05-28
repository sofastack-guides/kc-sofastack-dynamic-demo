package io.sofastack.ark.biz.two.impl;

import com.alipay.sofa.runtime.api.annotation.SofaService;
import io.sofastack.ark.biz.facade.JvmService;
import org.springframework.stereotype.Service;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/5/27 3:03 PM
 * @since:
 **/
@SofaService
@Service
public class JvmServiceImpl implements JvmService {
    @Override
    public String HelloDynamicModule() {
        return "Welcome to the dynamic module provider two";
    }
}
