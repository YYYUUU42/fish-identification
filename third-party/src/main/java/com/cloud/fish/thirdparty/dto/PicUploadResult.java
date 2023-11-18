package com.cloud.fish.thirdparty.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PicUploadResult
{
    private boolean isLegal;

    private String imgPath;

    private List<String> imgPahts;

}

