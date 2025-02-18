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
    private PostController postController;


    @PostMapping("/all")
    public BaseResponse<SearchVo> SearchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {

        String searchText = searchRequest.getSearchText();
        /**
         * 3个请求轮流发送 600ms左右
         * 这里做一个并发
         */
//        CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
//            //搜哪个用户 --》 searchText
//            UserQueryRequest userQueryRequest = new UserQueryRequest();
//            userQueryRequest.setUserName(searchText);
//            Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
//            return userVOPage;
//        });
//
//        CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(()->{
//            //搜哪篇帖子 --》 searchText
//            PostQueryRequest postQueryRequest = new PostQueryRequest();
//            postQueryRequest.setSearchText(searchText);
//            BaseResponse<Page<PostVO>> pageBaseResponse = postController.listPostVOByPage(postQueryRequest, request);
//            Page<PostVO> postVOPage = pageBaseResponse.getData();
//            return postVOPage;
//        });
//
//        CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(()->{
//            Page<Picture> picturePage = pictureService.listPictures(searchText, 1, 10);
//            //搜哪个图片
//            return picturePage;
//        });
//
//        CompletableFuture.allOf(userTask, postTask, pictureTask).join();
//        try {
//            Page<UserVO> userVOPage = userTask.get();
//            Page<PostVO> postVOPage = postTask.get();
//            Page<Picture> picturePage = pictureTask.get();
//            SearchVo searchVo = new SearchVo();
//            searchVo.setUserList(userVOPage.getRecords());
//            searchVo.setPostList(postVOPage.getRecords());
//            searchVo.setPictureVOList(picturePage.getRecords());
//            return ResultUtils.success(searchVo);
//        }catch (Exception e){
//            log.error("查询异常",e);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"查询异常");
//        }

            //搜哪个用户 --》 searchText
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
            //搜哪篇帖子 --》 searchText
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            BaseResponse<Page<PostVO>> pageBaseResponse = postController.listPostVOByPage(postQueryRequest, request);
            Page<PostVO> postVOPage = pageBaseResponse.getData();
            //搜哪个图片
            Page<Picture> picturePage = pictureService.listPictures(searchText, 1, 10);


            SearchVo searchVo = new SearchVo();
            searchVo.setPictureVOList(picturePage.getRecords());
            searchVo.setPostList(postVOPage.getRecords());
            searchVo.setUserList(userVOPage.getRecords());
            return ResultUtils.success(searchVo);
    }
}