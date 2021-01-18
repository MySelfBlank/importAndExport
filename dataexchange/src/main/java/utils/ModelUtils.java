package utils;

import model.EModel;
import model.EModelDef;
import onegis.psde.model.Model;
import onegis.psde.model.ModelDef;
import onegis.psde.model.Models;

import java.util.ArrayList;
import java.util.List;

public class ModelUtils {

    public static List<EModel> dsModels2EModels(Models models) throws Exception {
        if (models == null || models.getModels() == null) {
            return new ArrayList<>();
        }
        List<Model> modelList = models.getModels();
        List<EModel> eModels = new ArrayList<>(modelList.size());
        for (Model model : modelList) {
            eModels.add(dsModelsEModel(model));
        }
        return eModels;
    }

    private static EModel dsModelsEModel(Model model) throws Exception {
        EModel eModel = new EModel();
        eModel.setId(model.getId());
        eModel.setName(model.getName());
        eModel.setExecutor(model.getExecutor());
        eModel.setInitData(model.getInitData());
        eModel.setMobj(model.getMobj());
        eModel.setpLanguage(model.getpLanguage().getName());
        eModel.setMdef(dsModelDef2EModelDef(model.getMdef()));
        return eModel;
    }

    private static EModelDef dsModelDef2EModelDef(ModelDef modelDef) throws Exception {
        EModelDef eModelDef = new EModelDef();
        eModelDef.setId(modelDef.getId());
        eModelDef.setName(modelDef.getName());
        return eModelDef;
    }
}
