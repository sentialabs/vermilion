package nl.dimario.calc;

/**
 * Note that the values used in these calculations have been carefully
 * selected to yield the expected results both when you do the arithmatics
 * manually and when using the Java language library.
 * <p>
 * Due to the nature of floating point arithmatics, this does not
 * always hold true for randomly selected numbers. For instance you may expect
 * 6.02 - 3 to equal 3.02 when in fact the result calculated via
 * NumberRender is 3.0199999999999996.
 * This is not an error in the NumberRender code but follows from the
 * imperfections inherent in floating point arithmatics.
 * <p>
 * This careful selection of the example calculations was done in order
 * to not complicate the issue at hand by introducing real world concerns.
 */

import nl.dimario.numbercalc.NumberRenderer;
import nl.dimario.numbercalc.ScriptExpander;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class NumberRendererTest {

    private Map<String, Number> context;
    private ScriptExpander scriptExpander;

    @BeforeEach
    public void setup() {
        scriptExpander = new ScriptExpander();
        context = new HashMap<>();
    }

    private static final String STATIC_TEXT = "This is just static text";
    private static final String STATIC_WITH_DOLAR = "This is static text with a $ dolar";
    private static final String STATIC_WITH_DOLAR_BRACE = "This is static text with a ${ dolar } and curly";
    private static final String STATIC_WITH_DOLAR_SPACE_BRACE_STAR = "This is static text with a $ {* dolar } and curly and some space";
    private static final String STATIC_WITH_DOLAR_BRACE_SPACE_STAR = "This is static text with a ${ * dolar } and curly and some space";
    private static final String STATIC_WITH_EXPRESSION_SPACE = "This is static text with an expression ${* aa }";
    private static final String STATIC_WITH_EXPRESSION_SPACE_RESULT = "This is static text with an expression 10";
    private static final String STATIC_WITH_EXPRESSION_NO_SPACE = "This is static text with an expression ${*aa} and some more";
    private static final String STATIC_WITH_EXPRESSION_NO_SPACE_RESULT = "This is static text with an expression 10 and some more";

    @Test
    public void testIslandGrammar() {
        ParseTree tree = scriptExpander.parse(STATIC_TEXT);
        String actual = scriptExpander.render();
        assertEquals( STATIC_TEXT, actual);

        tree = scriptExpander.parse(STATIC_WITH_DOLAR);
        actual = scriptExpander.render();
        assertEquals(STATIC_WITH_DOLAR, actual);

        tree = scriptExpander.parse(STATIC_WITH_DOLAR_BRACE);
        actual = scriptExpander.render();
        assertEquals(STATIC_WITH_DOLAR_BRACE, actual);

        tree = scriptExpander.parse(STATIC_WITH_DOLAR_SPACE_BRACE_STAR);
        actual = scriptExpander.render();
        assertEquals(STATIC_WITH_DOLAR_SPACE_BRACE_STAR, actual);

        tree = scriptExpander.parse(STATIC_WITH_DOLAR_BRACE_SPACE_STAR);
        actual = scriptExpander.render();
        assertEquals(STATIC_WITH_DOLAR_BRACE_SPACE_STAR, actual);

        tree = scriptExpander.parse(STATIC_WITH_EXPRESSION_SPACE);
        context.put( "value", 10);
        actual = scriptExpander.render();
        assertEquals(STATIC_WITH_EXPRESSION_SPACE_RESULT, actual);

        tree = scriptExpander.parse(STATIC_WITH_EXPRESSION_NO_SPACE);
        context.put( "value", 10);
        actual = scriptExpander.render();
        assertEquals(STATIC_WITH_EXPRESSION_NO_SPACE_RESULT, actual);
    }

    @Test
    public void testGoodConstants() {

        ParseTree tree = scriptExpander.parse("${*3.21}");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(3.21D, number);

        tree = scriptExpander.parse("${*321}");
        number = renderer.visit(tree);
        assertEquals(321L, number);
    }

    @Test
    public void testBadConstant() {

        ParseTree tree;
        try {
            tree = scriptExpander.parse("${*3x.21}");
            assertTrue(false);
        } catch (Exception x) {
            assert (x.getMessage().contains("mismatched input 'x'"));
        }
    }

    @Test
    public void testGoodIdentifiers() {
        context.put("foo.bar", 1.23);
        ParseTree tree = scriptExpander.parse("${*foo.bar}");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(1.23D, number);

        context.put("foobar", 123);
        tree = scriptExpander.parse("${*foobar}");
        number = renderer.visit(tree);
        assertEquals(123, number);

        context.put("foo_bar", 4.56);
        tree = scriptExpander.parse("${*foo_bar}");
        number = renderer.visit(tree);
        assertEquals(4.56, number);

        context.put("_foo._bar", 789);
        tree = scriptExpander.parse("${*_foo._bar}");
        number = renderer.visit(tree);
        assertEquals(789, number);

        context.put("_3foo._2bar", 7.89);
        tree = scriptExpander.parse("${*_3foo._2bar}");
        number = renderer.visit(tree);
        assertEquals(7.89, number);
    }

    @Test
    public void testBadIdentifiers() {

        ParseTree tree;
        try {
            tree = scriptExpander.parse("${*.foo.bar}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert (x.getMessage().contains("'.foo'"));
        }
        try {
            tree = scriptExpander.parse("${*2foo}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert (x.getMessage().contains("'foo'"));
        }
        try {
            tree = scriptExpander.parse("${*#foo.bar}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert (x.getMessage().contains("'#'"));
        }
        try {
            tree = scriptExpander.parse("${*$foo.bar}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert (x.getMessage().contains("'$'"));
        }
    }

    @Test
    public void testBraces() {
        ParseTree tree = scriptExpander.parse("${*(33)}");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(33L, number);
        try {
            tree = scriptExpander.parse("${*((44)}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert (x.getMessage().contains("')'"));
        }
        try {
            tree = scriptExpander.parse("${*((44)))}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert (x.getMessage().contains("')'"));
        }
    }

    @Test
    public void testMultiply() {
        ParseTree tree = scriptExpander.parse("${*(2*3)}");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(6L, number);

        tree = scriptExpander.parse("${*(2.34*3)}");
        number = renderer.visit(tree);
        assertEquals(7.02D, number);

        tree = scriptExpander.parse("${*(2*3.01)}");
        number = renderer.visit(tree);
        assertEquals(6.02D, number);

        tree = scriptExpander.parse("${* ( 2.34 * 3.02 ) }");
        number = renderer.visit(tree);
        assertEquals(7.0668D, number);
    }

    @Test
    public void testDivision() {
        ParseTree tree = scriptExpander.parse("${*  (  6  /  3  )  }");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(2L, number);

        tree = scriptExpander.parse("${*(7.02/3)}");
        number = renderer.visit(tree);
        assertEquals(2.34D, number);

        tree = scriptExpander.parse("${*(12/2.4)}");
        number = renderer.visit(tree);
        assertEquals(5.0D, number);

        tree = scriptExpander.parse("${* ( 7.0668 / 3.02 ) }");
        number = renderer.visit(tree);
        assertEquals(2.34D, number);
    }

    @Test
    public void testAdditition() {
        ParseTree tree = scriptExpander.parse("${*  (  2+  5  )  }");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(7L, number);

        tree = scriptExpander.parse("${*(7.02+3 ) }");
        number = renderer.visit(tree);
        assertEquals(10.02D, number);

        tree = scriptExpander.parse("${*(12 + 2.4)}");
        number = renderer.visit(tree);
        assertEquals(14.4D, number);

        tree = scriptExpander.parse("${* ( 7.0668 + 3.02 ) }");
        number = renderer.visit(tree);
        assertEquals(10.0868D, number);
    }

    @Test
    public void testSubtraction() {
        ParseTree tree = scriptExpander.parse("${*  (  2 -  5  )  }");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(-3L, number);

        tree = scriptExpander.parse("${*(7.02 - 3 ) }");
        number = renderer.visit(tree);
        assertEquals(4.02D, number);

        tree = scriptExpander.parse("${* ( 1 - 3.36)}");
        number = renderer.visit(tree);
        assertEquals(-2.36D, number);

        tree = scriptExpander.parse("${* ( 7.03 - 2.03 ) }");
        number = renderer.visit(tree);
        assertEquals(5.0D, number);
    }

}