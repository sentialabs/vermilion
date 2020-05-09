package nl.dimario.numbercalc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This implements phase 1 of the Number Calculation evalparser.
 * In phase 1, the main parser program walks the parsed input text and
 * calls the callbacks in this source file whenever it encounters
 * a variable name.
 *
 * The variables are collected by name in a Map
 */

public class NumberVariableScanner extends NumberParserBaseListener {

    private Set<String> variableNames;

    public NumberVariableScanner() {
        this.variableNames = new HashSet<>();
    }

    @Override
    public void enterIdentifier(NumberParser.IdentifierContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        variableNames.add(name);
    }

    public Set getVariableNames() {
        return this.variableNames;
    }
}
