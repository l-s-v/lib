package com.lsv.lib.spring.security.web.audit;

import com.lsv.lib.core.audit.Auditable;
import com.lsv.lib.core.security.UserHandler;
import com.lsv.lib.spring.core.annotation.ConditionalAuditEnable;
import com.lsv.lib.spring.security.web.annotation.ConditionalWebSecurityEnable;
import com.lsv.lib.spring.security.web.properties.SpringSecurityWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.lsv.lib.security.web.helper.oidc.ConstantsWebOidc.*;

/**
 * Mounts the spring security module audit data.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j

@ConditionalWebSecurityEnable
@ConditionalAuditEnable(SpringSecurityWebProperties.PATH)
@Component
public class SpringSecurityWebAuditable implements Auditable {

    private static final String ID = "securityWeb";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public String id() {
        return ID;
    }

    @Override
    public Map<String, String> geData() {
        try {
            var user = UserHandler.resolveUser();

            return Map.of(
                "user", user.getName(),
                CLAIM_TOKEN_ISS, user.getAttribute(CLAIM_TOKEN_ISS),
                CLAIM_TOKEN_AZP, user.getAttribute(CLAIM_TOKEN_AZP),
                CLAIM_TOKEN_SUB, user.getAttribute(CLAIM_TOKEN_SUB)
            );
        } catch (Throwable e) {
            log.warn("Não foi possível recuperar os dados de auditoria de segurança da web", e);
            return null;
        }
    }
}
