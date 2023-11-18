package com.cloud.fish.identification.controller;
import java.util.Map;
import com.cloud.fish.common.utils.R;
import com.cloud.fish.identification.service.FishCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * 鱼类信息分类
 *
 * @author ${author}
 * @email ${email}
 * @date 2023-03-06 01:56:28
 */
@RestController
@RequestMapping("/fish/fishcategory")
public class FishCategoryController {
    @Autowired
    private FishCategoryService fishCategoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        return R.ok().put("data",fishCategoryService.getCategoriesFromRedis());
    }


}
