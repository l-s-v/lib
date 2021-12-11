module com.lsv.lib.spring.core {
    requires static lombok;
    requires static spring.context;
    requires static spring.beans;
    requires static spring.data.commons;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    requires transitive com.lsv.lib.core;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    exports com.lsv.lib.spring.core;
    opens com.lsv.lib.spring.core;
}