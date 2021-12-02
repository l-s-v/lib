import com.lsv.lib.core.behavior.Mapper;
import com.lsv.lib.mapping.modelmapper.ModelMapper;

module com.lsv.lib.mapping.modelmapper {
    requires lombok;

    requires modelmapper;

    requires com.lsv.lib.core;

    provides Mapper with ModelMapper;

    // Apenas para que reconheça os arquivos utilizados no teste
    exports com.lsv.lib.mapping.modelmapper.test to modelmapper;
}