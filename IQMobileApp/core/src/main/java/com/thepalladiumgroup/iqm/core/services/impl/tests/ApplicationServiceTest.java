package com.thepalladiumgroup.iqm.core.services.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IActiveSessionRepository;
import com.thepalladiumgroup.iqm.core.data.IConceptRepository;
import com.thepalladiumgroup.iqm.core.data.IDataTypeMapRepository;
import com.thepalladiumgroup.iqm.core.data.IDeviceRepository;
import com.thepalladiumgroup.iqm.core.data.IEncounterTypeRepository;
import com.thepalladiumgroup.iqm.core.data.ILookupRepository;
import com.thepalladiumgroup.iqm.core.data.IModuleRepository;
import com.thepalladiumgroup.iqm.core.data.IUserRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ActiveSessionRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ConceptRepository;
import com.thepalladiumgroup.iqm.core.data.impl.DataTypeMapRepository;
import com.thepalladiumgroup.iqm.core.data.impl.DeviceRepository;
import com.thepalladiumgroup.iqm.core.data.impl.EncounterTypeRepository;
import com.thepalladiumgroup.iqm.core.data.impl.LookupRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ModuleRepository;
import com.thepalladiumgroup.iqm.core.data.impl.UserRepository;
import com.thepalladiumgroup.iqm.core.model.ActiveSession;
import com.thepalladiumgroup.iqm.core.model.Device;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.MDataTypeMap;
import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.IApplicationService;
import com.thepalladiumgroup.iqm.core.services.impl.ApplicationService;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;
import com.thepalladiumgroup.iqm.core.tests.TestData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/14]
 */
public final class ApplicationServiceTest extends BaseCoreTest {

    private IApplicationService applicationService;
    private ActiveSession activeSession = new ActiveSession("ApplicationServiceTest");
    private IActiveSessionRepository activeSessionRepository;
    private List<Integer> testSessionIds;

    @Before
    public void setUp() throws SQLException {
        testSessionIds = new ArrayList<>();
        applicationService = new ApplicationService(applicationManager);
        activeSessionRepository = new ActiveSessionRepository(applicationManager);
        for (ActiveSession s : TestData.getActiveSessions()) {
            ActiveSession as = activeSessionRepository.save(s);
            testSessionIds.add(as.getId());
        }
    }

    @Test
    public void should_Initialize_Device() throws SQLException {
        IDeviceRepository repository = new DeviceRepository(applicationManager);
        List<Device> devices = repository.getAll();
        Assert.assertTrue(devices.size() > 0);
    }

    @Test
    public void should_Initialize_Users() throws SQLException {
        IUserRepository repository = new UserRepository(applicationManager);
        List<User> users = repository.getAll();
        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void should_Initialize_Lookups() throws SQLException {
        ILookupRepository repository = new LookupRepository(applicationManager);
        List<Lookup> lookups = repository.getAll();
        Assert.assertTrue(lookups.size() > 0);
    }

    @Test
    public void should_Initialize_DataTypesMap() throws SQLException {
        IDataTypeMapRepository repository = new DataTypeMapRepository(applicationManager);
        List<MDataTypeMap> dataTypeMaps = repository.getAll();
        Assert.assertTrue(dataTypeMaps.size() > 0);
    }

    @Test
    public void should_Initialize_Modules() throws SQLException {
        IModuleRepository repository = new ModuleRepository(applicationManager);
        List<Module> modules = repository.getAll();
        Assert.assertTrue(modules.size() > 0);
    }

    @Test
    public void should_Initialize_EncounterTypes() throws SQLException {
        IEncounterTypeRepository repository = new EncounterTypeRepository(applicationManager);
        List<EncounterType> encounterTypes = repository.getAll();
        Assert.assertTrue(encounterTypes.size() > 0);
    }

    @Test
    public void should_Initialize_Concepts() throws SQLException {
        IConceptRepository repository = new ConceptRepository(applicationManager);
        List<MConcept> concepts = repository.getAll();
        Assert.assertTrue(concepts.size() > 0);
        for (MConcept parentConcept : concepts) {
            if (parentConcept.isChildWithSingleParent(false)) {
                return;
            }
            System.out.println("" + parentConcept.toString() + "[" + parentConcept.getEncounterType().getModule().getDisplay() + "," + parentConcept.getEncounterType().getName() + "]");
            for (MConcept childConcept : parentConcept.getChildrenConceptsList()) {
                System.out.println("  (if " + childConcept.getParentcondition() + ") >" + childConcept.toString());
            }
            System.out.println("-------------------------------------------------------");
        }
    }

    @Test
    public void should_save_Active_session() throws SQLException {
        ActiveSession newactiveSession = null;
        activeSession.createSession("testSave", "saving");
        newactiveSession = applicationService.saveActiveSession(activeSession);
        testSessionIds.add(newactiveSession.getId());

        ActiveSession savedactiveSession = applicationService.getActiveSession(activeSession.getActiveActivity(), activeSession.getActivePatientId(), activeSession.getActiveKey());
        Assert.assertNotNull(savedactiveSession);
        System.out.println(savedactiveSession);
    }

    @Test
    public void should_clear_Active_session() throws SQLException {
        applicationService.clearActiveSession(testSessionIds.get(0));
        List<ActiveSession> activeSessions = applicationService.getActiveSessions("TestData");
        Assert.assertEquals(TestData.getActiveSessions().size() - 1, activeSessions.size());

        for (ActiveSession activeSession : activeSessions) {
            System.out.println(activeSession);
        }

    }

    @Test
    public void should_get_Active_sessions() throws SQLException {
        List<ActiveSession> sessions = applicationService.getActiveSessions("TestData");
        Assert.assertTrue(sessions.size() > 0);
        for (ActiveSession activeSession : sessions) {
            System.out.println(activeSession);
        }
    }

    @After
    public void tearDown() throws SQLException {
        for (int id : testSessionIds) {
            activeSessionRepository.delete(id);
        }
    }
}
