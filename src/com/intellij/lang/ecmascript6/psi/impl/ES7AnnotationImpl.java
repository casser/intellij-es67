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
import com.intellij.lang.ecmascript6.parsing.ES7ElementTypes;
import com.intellij.lang.ecmascript6.psi.*;
import com.intellij.lang.javascript.psi.JSArgumentList;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSElementVisitor;
import com.intellij.lang.javascript.psi.impl.JSElementImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ES7 Annotation AST Node Implementation
 */
public class ES7AnnotationImpl  extends JSElementImpl implements ES7Annotation {

    public ES7AnnotationImpl(ASTNode node) {
        super(node);
    }


    @Override
    public ES7AnnotationName getAnnotationName() {
        return (ES7AnnotationName) this.getNode().findChildByType(ES7ElementTypes.ANNOTATION_NAME).getPsi();
    }
    @Override
    public JSArgumentList getArgumentsList() {
        return (JSArgumentList) this.getNode().findChildByType(ES6ElementTypes.ARGUMENT_LIST).getPsi();
    }
}
