package model;


import onegis.psde.model.Mobj;

/**
 * 模型
 */
public class EModel extends AbstractObject {

    /**
     * 初始化信息
     */
    private String initData;

    /**
     * 行为类
     */
    private EModelDef mdef;

    /**
     * 行为引用
     */
    private Mobj mobj;

    /**
     * 行为执行语言
     */
    private String pLanguage;

    /**
     * 执行器
     */
    private String executor = "";

    public String getInitData() {
        return initData;
    }

    public void setInitData(String initData) {
        this.initData = initData;
    }

    public EModelDef getMdef() {
        return mdef;
    }

    public void setMdef(EModelDef mdef) {
        this.mdef = mdef;
    }

    public Mobj getMobj() {
        return mobj;
    }

    public void setMobj(Mobj mobj) {
        this.mobj = mobj;
    }

    public String getpLanguage() {
        return pLanguage;
    }

    public void setpLanguage(String pLanguage) {
        this.pLanguage = pLanguage;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }
}
