package com.yzh.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import onegis.psde.dictionary.ModelDefType;
import onegis.psde.model.IModel;

/**
 * @author Yzh
 * @create 2020-12-17 8:54
 */
public class Mobj implements IModel {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String script;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String source;

    public Mobj() {
    }

    public String getScript() {
        return this.script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public ModelDefType modelType() {
        return null;
    }

    @Override
    public String inTypes() {
        return null;
    }

    @Override
    public String outTypes() {
        return null;
    }
}
