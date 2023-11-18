package com.cloud.fish.identification.model.entity;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FishList {
    @ExcelProperty(value = "序号",index = 0)
    private String id;

    @ExcelProperty(value = "中文名",index = 1)
    private String fishKinds;

    @ExcelProperty(value = "拉丁文",index = 2)
    private String fishLatinName;
}
