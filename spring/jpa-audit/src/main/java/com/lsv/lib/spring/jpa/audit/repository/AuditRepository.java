package com.lsv.lib.spring.jpa.audit.repository;

import com.lsv.lib.core.audit.Auditable;
import com.lsv.lib.core.helper.HelperObj;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Makes the link between the data that can be audited and the transaction in the database:
 * - Receives a transaction start event.
 * - Collects application information that has been registered as auditable.
 * - Converts this information to a string in json format.
 * - Sends a (parameterized) command to the database forwarding the collected information.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
@RequiredArgsConstructor
public class AuditRepository {

    private final EntityManager entityManager;
    private final String sqlCommand;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public void executeUpdate() {
        log.trace("Enviando informações de auditoria para o banco de dados");

        String metadata;
        try {
            var data = Auditable.data();
            if (ObjectUtils.isEmpty(data)) {
                log.trace("Não foram encontrados dados para auditoria.");
                return;
            }

            metadata = HelperObj.toString(data);

        } catch (Throwable e) {
            // An error when retrieving data for auditing does not interrupt the flow, it just displays in the log
            log.error(e.getMessage(), e);
            return;
        }

        log.trace("Metadados = {}", metadata);

        entityManager
            .createNativeQuery(sqlCommand)
            .setParameter("p", metadata)
            .executeUpdate();
    }
}