import cc.kave.commons.model.naming.IName;

public class APIToken implements IName {
    
    String invocation;
    String type;
    String operation;
    
    String namespace;

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public boolean isUnknown() {
        return false;
    }

    @Override
    public boolean isHashed() {
        return false;
    }
}
