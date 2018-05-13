package evaluation;

import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.model.events.completionevents.ICompletionEvent;

public interface IEventVisitor<TContext, TReturn> {

    TReturn visit(IIDEEvent event, TContext context);
    TReturn visit(ICompletionEvent event, TContext context);
    
}
