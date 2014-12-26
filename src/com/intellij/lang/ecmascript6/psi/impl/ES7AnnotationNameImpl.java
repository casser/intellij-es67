/*
 * Copyright 2014-2015 Sergey Mamyan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * ES7 AnnotationName AST Node Implementation
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
