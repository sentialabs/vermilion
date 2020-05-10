package nl.dimario.numbercalc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This implements a scan to discover all identifiers in the script.
 * The variable names are collected by name in a Set.
 * Some other process or method must resolve Number values for each
 * name. These values will then be used by the renderer to perform
 * the actual calculations.
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
