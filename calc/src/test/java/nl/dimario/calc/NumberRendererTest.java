package nl.dimario.calc;


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

    @Test
    public void testGoodConstants() {

        ParseTree tree = scriptExpander.parse("${n3.21}");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(3.21D, number);

        tree = scriptExpander.parse("${n321}");
        number = renderer.visit(tree);
        assertEquals(321L, number);
    }

    @Test
    public void testBadConstant() {

        ParseTree tree;
        try {
            tree = scriptExpander.parse("${n3x.21}");
            assertTrue(false);
        } catch (Exception x) {
            assert( x.getMessage().contains("mismatched input 'x'"));
        }
    }

    @Test
    public void testGoodIdentifiers() {
        context.put("foo.bar", 1.23);
        ParseTree tree = scriptExpander.parse("${nfoo.bar}");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(1.23D, number);

        context.put("foobar", 123);
        tree = scriptExpander.parse("${nfoobar}");
        number = renderer.visit(tree);
        assertEquals(123, number);

        context.put("foo_bar", 4.56);
        tree = scriptExpander.parse("${nfoo_bar}");
        number = renderer.visit(tree);
        assertEquals(4.56, number);

        context.put("_foo._bar", 789);
        tree = scriptExpander.parse("${n_foo._bar}");
        number = renderer.visit(tree);
        assertEquals(789, number);

        context.put("_3foo._2bar", 7.89);
        tree = scriptExpander.parse("${n_3foo._2bar}");
        number = renderer.visit(tree);
        assertEquals(7.89, number);
    }

    @Test
    public void testBadIdentifiers() {

        ParseTree tree;
        try {
            tree = scriptExpander.parse("${n.foo.bar}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert( x.getMessage().contains("'.foo'"));
        }
        try {
            tree = scriptExpander.parse("${n2foo}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert( x.getMessage().contains("'foo'"));
        }
        try {
            tree = scriptExpander.parse("${n#foo.bar}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert( x.getMessage().contains("'#'"));
        }
        try {
            tree = scriptExpander.parse("${n$foo.bar}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert( x.getMessage().contains("'$'"));
        }
    }

    @Test
    public void testBraces() {
        ParseTree tree = scriptExpander.parse("${n(33)}");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(33L, number);
        try {
            tree = scriptExpander.parse("${n((44)}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert( x.getMessage().contains("')'"));
        }
        try {
            tree = scriptExpander.parse("${n((44)))}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert( x.getMessage().contains("')'"));
        }
    }

    @Test
    public void testMultiply() {
        ParseTree tree = scriptExpander.parse("${n(2*3)}");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(6L, number);
        tree = scriptExpander.parse("${n(2*3)}");
    }

}