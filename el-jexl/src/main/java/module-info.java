import com.lsv.lib.el.jexl.TemplateJexl;
import com.lsv.lib.template.Template;

module com.lsv.lib.el.jexl {
    requires static lombok;
    requires commons.jexl3;

    requires transitive com.lsv.lib.template;

    exports com.lsv.lib.el.jexl;
    provides Template with TemplateJexl;
}