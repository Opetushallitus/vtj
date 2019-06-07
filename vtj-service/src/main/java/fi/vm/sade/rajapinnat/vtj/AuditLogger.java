package fi.vm.sade.rajapinnat.vtj;

import fi.vm.sade.auditlog.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogger.class);

    private final Audit audit = new Audit(LOGGER::info, "vtj-service", ApplicationType.VIRKAILIJA);

    public void log(User user, Operation operation, Target target, Changes changes) {
        audit.log(user, operation, target, changes);
    }

}
