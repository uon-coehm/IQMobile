package com.thepalladiumgroup.iqm.core.data;

import com.thepalladiumgroup.iqm.core.model.ActiveSession;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/27]
 */
public interface IActiveSessionRepository extends IRepository<ActiveSession> {
    ActiveSession getByActivityKey(String activity, String pateintid, String key) throws SQLException;

    List<ActiveSession> getAllByActivityKey(String activity, String patientid, String key) throws SQLException;
}
