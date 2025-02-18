package com.naer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naer.common.BaseResponse;
import com.naer.common.ErrorCode;
import com.naer.common.ResultUtils;
import com.naer.exception.BusinessException;
import com.naer.exception.ThrowUtils;
import com.naer.manager.SearchFacade;
import com.naer.model.dto.post.PostQueryRequest;
import com.naer.model.dto.search.SearchRequest;
import com.naer.model.dto.user.UserQueryRequest;
import com.naer.model.entity.Picture;
import com.naer.model.enums.SearchTypeEnum;
import com.naer.model.vo.PostVO;
import com.naer.model.vo.SearchVo;
import com.naer.model.vo.UserVO;
import com.naer.service.PictureService;
import com.naer.service.PostService;
import com.naer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.card.enums.BusinessServiceType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;


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