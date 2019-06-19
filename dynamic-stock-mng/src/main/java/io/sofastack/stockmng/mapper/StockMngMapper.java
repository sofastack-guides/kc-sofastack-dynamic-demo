package io.sofastack.stockmng.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockMngMapper {
    @Select("select DISTINCT(product_code) from order_tb ")
    List<String> queryAllProductCode();

    @Select("select sum(count) from order_tb where product_code=#{productCode}")
    Integer queryCountByProductCode(@Param("productCode") String productCode);
}
