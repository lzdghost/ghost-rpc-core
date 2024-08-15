package icu.ghost.rpc.serializer;

import java.io.*;

/**
 * @author Ghost
 * @version 1.0
 * @className JDKSerializer
 * @date 2024/07/28 17:59
 * @since 1.0
 */
public class JDKSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T object) {
        ByteArrayOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) {
        ByteArrayInputStream inputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            inputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(inputStream);
            return (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
