package com.lsv.lib.spring.core.message;

import com.lsv.lib.spring.core.annotation.LibConfigurationProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.lsv.lib.spring.core.annotation.LibConfigurationProperties.LIB_NAME_PROPERTIES;

/**
 * @author Leandro da Silva Vieira
 */
@Getter
@Setter
@Accessors(fluent = false)

@LibConfigurationProperties(LIB_NAME_PROPERTIES + "message")
public class MessageProperties {

    /**
     * File names used as a basis for loading messages.
     * Message fetch priority is based on the file's position in the list. Ex: fileNames[0] = highest priority.
     * Default values: messages, libMessages.
     */
    private String[] fileNames = {"messages", "libMessages"};
}