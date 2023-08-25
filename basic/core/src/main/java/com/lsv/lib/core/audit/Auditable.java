package com.lsv.lib.core.audit;

import com.lsv.lib.core.loader.Loader;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Sets the default for objects that "can" be audited.
 *
 * @author Leandro da Silva Vieira
 */
public interface Auditable {

    /**
     * Must return an ID to group the data.
     */
    String id();

    /**
     * It must return information that can be audited, duly updated at the time of the call, if applicable.
     */
    Map<String, String> geData();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    List<Auditable> AUDITABLES = Loader.of(Auditable.class).findImplementationsByAllLoaders();

    /**
     * Returns in a unified way all data found, from all auditable objects.
     */
    static Map<String, Object> data() {
        return AUDITABLES.stream()
            .map(auditable -> Map.of(auditable.id(), auditable.geData()))
            .filter(map -> ObjectUtils.isNotEmpty(map.values()))
            .flatMap(map -> map.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}