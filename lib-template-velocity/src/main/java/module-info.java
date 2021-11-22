module com.lsv.lib.template.velocity {
    requires static lombok;
    requires velocity.engine.core;
    requires com.lsv.lib.template;

    exports com.lsv.lib.template.velocity;
}