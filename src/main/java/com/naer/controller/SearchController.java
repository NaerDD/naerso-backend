package com.naer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naer.common.BaseResponse;
import com.naer.common.ErrorCode;
import com.naer.common.ResultUtils;
import com.naer.exception.ThrowUtils;
import com.naer.model.dto.picture.PicturteQueryRequest;
import com.naer.model.dto.post.PostQueryRequest;
import com.naer.model.dto.search.SearchRequest;
import com.naer.model.dto.user.UserQueryRequest;
import com.naer.model.entity.Picture;
import com.naer.model.entity.User;
import com.naer.model.vo.SearchVo;
import com.naer.service.PictureService;
import com.naer.service.PostService;
import com.naer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


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

    private PostController postController;


    @PostMapping("/all")
    public List<SearchVo> SearchAll (SearchRequest searchRequest, HttpServletRequest request) {
        String searchText = searchRequest.getSearchText();
        pictureService.listPictures(searchText,1,10);

        //搜哪个用户 --》 searchText
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userService.listUserVOByPage(userQueryRequest);

        //搜哪篇帖子 --》 searchText
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setTitle(searchText);

        this.listPostVOPage(postQueryRequest,request);

        return null;
    }

}
