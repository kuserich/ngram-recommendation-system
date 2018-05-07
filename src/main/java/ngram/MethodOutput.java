package ngram;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import opennlp.tools.util.StringList;

import java.util.List;

public class MethodOutput implements IMethodName {

    private StringList methodName;

    public void setMethodName (StringList name) {
        this.methodName = name;
    }

    @Override
    public String toString () {
        return String.valueOf(this.methodName);
    }

    @Override
    public boolean isConstructor() {
        return false;
    }

    @Override
    public boolean isInit() {
        return false;
    }

    @Override
    public ITypeName getReturnType() {
        return null;
    }

    @Override
    public boolean isExtensionMethod() {
        return false;
    }

    @Override
    public boolean hasTypeParameters() {
        return false;
    }

    @Override
    public List<ITypeParameterName> getTypeParameters() {
        return null;
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
    public List<IParameterName> getParameters() {
        return null;
    }

    @Override
    public boolean hasParameters() {
        return false;
    }

    @Override
    public ITypeName getDeclaringType() {
        return null;
    }

    @Override
    public ITypeName getValueType() {
        return null;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getFullName() {
        return null;
    }

    @Override
    public int compareTo(IMethodName o) {
        return 0;
    }
}
