package org.example.utils;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {
    public static List<Descriptors.FileDescriptor> buildFileDescriptors(String filePath) {
        List<Descriptors.FileDescriptor> fileDescriptors = new ArrayList<>();
        try (InputStream is = ProtoUtils.class.getResourceAsStream(filePath)) {
            byte[] bytes = is.readAllBytes();
            DescriptorProtos.FileDescriptorSet fileDescriptorSet = DescriptorProtos.FileDescriptorSet.parseFrom(bytes);
            for (DescriptorProtos.FileDescriptorProto fileDescriptorProto : fileDescriptorSet.getFileList()) {
                Descriptors.FileDescriptor fileDescriptor =
                        Descriptors.FileDescriptor.buildFrom(fileDescriptorProto, new Descriptors.FileDescriptor[0]);
                fileDescriptors.add(fileDescriptor);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Descriptors.DescriptorValidationException e) {
            throw new RuntimeException(e);
        }
        return fileDescriptors;
    }
}
