import com.lsv.lib.core.concept.repository.RepositoryProvider;
import com.lsv.lib.core.helper.RepositoryProviderImpl;

module com.lsv.lib.core {
    requires static lombok;
    requires jakarta.validation;
    requires jakarta.el;
    requires org.reflections;

    exports com.lsv.lib.core.behavior;
    exports com.lsv.lib.core.pattern.register;
    exports com.lsv.lib.core.concept.service;
    exports com.lsv.lib.core.concept.service.validations;
    exports com.lsv.lib.core.concept.repository;
    exports com.lsv.lib.core.concept.dto;
    exports com.lsv.lib.core.helper;

    opens com.lsv.lib.core.concept.service.validations;

    provides RepositoryProvider with RepositoryProviderImpl;
}