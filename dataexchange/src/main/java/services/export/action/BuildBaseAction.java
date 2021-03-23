package services.export.action;

import enums.EActionEnum;
import model.EAction;
import model.EActionEvent;
import model.EBase;
import model.EVersion;
import onegis.common.utils.GeneralUtils;
import onegis.psde.psdm.Action;
import onegis.psde.psdm.OBase;
import onegis.psde.psdm.SObject;
import onegis.psde.reference.SpatialReferenceSystem;
import onegis.psde.reference.TimeReferenceSystem;

import java.util.List;

public class BuildBaseAction {

    /**
     * 判断基本信息的变化，目前只判断code，生成action
     *
     * @param eVersion
     * @param lastSObject
     * @param thisSObject
     */
    public static void setBaseAction(EVersion eVersion, SObject lastSObject, SObject thisSObject) {
        // 编码
        String lastCode = lastSObject.getCode() == null ? "" : lastSObject.getCode();
        String thisCode = thisSObject.getCode() == null ? "" : thisSObject.getCode();
        // 父对象
        List<OBase> lastParents = lastSObject.getParents();
        List<OBase> thisParents = thisSObject.getParents();
        // 空间参考
        TimeReferenceSystem lastTimeReferenceSystem = lastSObject.getTrs();
        TimeReferenceSystem thisTimeReferenceSystem = thisSObject.getTrs();
        // 空间参考
        SpatialReferenceSystem lastSpatialReferenceSystem = lastSObject.getSrs();
        SpatialReferenceSystem thisSpatialReferenceSystem = thisSObject.getSrs();
        EBase eBase = new EBase();
        // 编码
        if (!lastCode.equals(thisCode)) {
            eBase.setCode(thisCode);
        }
        // 父对象
        if (GeneralUtils.isNotEmpty(thisParents)) {
            if (GeneralUtils.isNotEmpty(lastParents)) {
                OBase lastOBase = lastParents.get(0);
                OBase thisOBase = thisParents.get(0);
                if (!lastOBase.getId().equals(thisOBase.getId())) {
                    eBase.setParentList(thisParents);
                }
            } else {
                eBase.setParentList(thisParents);
            }
        }
        // 时间参考
        if (GeneralUtils.isNotEmpty(thisTimeReferenceSystem)) {
            if (GeneralUtils.isNotEmpty(lastTimeReferenceSystem)) {
                String lastTrsId = lastTimeReferenceSystem.getId();
                String thisTrsId = thisTimeReferenceSystem.getId();
                if (!lastTrsId.equals(thisTrsId)) {
                    eBase.setTrs(thisTimeReferenceSystem);
                }
            } else {
                eBase.setTrs(thisTimeReferenceSystem);
            }
        }
        // 空间参考
        if (GeneralUtils.isNotEmpty(thisSpatialReferenceSystem)) {
            if (GeneralUtils.isNotEmpty(lastSpatialReferenceSystem)) {
                String lastSrsId = lastSpatialReferenceSystem.getId();
                String thisSrsId = thisSpatialReferenceSystem.getId();
                if (!lastSrsId.equals(thisSrsId)) {
                    eBase.setSrs(thisSpatialReferenceSystem);
                }
            } else {
                eBase.setSrs(thisSpatialReferenceSystem);
            }
        }
        isDeleteVersion(eVersion, thisSObject);

        EAction eAction = new EAction();
        eAction.setOperation(new EActionEvent(EActionEnum.MODIFY, EActionEnum.BASE));

        eVersion.setBase(eBase);
        eVersion.addAction(eAction);

    }

    /**
     * 判断是否是消亡版本
     *
     * @param eVersion
     * @param thisSObject
     */
    private static void isDeleteVersion(EVersion eVersion, SObject thisSObject) {
        // 获取当前版本所有动作，如果动作中包含删除对象，则表示该版本为消亡版本
        List<Action> actionList = thisSObject.getActions();
        for (Action action : actionList) {
            if (action.getOperation().equals(Action.DELETE | Action.BASE)) {
                EAction eAction = new EAction();
                eAction.setOperation(new EActionEvent(EActionEnum.DELETE, EActionEnum.BASE));
                eVersion.addAction(eAction);
            }
        }
    }
}
