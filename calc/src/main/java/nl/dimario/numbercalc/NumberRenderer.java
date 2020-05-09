package nl.dimario.numbercalc;

import org.antlr.v4.runtime.tree.ParseTree;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;

public class NumberRenderer extends NumberParserBaseVisitor<Number> {

    private static final int DEFAULTSIZE = 10000;

    private StringBuilder output;

    private Map<String, Number> variables;

    public NumberRenderer( Map<String,Number> variables) {
        this.variables =variables;
        this.output = new StringBuilder( DEFAULTSIZE);
    }

    public String render(ParseTree tree) {
        super.visit(tree);
        return this.output.toString();
    }

    @Override
    public Number visitStatictext(NumberParser.StatictextContext ctx) {
        String staticText = ctx.STATICTEXT().getText();
        output.append( staticText);
        return super.visitStatictext(ctx);
    }

    @Override
    public Number visitResult(NumberParser.ResultContext ctx) {
        try {
            Number value = visit(ctx.expression());
            output.append(value.toString());
            return value;
        } catch(  Exception x) {
            output.append( "*** ERROR ");
            output.append( x.getMessage());
            output.append( " ***");
            return null;
        }
    }

    @Override
    public Number visitConstant(NumberParser.ConstantContext ctx) {
        try {
            Number constant = NumberFormat.getNumberInstance().parse( ctx.CONSTANT().getText());
            return constant;
        } catch (ParseException e) {
            throw new NumberFormatException(e.getMessage());
        }
    }

    @Override
    public Number visitIdentifier(NumberParser.IdentifierContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        Number number = this.variables.get(name);
        return number;
    }

    @Override
    public Number visitBraces(NumberParser.BracesContext ctx) {
        return visit( ctx.expression());
    }

    @Override
    public Number visitMultdiv(NumberParser.MultdivContext ctx) {
        Number left = visit( ctx.expression(0));
        Number right = visit( ctx.expression(1));
        boolean divide = ctx.DIV() != null;
        return NumberUtils.multdiv(left, right, divide);
    }

    @Override
    public Number visitAddsub(NumberParser.AddsubContext ctx) {
        Number left = visit( ctx.expression(0));
        Number right = visit( ctx.expression(1));
        boolean add = ctx.PLUS() != null;
        return NumberUtils.plusmin(left, right, add);
    }
}
