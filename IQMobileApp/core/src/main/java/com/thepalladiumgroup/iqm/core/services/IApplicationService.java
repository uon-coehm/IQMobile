package com.thepalladiumgroup.iqm.core.services;

import com.thepalladiumgroup.iqm.core.model.ActiveSession;
import com.thepalladiumgroup.iqm.core.model.Server;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/13]
 */
public interface IApplicationService {
    void initialize();

    List<ActiveSession> getActiveSessions(String activityname);

    ActiveSession getActiveSession(String activityname, String patientid, String key);

    ActiveSession saveActiveSession(ActiveSession activeSession);

    void clearActiveSession(int activeSessionId);

    void saveServer(Server server) throws SQLException;
}
