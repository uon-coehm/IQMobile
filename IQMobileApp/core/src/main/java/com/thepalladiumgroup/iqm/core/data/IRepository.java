package com.thepalladiumgroup.iqm.core.data;

/**
 * Created by Koske Kimutai [2016/04/19]
 */

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/14]
 */
public interface IRepository<T> {
    T refresh(T entity) throws SQLException;
    T find(int id) throws SQLException;

    T findbyfield(String field, Object value) throws SQLException;

    T findbySyncId(String uuid) throws SQLException;

    T save(T entity) throws SQLException;

    void saveOnly(T entity) throws SQLException;

    T update(T entity) throws SQLException;

    void updateOnly(T entity) throws SQLException;

    void delete(int id) throws SQLException;

    void delete(T entity) throws SQLException;

    void deleteAll() throws SQLException;

    void deleteAll(List<T> entities) throws SQLException;
    List<T> getAll() throws SQLException;

    List<T> getAllbyfield(String field, Object value) throws SQLException;

}