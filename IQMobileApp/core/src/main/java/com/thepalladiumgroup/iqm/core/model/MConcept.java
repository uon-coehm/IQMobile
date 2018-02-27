package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/22]
 */
public class MConcept extends Entity implements Comparable<MConcept> {

//    @ForeignCollectionField(eager = false)
//    ForeignCollection<Observation> observations;


    @ForeignCollectionField(eager = true)
    ForeignCollection<MConcept> childrenConcepts;
    @DatabaseField
    private String display;
    @DatabaseField
    private String description;
    @DatabaseField
    private double rank;
    @DatabaseField(foreignAutoRefresh = true, foreign = true, foreignAutoCreate = false)
    private MDataTypeMap dataTypeMap;
    @DatabaseField
    private int lookupcodeid;
    @DatabaseField
    private boolean required;
    @DatabaseField(foreignAutoRefresh = true, foreign = true)
    private MConcept parent;
    @DatabaseField
    private int parentIqcareId;
    @DatabaseField
    private String parentcondition;
    @DatabaseField
    private int patientgender;
    @DatabaseField
    private boolean autcompute;
    @DatabaseField
    private String autcomputelogic;
    @DatabaseField
    private String autcomputeparentchildren;
    @DatabaseField
    private String skipto;
    @DatabaseField
    private String skiptocondition;
    @DatabaseField
    private String parentconditionchildren;
    @DatabaseField
    private String skiptoparent;
    @DatabaseField
    private String skiptoparentcondition;

    @DatabaseField(foreignAutoRefresh = true, foreign = true, foreignAutoCreate = false)
    private transient EncounterType encounterType;
    private transient MDataType dataType;
    private transient String iqDataType;
    private transient List<Lookup> conceptLookups = new ArrayList<>();
    private transient List<ParentCondition> multiparentConditions = new ArrayList<>();

    public MConcept() {
        this.patientgender = 0;
    }

    public MConcept(String display, String description, MDataType dataType, int iqcareid, double rank) {
        this.display = display;
        this.description = description;
        this.dataType = dataType;
        this.setIqcareid(iqcareid);
        this.rank = rank;
        this.patientgender = 0;
    }

    public MConcept(String display, String description, String iqDataType, int iqcareid, double rank) {
        this.display = display;
        this.description = description;
        this.iqDataType = iqDataType;
        this.setIqcareid(iqcareid);
        this.rank = rank;
        this.patientgender = 0;
    }

    public static MConcept CreateConcept(String display, String description, MDataType dataType, int iqcareid, double rank) {
        return new MConcept(display, description, dataType, iqcareid, rank);
    }

    public static MConcept CreateConcept(String display, String description, String iqDataType, int iqcareid, double rank) {
        return new MConcept(display, description, iqDataType, iqcareid, rank);
    }

    public static MConcept CreateConceptWithParent(String display, String description, MDataType dataType, int iqcareid, double rank, int parentIqcareId, String parentcondition) {
        MConcept concept = CreateConcept(display, description, dataType, iqcareid, rank);
        concept.setParentIqcareId(parentIqcareId);
        concept.setParentcondition(parentcondition);
        return concept;
    }

    public static MConcept CreateConceptWithParent(String display, String description, String iqDataType, int iqcareid, double rank, int parentIqcareId, String parentcondition) {
        MConcept concept = CreateConcept(display, description, iqDataType, iqcareid, rank);
        concept.setParentIqcareId(parentIqcareId);
        concept.setParentcondition(parentcondition);
        return concept;
    }

    public static MConcept CreateConceptWithLookUp(String display, String description, MDataType dataType, int iqcareid, double rank, int lookupcodeid) {
        MConcept concept = CreateConcept(display, description, dataType, iqcareid, rank);
        concept.setLookupcodeid(lookupcodeid);
        return concept;
    }

    public static MConcept CreateConceptWithLookUp(String display, String description, String iqDataType, int iqcareid, double rank, int lookupcodeid) {
        MConcept concept = CreateConcept(display, description, iqDataType, iqcareid, rank);
        concept.setLookupcodeid(lookupcodeid);
        return concept;
    }

    public static MConcept CreateConceptWithLookupAndParent(String display, String description, MDataType dataType, int iqcareid, double rank, int parentIqcareId, int lookupcodeid, String parentcondition) {
        MConcept concept = CreateConcept(display, description, dataType, iqcareid, rank);
        concept.setParentIqcareId(parentIqcareId);
        concept.setLookupcodeid(lookupcodeid);
        concept.setParentcondition(parentcondition);
        return concept;
    }

    public static MConcept CreateConceptWithLookupAndParent(String display, String description, String iqDataType, int iqcareid, double rank, int parentIqcareId, int lookupcodeid, String parentcondition) {
        MConcept concept = CreateConcept(display, description, iqDataType, iqcareid, rank);
        concept.setParentIqcareId(parentIqcareId);
        concept.setLookupcodeid(lookupcodeid);
        concept.setParentcondition(parentcondition);
        return concept;
    }

    public MDataTypeMap getDataTypeMap() {
        return dataTypeMap;
    }

    public void setDataTypeMap(MDataTypeMap dataTypeMap) {
        this.dataTypeMap = dataTypeMap;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public int getLookupcodeid() {
        return lookupcodeid;
    }

    public void setLookupcodeid(int lookupcodeid) {
        this.lookupcodeid = lookupcodeid;
    }

    public EncounterType getEncounterType() {
        return encounterType;
    }

    public void setEncounterType(EncounterType encounterType) {
        this.encounterType = encounterType;
    }

    public MDataType getDataType() {
        return dataType;
    }

    public String getIqDataType() {
        return iqDataType;
    }

    public int getParentIqcareId() {
        return parentIqcareId;
    }

    public void setParentIqcareId(int parentIqcareId) {
        this.parentIqcareId = parentIqcareId;
    }

    public MConcept getParent() {
        return parent;
    }

    public ForeignCollection<MConcept> getChildrenConcepts() {
        return childrenConcepts;
    }

    public List<MConcept> getChildrenConceptsList() {
        return new ArrayList<>(childrenConcepts);
    }

    public String getParentcondition() {
        return parentcondition;
    }

    public void setParentcondition(String parentcondition) {
        this.parentcondition = parentcondition;
    }

    public String getCleanParentcondition() {
        return parentcondition.replace("!", "");
    }

    public String getCleanParentconditionChildren() {
        return parentconditionchildren.replace("!", "");
    }
    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<Lookup> getConceptLookups() {
        return conceptLookups;
    }

    public void setConceptLookups(List<Lookup> conceptLookups) {
        this.conceptLookups = conceptLookups;
    }

    public int getPatientgender() {
        return patientgender;
    }

    public void setPatientgender(int patientgender) {
        this.patientgender = patientgender;
    }

    public boolean isAutcompute() {
        return autcompute;
    }

    public void setAutcompute(boolean autcompute) {
        this.autcompute = autcompute;
    }

    public String getAutcomputelogic() {
        return autcomputelogic;
    }

    public void setAutcomputelogic(String autcomputelogic) {
        this.autcomputelogic = autcomputelogic;
    }

    public String getSkipto() {
        return skipto;
    }

    public void setSkipto(String skipto) {
        this.skipto = skipto;
    }

    public String getSkiptocondition() {
        return skiptocondition;
    }

    public void setSkiptocondition(String skiptocondition) {
        this.skiptocondition = skiptocondition;
    }

    public String getAutcomputeparentchildren() {
        return autcomputeparentchildren;
    }

    public void setAutcomputeparentchildren(String autcomputeparentchildren) {
        this.autcomputeparentchildren = autcomputeparentchildren;
    }

    public String getParentconditionchildren() {
        return parentconditionchildren;
    }

    public void setParentconditionchildren(String parentconditionchildren) {
        this.parentconditionchildren = parentconditionchildren;
    }

    public String getSkiptoparent() {
        return skiptoparent;
    }

    public void setSkiptoparent(String skiptoparent) {
        this.skiptoparent = skiptoparent;
    }

    public String getSkiptoparentcondition() {
        return skiptoparentcondition;
    }

    public void setSkiptoparentcondition(String skiptoparentcondition) {
        this.skiptoparentcondition = skiptoparentcondition;
    }

    public boolean hasLookupCompute() {

        if (isNotNullorEmpty(this.autcomputelogic)) {
            return autcomputelogic.contains("s.lookup");
        }
        return false;
    }

    public boolean hasSkiptoCondition() {
        return isNotNullorEmpty(this.skiptocondition);
    }

    public boolean hasSkiptoParentCondition() {
        return isNotNullorEmpty(this.skiptoparentcondition);
    }
    public boolean hasJumpSkip() {
        return isNotNullorEmpty(this.skipto);
    }

    public boolean hasJumpSkipByParent() {

        return isNotNullorEmpty(this.skiptoparent);
    }


    public boolean isParent() {
        if (this.childrenConcepts != null) {
            return this.childrenConcepts.size() > 0;
        }
        return false;
    }

    public void setParent(MConcept parent) {
        this.parent = parent;
    }

    public boolean isChildWithSingleParent(boolean checkchildren) {
        if (checkchildren) {
            return (this.parentIqcareId > 0) && isNotNullorEmpty(this.parentconditionchildren);
        } else {
            return (this.parentIqcareId > 0);
        }
    }


    public boolean isChildWithMultipleParents() {
        boolean hasParents = false;
        if (isNotNullorEmpty(this.parentcondition)) {
            hasParents = this.parentcondition.contains("|");
            if (hasParents) {
                multiparentConditions = new ArrayList<>();
                String[] pc = this.parentcondition.split("|");
                for (String s : pc) {
                    String[] pcinfo = s.split("-");
                    ParentCondition parentCondition = new ParentCondition(Integer.parseInt(pcinfo[0]), pcinfo[1]);
                    multiparentConditions.add(parentCondition);
                }
            }
        }
        return hasParents;
    }

    public List<ParentCondition> getMultiparentConditions() {
        return multiparentConditions;
    }

    public void setMultiparentConditions(List<ParentCondition> multiparentConditions) {
        this.multiparentConditions = multiparentConditions;
    }

    public boolean isMaleOnly() {
        return this.patientgender == 16;
    }

    public boolean isFremaleOnly() {
        return this.patientgender == 17;
    }

    public boolean hasLookups() {
        return this.lookupcodeid > 0;
    }

    public boolean negateCondition() {
        if (isNotNullorEmpty(this.parentcondition)) {
            return this.parentcondition.contains("!");
        }
        return false;
    }

    public boolean negateConditionChildren() {
        if (isNotNullorEmpty(this.parentconditionchildren)) {
            return this.parentconditionchildren.contains("!");
        }
        return false;
    }

    public boolean anyCondition() {
        if (isNotNullorEmpty(this.parentcondition)) {
            return this.parentcondition.contains("*");
        }
        return false;
    }

    public boolean anyConditionChildren() {
        if (isNotNullorEmpty(this.parentconditionchildren)) {
            return this.parentconditionchildren.contains("*");
        }
        return false;
    }

    public void addLookup(Lookup lookup) {
        lookup.setConceptId(getId());
        this.conceptLookups.add(lookup);
    }

    public void addLookups(List<Lookup> lookups) {
        this.conceptLookups = new ArrayList<>();

        for (Lookup l : lookups) {
            addLookup(l);
        }
    }

    public String getComputedResult(Patient patient, List<Observation> observations, User currentuser, String sRegTime, String sStartTime) {
        String resultValue = null;

        if (!isAutcompute()) {
            return null;
        }

        //p.couple?=4031

        if (autcomputelogic.contains("p.couple")) {
            if (patient.isCouple()) {
                //set obs value
                if (autcomputelogic.contains("=")) {
                    String[] vals = autcomputelogic.split("=");
                    resultValue = vals[1];
                }
                //set obs value from attribute
                if (autcomputelogic.contains(":")) {
                    String[] vals = autcomputelogic.split(":");
                    if (vals[1].equals("couple.id")) {
                        resultValue = patient.getPartner().getClientcode();
                    }
                }
            }
        }

        // s.concept=53=60001?20001-60002?20002-60003?20003:

        // s.concept=32=10001?20002:
        // s.concept=52=10002?20001

        if (autcomputelogic.contains("s.concept")) {

            if (autcomputelogic.contains(":")) {
                String[] vals = autcomputelogic.split(":");
                for (String val : vals) {
                    String[] conceptVals = val.split("=");
                    //53
                    Observation obs = findObservation(Integer.parseInt(conceptVals[1]), observations);
                    if (obs != null) {

                        //  60001?20001-60002?20002-60003?20003

                        List<String> checkvalsList = new ArrayList<>();

                        if (conceptVals[2].contains("-")) {
                            String[] checkvals = conceptVals[2].split("-");

                            for (String s : checkvals) {
                                checkvalsList.add(s);
                            }

                        } else {
                            checkvalsList.add(conceptVals[2]);
                        }


                        for (String evalstr : checkvalsList) {

                            //60001?20001
                            String rawstr = evalstr.replace("?", "-");
                            String[] tocheck = rawstr.split("-");
                            if (obs.getObsvalueString().equals(tocheck[0])) {
                                resultValue = tocheck[1];
                                break;
                            }
                        }
                    }
                }
            }
        }

        //  couple.concept= 37 = <>?1

        if (autcomputelogic.contains("couple.concept")) {

            String[] vals = autcomputelogic.split("=");

            // 37

            Observation obs = findObservation(Integer.parseInt(vals[1]), observations);
            Observation partnerObs = findPartnerObservation(Integer.parseInt(vals[1]), patient.getPartner());

            if (obs != null && partnerObs != null) {
                //  <>?1
                String rawstr = vals[2].replace("?", "-");
                String[] tocheck = rawstr.split("-");
                if (tocheck[0].equals("<>")) {

                    if (!obs.getObsvalueString().equals(partnerObs.getObsvalueString())) {
                        resultValue = tocheck[1];
                    }
                }

            }

        }

        //  s.lookup=49=90001?^30001-90002?^30002-90003?^30003

        if (autcomputelogic.contains("s.lookup")) {
            //s.lookup=49=90001?^30001-90002?^30002-90003?^30003
            resultValue = null;
/*
            String[] conceptVals = autcomputelogic.split("=");
            // 0> s.lookup
            // 1> 49
            // 2> 90001?^30001-90002?^30002-90003?^30003

            Observation obs = findObservation(Integer.parseInt(conceptVals[1]), observations);
            //49
            if (obs != null) {

                //  90001?^30001-90002?^30002-90003?^30003

                List<String> checkvalsList = new ArrayList<>();

                if (conceptVals[2].contains("-")) {

                    String[] checkvals = conceptVals[2].split("-");
                    //  0>  90001?^30001
                    //  1>  90002?^30002
                    //  2>  90003?^30003
                    for (String s : checkvals) {
                        checkvalsList.add(s);
                    }

                } else {
                    checkvalsList.add(conceptVals[2]);
                }


                for (String evalstr : checkvalsList) {

                    //90001?^30001
                    String rawstr = evalstr.replace("?^", "-");
                    //90001-30001
                    String[] tocheck = rawstr.split("-");
                    //      0>  90001
                    //      1>  30001
                    if (obs.getObsvalueString().equals(tocheck[0])) {

                        resultValue = tocheck[1];
                        break;
                    }
                }
            }
*/
        }

        if (autcomputelogic.contains("u.strategy")) {
            resultValue = String.valueOf(currentuser.getStrategy());
        }
        else if(autcomputelogic.contains("reg_time")){
            resultValue = sRegTime;
        }
        else if(autcomputelogic.contains("start_time")){
            resultValue = sStartTime;
        }
        else if(autcomputelogic.contains("stop_time")){
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            resultValue = dateFormat.format(date);
        }

        return resultValue;
    }

    public String getComputedLookup(Patient patient, List<Observation> observations) {
        String resultValue = null;

        if (!isAutcompute()) {
            return null;
        }

        //  s.lookup=49=90001?^30001-90002?^30002-90003?^30003

        if (autcomputelogic.contains("s.lookup")) {
            //s.lookup=49=90001?^30001-90002?^30002-90003?^30003

            String[] conceptVals = autcomputelogic.split("=");
            // 0> s.lookup
            // 1> 49
            // 2> 90001?^30001-90002?^30002-90003?^30003

            //49
            Observation obs = findObservation(Integer.parseInt(conceptVals[1]), observations);
            if (obs != null) {

                //  90001?^30001-90002?^30002-90003?^30003

                List<String> checkvalsList = new ArrayList<>();

                if (conceptVals[2].contains("-")) {
                    String[] checkvals = conceptVals[2].split("-");
                    //  0>  90001?^30001
                    //  1>  90002?^30002
                    //  2>  90003?^30003

                    for (String s : checkvals) {
                        checkvalsList.add(s);
                    }

                } else {
                    checkvalsList.add(conceptVals[2]);
                }


                for (String evalstr : checkvalsList) {

                    //90001?^30001

                    String rawstr = evalstr.replace("?^", "-");
                    //  90001-30001

                    String[] tocheck = rawstr.split("-");
                    //      0>  90001
                    //      1>  30001

                    if (obs.getObsvalueString().equals(tocheck[0])) {
                        resultValue = tocheck[1];
                        break;
                    }
                }
            }

        }

        return resultValue;
    }

    @Override
    public String toString() {
        return display + " " + description + " [" + dataTypeMap.getDataType() + "]";
    }

    @Override
    public boolean equals(Object ob) {
        if (ob == null) {
            return false;
        }
        if (this == ob) {
            return true;
        }
        if (ob instanceof MConcept) {
            MConcept other = (MConcept) ob;
            return this.getId() == other.getId();
        }
        return false;
    }

    @Override
    public int compareTo(MConcept concept) {
        return this.rank > concept.rank ? 1 : (this.rank < concept.rank ? -1 : 0);
    }

    private Observation findObservation(int conceptId, List<Observation> observations) {

        for (Observation o : observations) {
            if (o.getmConcept().getId() == conceptId) {
                return o;
            }
        }
        return null;
    }

    private Observation findPartnerObservation(int conceptId, Patient patient) {

        if (!patient.hasEncounter()) {
            return null;
        }


        List<Observation> observations = null;

        Encounter encounter = patient.getEncountersList().get(0);
        if (encounter != null) {
            observations = encounter.getObservationsList();
            for (Observation o : observations) {
                if (o.getmConcept().getId() == conceptId) {
                    return o;
                }
            }
        }

        return null;
    }

}

