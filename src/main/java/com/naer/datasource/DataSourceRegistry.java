package com.naer.datasource;

import com.naer.model.enums.SearchTypeEnum;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册模式 根据传递的类型 注册不同类型的
 */
@Component
public class DataSourceRegistry {

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    private Map<String,DataSource<T>> typeDataSourceMap;

    /**
     * 使用@PostConstruct注解。这个注解用于标记一个方法，这个方法将在Bean初始化完成后被执行。而且，它是所有注解中最后一个执行的。
     *  @PostConstruct 的使用和特点：
     * 只有一个非静态方法能使用此注解；
     * 被注解的方法不得有任何参数；
     * 被注解的方法返回值必须为void；
     * 被注解方法不得抛出已检查异常；
     * 此方法只会被执行一次；
     *
     * @PostConstruct注解用于指定一个方法在对象创建后由容器自动执行，用于完成一些初始化操作
     */
    @PostConstruct
    public void init(){
        System.out.println(1);
        typeDataSourceMap = new HashMap(){{
            put(SearchTypeEnum.POST.getValue(),postDataSource);
            put(SearchTypeEnum.USER.getValue(),userDataSource);
            put(SearchTypeEnum.PICTURE.getValue(),pictureDataSource);
        }};
    }

    public DataSource getDataSourceByType(String type){
        if(typeDataSourceMap == null){
            return null;
        }
        return typeDataSourceMap.get(type);
    }
}
