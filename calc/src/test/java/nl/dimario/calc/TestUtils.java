package nl.dimario.calc;

import nl.dimario.numbercalc.CustomErrorListener;
import nl.dimario.numbercalc.NumberLexer;
import nl.dimario.numbercalc.NumberParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class TestUtils {

    public static ParseTree parseString(String s) {
        CharStream charStream = CharStreams.fromString(s);
        NumberLexer lexer = new NumberLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(CustomErrorListener.INSTANCE);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        NumberParser parser = new NumberParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(CustomErrorListener.INSTANCE);
        ParseTree tree = parser.document();
        return tree;
    }
}
