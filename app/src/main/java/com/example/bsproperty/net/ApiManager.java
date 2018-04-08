package com.example.bsproperty.net;

/**
 * Created by yezi on 2018/1/27.
 */

public class ApiManager {

    private static final String HTTP = "http://";
    private static final String IP = "192.168.55.102";
    private static final String PROT = ":8080";
    private static final String HOST = HTTP + IP + PROT;
    private static final String API = "/api";
    private static final String USER = "/user";
    private static final String SHOP = "/shop";
    private static final String PRODUCT = "/product";
    private static final String ORDER = "/order";
    private static final String APPLY = "/apply";

    public static final String IMAGE = HOST + API + PRODUCT;

    public static final String USER_RG = HOST + API + USER + "/";
    public static final String USER_LOGIN = HOST + API + USER + "/login";
    public static final String USER_CHANGE = HOST + API + USER + "/change/";

    public static final String SHOP_LIST = HOST + API + SHOP + "/list";
    public static final String SHOP_INFO = HOST + API + SHOP + "/";
    public static final String SHOP_ADD = HOST + API + SHOP + "/add";

    public static final String PRODUCT_ADD = HOST + API + PRODUCT + "/add";

    public static final String ORDER_ADD = HOST + API + ORDER + "/add";
    public static final String ORDER_LIST = HOST + API + ORDER + "/list";

    public static final String APPLY_ADD = HOST + API + APPLY + "/add";
    public static final String APPLY_LIST = HOST + API + APPLY + "/list";
    public static final String APPLY_CHANGE = HOST + API + APPLY + "/change";
}
