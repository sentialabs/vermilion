package nl.dimario.numbercalc;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Map;
import java.util.Set;

public class ScriptExpander {

    private ParseTree tree;
    private String script;
    private Set<String> variableNames;

    public ScriptExpander(String script) {
        this.script = script;
    }

    public void parse() throws ParseCancellationException {
        CharStream charStream = CharStreams.fromString(this.script);
        NumberLexer lexer = new NumberLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(CustomErrorListener.INSTANCE);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        NumberParser parser = new NumberParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(CustomErrorListener.INSTANCE);
        this.tree = parser.document();
        this.scanVariables();
    }

    private void scanVariables() {
        NumberVariableScanner scanner  = new NumberVariableScanner();
        ParseTreeWalker johnny = new ParseTreeWalker();
        johnny.walk(scanner, tree);
        this.variableNames = scanner.getVariableNames();
    }

    public Set<String> getVariableNames() {
        return this.variableNames;
    }

    public String render( Map<String,Number> variables) {
        NumberRenderer renderer = new NumberRenderer(variables);
        return renderer.render(tree);
    }
}
