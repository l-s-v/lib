package com.lsv.lib.core.helper;

import com.lsv.lib.core.annotation.Priority;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.List;

/**
 * Provides methods related to @Priority.
 *
 * @author Leandro da Silva Vieira
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HelperPriority {

    /**
     * Sort by descending priority (highest to lowest).
     */
    public static <T> List<T> sortPriorityDescending(@NonNull Collection<T> collection) {
        return collection.stream().sorted(HelperPriority::comparePriorityDescending).toList();
    }

    /**
     * Compare by descending priority (highest to lowest).
     */
    public static int comparePriorityDescending(Object obj1, Object obj2) {
        return ObjectUtils.compare(getPriority(obj2), getPriority(obj1));
    }

    /**
     * Returns the priority informed in the @Priority annotation.
     * Returns null if not found.
     */
    public static Integer getPriority(Object obj) {
        var annotation = obj.getClass().getAnnotation(Priority.class);
        if(annotation != null) {
            return annotation.value();
        }

        return null;
    }
}