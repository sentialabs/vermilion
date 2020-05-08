package nl.dimario.calc;

import nl.dimario.numbercalc.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestRunMain {

    public static void main( String[] args) throws IOException {
        CharStream charStream = CharStreams.fromFileName( "src/test/resources/number.test");
        NumberLexer lexer = new NumberLexer( charStream);
        CommonTokenStream tokens = new CommonTokenStream( lexer);
        NumberParser parser = new NumberParser( tokens);
        ParseTree tree = parser.document();
        // Phase 1: collect the variable names
        NumberVariableScanner scanner  = new NumberVariableScanner();
        ParseTreeWalker johnny = new ParseTreeWalker();
        johnny.walk(scanner, tree);
        // Phase 2: resolve the variable names to number values
        Map<String, Number> variables = scanner.getVariables();
        Map<String, Number> context = new HashMap<>();
        int ct = 2;
        for( String name: variables.keySet()) {
            context.put(name, ct++);
        }
        // Phase 3: evaluate the expressions using the resolved number values.
        NumberRenderer renderer = new NumberRenderer(context);
        String result = renderer.render(tree);
        System.out.println( result);
    }
}