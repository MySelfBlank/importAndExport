package com.yzh.importTest.requestEntity;


import com.yzh.dao.exportModel.AbstractObject;
import onegis.psde.model.IModel;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2021/1/12 15:00
 */
public class ModelEntity extends AbstractObject {
    private ModelDefEntity mdef;
    private Integer pLanguage;
    private IModel mobj;


    public ModelDefEntity getMdef() {
        return mdef;
    }

    public void setMdef(ModelDefEntity mdef) {
        this.mdef = mdef;
    }

    public Integer getpLanguage() {
        return pLanguage;
    }

    public void setpLanguage(Integer pLanguage) {
        this.pLanguage = pLanguage;
    }

    public IModel getMobj() {
        return mobj;
    }

    public void setMobj(IModel mobj) {
        this.mobj = mobj;
    }
}
