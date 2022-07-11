package com.serookie.search.web;

import com.serookie.search.vo.SearchParam;
import com.serookie.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.serookie.search.service.MallSearchService;
/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/28
 */
@Controller
public class IndexController {

    @Autowired
    private MallSearchService mallSearchService;

    /**
     * springmvc自动将所有的参数自动封装成指定的对象
     * @param param
     * @return
     */
    @GetMapping({"/","/list.html"})
    public String index(SearchParam param, Model model){
       SearchResult result=mallSearchService.search(param);
       model.addAttribute("result",result);
        return "list";
    }
}
