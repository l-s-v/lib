module com.lsv.lib.template.velocity {
    requires static lombok;
    requires io.pebbletemplates;
    requires com.lsv.lib.template;

    exports com.lsv.lib.template.pebble;
    provides com.lsv.lib.template.Template with com.lsv.lib.template.pebble.TemplatePebble;
}