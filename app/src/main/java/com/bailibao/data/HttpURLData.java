package com.bailibao.data;

/**
 * Created by Administrator on 2016/5/4.
 */
public class HttpURLData {

    public static final String INTENT_API_DATA_KEY_DATA = "intent_api_data_key_data";

    public  static  final String HOST_BAILIBAO_API = "http://139.196.173.191:42000/blb-api/";
    //富友接口地址
    private static final String JZH_API_BASE_URL = "http://www-1.fuiou.com:9057/jzh";

    //判断用户是否注册过
    public  static  final String APPFUN_USER_EXISTENCE = HOST_BAILIBAO_API + "existence";

    //获取验证码
    public  static  final String APPFUN_USER_CHECKCODE = HOST_BAILIBAO_API + "checkcode";

    //注册
    public  static  final String APPFUN_USER_REGISTER = HOST_BAILIBAO_API + "register";

    //忘记密码
    public  static  final String APPFUN_USER_FORGETPASSWORD = HOST_BAILIBAO_API + "forgetPassword";

    //登入
    public  static  final String APPFUN_USER_LOGIN = HOST_BAILIBAO_API + "login";

    //手势登录密码验证
    public static final String APPFUN_PASSWORD_CHECK = HOST_BAILIBAO_API + "checkPassword";

    //产品界面
    public static  final String APPFUN_PRODUCT_ITEMS = HOST_BAILIBAO_API + "product";

    //消息界面
    public static  final  String APPFUN_MESSAGE = HOST_BAILIBAO_API + "message";

    public static  final  String APPFUN_MESSAGE_READ = HOST_BAILIBAO_API + "message/read";

    //登入首页
    public static  final String APPFUN_INDEX_LOGIN = HOST_BAILIBAO_API + "index";

    //购买提示广播
    public static  final String APPFUN_BUY_TIP = HOST_BAILIBAO_API + "orderTips";

    //新用户还没登入的首页
    public static  final String APPFUN_INDEX_UNLOGIN = HOST_BAILIBAO_API + "product/new";

    //首页的广播
    public static  final String APPFUN_GET_PROFIT = HOST_BAILIBAO_API + "profitTips";

    //个人账户
    public static  final String APPFUN_PERSON_ACCOUNT = HOST_BAILIBAO_API + "account";

    //修改交易的密码
    public  static final String APPFUN_MODIFY_TRADE =HOST_BAILIBAO_API + "forgetTradePassword";

    //退出的接口
    public  static final String APPFUN_LOGOUT = HOST_BAILIBAO_API + "logout";

    //反馈意见的接口
    public static  final String APPFUN_FEEDBACK = HOST_BAILIBAO_API + "feedback";

    //删除消息
    public static  final String APPFUN_DELETE_MESSAGE = HOST_BAILIBAO_API + "message/delete";

    //在投资金列表
    public static final String APPFUN_ACCOUNT_INVEST = HOST_BAILIBAO_API + "account/invest";

    //产品列表（简易）
    public static final String APPFUN_PRODUCT_SIMPLE = HOST_BAILIBAO_API + "product/simple";

    //累计收益列表
    public static final String APPFUN_ACCOUNT_PROFIT = HOST_BAILIBAO_API + "account/profit";

    //充值提现记录
    public static final String APPFUN_USER_ACCOUT = HOST_BAILIBAO_API + "account/trade";

    //交易
    public static final String APPFUN_USER_TRADE = HOST_BAILIBAO_API + "trade";

    //项目进展列表
    public static final String APPFUN_PRODUCT_PROGRESS = HOST_BAILIBAO_API + "product/project";

    //注册
    public static final String APPFUN_MONEY_REGISTER = HOST_BAILIBAO_API + "fuiou/register";

    //支付
    public static final String APPFUN_MONEY_PAY = HOST_BAILIBAO_API + "fuiou/pay";

    //用户购买协议
    public static final String APPFUN_PRODUCT_AGREEMENT = HOST_BAILIBAO_API + "product/agreement";

    //购买产品的具体界面
    public static final String APPFUN_PRODUCT_BUY = HOST_BAILIBAO_API + "order";

    public static final String APPFUN_PRODUCT_PAY = HOST_BAILIBAO_API + "buy";

    public static final String APPFUN_PRODUCT_REDEEM = HOST_BAILIBAO_API + "redeem";

    //获取银行卡的接口
    public static final String APPFUN_BANK_CARD = HOST_BAILIBAO_API + "account/bank";
    //提现接口
    public static final String APPFUN_MONEY_WITHDRAW = HOST_BAILIBAO_API + "withdraw";
    //用户注册协议
    public static final String APPFUN_USER_AGREEMENT = HOST_BAILIBAO_API +"user/agreement";

    //获取广告的接口
    public static final String APPFUN_GET_AD = HOST_BAILIBAO_API + "ad";


    public static final String JZH_API_APP_WEB_REG_URL = JZH_API_BASE_URL+"/app/appWebReg.action";
    public static final String JZH_API_APP_500001_URL = JZH_API_BASE_URL+"/app/500001.action";
    public static final String JZH_API_APP_500002_URL = JZH_API_BASE_URL+"/app/500002.action";
    public static final String JZH_API_APP_500003_URL = JZH_API_BASE_URL+"/app/500003.action";
    public static final String JZH_API_APP_400101_URL = JZH_API_BASE_URL+"/app/400101.action";
    public static final String JZH_API_APP_SIGN_URL = JZH_API_BASE_URL+"/app/appSign_Card";

}
