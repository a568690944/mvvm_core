package com.jiantaokeji.answerliu.mvvm_core.constant;



public class ComParamContact {


        public final static long   DELAY_TIME = 500;//延迟时间 单位：毫秒
        public final static int WINDOW_DURATION=1000;//防抖动时间间隔

        //http请求成功状态码
        public final static String HTTP_SUCESS = "00";
        //用户访问无权限
        public final static String USER_NO_PERMISSION = "701";
        //用户未激活
        public final static String USER_NO_ACTIVATION = "702";
        //用户未登录
        public final static String USER_NO_SIGN_IN = "8000";
        //用户不存在
        public final static String USER_LOGIN_NO_USER = "8001";
        //用户名或密码错误
        public final static String USER_LOGIN_INVALID = "8002";
        //终端IP发生变化，请重新登录
        public final static String USER_LOGIN_IP_CHANG = "8006";
        public final static String TOKEN = "access_token";
        public final static String PATH = "/v1/account/token/refresh";
        public final static String AUTH_MODEL = "authModel";



}
