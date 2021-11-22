module com.lsv.lib.template {
    requires static lombok;
    requires org.apache.commons.lang3;

    exports com.lsv.lib.template;
    uses com.lsv.lib.template.Template;
    provides com.lsv.lib.template.Template with com.lsv.lib.template.mock.TemplateSimulaMock;
}