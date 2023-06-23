package com.lsv.lib.core.test.helper;

import com.lsv.lib.core.loader.Loader;
import com.lsv.lib.core.test.TestForFactory;
import lombok.NonNull;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

public final class HelperDynamicTest {

    private static final String SEPARATOR_DISPLAY = "------------------------------------------------------------";

    @SafeVarargs
    public static Stream<DynamicNode> join(@NonNull Stream<DynamicNode>... dynamicNodeStream) {
        return reduce(Stream.of(dynamicNodeStream));
    }

    @SafeVarargs
    public static Stream<DynamicNode> joinAndRemoveDuplicatedByName(@NonNull Stream<DynamicNode>... dynamicNodeStream) {
        return reduceAndRemoveDuplicatedByName(Stream.of(dynamicNodeStream));
    }

    @SafeVarargs
    public static Stream<DynamicNode> createContainer(@NonNull String displayName, @NonNull Stream<DynamicNode>... dynamicNodeStream) {
        return Stream.of(DynamicContainer.dynamicContainer(displayName,
                reduce(Stream.of(dynamicNodeStream))));
    }

    public static Stream<DynamicNode> reduce(@NonNull Stream<Stream<DynamicNode>> dynamicNodeStream) {
        return dynamicNodeStream.flatMap(o -> o);
    }

    public static Stream<DynamicNode> reduceAndRemoveDuplicatedByName(@NonNull Stream<Stream<DynamicNode>> dynamicNodeStream) {
        return removeDuplicatedByName(dynamicNodeStream.flatMap(o -> o));
    }

    public static Stream<DynamicNode> removeDuplicatedByName(@NonNull Stream<DynamicNode> dynamicNodeStream) {
        HashSet<String> uniqueNames = new HashSet<>();
        return dynamicNodeStream.filter(dynamicTest -> uniqueNames.add(dynamicTest.getDisplayName()));
    }

    public static Stream<DynamicNode> findTestForFactory(@NonNull String... packageName) {
        return findTestForFactory(null, packageName);
    }

    public static Stream<DynamicNode> findTestForFactory(@NonNull Class<?> aClass) {
        return findTestForFactory(aClass, aClass.getPackageName());
    }

    public static Stream<DynamicNode> findTestForFactory(Class<?> aClass, @NonNull String... packagesNames) {
        return formatDisplayWithBottomSeparator(reduce(
                Arrays.stream(packagesNames).map(packageName ->
                        reduce(Loader.of(TestForFactory.class)
                                .findImplementationsByReflection(packageName)
                                .stream()
                                .filter(testForFactory -> aClass == null || aClass.equals(testForFactory.getClass()))
                                .map(testForFactory -> formatDisplayWithTopSeparator(
                                        "- Dynamic " + testForFactory.getClass().getSimpleName(),
                                        testForFactory.of())))
                )));
    }

    public static Stream<DynamicNode> formatDisplayWithTopSeparator(@NonNull String displayName, @NonNull Stream<DynamicNode> dynamicNodeStream) {
        return createContainer(SEPARATOR_DISPLAY,
                createContainer(displayName, dynamicNodeStream));
    }

    public static Stream<DynamicNode> formatDisplayWithBottomSeparator(@NonNull Stream<DynamicNode> dynamicNodeStream) {
        return join(dynamicNodeStream, createContainer(SEPARATOR_DISPLAY, Stream.empty()));
    }

    public static Stream<DynamicNode> formatDisplayWithTopAndBottomSeparator(@NonNull Class<?> aClass, @NonNull Stream<DynamicNode> dynamicNodeStream) {
        return formatDisplayWithBottomSeparator(
                formatDisplayWithTopSeparator(
                        "- Dynamic " + aClass.getSimpleName(),
                        dynamicNodeStream));
    }
}