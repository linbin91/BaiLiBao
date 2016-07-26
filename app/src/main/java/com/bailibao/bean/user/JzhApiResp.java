package com.bailibao.bean.user;

import java.util.Map;

/**
 * Created by Administrator on 2016/7/10.
 */
public class JzhApiResp {
    public static final String RSP_SUCCESS = "000000";
    public static final String RSP_ERROR = "009999";
    private String respMsg;
    private String respCode;
    private Map<String, String> formData;


    public String getRespDesc() {
        return respMsg;
    }

    public void setRespDesc(String respDesc) {
        this.respMsg = respDesc;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public Map<String, String> getFormData() {
        return formData;
    }

    public void setFormData(Map<String, String> formData) {
        this.formData = formData;
    }

    public byte[] toPostData() {
        return null;
    }
}
