package com.intellij.lang.ecmascript6.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import com.intellij.lang.ecmascript6.psi.ES7Annotation;
import com.intellij.lang.ecmascript6.psi.ES7AnnotationName;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSReferenceExpression;
import com.intellij.lang.javascript.psi.impl.JSElementImpl;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Sergey on 12/24/14.
 */
public class ES7AnnotationNameImpl extends JSElementImpl implements ES7AnnotationName {

    public ES7AnnotationNameImpl(ASTNode node) {
        super(node);
    }

    @Override
    public JSReferenceExpression getReferenceExpression() {
        return (JSReferenceExpression) this.getNode().findChildByType(ES6ElementTypes.REFERENCE_EXPRESSION).getPsi();
    }
}
