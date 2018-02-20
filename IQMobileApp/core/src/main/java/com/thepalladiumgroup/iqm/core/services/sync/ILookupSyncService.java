package com.thepalladiumgroup.iqm.core.services.sync;

import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.services.sync.endpoints.ILookupWapi;

import java.io.IOException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public interface ILookupSyncService extends ISyncService {
    ILookupWapi getLookupWapi();
    List<Lookup> readAllLookups() throws IOException;
}
