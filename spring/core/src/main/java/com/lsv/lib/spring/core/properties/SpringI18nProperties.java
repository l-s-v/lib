package com.lsv.lib.spring.core.properties;

import com.lsv.lib.core.helper.LibConstants;
import com.lsv.lib.core.properties.LibProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * @author Leandro da Silva Vieira
 */
@Data

@Validated
@ConfigurationProperties(SpringI18nProperties.PATH)
public class SpringI18nProperties implements LibProperties {

    public static final String PATH = LibConstants.BASE_LIB_PROPERTIES + "i18n";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * File names used as a basis for loading messages.
     * Message fetch priority is based on the file's position in the list. Ex: fileNames[0] = highest priority.
     * Default values: messages, libMessages.
     */
    @NotNull
    private String[] fileNames = {"messages", "libMessages"};
}