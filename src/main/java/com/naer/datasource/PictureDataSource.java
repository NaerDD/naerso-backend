package com.naer.datasource;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naer.common.ErrorCode;
import com.naer.exception.BusinessException;
import com.naer.model.entity.Picture;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 图片服务实现类
 */
@Service
public class PictureDataSource implements DataSource<Picture> {

    public Page<Picture> doSearch(String searchText, long pageNum, long pageSize)  {
        int current = (int) ((pageNum-1)*pageSize);
        String url = String.format("https://www.bing.com/images/search?q=%s&form=HDRSC2&first=%s",searchText,current);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据获取异常");
        }
        Elements elements = doc.select(".iuscp.isv");
        ArrayList<Picture> Pictures = new ArrayList<>();
        for(Element element:  elements){
            //取图片地址
            String m = element.select(".iusc").get(0).attr("m");
            //m里面是一个json字符串
            Map<String,Object> map = JSONUtil.toBean(m, Map.class);
            String o = (String)map.get("murl");
            //取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            Picture p = new Picture(title,o);
            Pictures.add(p);
            //每次10条
            if(Pictures.size() >= pageSize){
                break;
            }
        }
        Page<Picture> picturePage = new Page<>(pageNum,pageSize);
        picturePage.setRecords(Pictures);
        return picturePage;
    }

}
