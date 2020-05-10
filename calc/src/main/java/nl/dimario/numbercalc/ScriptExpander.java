package nl.dimario.numbercalc;

/**
 * The ScriptExpander ties together the other components
 * for interpreting and evaluating embedded scripting.
 */

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
    private Set<String> variableNames;

    /**
     * parse the source script into a tree of expression elements,
     * then fill up the set with variable names found in the embedded
     * scripting.
     *
     * @param script the source code that must be evaluated.
     * @returns the ParseTree resulting from the parse.
     * @throws ParseCancellationException
     *      when syntax errors are encountered.
     */
    public ParseTree parse(String script) throws ParseCancellationException {
        CharStream charStream = CharStreams.fromString(script);
        NumberLexer lexer = new NumberLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(CustomErrorListener.INSTANCE);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        NumberParser parser = new NumberParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(CustomErrorListener.INSTANCE);
        this.tree = parser.document();
        this.scanVariables();
        return tree;
    }

    /**
     * Walk the parsed expression tree and make a note of the value
     * of each unique identifier we encounter. These are the names
     * of variables that are used in the  embedded script. The names of the
     * values must be resolved to Number values. This is the responsibility
     * of the caller that wants the scripting to be evaluated.
     */
    private void scanVariables() {
        NumberVariableScanner scanner  = new NumberVariableScanner();
        ParseTreeWalker johnny = new ParseTreeWalker();
        johnny.walk(scanner, this.tree);
        this.variableNames = scanner.getVariableNames();
    }

    public Set<String> getVariableNames() {
        return this.variableNames;
    }

    /**
     * render() takes a Map that holds the names of all variables used in the
     * script together with a Number value for that identifier. This map
     * is used by the rendering code to resolve names to values.
     *
     * @param variables a Map containing all identifiers found by scanVariables()
     *                  together with their value. It is the responseblity of the
     *                  caller to find values for the names, here we just use them.
     * @return          A string containing unaltered HTML mixed with the rendered
     *                  calculations.
     */
    public String render( Map<String,Number> variables) {
        NumberRenderer renderer = new NumberRenderer(variables);
        return renderer.render(tree);
    }
}
