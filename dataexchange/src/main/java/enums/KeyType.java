package enums;

public enum KeyType {
    QUERY("查询"),
    PREVIOUS("上一页"),
    NEXT("下一页");

    private String des;

    KeyType(String des) {
        this.des = des;
    }


}
