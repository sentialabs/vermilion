package nl.dimario.numbercalc;

import java.util.HashMap;
import java.util.Map;

/**
 * This implements phase 1 of the Number Calculation evaparser.
 * The main parser program walks the parsed input text and calls the callbacks in
 * this source file whenever it encounters a variable name.
 *
 * The variables are collected by name in a Map and are available via a getter
 */

public class NumberVariableScanner extends NumberParserBaseListener {

    private Map<String, NumberVariable> variables;

    public NumberVariableScanner() {
        this.variables  = new HashMap<>();
    }

    @Override
    public void enterIdentifier(NumberParser.IdentifierContext ctx) {
        // TODO we probably want to get something indicating the position in the source code
        // so that we can report relevant information in case of missing values.
        String name = ctx.IDENTIFIER().getText();
        if( ! variables.containsKey( name)) {
            NumberVariable variable = new NumberVariable(name);
            variables.put(name, variable);
        }
    }

    public Map getVariables() {
        return this.variables;
    }
}
