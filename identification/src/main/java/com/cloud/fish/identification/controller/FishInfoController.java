package com.cloud.fish.identification.controller;
import com.cloud.fish.common.utils.R;
import com.cloud.fish.identification.model.entity.FishInfoEntity;
import com.cloud.fish.identification.service.FishInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
 * 鱼类信息表
 *
 * @author ${author}
 * @email ${email}
 * @date 2023-02-13 17:38:20
 */
@RestController
@RequestMapping("/fish/fishinfo")
public class FishInfoController {
    @Autowired
    private FishInfoService fishInfoService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(){
        List<FishInfoEntity> list = fishInfoService.queryList();
        return R.ok().put("data",list);
    }


}
