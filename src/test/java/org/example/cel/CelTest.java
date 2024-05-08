package org.example.cel;

import com.google.protobuf.*;
import com.google.protobuf.util.JsonFormat;
import dev.cel.common.CelValidationException;
import dev.cel.common.types.CelType;
import dev.cel.common.types.MapType;
import dev.cel.common.types.SimpleType;
import dev.cel.common.types.StructTypeReference;
import dev.cel.runtime.CelEvaluationException;
import dev.cel.runtime.CelRuntime;
import org.example.utils.ProtoUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CelTest {
    @Test
    public void testWithFileDescriptorSet() throws InvalidProtocolBufferException, CelValidationException, CelEvaluationException {
        // load FileDescriptors from DescriptorSet
        List<Descriptors.FileDescriptor> fileDescriptors = ProtoUtils.buildFileDescriptors("/test.pb");
        List<Descriptors.Descriptor> testDescriptors = fileDescriptors.getFirst().getMessageTypes();
        String query = "contents.map_field_b.size() > 0";
        Map<String, CelType> vars = Map.ofEntries(
                Map.entry("map_field_a", MapType.create(SimpleType.STRING, SimpleType.STRING)),
                Map.entry("contents", StructTypeReference.create("test.Test.Contents"))
        );

        CelRuntime.Program program = CelUtils.buildProgram(vars, query, testDescriptors);
        String stringJson = """
                {
                    "contents" : {
                        "map_field_b" : {
                            "foo" : "bar"
                        }
                    }
                }
                """;
        // =========
        // build DynamicMessage
        // =========
        DynamicMessage.Builder messageBuilder = DynamicMessage.newBuilder(testDescriptors.getFirst());
        JsonFormat.parser().ignoringUnknownFields().merge(stringJson, messageBuilder);

        // This will cause ClassCastException
        Assertions.assertDoesNotThrow(() -> program.eval(toMap(messageBuilder.build())));
    }

    private static Map<String, ?> toMap(Message message) {
        Map<String, Object> result = new HashMap<>();
        for (Descriptors.FieldDescriptor d : message.getDescriptorForType().getFields()) {
            Object fieldValue = message.getAllFields().getOrDefault(d, message.getField(d));
            result.put(d.getName(), fieldValue);
        }
        return result;
    }
}
