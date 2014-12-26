package com.intellij.lang.ecmascript6.psi;

import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSReferenceExpression;
import com.intellij.lang.javascript.psi.JSSourceElement;

public interface ES7AnnotationName extends JSSourceElement {
    JSReferenceExpression getReferenceExpression();
}