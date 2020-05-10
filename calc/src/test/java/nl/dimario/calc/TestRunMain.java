package nl.dimario.calc;

import nl.dimario.numbercalc.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestRunMain {

    public static void main( String[] args) throws IOException {

        String script = FileUtils.readFileToString(new File("src/test/resources/number.test"), StandardCharsets.UTF_8);
        ScriptExpander scriptExpander = new ScriptExpander();

        try {
            scriptExpander.parse(script);
            Set<String> variables = scriptExpander.getVariableNames();
            Map<String, Number> context = new HashMap<>();
            int ct = 2;
            for (String name : variables) {
                context.put(name, ct++);
            }
            String result = scriptExpander.render(context);
            System.out.println(result);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}