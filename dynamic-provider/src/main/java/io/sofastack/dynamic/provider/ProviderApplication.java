package io.sofastack.dynamic.provider;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/6/12 7:48 PM
 * @since:
 **/
@SpringBootApplication
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ProviderApplication.class).web(WebApplicationType.NONE);
        builder.build().run(args);
    }
}
