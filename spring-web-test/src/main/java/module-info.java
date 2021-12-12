module com.lsv.lib.spring.web.test {
    requires lombok;
    requires org.junit.jupiter.api;
    requires static org.mockito.junit.jupiter;
    requires static org.mockito;

    requires static com.fasterxml.jackson.databind;

    requires static spring.boot.test;
    requires static spring.boot.test.autoconfigure;
    requires static spring.test;
    requires static spring.beans;
    requires static spring.data.commons;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    requires transitive com.lsv.lib.core.test;
    requires transitive com.lsv.lib.spring.web;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    exports com.lsv.lib.spring.web.test.controller;
}