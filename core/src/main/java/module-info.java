import com.lsv.lib.core.behavior.Identifiable;

module com.lsv.lib.core {
    requires static lombok;
    requires jakarta.validation;
    requires jakarta.el;

    exports com.lsv.lib.core.behavior;
    exports com.lsv.lib.core.pattern.register;
    exports com.lsv.lib.core.concept.service;
    exports com.lsv.lib.core.concept.service.validations;
    exports com.lsv.lib.core.concept.repository;
    exports com.lsv.lib.core.concept.dto;
    exports com.lsv.lib.core.helper;

    opens com.lsv.lib.core.concept.service.validations;

    uses Identifiable;
}