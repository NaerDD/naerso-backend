package com.naer.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naer.model.entity.Picture;



public interface PictureService {

    Page<Picture> listPictures(String searchText, long pageNum, long pageSize);

}