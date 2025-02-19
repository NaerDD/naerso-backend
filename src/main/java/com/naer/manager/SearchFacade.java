package com.naer.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naer.common.ErrorCode;
import com.naer.datasource.*;
import com.naer.exception.BusinessException;
import com.naer.exception.ThrowUtils;
import com.naer.model.dto.search.SearchRequest;
import com.naer.model.entity.Picture;
import com.naer.model.enums.SearchTypeEnum;
import com.naer.model.vo.PostVO;
import com.naer.model.vo.SearchVo;
import com.naer.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * 搜索门面类  门面模式
 */
@Component
@Slf4j
public class SearchFacade {

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;


    public SearchVo SearchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String type = searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();
        //搜索出所有数据
        if(searchTypeEnum == null) {
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(()->{
//                UserQueryRequest userQueryRequest = new UserQueryRequest();
//                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userDataSource.doSearch(searchText,current,pageSize);
                return  userVOPage;
            });

            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(()->{
//                PostQueryRequest postQueryRequest = new PostQueryRequest();
//                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postDataSource.doSearch(searchText,current,pageSize);
                return postVOPage;
            });

            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(()->{
                Page<Picture> picturePage =pictureDataSource.doSearch(searchText,current,pageSize);
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
                return searchVo;
            }catch (Exception e){
                log.error("查询异常",e);
                throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"查询异常");
            }
        } else{
            SearchVo searchVo = new SearchVo();
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page<?> page = dataSource.doSearch(searchText, current, pageSize);
            searchVo.setDataList(page.getRecords());
            return searchVo;
        }
    }
}
