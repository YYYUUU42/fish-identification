package com.cloud.fish.identification.utils;

import java.io.File;

public class ImgUtils {
    public static void deleteImg(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                String[] list = file.list();
                if (list != null && list.length > 0) {
                    for (String s : list) {
                        File file1 = new File(file, s);
                        deleteImg(file1);
                        String[] strs = s.split("\\.");
                        if (strs.length > 0) {
                            String type = strs[strs.length - 1];
                            if (type.equals("png") || type.equals("jpg") || type.equals("jpeg")) {
                                boolean b = file1.delete();
                                System.out.println(file1.getAbsolutePath() + "\t" + b);
                            }
                        }
                    }
                }
            }
        }
    }

}
