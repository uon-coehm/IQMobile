package com.thepalladiumgroup.iqm.core.data.impl;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IActiveSessionRepository;
import com.thepalladiumgroup.iqm.core.model.ActiveSession;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/27]
 */
public class ActiveSessionRepository extends BaseRepository<ActiveSession> implements IActiveSessionRepository {
    public ActiveSessionRepository(IApplicationManager applicationManager) {
        super(applicationManager);
    }

    @Override
    public ActiveSession getByActivityKey(String activity, String pateintid, String key) throws SQLException {
        List<ActiveSession> sessions = null;
        QueryBuilder<ActiveSession, Integer> qb = entityDao.queryBuilder();

        Where where = qb.where();

        where.eq("activeActivity", activity);
        where.and();
        where.eq("activePatientId", pateintid);
        where.and();
        where.eq("activeKey", key);
        qb.orderBy("activeTime", false);
        sessions = qb.query();

        if (sessions.size() > 0) {
            return sessions.get(0);
        }
        return null;
    }

    @Override
    public List<ActiveSession> getAllByActivityKey(String activity, String pateintid, String key) throws SQLException {
        QueryBuilder<ActiveSession, Integer> qb = entityDao.queryBuilder();
        //activePatientId
        Where where = qb.where();
        where.eq("activeActivity", activity);
        where.and();
        where.eq("activePatientId", pateintid);
        where.and();
        where.eq("activeKey", key);
        return qb.query();
    }

    @Override
    public ActiveSession save(ActiveSession entity) throws SQLException {
        List<ActiveSession> currentSessions = getAllByActivityKey(entity.getActiveActivity(), entity.getActivePatientId(), entity.getActiveKey());
        for (ActiveSession as : currentSessions) {
            delete(as.getId());
        }
        return super.save(entity);
    }
}
