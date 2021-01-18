package com.yzh.dao.exportModel;

import com.yzh.dao.EModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yzh
 * @create 2021-01-14 10:19
 * @details
 */
public class EModels {
    private List<EModel> models = new ArrayList();

    public List<EModel> getModels() {
        return models;
    }

    public void setModels(List<EModel> models) {
        this.models = models;
    }
}
