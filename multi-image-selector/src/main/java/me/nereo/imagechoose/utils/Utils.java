package me.nereo.imagechoose.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.nereo.imagechoose.bean.Image;

/**
 * Created by tangt on 2016/4/25.
 */
public class Utils {
    public static void sort(List<Image> collect_list) {
        Collections.sort(collect_list, new Comparator<Image>() {
            @Override
            public int compare(Image o, Image t) {
                int cha = 0;
                long t1 = o.getTime();
                long t2 = t.getTime();
                if (t2 - t1 > 0) {
                    cha = 1;
                } else if (t2 - t1 < 0) {
                    cha = -1;
                }
                return cha;
            }
        });
    }
    public static boolean stringIsNull(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }
}
