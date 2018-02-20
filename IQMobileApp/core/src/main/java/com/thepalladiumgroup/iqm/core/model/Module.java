package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/22]
 */
public class Module extends Entity {

    @ForeignCollectionField(eager = true)
    Collection<EncounterType> encounterTypes = new ArrayList<>();
    List<EncounterType> newEncounterTypes = new ArrayList<>();
    @DatabaseField
    private String name;
    @DatabaseField
    private String display;
    @DatabaseField
    private String displayshort;
    @DatabaseField
    private String icon;

    public Module() {
    }

    public Module(String name, String display, String displayshort) {
        this.name = name;
        this.display = display;
        this.displayshort = displayshort;
    }

    public Module(String name, String display, String displayshort, String icon) {
        this.display = display;
        this.displayshort = displayshort;
        this.icon = icon;
        this.name = name;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<EncounterType> getEncounterTypes() {
        return encounterTypes;
    }

    public List<EncounterType> getEncounterTypesList() {
        return new ArrayList<>(encounterTypes);

    }

    public List<EncounterType> getNewEncounterTypes() {
        return newEncounterTypes;
    }

    @Override
    public String toString() {
        return getId() + ". " + displayshort + "-" + display;
    }

    public void addEncounterType(EncounterType encounterType) {
        newEncounterTypes.add(encounterType);
    }

    public void addEncounterTypes(List<EncounterType> encounterTypeslist) {
        for (EncounterType e : encounterTypeslist) {
            addEncounterType(e);
        }
    }

}
