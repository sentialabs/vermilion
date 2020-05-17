package nl.dimario.numbercalc;

import java.util.HashSet;
import java.util.Set;

/**
 * This implements a scan to discover all identifiers in the script.
 * The variable names are collected by name in a Set.
 * Some other process or method must resolve Number values for each
 * name. These values will then be used by the renderer to perform
 * the actual calculations.
 * 
 * You should use this scanner when your external datasource wants to 
 * know the names for the values that you want to do calculations with.
 * 
 * The alternative is that the datasource returns an object that always
 * has the same structure (or at least you know in advance what names
 * it will use) and in that case you can parse the returned object
 * into a Map and use that. 
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
