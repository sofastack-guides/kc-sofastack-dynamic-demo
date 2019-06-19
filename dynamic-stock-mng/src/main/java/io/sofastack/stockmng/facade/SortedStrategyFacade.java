package io.sofastack.stockmng.facade;

import io.sofastack.stockmng.model.ProductInfo;

import java.util.List;

/**
 * @author: guolei.sgl (guolei.sgl@antfin.com) 2019/6/19 10:41 AM
 * @since:
 **/
public interface SortedStrategyFacade {
    /**
     * 获取排序后的商品列表
     * @return
     */
    List<ProductInfo> getSorted();
}
