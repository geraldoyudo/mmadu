package com.mmadu.security;

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

class MmaduMethodSecurityExpressionRoot extends MmaduSecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;
    private Object target;

    MmaduMethodSecurityExpressionRoot(Authentication a) {
        super(a);
    }

    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    public Object getFilterObject() {
        return this.filterObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    public Object getReturnObject() {
        return this.returnObject;
    }

    void setThis(Object target) {
        this.target = target;
    }

    public Object getThis() {
        return this.target;
    }
}
