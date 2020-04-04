package nl.dimario.calc;

import java.io.IOException;
import java.util.Map;

import nl.dimario.numbercalc.NumberLexer;
import nl.dimario.numbercalc.NumberParser;
import nl.dimario.numbercalc.NumberVariable;
import nl.dimario.numbercalc.NumberVariableScanner;
import nl.dimario.xxx.CalcLexer;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class TestRunMain {

    public static void main( String[] args) throws IOException {
        CharStream charStream = CharStreams.fromFileName( "src/test/resources/number.test");
        NumberLexer lexer = new NumberLexer( charStream);
        CommonTokenStream tokens = new CommonTokenStream( lexer);
        NumberParser parser = new NumberParser( tokens);
        ParseTree tree = parser.document();
        NumberVariableScanner scanner  = new NumberVariableScanner();
        ParseTreeWalker johnny = new ParseTreeWalker();
        johnny.walk(scanner, tree);
        Map<String, NumberVariable> variables = scanner.getVariables();
        int ct = 2;
        for( String name: variables.keySet()) {
            NumberVariable var = variables.get(name);
            var.setValue(ct);
            ct++;
        }
    }
}