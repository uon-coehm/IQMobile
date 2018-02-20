package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/22]
 */
public class EncounterType extends Entity {

    @ForeignCollectionField(eager = true)
    Collection<MConcept> concepts;
    List<MConcept> newConcepts = new ArrayList<>();
    @ForeignCollectionField(eager = true)
    transient Collection<Encounter> encounters;
    @DatabaseField(foreignAutoRefresh = true, foreign = true, foreignAutoCreate = true)
    transient private Module module;
    @DatabaseField
    private String name;
    @DatabaseField
    private String display;
    @DatabaseField
    private String displayshort;

    public EncounterType() {
    }

    public EncounterType(String name, String display, String displayshort) {
        this.name = name;
        this.display = display;
        this.displayshort = displayshort;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDisplayshort() {
        return displayshort;
    }

    public void setDisplayshort(String displayshort) {
        this.displayshort = displayshort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setEncounters(Collection<Encounter> encounters) {
        this.encounters = encounters;
    }

    public Collection<MConcept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Collection<MConcept> concepts) {
        this.concepts = concepts;
    }

    public Collection<Encounter> getEncounters() {
        return encounters;
    }

    public void setEncounters(ForeignCollection<Encounter> encounters) {
        this.encounters = encounters;
    }

    public List<Encounter> getEncountersList() {

        return new ArrayList<>(encounters);
    }

    public List<MConcept> getConceptsList() {
        return new ArrayList<>(concepts);

    }

    public List<MConcept> getNewConcepts() {
        return newConcepts;
    }

    public void addConcept(MConcept concept) {
        newConcepts.add(concept);
    }

    public void addConcepts(List<MConcept> conceptList) {
        for (MConcept e : conceptList) {
            addConcept(e);
        }
    }

    @Override
    public String toString() {
        return getId() + ". " + displayshort + "-" + display;
    }
}
