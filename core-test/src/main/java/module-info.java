module com.lsv.lib.core.test {
    requires org.junit.jupiter.api;
    requires org.mockito.junit.jupiter;
    requires org.mockito;

    requires transitive com.lsv.lib.core;

    exports com.lsv.lib.core.test;
    exports com.lsv.lib.core.test.concept.service;
}