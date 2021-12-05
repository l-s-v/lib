import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.mapping.modelmapper.ModelMapper;

module com.lsv.lib.mapping.modelmapper {
    requires lombok;

    requires modelmapper;

    requires com.lsv.lib.core;

    provides Mappable with ModelMapper;

    // Apenas para que reconheça os arquivos utilizados no teste
    exports com.lsv.lib.mapping.modelmapper.test to modelmapper;
}