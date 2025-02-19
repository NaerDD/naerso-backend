package com.naer.controller;

import com.naer.common.BaseResponse;
import com.naer.common.ResultUtils;
import com.naer.manager.SearchFacade;
import com.naer.model.dto.search.SearchRequest;
import com.naer.model.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片接口
 *
 * @author Naer
 * 
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {
    @Resource
    SearchFacade searchFacade;

    @PostMapping("/all")
    public BaseResponse<SearchVo> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        return ResultUtils.success(searchFacade.SearchAll(searchRequest, request));
    }

}