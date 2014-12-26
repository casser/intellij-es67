package com.intellij.lang.ecmascript6.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import com.intellij.lang.ecmascript6.parsing.ES7ElementTypes;
import com.intellij.lang.ecmascript6.psi.ES7Annotation;
import com.intellij.lang.ecmascript6.psi.ES7AnnotationName;
import com.intellij.lang.ecmascript6.psi.ES7Annotations;
import com.intellij.lang.javascript.psi.JSArgumentList;
import com.intellij.lang.javascript.psi.impl.JSElementImpl;

/**
 * Created by Sergey on 12/24/14.
 */
public class ES7AnnotationsImpl extends JSElementImpl implements ES7Annotations {
    public ES7AnnotationsImpl(ASTNode node) {
        super(node);
    }

    @Override
    public String getFoldingText() {
        String name = "";
        for(ES7Annotation annotation:this.findChildrenByClass(ES7Annotation.class)){
            name += annotation.getAnnotationName().getReferenceExpression().getReferenceName()+",";
        }
        return "@("+name.substring(0,name.length()-1)+")";
    }
}
