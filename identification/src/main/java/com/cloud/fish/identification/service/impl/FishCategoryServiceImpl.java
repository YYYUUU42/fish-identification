package com.cloud.fish.identification.service.impl;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.fish.identification.mapper.FishCategoryDao;
import com.cloud.fish.identification.model.entity.FishCategoryEntity;
import com.cloud.fish.identification.model.entity.FishImgEntity;
import com.cloud.fish.identification.model.vo.CategoryVo;
import com.cloud.fish.identification.service.FishCategoryService;
import com.cloud.fish.identification.service.FishImgService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("fishCategoryService")
public class FishCategoryServiceImpl extends ServiceImpl<FishCategoryDao, FishCategoryEntity> implements FishCategoryService {
    private final static String CATEGORY="category_json";
    private final static Long TTL=12L;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private FishImgService fishImgService;


    @Override
    public Map<String, List<CategoryVo>> getCategoriesFromRedis() {

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        String categoryJson = ops.get(CATEGORY);

        //有缓存数据
        if(StringUtils.isNotEmpty(categoryJson))
        {

            //反序列化
            return JSON.parseObject(categoryJson,new TypeReference<Map<String, List<CategoryVo>>>(){});
        }

        //无缓存数据直接查数据库
        Map<String, List<CategoryVo>> map = getCategories();

        //序列化
        String jsonString = JSON.toJSONString(map);

        //保存到数据库
        ops.set(CATEGORY,jsonString,TTL, TimeUnit.HOURS);

        return map;

    }


    @Override
    public Map<String, List<CategoryVo>> getCategories() {

        //查出全部的分类数据
        List<FishCategoryEntity> list = this.baseMapper.selectList(null);

        //获取第1层分类的信息
        List<FishCategoryEntity> level1 =this.baseMapper.selectList(new QueryWrapper<FishCategoryEntity>().eq("parent_id", 0));

        //全部分类数据的封装
        Map<String, List<CategoryVo>> vo=level1.stream().collect(Collectors.toMap(k->k.getName(), v->{

            //获取当前的分类的下一层分类
            List<FishCategoryEntity> level2 = getParent_id(list, v.getId());


            //封装上面的结果
            List<CategoryVo> category2Vos = null;
            if(level2!=null)
            {
                //第2层分类
                category2Vos = level2.stream().map(l2 -> {

                    CategoryVo vo2 = new CategoryVo(v.getId().toString(), l2.getId().toString(), l2.getName());

                    //第3层分类
                    List<FishCategoryEntity> level3 = getParent_id(list, l2.getId());

                    if (level3 != null) {


                        List<CategoryVo> category3Vos = level3.stream().map(l3 -> {
                            CategoryVo vo3 = new CategoryVo(l2.getParentId().toString(), l3.getId().toString(), l3.getName());

                            //第4层分类
                            List<FishCategoryEntity> level4 = getParent_id(list, l3.getId());

                            if(level4!=null)
                            {
                                List<CategoryVo> category4Vos=level4.stream().map(l4->{
                                    CategoryVo vo4=new CategoryVo(l3.getParentId().toString(),l4.getId().toString(),l4.getName());

                                    //第5层分类
                                    List<FishCategoryEntity> level5 = getParent_id(list, l4.getId());

                                    if(level5!=null)
                                    {
                                        List<CategoryVo> category5Vos=level5.stream().map(l5->{
                                            CategoryVo vo5=new CategoryVo(l4.getParentId().toString(),l5.getId().toString(),l5.getName());

                                            //第6层分类
                                            List<FishCategoryEntity> level6 = getParent_id(list, l5.getId());

                                            if(level6!=null)
                                            {
                                                List<CategoryVo> category6Vos=level6.stream().map(l6->{
                                                    FishImgEntity fishImg = fishImgService.getFishImg(l6.getId());

                                                    return new CategoryVo(l5.getParentId().toString(),l6.getId().toString(),l6.getName(),fishImg.getImgPath());

                                                }).collect(Collectors.toList());


                                                vo5.setCategoryList(category6Vos);
                                            }
                                            return vo5;
                                        }).collect(Collectors.toList());
                                        vo4.setCategoryList(category5Vos);
                                    }
                                    return vo4;
                                }).collect(Collectors.toList());
                                vo3.setCategoryList(category4Vos);
                            }

                            return vo3;
                        }).collect(Collectors.toList());
                        vo2.setCategoryList(category3Vos);
                    }
                    return vo2;
                }).collect(Collectors.toList());
            }
            return category2Vos;
        },(k1, k2) -> k1,LinkedHashMap::new));
        return vo;
    }



    private List<FishCategoryEntity> getParent_id(List<FishCategoryEntity> selectList, Long parent_id) {
        List<FishCategoryEntity> collect = selectList.stream().filter(item -> {
            return item.getParentId().equals(parent_id);
        }).collect(Collectors.toList());
        return collect;
    }


}