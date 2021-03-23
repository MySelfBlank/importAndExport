package com.yzh.dao;

import onegis.psde.reference.TimeReferenceSystem;
import org.apache.commons.lang.StringUtils;


/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/23 15:42
 */
public class ETrs extends TimeReferenceSystem {
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getName() == null ? 0 : getName().hashCode());
        result = 31 * result + (getId() == null ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof TimeReferenceSystem) {
            TimeReferenceSystem trs = (TimeReferenceSystem) obj;
            if (equalsStr(this.getName(), trs.getName()) &&
                    equalsStr(this.getId(), trs.getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean equalsStr(String str1, String str2) {
        if (StringUtils.isEmpty(str1) && StringUtils.isEmpty(str2)) {
            return true;
        }
        if (!StringUtils.isEmpty(str1) && str1.equals(str2)) {
            return true;
        }
        return false;
    }
}
