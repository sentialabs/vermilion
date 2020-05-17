package nl.dimario.calc;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import nl.dimario.numbercalc.ScriptExpander;

public class TestRunMain {

    public static void main( String[] args) throws IOException {

        String script = FileUtils.readFileToString(new File("src/test/resources/number.test"), StandardCharsets.UTF_8);
        ScriptExpander scriptExpander = new ScriptExpander();

        try {
            scriptExpander.parse(script);
//            Set<String> variables = scriptExpander.getVariableNames();
//            Map<String, Number> context = new HashMap<>();
//            int ct = 2;
//            for (String name : variables) {
//                context.put(name, ct++);
//            }
            String result = scriptExpander.render();
            System.out.println(result);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}