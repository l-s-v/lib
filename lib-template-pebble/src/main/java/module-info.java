import com.lsv.lib.template.Template;
import com.lsv.lib.template.pebble.TemplatePebble;

module com.lsv.lib.template.velocity {
    requires static lombok;
    requires io.pebbletemplates;

    requires transitive com.lsv.lib.template;

    exports com.lsv.lib.template.pebble;
    provides Template with TemplatePebble;
}