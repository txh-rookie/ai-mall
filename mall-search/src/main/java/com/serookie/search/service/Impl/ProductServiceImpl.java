package com.serookie.search.service.Impl;

import com.alibaba.fastjson.JSON;
import com.serookie.search.entity.SpuESModel;
import com.serookie.search.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/25
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

   @Autowired
   private RestHighLevelClient restHighLevelClient;//使用摸排来操作elasticsearch

    public static final String PRODUCT_INDEX="product";
    @Override
    public Boolean productStatusUp(List<SpuESModel> spuESModels) throws IOException {
       //保存到es服务
        CreateIndexRequest createIndexRequest = new CreateIndexRequest();
        createIndexRequest.index(PRODUCT_INDEX);
        restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);//创建一个索引
        BulkRequest bulkRequest = new BulkRequest();
        if(!StringUtils.isEmpty(spuESModels)){
           spuESModels.forEach(elem->{
               //将内容进行放入
               IndexRequest indexRequest = new IndexRequest();
               indexRequest.id(elem.getSpuId().toString());
               indexRequest.source(elem);
               bulkRequest.add(indexRequest);
           });
       }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
        boolean flag = bulk.hasFailures();//拿到信息是否放入成功，有错误会返回false，无错误会返回true
        log.info("商品信息添加是否成功:{}",flag);
        // TODO: 2022/6/25 进行批量处理
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.error("商品上架错误:{}",collect);
        return flag;
    }
}
