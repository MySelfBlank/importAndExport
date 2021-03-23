package services.export.action;

import com.vividsolutions.jts.geom.Geometry;
import enums.EActionEnum;
import enums.EFormEnum;
import model.*;
import onegis.common.utils.GeneralUtils;
import onegis.common.utils.JsonUtils;
import onegis.psde.dictionary.FormEnum;
import onegis.psde.form.Form;
import onegis.psde.form.Forms;
import onegis.psde.form.ModelBlock;
import onegis.psde.io.OsmReader;
import onegis.psde.psdm.SObject;
import services.export.ExecuteContainer;
import services.RequestServices;
import services.impl.RequestServicesImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildFormAction {
    /**
     * 判断形态的变化
     *
     * @param eVersion
     * @param lastSObject
     * @param thisSObject
     */
    public static void setFormAction(EVersion eVersion, SObject lastSObject, SObject thisSObject) {
        Forms lastForms = lastSObject.getForms();
        List<Form> lastFormList = new ArrayList<>();
        Map<Long, Form> lastFormMap = new HashMap<>();
        if (lastForms != null) {
            lastFormList = lastForms.getForms();
            lastFormList.forEach(form -> lastFormMap.put(form.getFid(), form));
        }
        Forms thisForms = thisSObject.getForms();
        List<Form> thisFormList = new ArrayList<>();
        Map<Long, Form> thisFormMap = new HashMap<>();
        if (thisForms != null) {
            thisFormList = thisForms.getForms();
            thisFormList.forEach(form -> thisFormMap.put(form.getFid(), form));
        }

        // 遍历上一版本的形态
        for (Form lastForm : lastFormList) {
            Long id = lastForm.getId();
            Long fid = lastForm.getFid();
            Form thisForm = thisFormMap.get(fid);
            if (thisForm == null) {
                operationForm(eVersion, lastForm, EActionEnum.DELETE);
            } else if (!thisForm.getId().equals(id)) {
                thisForm.setId(lastForm.getId());
                operationForm(eVersion, thisForm, EActionEnum.MODIFY);
            }
        }

        // 遍历当前版本的形态
        for (Form thisForm : thisFormList) {
            Long fid = thisForm.getFid();
            Form lastForm = lastFormMap.get(fid);
            if (lastForm == null) {
                operationForm(eVersion, thisForm, EActionEnum.ADDING);
            }
        }
    }

    /**
     * 记录对象属性的变化
     *
     * @param eVersion
     * @param form
     * @param eActionEnum
     */
    private static void operationForm(EVersion eVersion, Form form, EActionEnum eActionEnum) {
        if (form == null) {
            return;
        }
        Long id = form.getId();
        EForm eForm = getEForm(form);
        EAction eAction = new EAction();
        eAction.setId(id);
        eAction.setOperation(new EActionEvent(eActionEnum, EActionEnum.FORM));

        eVersion.addAction(eAction);

        if (!eActionEnum.equals(EActionEnum.DELETE)) {
            eVersion.addForm(eForm);
        }
    }

    /**
     * Form转EForm
     */
    public static EForm getEForm(Form form) {
        if (form == null) {
            return null;
        }
        EForm eForm = new EForm();
        eForm.setId(form.getId());
        eForm.setFid(form.getFid());
        eForm.setType(EFormEnum.getEnum(form.getType().getValue()).getName());
        eForm.setDim(GeneralUtils.isNotEmpty(form.getId()) ? form.getDim() : 2);
        eForm.setMinGrain(form.getMinGrain());
        eForm.setMaxGrain(form.getMaxGrain());
        eForm.setStyle(form.getStyle());
        try {
            EPosition ePosition = new EPosition();
            Object geom = form.getGeom();
            if (geom instanceof Geometry) {
                String geoJson = OsmReader.geometryToGeoJson((Geometry) geom);
                ePosition.setData(JsonUtils.jsonToPojo(geoJson.replace("type", "geotype"), EGeom.class));
                ePosition.getData().setGeotype(ePosition.getData().getGeotype().toLowerCase());
                ePosition.setId(form.getPosition().getId());
            }
            eForm.setGeom(ePosition);
        } catch (Exception ex) {
            System.out.println("转形态异常：" + ex.getMessage());
        }

        // 设置形态引用

        EFormRef eFormRef = new EFormRef();
        if (FormEnum.MODEL.equals(form.getType())) {
            ModelBlock modelBlock = (ModelBlock) form.getFormref();
            eFormRef.setName(GeneralUtils.isNotEmpty(modelBlock.getName()) ? modelBlock.getName() : "");
            eFormRef.setDesc(GeneralUtils.isNotEmpty(modelBlock.getDes()) ? modelBlock.getDes() : "");

            String fname = modelBlock.getFname();
            if (GeneralUtils.isNotEmpty(fname)) {
                fname = fname.replaceAll("/", "_").replaceAll(":", "");
            }
            fname = fname + "_" + modelBlock.getRefid();
            if (!GeneralUtils.isNotEmpty(fname) || fname.contains("?")) {
                fname = modelBlock.getName() + "_" + modelBlock.getRefid() + "";
            }
            eFormRef.setFname(fname);
            eFormRef.setExtension(GeneralUtils.isNotEmpty(modelBlock.getExtension()) ? modelBlock.getExtension() : "");

            if (!"".equals(eFormRef.getName()) && !"".equals(eFormRef.getFname())) {
                eForm.setFormRef(eFormRef);
                Long modelID = modelBlock.getRefid();
                ExecuteContainer.addModelId(modelID);
                ExecuteContainer.addModelName(modelID + "", fname);
            } else if (modelBlock.getRefid() != null) {
                Long fid = modelBlock.getRefid();

                try {
                    // 读取模型信息(线上)
                    RequestServices requestServices = new RequestServicesImpl();
                    List<ModelBlock> modelBlocks = requestServices.getModel(fid);
                    if (modelBlocks != null && modelBlocks.size() > 0) {
                        modelBlock = modelBlocks.get(0);
                        eFormRef.setName(GeneralUtils.isNotEmpty(modelBlock.getName()) ? modelBlock.getName() : "");
                        eFormRef.setDesc(GeneralUtils.isNotEmpty(modelBlock.getDes()) ? modelBlock.getDes() : "");

                        fname = modelBlock.getFname();
                        if (GeneralUtils.isNotEmpty(fname)) {
                            fname = fname.replaceAll("/", "_").replaceAll(":", "");
                        }
                        fname = fname + "_" + modelBlock.getRefid();
                        if (!GeneralUtils.isNotEmpty(fname) || fname.contains("?")) {
                            fname = modelBlock.getName() + "_" + fid + "";
                        }
                        eFormRef.setFname(fname);

                        eFormRef.setExtension(GeneralUtils.isNotEmpty(modelBlock.getExtension()) ? modelBlock.getExtension() : "");
                        eForm.setFormRef(eFormRef);
                        // 下载
//                            downLoadModelIDs.add(fid);
//                            modelIdMaps.put(fid, fname);
                        ExecuteContainer.addModelId(fid);
                        ExecuteContainer.addModelName(fid + "", fname);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return eForm;
    }
}
