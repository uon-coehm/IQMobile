package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/22]
 */
public class MDataTypeMap extends Entity {
    @ForeignCollectionField(eager = true)
    ForeignCollection<MConcept> concepts;
    List<MConcept> newConcepts = new ArrayList<>();
    @DatabaseField(dataType = DataType.ENUM_STRING)
    private MDataType dataType;
    @DatabaseField
    private String iqType;


    public MDataTypeMap() {
    }

    public MDataTypeMap(MDataType dataType, String iqType) {
        this.dataType = dataType;
        this.iqType = iqType;
    }

    public MDataType getDataType() {
        return dataType;
    }

    public void setDataType(MDataType dataType) {
        this.dataType = dataType;
    }

    public String getIqType() {
        return iqType;
    }

    public void setIqType(String iqType) {
        this.iqType = iqType;
    }

    public ForeignCollection<MConcept> getConcepts() {
        return concepts;
    }

    public List<MConcept> getConceptssList() {
        return new ArrayList<>(concepts);
    }

    public List<MConcept> getNewConcepts() {
        return newConcepts;
    }

    @Override
    public String toString() {
        return getId() + ". " + dataType + " " + iqType;
    }

    public void addConcept(MConcept concept) {
        newConcepts.add(concept);
    }

    public void addEncounterTypes(List<MConcept> conceptList) {
        for (MConcept e : conceptList) {
            addConcept(e);
        }
    }
}
