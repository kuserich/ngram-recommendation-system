package evaluation;

import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.model.events.completionevents.ICompletionEvent;

public class EvaluationVisitor implements IEventVisitor<Void, Void> {


    @Override
    public Void visit(IIDEEvent event, Void aVoid) {
        System.out.println("is IIDEvent");
        return null;
    }

    @Override
    public Void visit(ICompletionEvent event, Void aVoid) {
        System.out.println("Is ICompletionEvent");
        return null;
    }
}
