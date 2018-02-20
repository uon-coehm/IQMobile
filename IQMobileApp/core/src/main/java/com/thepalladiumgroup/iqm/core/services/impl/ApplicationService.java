package com.thepalladiumgroup.iqm.core.services.impl;

import android.os.Build;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IActiveSessionRepository;
import com.thepalladiumgroup.iqm.core.data.IConceptRepository;
import com.thepalladiumgroup.iqm.core.data.IDataTypeMapRepository;
import com.thepalladiumgroup.iqm.core.data.IDeviceRepository;
import com.thepalladiumgroup.iqm.core.data.IEncounterTypeRepository;
import com.thepalladiumgroup.iqm.core.data.ILookupRepository;
import com.thepalladiumgroup.iqm.core.data.IModuleRepository;
import com.thepalladiumgroup.iqm.core.data.IServerRepository;
import com.thepalladiumgroup.iqm.core.data.IUserRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ActiveSessionRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ConceptRepository;
import com.thepalladiumgroup.iqm.core.data.impl.DataTypeMapRepository;
import com.thepalladiumgroup.iqm.core.data.impl.DeviceRepository;
import com.thepalladiumgroup.iqm.core.data.impl.EncounterTypeRepository;
import com.thepalladiumgroup.iqm.core.data.impl.LookupRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ModuleRepository;
import com.thepalladiumgroup.iqm.core.data.impl.ServerRepository;
import com.thepalladiumgroup.iqm.core.data.impl.UserRepository;
import com.thepalladiumgroup.iqm.core.model.ActiveSession;
import com.thepalladiumgroup.iqm.core.model.Device;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.MDataType;
import com.thepalladiumgroup.iqm.core.model.MDataTypeMap;
import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.model.Server;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.IApplicationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/13]
 */
public class ApplicationService implements IApplicationService {
    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(ApplicationService.class);

    private final IApplicationManager applicationManager;
    private final IActiveSessionRepository activeSessionRepository;
    private final IServerRepository serverRepository;
    private EncounterType defaultEncounterType = null;
    private Module defaultModule = null;

    public ApplicationService(IApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        activeSessionRepository = new ActiveSessionRepository(applicationManager);
        serverRepository = new ServerRepository(applicationManager);
    }

    @Override
    public void initialize() {
        SLF_LOGGER.debug("Initializing database");
        setupDevice();
        setupUsers();
        //setupLookups();
        //setupConcepts();
        /*

        setupDataTypes();
        setupModules();
        setupEncounterTypes();

        */
    }

    @Override
    public List<ActiveSession> getActiveSessions(String activityname) {
        List<ActiveSession> activeSessions = new ArrayList<>();
        try {
            activeSessions = activeSessionRepository.getAllbyfield("activeActivity", activityname);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activeSessions;
    }

    @Override
    public ActiveSession getActiveSession(String activityname, String patientid, String key) {
        ActiveSession activeSession = null;
        try {
            activeSession = activeSessionRepository.getByActivityKey(activityname, patientid, key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activeSession;
    }

    @Override
    public ActiveSession saveActiveSession(ActiveSession activeSession) {
        ActiveSession session = null;
        try {
            session = activeSessionRepository.save(activeSession);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return session;
    }

    @Override
    public void clearActiveSession(int activeSessionId) {
        try {
            activeSessionRepository.delete(activeSessionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveServer(Server server) throws SQLException {
        serverRepository.update(server);
    }


    private void setupDevice() {
        Device device = null;

        IDeviceRepository deviceRepository = new DeviceRepository(applicationManager);
        try {
            device = deviceRepository.getDeviceInfo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (device == null) {
            SLF_LOGGER.debug("Initializing Device");
            Device newdevice = new Device(
                    Build.SERIAL,
                    "KNH01",
                    12345,
                    "Kenyatta National Hospital"
            );
            try {
                deviceRepository.save(newdevice);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            if (!device.getSerial().equalsIgnoreCase(Build.SERIAL)) {
                device.setSerial(Build.SERIAL);
                try {
                    deviceRepository.update(device);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setupUsers() {
        User defualtUser = null;
        IUserRepository userRepository = new UserRepository(applicationManager);
        try {
            defualtUser = userRepository.findbyUsername("android-admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (defualtUser == null) {
            SLF_LOGGER.debug("Initializing Users");
            User newdefualtUser = new User("android-admin", "admin1");
            newdefualtUser.setCounsellorcode("1001");
            try {
                userRepository.save(newdefualtUser);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupLookups() {

        Lookup defaultLookup = null;
        ILookupRepository lookupRepository = new LookupRepository(applicationManager);
        try {
            defaultLookup = lookupRepository.getByCode(90000);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (defaultLookup != null) {
            return;
        }

        SLF_LOGGER.debug("Initializing Lookups");
        List<Lookup> lookups = new ArrayList<>();


        lookups.add(new Lookup(90001, "KHB", 90000));
        lookups.add(new Lookup(90002, "Determine", 90000));


        for (Lookup lookup : lookups) {
            try {
                lookupRepository.save(lookup);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupDataTypes() {

        MDataTypeMap defaultDataTypeMap = null;
        IDataTypeMapRepository dataTypeMapRepository = new DataTypeMapRepository(applicationManager);

        try {
            defaultDataTypeMap = dataTypeMapRepository.findbyfield("iqType", "Time");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (defaultDataTypeMap != null) {
            return;
        }

        SLF_LOGGER.debug("Initializing datatypes");

        List<MDataTypeMap> dataTypeMaps = new ArrayList<>();

        dataTypeMaps.add(new MDataTypeMap(MDataType.DATE, "Date"));
        dataTypeMaps.add(new MDataTypeMap(MDataType.MULTISELECT, "Multi Select "));
        dataTypeMaps.add(new MDataTypeMap(MDataType.NUMERIC, "Numeric"));
        dataTypeMaps.add(new MDataTypeMap(MDataType.NUMERICDECIMAL, "DecimalTextBox"));
        dataTypeMaps.add(new MDataTypeMap(MDataType.SELECT, "Select List"));
        dataTypeMaps.add(new MDataTypeMap(MDataType.TEXT, "Text SingleLine"));
        dataTypeMaps.add(new MDataTypeMap(MDataType.TEXTMULTI, "Text MulitLine"));
        dataTypeMaps.add(new MDataTypeMap(MDataType.TIME, "Time"));
        dataTypeMaps.add(new MDataTypeMap(MDataType.YESNO, "Yes No"));

        for (MDataTypeMap dataTypeMap : dataTypeMaps) {

            try {
                dataTypeMapRepository.save(dataTypeMap);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupModules() {
        Module module = null;

        IModuleRepository moduleRepository = new ModuleRepository(applicationManager);
        try {
            defaultModule = module = moduleRepository.findByName("KNH HTC Form");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (module == null) {
            SLF_LOGGER.debug("Initializing Modules");
            Module newdevice = new Module(
                    "KNH HTC Form",
                    "HTS",
                    "HTS"
            );
            defaultModule = newdevice;
            try {
                moduleRepository.save(newdevice);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupEncounterTypes() {

        if (defaultModule.getNewEncounterTypes().toArray().length > 0) {
            return;
        }

        IEncounterTypeRepository deviceRepository = new EncounterTypeRepository(applicationManager);
        try {
            defaultEncounterType = deviceRepository.findbyfield("name", "KNH HTC Form");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (defaultEncounterType == null) {
            SLF_LOGGER.debug("Initializing Encounter Type");
            EncounterType newdevice = new EncounterType(
                    "KNH HTC Form",
                    "HTS Form",
                    "HTS"
            );
            newdevice.setModule(defaultModule);
            try {
                defaultEncounterType = deviceRepository.save(newdevice);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupConcepts() {
        MConcept newConcept = null;
        IEncounterTypeRepository deviceRepository = new EncounterTypeRepository(applicationManager);
        try {
            defaultEncounterType = deviceRepository.findbyfield("name", "KNH HTC Form");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        IConceptRepository conceptRepository = new ConceptRepository(applicationManager);
        try {
            newConcept = conceptRepository.findbyfield("lookupcodeid", 90002);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (newConcept == null) {
            List<MConcept> concepts = new ArrayList<>();

            // concepts.add(MConcept.CreateConceptWithLookUp("Select Test", "Select Test", "Select List", 1, 100, 90000));
            //concepts.add(MConcept.CreateConceptWithLookUp("Test No 2 First Response", "Test No 2 First Response", "Select List", 8888732, 103.1, 240));
            //concepts.add(MConcept.CreateConceptWithLookUp("Tie breaker: Unigold", "Tie breaker: Unigold", "Select List", 3, 104.1, 241));


///5	Test No 2 First Response	Test No 2 First Response	1		4135|6796	103.0	240	0	0	0	2016-05-02 20:11:43.968000	901799ac-bde7-4c59-9613-268650f6897d	33	8888732	0	-1


            for (MConcept c : concepts) {
                c.setEncounterType(defaultEncounterType);
                try {
                    conceptRepository.save(c);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
