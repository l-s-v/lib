import com.lsv.lib.el.jexl.TemplateJexl;

module com.lsv.lib.el.jexl {
    requires static lombok;
    requires commons.jexl3;

    requires transitive com.lsv.lib.template;

    exports com.lsv.lib.el.jexl;
    provides com.lsv.lib.template.Template with TemplateJexl;
}