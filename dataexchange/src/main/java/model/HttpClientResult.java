package model;

public class HttpClientResult {
    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private String content;

    public HttpClientResult(int scInternalServerError) {
        this.code = scInternalServerError;
    }

    public HttpClientResult(int scInternalServerError, String content) {
        this.code = scInternalServerError;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
