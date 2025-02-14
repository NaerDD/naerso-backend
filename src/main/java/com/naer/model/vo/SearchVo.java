package com.naer.model.vo;

import com.google.gson.Gson;
import com.naer.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 聚合搜索  为了不让前端一上来发起多个请求 将多个请求封装到一个中
 *
 * @author Naer
 * 
 */
@Data
public class SearchVo implements Serializable {

    private List<UserVO> UserList;

    private List<PostVO> PostList;

    private List<Picture> PictureVOList;

    private static final long serialVersionUID = 1L;
}