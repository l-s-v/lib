import com.lsv.lib.template.Template;
import com.lsv.lib.template.mock.TemplateSimulaMock;

module com.lsv.lib.template {
    requires static lombok;
    requires org.apache.commons.lang3;

    requires transitive com.lsv.lib.core;

    exports com.lsv.lib.template;

    uses Template;
    provides Template with TemplateSimulaMock;
}