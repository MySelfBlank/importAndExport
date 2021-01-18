package com.yzh.utilts;

import cn.hutool.core.util.ObjectUtil;
import com.yzh.dao.EForm;
import onegis.psde.dictionary.FormEnum;
import onegis.psde.form.FormStyle;
import onegis.psde.form.FormStyles;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yzh
 * @create 2020-12-18 9:36
 * @details
 */
public class FormStyleUtils {
    public static List<EForm> forms2EForms(FormStyles formStyles) {
        List<EForm> eFormList = new ArrayList<>();
        if (ObjectUtil.isEmpty(formStyles) && ObjectUtil.isNull(formStyles)) {
            return eFormList;
        }
        List<FormStyle> styles = formStyles.getStyles();
        for (FormStyle style : styles) {
            eFormList.add(formStyle2EForm(style));
        }
        return eFormList;
    }

    public static EForm formStyle2EForm(FormStyle style) {

        EForm eForm = new EForm();
        eForm.setId(style.getId());
        //Dim 形态的维度 0，1，2，3
        if (ObjectUtil.isNotNull(style.getType())) {
            eForm.setType(style.getType().getName().toLowerCase());
            if (eForm.getType().equals("Bim")) {
                eForm.setType(FormEnum.getEnum(50).getName().toLowerCase());
            }
        }
        if (style.getType() == null || style.getType().getValue() < 30) {
            eForm.setDim(2);
        } else {
            eForm.setDim(3);
        }

        if (ObjectUtil.isNotNull(style.getMaxGrain())) {
            eForm.setMaxGrain(style.getMaxGrain());
        }
        if (ObjectUtil.isNotNull(style.getMinGrain())) {
            eForm.setMinGrain(style.getMinGrain());
        }
        return eForm;
    }
}
