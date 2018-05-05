import cc.kave.commons.model.naming.IName;

public class APIToken implements IName {
    
    private String invocation;
    private String type;
    private String operation;
    
    private String namespace;

    public String getInvocation() {
        return invocation;
    }

    public void setInvocation(String invocation) {
        this.invocation = invocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getNamespace() {
        return namespace;
    }
    
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

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

    @Override
    public String toString() {
        return "<"+namespace+", "+operation+">";
    }

}
