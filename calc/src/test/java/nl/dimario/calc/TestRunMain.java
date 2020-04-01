package nl.dimario.calc;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class TestRunMain {

    public static void main( String[] args) throws IOException {
        CalcLexer lexer = new CalcLexer(new ANTLRFileStream("src/test/resources/calc.test"));
        CommonTokenStream tokens = new CommonTokenStream( lexer);
        CalcParser parser = new CalcParser( tokens);
        ParseTree tree = parser.document();
        CalcRenderer renderer  = new CalcRenderer();
        renderer.visit( tree);
        String output = renderer.getOutput();
        System.out.println( output);
    }
}