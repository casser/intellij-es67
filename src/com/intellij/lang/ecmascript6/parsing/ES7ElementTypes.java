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

package com.intellij.lang.ecmascript6.parsing;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import com.intellij.lang.ecmascript6.psi.impl.ES7AnnotationImpl;
import com.intellij.lang.ecmascript6.psi.impl.ES7AnnotationNameImpl;
import com.intellij.lang.ecmascript6.psi.impl.ES7AnnotationsImpl;
import com.intellij.lang.javascript.JSCompositeElementType;
import com.intellij.lang.javascript.psi.JSElement;
import com.intellij.psi.tree.IElementType;

/**
 * ES7 AST Element Types
 */
public interface ES7ElementTypes extends ES6ElementTypes {
    IElementType ANNOTATION = new JSCompositeElementType("ANNOTATION") {
        public JSElement construct(ASTNode node) {
            return new ES7AnnotationImpl(node);
        }
    };
    IElementType ANNOTATIONS = new JSCompositeElementType("ANNOTATION") {
        public JSElement construct(ASTNode node) {
            return new ES7AnnotationsImpl(node);
        }
    };
    IElementType ANNOTATION_NAME = new JSCompositeElementType("ANNOTATION_NAME") {
        public JSElement construct(ASTNode node) {
            return new ES7AnnotationNameImpl(node) ;
        }
    };
}
