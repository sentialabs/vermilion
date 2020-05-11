package nl.dimario.contentrewriter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;

import org.hippoecm.hst.content.rewriter.impl.SimpleContentRewriter;
import org.hippoecm.hst.core.request.HstRequestContext;

import nl.dimario.numbercalc.ScriptExpander;

public class ScriptingContentRewriter extends SimpleContentRewriter {

    @Override
    public String rewrite(final String html, Node node, HstRequestContext requestContext) {
        ScriptExpander scriptExpander = new ScriptExpander();
        try {
            scriptExpander.parse(html);
            Set<String> variables = scriptExpander.getVariableNames();
            Map<String, Number> context = new HashMap<>();
            int ct = 2;
            for (String name : variables) {
                context.put(name, ct++);
            }
            return scriptExpander.render(context);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return null;
    }
}
