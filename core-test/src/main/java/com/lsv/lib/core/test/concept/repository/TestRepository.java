package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.pattern.register.RegisterByInterface;
import com.lsv.lib.core.test.TestForFactory;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

public interface TestRepository<
    D extends Identifiable<?>,
    R extends Repository<D>>
    extends
    TestRepositoryProvider<D>,
    TestForFactory {

    @SuppressWarnings({"unchecked", "rawtypes"})
    default R repository() {
        /*
         * Simulates Mappable.findInstance so that it is not necessary to
         * define the dependency of some implementation inside the module.
         * It was the possible way to test the automatic functioning (by service module)
         * because the instances are created with default constructors.
         * */
        try (MockedStatic<Mappable> mappableMockedStatic = Mockito.mockStatic(Mappable.class)) {
            mappableMockedStatic
                .when(() -> Mappable.findInstance(any(), any(), any()))
                .thenAnswer(invocation -> mappable().setup(
                    (Class<Identifiable<?>>) (Object) HelperClass.identifyGenericsClass(invocation.getArguments()[0], Identifiable.class),
                    (Class<Persistable<?>>) (Object) HelperClass.identifyGenericsClass(invocation.getArguments()[0], Persistable.class)
                ));

            return (R) RegisterByInterface.findImplementation(
                HelperClass.identifyGenericsClass(this, Repository.class));
        }
    }

    Mappable<Identifiable<?>, Persistable<?>> mappable();
}