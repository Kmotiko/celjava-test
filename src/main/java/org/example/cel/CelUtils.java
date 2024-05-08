package org.example.cel;

import com.google.protobuf.Descriptors;
import dev.cel.bundle.Cel;
import dev.cel.bundle.CelBuilder;
import dev.cel.bundle.CelFactory;
import dev.cel.common.CelOptions;
import dev.cel.common.CelValidationException;
import dev.cel.common.CelValidationResult;
import dev.cel.common.types.CelType;
import dev.cel.parser.CelStandardMacro;
import dev.cel.runtime.CelEvaluationException;
import dev.cel.runtime.CelRuntime;

import java.util.List;
import java.util.Map;

public class CelUtils {
    public static CelRuntime.Program buildProgram(
            Map<String, CelType> vars,
            String expr,
            Descriptors.Descriptor descriptor
    ) throws CelValidationException, CelEvaluationException {
        return buildProgram(vars, expr, List.of(descriptor));
    }

    public static CelRuntime.Program buildProgram(
            Map<String, CelType> vars,
            String expr,
            List<Descriptors.Descriptor> descriptors
    ) throws CelValidationException, CelEvaluationException {
        CelBuilder celBuilder = CelFactory.standardCelBuilder()
                .addMessageTypes(descriptors)
                .setOptions(celOption())
                .setStandardMacros(CelStandardMacro.STANDARD_MACROS)
                .setStandardEnvironmentEnabled(true);
        for (Map.Entry<String, CelType> type : vars.entrySet()) {
            celBuilder.addVar(type.getKey(), type.getValue());
        }
        Cel cel = celBuilder.build();

        CelValidationResult validationResult = cel.compile(expr);
        CelRuntime.Program program = cel.createProgram(validationResult.getAst());
        return program;
    }

    private static CelOptions celOption() {
        return CelOptions.newBuilder()
                .enableCelValue(true)
                .enableOptionalSyntax(true)
                .build();
    }
}
