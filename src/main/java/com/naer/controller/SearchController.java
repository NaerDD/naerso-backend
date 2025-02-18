package com.naer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naer.common.BaseResponse;
import com.naer.common.ErrorCode;
import com.naer.common.ResultUtils;
import com.naer.exception.BusinessException;
import com.naer.exception.ThrowUtils;
import com.naer.model.dto.picture.PicturteQueryRequest;
import com.naer.model.dto.post.PostQueryRequest;
import com.naer.model.dto.search.SearchRequest;
import com.naer.model.dto.user.UserQueryRequest;
import com.naer.model.entity.Picture;
import com.naer.model.entity.Post;
import com.naer.model.entity.User;
import com.naer.model.vo.PostVO;
import com.naer.model.vo.SearchVo;
import com.naer.model.vo.UserVO;
import com.naer.service.PictureService;
import com.naer.service.PostService;
import com.naer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    private PictureService pictureService;

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;


    @PostMapping("/all")
    public BaseResponse<SearchVo> SearchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {

            String searchText = searchRequest.getSearchText();
            Page<Picture> picturePage = pictureService.listPictures(searchText, 1, 10);

            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);

            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);

            SearchVo searchVo = new SearchVo();
            searchVo.setPictureVOList(picturePage.getRecords());
            searchVo.setPostList(postVOPage.getRecords());
            searchVo.setUserList(userVOPage.getRecords());
            return ResultUtils.success(searchVo);
    }
}