package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.GeometryFactory;
import enums.EFormEnum;
import onegis.psde.form.Position;

public class Form1 {
    @JsonIgnore
    public static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
    protected Long id = 0L;
    protected Long fid = 0L;
    protected PositionSerialize formref;
    protected PositionSerialize formRef;
    @JsonIgnore
    protected Position position = new Position();
    protected EFormEnum type;
    protected Integer geotype;
    protected String style = "";
    protected Integer dim;
    protected Double minGrain;
    protected Double maxGrain;
    @JsonSerialize(using = CustomerGeomSerialize.class)
    protected CustomerGeom geom;

    protected String geomref;

    public Form1() {
        this.type = EFormEnum.NULL;
        this.dim = -1;
        this.minGrain = 0.0D;
        this.maxGrain = 0.0D;
    }

    public Form1(Long id, EFormEnum type, String style, Integer dim, Double minGrain, Double maxGrain) {
        this.type = EFormEnum.NULL;
        this.dim = -1;
        this.minGrain = 0.0D;
        this.maxGrain = 0.0D;
        this.id = id;
        this.type = type;
        this.style = style;
        this.dim = dim;
        this.minGrain = minGrain;
        this.maxGrain = maxGrain;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setGeometry(Object geometry) throws Exception {
        this.position.setGeometry(geometry);
    }

    public void setPositionID(Long positionID) {
        this.position.setId(positionID);
    }

    public EFormEnum getType() {
        return this.type;
    }

    public void setType(EFormEnum type) {
        this.type = type;
    }

    public Integer getDim() {
        return this.dim;
    }

    public void setDim(Integer dim) {
        this.dim = dim;
    }

    public Double getMinGrain() {
        return this.minGrain;
    }

    public void setMinGrain(Double minGrain) {
        this.minGrain = minGrain;
    }

    public Double getMaxGrain() {
        return this.maxGrain;
    }

    public void setMaxGrain(Double maxGrain) {
        this.maxGrain = maxGrain;
    }

    public String getStyle() {
        return this.style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Long getFid() {
        return this.fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    /*public final Object getGeom() throws Exception {
        return this.position != null && this.position.geometry() != null ? this.position.geometry() : null;
    }*/

    public PositionSerialize getFormref() {
        return formref;
    }

    public void setFormref(PositionSerialize formref) {
        this.formref = formref;
    }

    public CustomerGeom getGeom() {
        return geom;
    }

    public void setGeom(CustomerGeom geom) {
        this.geom = geom;
    }

    public Integer getGeotype() {
        return geotype;
    }

    public void setGeotype(Integer geotype) {
        this.geotype = geotype;
    }

    public String getGeomref() {
        return geomref;
    }

    public void setGeomref(String geomref) {
        this.geomref = geomref;
    }

    public PositionSerialize getFormRef() {
        return formRef;
    }

    public void setFormRef(PositionSerialize formRef) {
        this.formRef = formRef;
    }

}
