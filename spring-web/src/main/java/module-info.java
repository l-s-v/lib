module com.lsv.lib.spring.web {
    requires static lombok;
    requires static jakarta.validation;
    requires static spring.data.commons;
    requires static spring.web;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    requires transitive com.lsv.lib.spring.core;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    exports com.lsv.lib.spring.web.controller;
    exports com.lsv.lib.spring.web.advice;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    opens com.lsv.lib.spring.web.advice;
}