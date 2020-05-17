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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

public class ScriptExpander {

    private ParseTree tree;
    private Set<String> variableNames;

    /**
     * parse the source script into a tree of expression nodes,
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
     * 
     * Note that in the current state of things, the set of scanned
     * variables is never used. Instead the external data is
     * obtained via a JsonDataSourrce.
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
    
    private String loadDataFromResource(String resourceName) throws IOException {
    	File file = new File(getClass().getClassLoader().getResource(resourceName).getFile());
    	return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    /**
     * render() uses a JsonDataSource to get some variables from an
     * external source (in this case a file from project resources).
     * It then tries to parse the input and evaluate the embedded expressions.
     * When errors occur, these are returned instead.
     * 
     * @return          A string containing unaltered HTML mixed with the result of the
     *                  calculations.
     */
    public String render(String dataFile) {
    	try {
    		String data = loadDataFromResource(dataFile);
	    	JsonDataSource jsonDataSource = new JsonDataSource();
	    	Map<String,Number> variables = jsonDataSource.getValues(data);
	        NumberRenderer renderer = new NumberRenderer(variables);
	        return renderer.render(tree);
    	} catch(Exception x) {
    		return String.format("*** ERROR ***\n\n%s",x.getMessage());
    	}
    }
}
