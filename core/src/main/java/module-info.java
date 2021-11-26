import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.pattern.register.mock.Implementation;
import com.lsv.lib.core.pattern.register.mock.InterfaceTest;

module com.lsv.lib.core {
    requires static lombok;

    exports com.lsv.lib.core.behavior;
    exports com.lsv.lib.core.pattern.register;
    exports com.lsv.lib.core.concept.service;
    exports com.lsv.lib.core.concept.repository;
    exports com.lsv.lib.core.concept.dto;

    uses Identifiable;
    // Apenas para que os testes encontrem
    uses InterfaceTest;
    provides InterfaceTest with Implementation;
}