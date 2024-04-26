package org.example;

import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import dev.cel.bundle.Cel;
import dev.cel.bundle.CelBuilder;
import dev.cel.bundle.CelFactory;
import dev.cel.common.CelOptions;
import dev.cel.common.CelValidationException;
import dev.cel.common.CelValidationResult;
import dev.cel.common.types.MapType;
import dev.cel.common.types.SimpleType;
import dev.cel.common.types.StructTypeReference;
import dev.cel.runtime.CelEvaluationException;
import dev.cel.runtime.CelRuntime;
import org.example.test.Test;

import java.util.Map;

public class Main {
    public static void main(String[] args) throws CelValidationException, CelEvaluationException, InvalidProtocolBufferException {
        CelBuilder celBuilder = CelFactory.standardCelBuilder()
                .addMessageTypes(Test.getDescriptor())
                .setOptions(CelOptions.newBuilder()
                        .enableCelValue(true)
                        .enableOptionalSyntax(true)
                        .build());

        Cel cel = celBuilder
                .addVar("a", MapType.create(SimpleType.STRING, SimpleType.STRING))
                .addVar("contents", StructTypeReference.create(Test.Contents.getDescriptor().getFullName()))
                .build();
        CelValidationResult validationResult = cel.compile("contents.b.size() > 0");
        CelRuntime.Program program = cel.createProgram(validationResult.getAst());


        Test testValue = Test.newBuilder()
                .putA("test_key_1", "test_key_2")
                .setContents(
                        Test.Contents.newBuilder()
                                .putAllB(
                                        Map.ofEntries(
                                                Map.entry("test_key_b_1", "test_value_b_1"),
                                                Map.entry("test?key_b_2", "test_value_b_2")
                                        )
                                )
                ).build();
        Object queryResult = program.eval(testValue);
        System.out.println(queryResult instanceof Boolean ? queryResult : false);


        String stringJson = """
                {
                    "a" : {
                        "test_key_a" : "test_value_a"
                    },
                    "contents" : {
                        "b" : {
                            "test_key_b_1" : "test_value_b_1",
                            "test_key_b_2" : "test_value_b_2"
                        }
                    }
                }
                """;
        DynamicMessage.Builder messageBuilder = DynamicMessage.newBuilder(Test.getDescriptor());
        JsonFormat.parser().ignoringUnknownFields().merge(stringJson, messageBuilder);
        // Following cause Exception : Exception in thread "main" java.lang.ClassCastException: class com.google.protobuf.DynamicMessage cannot be cast to class com.google.protobuf.MapEntry (com.google.protobuf.DynamicMessage and com.google.protobuf.MapEntry are in unnamed module of loader 'app')
        Object queryResult2 = program.eval(messageBuilder.build());
        System.out.println(queryResult2 instanceof Boolean ? queryResult2 : false);
    }
}