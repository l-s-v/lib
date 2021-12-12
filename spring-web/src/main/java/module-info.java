module com.lsv.lib.spring.web {
    requires static lombok;
    requires static spring.data.commons;
    requires static spring.web;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    requires transitive com.lsv.lib.spring.core;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    exports com.lsv.lib.spring.web.controller;
}