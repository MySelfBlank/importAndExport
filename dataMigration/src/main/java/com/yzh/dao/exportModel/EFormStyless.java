package com.yzh.dao.exportModel;

import onegis.psde.dictionary.FormEnum;
import onegis.psde.form.FormStyle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yzh
 * @create 2021-01-14 10:43
 * @details
 */
public class EFormStyless {

    private List<EFormStyles> styles;

    public EFormStyless() {
        this.styles = new ArrayList();
    }

    public List<EFormStyles> getStyles() {
        return this.styles;
    }

    public FormStyle getFormStyleByType(int type) {
        if (this.styles != null && this.styles.size() > 0) {
            Iterator var2 = this.styles.iterator();

            while(var2.hasNext()) {
                FormStyle formStyle = (FormStyle)var2.next();
                FormEnum formEnum = formStyle.getType();
                if (formEnum == null) {
                    return null;
                }

                if (type == formEnum.getValue()) {
                    return formStyle;
                }
            }
        }

        return null;
    }

    public void setStyles(List<EFormStyles> styles) {
        this.styles = styles;
    }


    public List<Long> toList() {
        return (List)this.styles.stream().map((f) -> {
            return f.getId();
        }).collect(Collectors.toList());
    }

    public String toString() {
        String result = "[]";
        if (this.styles != null) {
            List<Long> formStyleIdList = (List)this.styles.stream().map((f) -> {
                return f.getId();
            }).collect(Collectors.toList());
            result = formStyleIdList.toString();
        }

        return result;
    }
}
