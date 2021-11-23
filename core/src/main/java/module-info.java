import com.lsv.lib.core.mock.Implementation;
import com.lsv.lib.core.mock.InterfaceTest;

module com.lsv.lib.core {
    requires static lombok;

    exports com.lsv.lib.core;

    // Apenas para que os testes encontrem
    uses InterfaceTest;
    provides InterfaceTest with Implementation;
}