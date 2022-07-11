package com.serookie.search.service;

import com.serookie.search.vo.SearchParam;
import com.serookie.search.vo.SearchResult;

public interface MallSearchService {
    SearchResult search(SearchParam param);
}
