module com.lsv.lib.spring.jpa.test {
    requires org.junit.jupiter.api;
    requires spring.boot.test.autoconfigure;

    requires transitive com.lsv.lib.core;

    exports com.lsv.lib.spring.jpa.test;
}