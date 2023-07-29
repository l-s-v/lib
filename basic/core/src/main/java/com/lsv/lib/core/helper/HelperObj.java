package com.lsv.lib.core.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsv.lib.core.loader.Loader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;

/**
 * @author Leandro da Silva Vieira
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HelperObj {

    private static ObjectMapper objectMapper;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <T extends Serializable> T cloneSerializable(@NonNull T obj) {
        return deserealize(serialize(obj));
    }

    @SneakyThrows
    public static <T extends Serializable> byte[] serialize(@NonNull T obj) {
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bout)) {

            out.writeObject(obj);

            return bout.toByteArray();
        }
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deserealize(@NonNull byte[] objAsBytes) {
        try (ByteArrayInputStream bin = new ByteArrayInputStream(objAsBytes);
             ObjectInputStream in = new ObjectInputStream(bin)) {

            return (T) in.readObject();
        }
    }

    @SneakyThrows
    public static byte[] compress(byte[] byteArray) {
        var compresser = new Deflater(Deflater.BEST_COMPRESSION, true);

        try(var stream = new ByteArrayOutputStream();
            var deflaterOutputStream = new DeflaterOutputStream(stream, compresser);
        ) {
            deflaterOutputStream.write(byteArray);
            deflaterOutputStream.finish();
            return stream.toByteArray();
        }
    }

    @SneakyThrows
    public static byte[] decompress(byte[] byteArray) {
        var decompresser = new Inflater(true);

        try(var stream = new ByteArrayOutputStream();
            var inflaterOutputStream = new InflaterOutputStream(stream, decompresser);
        ) {
            inflaterOutputStream.write(byteArray);
            inflaterOutputStream.finish();
            return stream.toByteArray();
        }
    }

    @SneakyThrows
    public static String toString(Object value) {
        return objectMapper().writeValueAsString(value);
    }

    public static <T> T convertValue(Object value, Class<T> type) {
        return objectMapper().convertValue(value, type);
    }

    @SneakyThrows
    public static <T> T readValue(String value, Class<T> type) {
        return objectMapper().readValue(value, type);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static ObjectMapper objectMapper() {
        if(objectMapper == null){
            try {
                objectMapper = Loader.of(ObjectMapper.class).findUniqueImplementationByFirstLoader();
            } catch (NoSuchElementException e) {
                objectMapper = new ObjectMapper().setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
            }
        }
        return objectMapper;
    }
}