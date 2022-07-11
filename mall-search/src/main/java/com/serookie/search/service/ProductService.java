package com.serookie.search.service;

import com.serookie.search.entity.SpuESModel;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Boolean productStatusUp(List<SpuESModel> spuESModels) throws IOException;
}
