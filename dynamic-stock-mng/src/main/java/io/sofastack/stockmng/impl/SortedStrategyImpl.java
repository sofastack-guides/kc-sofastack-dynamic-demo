package io.sofastack.stockmng.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sofa.runtime.api.annotation.SofaReference;
import io.sofastack.dynamic.facade.StrategyService;
import io.sofastack.stockmng.facade.SortedStrategyFacade;
import io.sofastack.stockmng.mapper.StockMngMapper;
import io.sofastack.stockmng.model.ProductInfo;
import io.sofastack.stockmng.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/6/19 10:41 AM
 * @since:
 **/
@Service
public class SortedStrategyImpl implements SortedStrategyFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(SortedStrategyImpl.class);

    @SofaReference
    private StrategyService     strategyService;

    @Autowired
    private StockMngMapper      stockMngMapper;

    @Override
    public List<ProductInfo> getSorted() {
        List<ProductInfo> result = new ArrayList<>();
        try {
            String strategy = strategyService.strategy();
            Map<ProductInfo, Integer> map = new HashMap<>();
            // 按照订单量排序
            if ("order".equals(strategy)) {
                List<String> productCodes = stockMngMapper.queryAllProductCode();
                final List<ProductInfo> products = initProducts();
                for (String productCode : productCodes) {
                    int totalOrderCount = stockMngMapper.queryCountByProductCode(productCode);
                    ProductInfo productInfo = getProductInfoByCode(productCode, products);
                    if (productInfo != null) {
                        map.put(productInfo, totalOrderCount);
                    }
                }
                result = CommonUtil.sorted(map);
            }
        } catch (Throwable t) {
            LOGGER.error("Error to getSorted.", t);
            result = initProducts();
        }
        return result;
    }

    private ProductInfo getProductInfoByCode(String productCode, List<ProductInfo> products) {
        for (ProductInfo productInfo : products) {
            if (productInfo.getProductCode().equals(productCode)) {
                return productInfo;
            }
        }
        return null;
    }

    private List<ProductInfo> initProducts() {
        List<ProductInfo> products = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setName(DatabaseSeed.name[i]);
            productInfo.setDescription(DatabaseSeed.description[i]);
            JSONObject jsonObject = JSONObject.parseObject(DatabaseSeed.description[i]);
            productInfo.setSrc(jsonObject.get("image_url").toString());
            productInfo.setAuthor(jsonObject.get("author").toString());
            productInfo.setPrice(DatabaseSeed.price[i]);
            productInfo.setProductCode("0000" + (i + 1));
            products.add(productInfo);
        }
        return products;
    }
}
