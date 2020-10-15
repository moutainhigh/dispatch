package org.example.dispatch.request;

/**
 * @author tangaq
 * @date 2020/10/14
 */
public class CommonResponse {
    private EnumResponseType responseType;

    private String errMessage;

    private String errCode;

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getErrCode() {
        return errCode;
    }

    public EnumResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(EnumResponseType responseType) {
        this.responseType = responseType;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
}
