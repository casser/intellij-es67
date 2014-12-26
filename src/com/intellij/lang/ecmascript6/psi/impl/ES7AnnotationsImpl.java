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
import com.intellij.lang.ecmascript6.psi.ES7Annotation;
import com.intellij.lang.ecmascript6.psi.ES7AnnotationName;
import com.intellij.lang.ecmascript6.psi.ES7Annotations;
import com.intellij.lang.javascript.psi.JSArgumentList;
import com.intellij.lang.javascript.psi.impl.JSElementImpl;
/**
 * ES7 Annotations AST Node Implementation
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
