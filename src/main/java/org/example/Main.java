package org.example;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import dev.cel.common.CelValidationException;
import dev.cel.common.types.CelType;
import dev.cel.common.types.MapType;
import dev.cel.common.types.SimpleType;
import dev.cel.common.types.StructTypeReference;
import dev.cel.runtime.CelEvaluationException;
import dev.cel.runtime.CelRuntime;
import org.example.cel.CelUtils;
import org.example.utils.ProtoUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws CelValidationException, CelEvaluationException, InvalidProtocolBufferException {
        List<Descriptors.FileDescriptor> fileDescriptors= ProtoUtils.buildFileDescriptors("/test.pb");
        List<Descriptors.Descriptor> testDescriptors = fileDescriptors.getFirst().getMessageTypes();

        Map<String, CelType> vars = Map.ofEntries(
                Map.entry("map_field_a", MapType.create(SimpleType.STRING, SimpleType.STRING)),
                Map.entry("contents", StructTypeReference.create("test.Test.Contents"))
        );
        String expr = "contents.map_field_b.size() > 0";
        CelRuntime.Program program = CelUtils.buildProgram(vars, expr, testDescriptors);

        String stringJson = """
                {
                    "contents" : {
                        "map_field_b" : {
                            "test_key_b_1" : "test_value_b_1",
                            "test_key_b_2" : "test_value_b_2"
                        }
                    }
                }
                """;
        DynamicMessage.Builder messageBuilder = DynamicMessage.newBuilder(testDescriptors.getFirst());
        JsonFormat.parser().ignoringUnknownFields().merge(stringJson, messageBuilder);
        // Following cause Exception : Exception in thread "main" java.lang.ClassCastException: class com.google.protobuf.DynamicMessage cannot be cast to class com.google.protobuf.MapEntry (com.google.protobuf.DynamicMessage and com.google.protobuf.MapEntry are in unnamed module of loader 'app')
        Object result = program.eval(toMap(messageBuilder.build()));
        System.out.println(result);
    }

    private static Map<String, ?> toMap(DynamicMessage message) {
        Map<String, Object> result = new HashMap<>();
        for (Descriptors.FieldDescriptor d : message.getDescriptorForType().getFields()) {
            Object fieldValue = message.getAllFields().getOrDefault(d, message.getField(d));
            result.put(d.getName(), fieldValue);
        }
        return result;
    }
}
