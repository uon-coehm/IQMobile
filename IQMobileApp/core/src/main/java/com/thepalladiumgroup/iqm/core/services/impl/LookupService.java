package com.thepalladiumgroup.iqm.core.services.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.ILookupRepository;
import com.thepalladiumgroup.iqm.core.data.impl.LookupRepository;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.services.ILookupService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/18]
 */
public class LookupService implements ILookupService {

    private final IApplicationManager applicationManager;
    private final ILookupRepository lookupRepository;
    private List<Lookup> existingLookups = new ArrayList<>();

    public LookupService(IApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        this.lookupRepository = new LookupRepository(applicationManager);
    }

    @Override
    public void ClearAll() throws SQLException {
        lookupRepository.deleteAll();
    }

    @Override
    public Lookup getLookupByCode(int codeid) throws SQLException {
        return lookupRepository.getByCode(codeid);
    }

    @Override
    public Lookup getLookupIQcareId(int id) throws SQLException {
        return lookupRepository.findbyfield("iqcareid", id);
    }

    @Override
    public void syncLookup(Lookup lookup) throws SQLException {

        Lookup subject = checkLookupExsist(lookup);
        if (subject != null) {
        if (lookup.equals(subject))
            {
                lookup.setId(subject.getId());
                lookupRepository.updateOnly(lookup);
            } else {

                lookupRepository.saveOnly(lookup);
            }
        }
        else {

            lookupRepository.saveOnly(lookup);
        }

        /*Lookup subject = checkLookupExsist(lookup);
        if (lookup.equals(subject)) {
            lookup.setId(subject.getId());
            lookupRepository.updateOnly(lookup);
        } else {
            lookupRepository.saveOnly(lookup);
        }
*/


    }

    @Override
    public List<Lookup> getAllLookups() throws SQLException {
        existingLookups = lookupRepository.getAllLookups();
        return existingLookups;
    }

    @Override
    public List<Lookup> getLookupsByCodeId(int codeid) throws SQLException {
        List<Lookup> list = lookupRepository.getAllByCode(codeid);
        Collections.sort(list);
        return list;
    }

    private Lookup checkLookupExsist(Lookup find) {
        Lookup found = null;
        for (Lookup u : existingLookups) {
            if (find.equals(u)) {
                found = u;
                break;
            }
        }
        return found;
    }
}
