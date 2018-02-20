package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by Koske Kimutai [2016/04/22]
 */
public class Observation extends Entity implements Comparable {

    @DatabaseField
    private Date obsDate;
    @DatabaseField(foreignAutoRefresh = true, foreign = true, foreignAutoCreate = false, columnName = "concept",
            columnDefinition = "Integer REFERENCES mconcept(id) ON DELETE SET NULL ON UPDATE SET NULL")
    private MConcept mConcept;

    private int mConceptPk;
    @DatabaseField
    private String valueText;
    @DatabaseField
    private String valueMultipleChoice;
    @DatabaseField
    private int valueNumeric;
    @DatabaseField
    private double valueDecimal;
    @DatabaseField
    private int valueLookup;
    @DatabaseField
    private Date valueDate;
    @DatabaseField
    private Date valueDateTime;
    @DatabaseField
    private Boolean valueBoolean;

    @DatabaseField(foreignAutoRefresh = true, foreign = true, foreignAutoCreate = false, columnName = "encounter", maxForeignAutoRefreshLevel = 5,
            columnDefinition = "Integer REFERENCES encounter(id) ON DELETE CASCADE ON UPDATE CASCADE")
    private transient Encounter encounter;
    private int encounterPk;
    private transient int recordId;

    public Observation() {
    }

    public Observation(Encounter encounter) {
        this.encounter = encounter;
        this.obsDate = new Date();
    }

    public Observation(Encounter encounter, MConcept mConcept, Date obsDate, Date valueDate, double valueDecimal, int valueLookup, int valueNumeric, String valueText) {
        this.encounter = encounter;
        this.mConcept = mConcept;
        this.obsDate = obsDate;
        this.valueDate = valueDate;
        this.valueDecimal = valueDecimal;
        this.valueLookup = valueLookup;
        this.valueNumeric = valueNumeric;
        this.valueText = valueText;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public MConcept getmConcept() {
        return mConcept;
    }

    public void setmConcept(MConcept mConcept) {
        this.mConcept = mConcept;
    }

    public Date getObsDate() {
        return obsDate;
    }

    public void setObsDate(Date obsDate) {
        this.obsDate = obsDate;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public double getValueDecimal() {
        return valueDecimal;
    }

    public void setValueDecimal(double valueDecimal) {
        this.valueDecimal = valueDecimal;
    }

    public int getValueLookup() {
        return valueLookup;
    }

    public void setValueLookup(int valueLookup) {
        this.valueLookup = valueLookup;
    }

    public int getValueNumeric() {
        return valueNumeric;
    }

    public void setValueNumeric(int valueNumeric) {
        this.valueNumeric = valueNumeric;
    }

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }

    public Date getValueDateTime() {
        return valueDateTime;
    }

    public void setValueDateTime(Date valueDateTime) {
        this.valueDateTime = valueDateTime;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(Boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
    }

    public String getValueMultipleChoice() {
        return valueMultipleChoice;
    }

    public void setValueMultipleChoice(String valueMultipleChoice) {
        this.valueMultipleChoice = valueMultipleChoice;
    }

    public String[] getValueMultipleChoiceArray() {
        return valueMultipleChoice.split(",");
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getObsvalueString() {
        /*
        TEXT, TEXTMULTI,
    NUMERIC, NUMERICDECIMAL,
    SELECT, MULTISELECT,
    DATETIME, DATE, TIME,
    YESNO
         */
        String obs = "";
        if (mConcept.getDataTypeMap().getDataType() == MDataType.TEXT) {
            obs = valueText;
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.TEXTMULTI) {
            obs = valueText;
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.NUMERIC) {
            obs = String.valueOf(valueNumeric);
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.NUMERICDECIMAL) {
            obs = String.valueOf(valueDecimal);
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.DATE) {
            obs = getDateString(valueDate);
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.DATETIME) {
            obs = getDateString(valueDateTime);
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.SELECT) {
            obs = String.valueOf(valueNumeric);
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.MULTISELECT) {
            obs = String.valueOf(valueMultipleChoice);
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.YESNO) {
            obs = "0";
            if (valueBoolean == true) {
                obs = "1";
            }
        }
        return obs;
    }

    public void setObsValue(Object object) {
        /*
        TEXT, TEXTMULTI,
    NUMERIC, NUMERICDECIMAL,
    SELECT, MULTISELECT,
    DATETIME, DATE, TIME,
    YESNO
         */
        String obs = "";
        if (mConcept.getDataTypeMap().getDataType() == MDataType.TEXT) {
            valueText = object.toString();
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.TEXTMULTI) {
            valueText = object.toString();
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.NUMERIC) {
            valueNumeric = (int) object;
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.NUMERICDECIMAL) {
            valueDecimal = (double) object;
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.DATE) {
            valueDate = (Date) object;
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.DATETIME) {
            valueDateTime = (Date) object;
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.SELECT) {
            valueNumeric = (int) object;
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.MULTISELECT) {
            valueMultipleChoice = object.toString();
        }
        if (mConcept.getDataTypeMap().getDataType() == MDataType.YESNO) {
            valueBoolean = (boolean) object;
        }
    }

    public boolean hasYesNo() {
        return mConcept.getDataTypeMap().getDataType() == MDataType.YESNO;
    }

    public String getYesNoAlternativeValue() {
        if (valueBoolean == true) {
            return "1";
        }
        return "0";
    }
    public String getDateString(Date date) {
        DateTime dt = new DateTime(date);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMM yyyy HH:mm:ss");
        return fmt.print(dt);
    }

    public boolean valueInMultipleChoice(String value) {
        boolean contains = false;
        for (String item : getValueMultipleChoiceArray()) {
            if (value.equalsIgnoreCase(item)) {
                contains = true;
                break; // No need to look further.
            }
        }
        return contains;
    }

    public int getmConceptPk() {
        return mConceptPk;
    }

    public void setmConceptPk(int mConceptPk) {
        this.mConceptPk = mConceptPk;
    }

    public int getEncounterPk() {
        return encounterPk;
    }

    public void setEncounterPk(int encounterPk) {
        this.encounterPk = encounterPk;
    }

    public boolean isRequired() {
        if (mConcept != null) {
            return mConcept.isRequired();
        }
        return false;
    }
    @Override
    public String toString() {
        return getDateString(obsDate) + "|" + encounter.getEncounterType().getDisplay() + "|" + mConcept.getDisplay() + "|" + getObsvalueString();
    }

    @Override
    public int compareTo(Object observation) {
        double compareRank = ((Observation) observation).getmConcept().getRank();

        // ascending order
        return (int) (this.getmConcept().getRank() - compareRank);
    }


    @Override
    public boolean equals(Object ob) {
        if (ob == null) {
            return false;
        }
        if (this == ob) {
            return true;
        }
        if (ob instanceof Observation) {
            Observation other = (Observation) ob;
            return this.getId() == other.getId();
        }
        return false;
    }
}
