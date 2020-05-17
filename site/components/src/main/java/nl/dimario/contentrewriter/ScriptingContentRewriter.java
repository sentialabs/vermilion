package nl.dimario.contentrewriter;

import javax.jcr.Node;

import org.hippoecm.hst.content.rewriter.impl.SimpleContentRewriter;
import org.hippoecm.hst.core.request.HstRequestContext;

import nl.dimario.numbercalc.ScriptExpander;

public class ScriptingContentRewriter extends SimpleContentRewriter {

    @Override
    public String rewrite(final String html, Node node, HstRequestContext requestContext) {
        ScriptExpander scriptExpander = new ScriptExpander();
        scriptExpander.parse(html);
        return scriptExpander.render();
    }
}
