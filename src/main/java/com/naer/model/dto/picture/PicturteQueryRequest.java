package com.naer.model.dto.picture;

import com.naer.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PicturteQueryRequest extends PageRequest implements Serializable {
    /**
     * 搜索词
     */
    private String searchText;

    private static final long serialVersionUID = 1L;
}
