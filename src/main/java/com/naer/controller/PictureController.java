package com.naer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naer.common.BaseResponse;
import com.naer.common.ErrorCode;
import com.naer.common.ResultUtils;
import com.naer.exception.ThrowUtils;
import com.naer.model.dto.picture.PicturteQueryRequest;
import com.naer.model.entity.Picture;
import com.naer.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 图片接口
 *
 * @author Naer
 * 
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 分页获取列表（封装类）
     *
     * @param picturteQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPostVOByPage(@RequestBody PicturteQueryRequest picturteQueryRequest) {
        long current = picturteQueryRequest.getCurrent();
        long size = picturteQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = picturteQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.listPictures(searchText, current, size);
        return ResultUtils.success(picturePage);
    }

}
