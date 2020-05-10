package nl.dimario.calc;


import nl.dimario.numbercalc.NumberParser;
import nl.dimario.numbercalc.NumberRenderer;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class NumberRendererTest {

    private Map<String, Number> context;

    @Mock private NumberParser.MultdivContext multdivContext;

    @BeforeEach
    public void setup() {
        context = new HashMap<>();
    }

    @Test
    public void testGoodConstants() {

        ParseTree tree = TestUtils.parseString("${n3.21}");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(3.21D, number);

        tree = TestUtils.parseString("${n321}");
        number = renderer.visit(tree);
        assertEquals(321L, number);
    }

    @Test
    public void testBadConstant() {

        ParseTree tree;
        try {
            tree = TestUtils.parseString("${n3x.21}");
            assertTrue(false);
        } catch (Exception x) {
            assert( x.getMessage().contains("mismatched input 'x'"));
        }
    }

    @Test
    public void testGoodIdentifiers() {
        context.put("foo.bar", 1.23);
        ParseTree tree = TestUtils.parseString("${nfoo.bar}");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(1.23D, number);

        context.put("foobar", 123);
        tree = TestUtils.parseString("${nfoobar}");
        number = renderer.visit(tree);
        assertEquals(123, number);

        context.put("foo_bar", 4.56);
        tree = TestUtils.parseString("${nfoo_bar}");
        number = renderer.visit(tree);
        assertEquals(4.56, number);

        context.put("_foo._bar", 789);
        tree = TestUtils.parseString("${n_foo._bar}");
        number = renderer.visit(tree);
        assertEquals(789, number);

        context.put("_3foo._2bar", 7.89);
        tree = TestUtils.parseString("${n_3foo._2bar}");
        number = renderer.visit(tree);
        assertEquals(7.89, number);
    }

    @Test
    public void testBadIdentifiers() {

        ParseTree tree;
        try {
            tree = TestUtils.parseString("${n.foo.bar}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert( x.getMessage().contains("'.foo'"));
        }
        try {
            tree = TestUtils.parseString("${n2foo}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert( x.getMessage().contains("'foo'"));
        }
        try {
            tree = TestUtils.parseString("${n#foo.bar}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert( x.getMessage().contains("'#'"));
        }
        try {
            tree = TestUtils.parseString("${n$foo.bar}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert( x.getMessage().contains("'$'"));
        }
    }

    @Test
    public void testBraces() {
        ParseTree tree = TestUtils.parseString("${n(33)}");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals(33L, number);
        try {
            tree = TestUtils.parseString("${n((44)}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert( x.getMessage().contains("')'"));
        }
        try {
            tree = TestUtils.parseString("${n((44)))}");
            assertTrue(false);
        } catch (ParseCancellationException x) {
            assert( x.getMessage().contains("')'"));
        }
    }


}