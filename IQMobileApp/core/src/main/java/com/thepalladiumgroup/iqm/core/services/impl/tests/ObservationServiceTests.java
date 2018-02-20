package com.thepalladiumgroup.iqm.core.services.impl.tests;

import com.thepalladiumgroup.iqm.core.services.IObservationService;
import com.thepalladiumgroup.iqm.core.services.impl.ObservationService;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;

import org.junit.Before;

import java.sql.SQLException;

/**
 * Created by Koske Kimutai [2016/05/09]
 */
public class ObservationServiceTests extends BaseCoreTest {
    private IObservationService observationService;

    @Before
    public void setUp() throws SQLException {
        observationService = new ObservationService(applicationManager);
    }


}
