package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import onegis.psde.psdm.OBase;
import onegis.psde.reference.SpatialReferenceSystem;
import onegis.psde.reference.TimeReferenceSystem;

import java.util.List;
import java.util.Objects;

public class EBase {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OBase> parentList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TimeReferenceSystem trs;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SpatialReferenceSystem srs;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<OBase> getParentList() {
        return parentList;
    }

    public void setParentList(List<OBase> parentList) {
        this.parentList = parentList;
    }

    public TimeReferenceSystem getTrs() {
        return trs;
    }

    public void setTrs(TimeReferenceSystem trs) {
        this.trs = trs;
    }

    public SpatialReferenceSystem getSrs() {
        return srs;
    }

    public void setSrs(SpatialReferenceSystem srs) {
        this.srs = srs;
    }

    @Override
    public String toString() {
        return "EBase{" +
                "code='" + code + '\'' +
                ", parentList=" + parentList +
                ", trs=" + trs +
                ", srs=" + srs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EBase eBase = (EBase) o;
        return Objects.equals(code, eBase.code) &&
                Objects.equals(parentList, eBase.parentList) &&
                Objects.equals(trs, eBase.trs) &&
                Objects.equals(srs, eBase.srs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, parentList, trs, srs);
    }
}
