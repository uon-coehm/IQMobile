package com.thepalladiumgroup.iqm.core.data.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IDataTypeMapRepository;
import com.thepalladiumgroup.iqm.core.data.impl.DataTypeMapRepository;
import com.thepalladiumgroup.iqm.core.model.MDataType;
import com.thepalladiumgroup.iqm.core.model.MDataTypeMap;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/25]
 */
public class DataTypeMapRepositoryTests extends BaseCoreTest {
    private IDataTypeMapRepository repository;


    @Before
    public void setUp() throws SQLException {
        repository = new DataTypeMapRepository(applicationManager);
    }

    @Test
    public void should_getall() throws SQLException {
        List<MDataTypeMap> dataTypeMaps = repository.getAll();
        Assert.assertTrue(dataTypeMaps.size() > 0);
        System.out.println("TEST DATA:users");
        for (MDataTypeMap dataTypeMap : dataTypeMaps) {
            System.out.println("DataTypeMap:" + dataTypeMap.toString());
        }
    }

    @Test
    public void should_getByDataType() throws SQLException {
        MDataTypeMap dataTypeMap = repository.getByDataType(MDataType.TIME);
        Assert.assertEquals(MDataType.TIME, dataTypeMap.getDataType());
        System.out.println("DataTypeMap:" + dataTypeMap.toString());
    }

    @Test
    public void should_getByIQDataType() throws SQLException {
        MDataTypeMap dataTypeMap = repository.getByIQDataType("Time");
        Assert.assertEquals(MDataType.TIME, dataTypeMap.getDataType());
        System.out.println("DataTypeMap:" + dataTypeMap.toString());
    }
}
