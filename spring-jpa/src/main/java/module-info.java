module com.lsv.lib.spring.jpa {
    requires static lombok;
    requires spring.boot.starter.data.jpa;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires spring.context;
    requires spring.beans;

    requires transitive com.lsv.lib.core;

    exports com.lsv.lib.spring.jpa.repository;
    exports com.lsv.lib.spring.jpa.helper;
}