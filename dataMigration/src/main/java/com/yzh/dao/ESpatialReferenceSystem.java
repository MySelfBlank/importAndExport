package com.yzh.dao;

import onegis.psde.reference.SpatialReferenceSystem;
import org.apache.commons.lang.StringUtils;

/**
 * @author Yzh
 * @create 2020-12-23 15:44
 * @details
 */
public class ESpatialReferenceSystem extends SpatialReferenceSystem {
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (this.getId() == null ? 0 : this.getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;//地址相等
        }

        if (obj == null) {
            return false;//非空性：对于任意非空引用x，x.equals(null)应该返回false。
        }

        if (obj instanceof ESpatialReferenceSystem) {
            ESpatialReferenceSystem other = (ESpatialReferenceSystem) obj;
            //需要比较的字段相等，则这两个对象相等
            if (equalsStr(this.getId(), other.getId())) {
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
