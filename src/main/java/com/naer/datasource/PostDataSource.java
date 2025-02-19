package com.naer.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naer.model.dto.post.PostQueryRequest;
import com.naer.model.vo.PostVO;
import com.naer.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子服务实现
 *
 * @author Naer
 * 
 */
@Service
@Slf4j
public class PostDataSource  implements DataSource<PostVO> {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVO> doSearch(String searchText, long pageNum, long pageSize) {
        //两个方法在没有使用JSF的项目中是没有区别的
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();

        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        postQueryRequest.setCurrent((int)pageNum);
        postQueryRequest.setPageSize((int)pageSize);
        return postService.listPostVOByPage(postQueryRequest,request);
    }

}




