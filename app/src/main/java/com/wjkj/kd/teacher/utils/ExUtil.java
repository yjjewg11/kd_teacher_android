package com.wjkj.kd.teacher.utils;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.sql.SQLException;
import java.util.EmptyStackException;

public class ExUtil {
    public static void e(Exception e){
        if(e instanceof JSONException){
            throw new RuntimeException("json解析异常");
//json解析异常
        }else if(e instanceof NullPointerException){
            e.printStackTrace();
            throw new RuntimeException("空指针异常");

//空指针异常
        }else if(e instanceof UnsupportedOperationException){
            e.printStackTrace();
            throw new RuntimeException("不支持的操作异常");
//不支持的操作异常
        }else if(e instanceof IndexOutOfBoundsException){
            throw new RuntimeException("索引下标越界");
//索引越界
        }else if(e instanceof SQLException){
            e.printStackTrace();
            throw new RuntimeException("数据库操作异常");
//数据库操作异常
        }else if(e instanceof ClassCastException){
            e.printStackTrace();
            throw new RuntimeException("类型转换异常");
//类型转换异常
        }else if(e instanceof  NumberFormatException){
            e.printStackTrace();
            throw new RuntimeException("数字解析为字符串异常");
//数字解析为字符串异常
        }else if(e instanceof ArithmeticException){
            e.printStackTrace();
            throw new RuntimeException("算术异常");
//算术异常
        }else if(e instanceof BufferOverflowException){
            e.printStackTrace();
            throw new RuntimeException("向上缓冲异常");
//向上缓冲异常
        }else if(e instanceof BufferUnderflowException){
            e.printStackTrace();
            throw new RuntimeException("向下缓冲异常");
//向下缓冲异常
        }else if(e instanceof EmptyStackException){
            e.printStackTrace();
            throw new RuntimeException("堆栈为空异常");
//堆栈为空异常
        }else if(e instanceof  IllegalArgumentException){
            e.printStackTrace();
            throw new RuntimeException("元素非法异常");
//非法元素
        }else if(e instanceof IllegalAccessException){
            e.printStackTrace();
            throw new RuntimeException("非法存取异常");
//非法存取
        }else if(e instanceof FileNotFoundException){
            e.printStackTrace();
            throw new RuntimeException("文件未找到异常");
//文件未找到
        }else if(e instanceof NoSuchMethodException){
            e.printStackTrace();
            throw new RuntimeException("方法不存在异常");
//方法不存在异常
        }else{
            throw new RuntimeException(e);
        }

    }
}

