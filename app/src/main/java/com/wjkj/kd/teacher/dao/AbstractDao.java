package com.wjkj.kd.teacher.dao;


import com.wjkj.kd.teacher.utils.GloableUtils;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public abstract class AbstractDao {

    public Header[] getHttpHeader() {
        Header[] headers;
        headers = new BasicHeader[]{
                new BasicHeader("Cookie", "JSESSIONID=" + GloableUtils.JESSIONID + ";")
        };
        return headers;
    }
}
