import com.lsv.lib.core.concept.repository.RepositoryProvider;
import com.lsv.lib.spring.jpa.helper.RepositoryProviderSpringJpaImpl;

module com.lsv.lib.spring.jpa {
    requires static lombok;
    requires static spring.data.commons;
    requires static spring.data.jpa;
    requires static java.persistence;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    requires transitive com.lsv.lib.spring.core;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    opens com.lsv.lib.spring.jpa.helper;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    exports com.lsv.lib.spring.jpa;
    exports com.lsv.lib.spring.jpa.repository;
    exports com.lsv.lib.spring.jpa.helper;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    provides RepositoryProvider with RepositoryProviderSpringJpaImpl;
}