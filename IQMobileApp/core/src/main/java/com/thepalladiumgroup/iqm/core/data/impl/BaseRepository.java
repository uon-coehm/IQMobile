package com.thepalladiumgroup.iqm.core.data.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IRepository;
import com.thepalladiumgroup.iqm.core.model.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/14]
 */
public abstract class BaseRepository<T extends Entity> implements IRepository<T> {
    protected static final Logger SLF_LOGGER = LoggerFactory.getLogger(BaseRepository.class);
    private final IApplicationManager applicationManager;
    protected Dao<T, Integer> entityDao = null;
    private Class<T> clazz;


    public BaseRepository(IApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        this.clazz = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

        if (entityDao == null) {
            try {
                SLF_LOGGER.debug("creating new dao...");
                entityDao = DaoManager.createDao(this.applicationManager.getDatabaseHelper().getConnectionSource(), clazz);
                //  entityDao =  this.applicationManager.getDatabaseHelper().getDao(clazz);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public T refresh(T entity) throws SQLException {
        entityDao.refresh(entity);
        return entity;
    }

    @Override
    public T find(int id) throws SQLException {
        return entityDao.queryForId(id);
    }

    @Override
    public T findbyfield(String field, Object value) throws SQLException {

        List<T> results = entityDao.queryForEq(field, value);

        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public T findbySyncId(String uuid) throws SQLException {
        return findbyfield("uuid", uuid);
    }

    @Override
    public T save(T entity) throws SQLException {
        entityDao.create(entity);
        int newid = entity.getId();
        return find(newid);

        /*
        Account accountResult = accountDao.queryForId(account.getId());
		ForeignCollection<Order> orders = accountResult.getOrders();

		// sanity checks
		CloseableIterator<Order> iterator = orders.closeableIterator();
		try {
			assertTrue(iterator.hasNext());
			Order order = iterator.next();
			assertEquals(itemNumber1, order.getItemNumber());
			assertSame(accountResult, order.getAccount());
			assertTrue(iterator.hasNext());
			order = iterator.next();
			assertEquals(itemNumber2, order.getItemNumber());
			assertFalse(iterator.hasNext());
		} finally {
			// must always close our iterators otherwise connections to the database are held open
			iterator.close();
		}
         */
    }

    @Override
    public void saveOnly(T entity) throws SQLException {
        entityDao.create(entity);
    }

    @Override
    public T update(T entity) throws SQLException {
        entity.prepareUpdate();
        entityDao.update(entity);
        return find(entity.getId());
    }

    @Override
    public void updateOnly(T entity) throws SQLException {
        entity.prepareUpdate();
        entityDao.update(entity);
    }

    @Override
    public void delete(T entity) throws SQLException {
        entityDao.delete(entity);
    }

    @Override
    public void delete(int id) throws SQLException {
        entityDao.deleteById(id);
    }


    @Override
    public void deleteAll() throws SQLException {
        List<T> list = entityDao.queryForAll();
        entityDao.delete(list);
    }

    @Override
    public void deleteAll(List<T> entities) throws SQLException {
        entityDao.delete(entities);
    }

    @Override
    public List<T> getAll() throws SQLException {
        return entityDao.queryForAll();
    }

    @Override
    public List<T> getAllbyfield(String field, Object value) throws SQLException {
        List<T> results = entityDao.queryForEq(field, value);
        return results;
    }
}

