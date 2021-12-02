package com.lsv.lib.core.concept.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Encapsula as informações utilizadas no envio de email.
 *
 * @author leandro.vieira
 */
@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "get")
@Accessors(fluent = true)
public final class Email implements Dto {

    @NonNull
    private String sender;
    private String subject;
    private String content;
    @NonNull @Builder.Default
    private EmailType emailType = EmailType.HTML;
    @NonNull
    private List<String> addressees;
    private List<String> addresseesCc;
    private List<String>addresseesCco;

    private byte[] attachmentFile;
    private String attachmentName;
    private AttachmentType attachmentType;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public enum EmailType {
        TXT(AttachmentType.TXT.content()),
        HTML(AttachmentType.HTML.content()),
        ;

        private String content;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public enum AttachmentType {
        XLS("application/vnd.ms-excel"),
        XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        PDF("application/pdf"),
        ZIP("application/octet-stream"),
        TXT("text/plain"),
        HTML("text/html"),
        CSV("text/plain"),
        JAVA("text/x-java-source,java"),
        XML("application/atom+xml"),
        ;

        private String content;
    }
}