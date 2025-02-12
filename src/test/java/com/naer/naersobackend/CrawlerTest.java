package com.naer.naersobackend;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.naer.model.entity.Picture;
import com.naer.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;

    @Test
    void testFetchPicture() throws IOException {
        String url = "https://www.bing.com/images/search?q=小黑子&form=HDRSC2&first=1";
        Document doc = Jsoup.connect(url).get();
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
            Picture p = new Picture(o,title);
            Pictures.add(p);
        }
        System.out.println(Pictures);
    }

    @Test
    void testFestPassage(){
        //1.获取数据
        String json = "{\"current\":1,\"pageSize\":5,\"sortField\":\"priority\",\"sortOrder\":\"descend\",\"reviewStatus\":1}";
        String url = "http://api.codefather.cn/api/column/list/winnow/vo";
        String result = HttpRequest
                .post(url)
                .header("Content-Type", "application/json")
                .body(json)
                .execute()
                .body();
        System.out.println(result);
        //2.json 转对象
//        Map<String,Object> map = JSONUtil.toBean(result, Map.class);
//        System.out.println(map);

    }
}
