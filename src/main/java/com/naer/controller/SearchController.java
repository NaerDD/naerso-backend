package com.naer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naer.common.BaseResponse;
import com.naer.common.ErrorCode;
import com.naer.common.ResultUtils;
import com.naer.exception.BusinessException;
import com.naer.exception.ThrowUtils;
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
    private PictureService pictureService;

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;


    @PostMapping("/all")
    public BaseResponse<SearchVo> SearchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String type = searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        ThrowUtils.throwIf(StringUtils.isBlank(type),ErrorCode.PARAMS_ERROR);
        String searchText = searchRequest.getSearchText();
        //搜索出所有数据
        if(searchTypeEnum == null) {
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(()->{
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
                return  userVOPage;
            });

            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(()->{
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                return postVOPage;
            });

            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(()->{
                Page<Picture> picturePage = pictureService.listPictures(searchText, 1, 10);
                return  picturePage;
            });

            CompletableFuture.allOf(userTask, postTask, pictureTask).join();
            try {
                SearchVo searchVo = new SearchVo();
                Page<UserVO> userVOPage = userTask.get();
                Page<Picture> picturePage = pictureTask.get();
                Page<PostVO> postVOPage = postTask.get();
                searchVo.setUserList(userVOPage.getRecords());
                searchVo.setPictureVOList(picturePage.getRecords());
                searchVo.setPostList(postVOPage.getRecords());
                return ResultUtils.success(searchVo);
            }catch (Exception e){
                throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"查询异常");
            }
        } else{
            SearchVo searchVo = new SearchVo();
            //根据对应类型去查对应数据
            switch (searchTypeEnum){
                case POST:
                    PostQueryRequest postQueryRequest = new PostQueryRequest();
                    postQueryRequest.setSearchText(searchText);
                    Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                    searchVo.setPostList(postVOPage.getRecords());
                    break;
                case USER:
                    UserQueryRequest userQueryRequest = new UserQueryRequest();
                    userQueryRequest.setUserName(searchText);
                    Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
                    searchVo.setUserList(userVOPage.getRecords());
                    break;
                case PICTURE:
                    Page<Picture> picturePage = pictureService.listPictures(searchText, 1, 10);
                    searchVo.setPictureVOList(picturePage.getRecords());
                    break;
            }
                return ResultUtils.success(searchVo);
        }
    }
}