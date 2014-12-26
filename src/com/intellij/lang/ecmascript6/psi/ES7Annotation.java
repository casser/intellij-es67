package com.intellij.lang.ecmascript6.psi;

import com.intellij.lang.javascript.psi.JSArgumentList;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSSourceElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ES7Annotation extends JSSourceElement {
    ES7AnnotationName getAnnotationName();
    JSArgumentList getArgumentsList();
}