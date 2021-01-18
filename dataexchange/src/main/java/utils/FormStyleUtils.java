package utils;

import model.EForm;
import onegis.common.utils.IdMakerUtils;
import onegis.psde.dictionary.FormEnum;
import onegis.psde.form.FormStyle;
import onegis.psde.form.FormStyles;

import java.util.ArrayList;
import java.util.List;

public class FormStyleUtils {

    public static List<EForm> dsFormStyles2EForms(FormStyles formStyles) throws Exception {
        if (formStyles == null || formStyles.getStyles() == null) {
            return new ArrayList<>();
        }
        List<FormStyle> styles = formStyles.getStyles();
        List<EForm> eForms = new ArrayList<>();
        for (FormStyle formStyle : styles) {
            eForms.add(dsFormStyle2EForm(formStyle));
        }
        return eForms;
    }

    public static EForm dsFormStyle2EForm(FormStyle formStyle) throws Exception {
        EForm eForm = new EForm();
        eForm.setId(new IdMakerUtils().nextId());
        if (formStyle.getMinGrain() != null) {
            eForm.setMinGrain(formStyle.getMinGrain());
        }
        if (formStyle.getMaxGrain() != null) {
            eForm.setMaxGrain(formStyle.getMaxGrain());
        }
        if (formStyle.getType() != null) {
            eForm.setType(formStyle.getType().getName().toLowerCase());
            if (eForm.getType().equals("bim")){
                eForm.setType(FormEnum.getEnum(50).getName().toLowerCase());
            }
        }
        if (formStyle.getType() == null || formStyle.getType().getValue() < 30) {
            eForm.setDim(2);
        } else {
            eForm.setDim(3);
        }
        return eForm;
    }
}
