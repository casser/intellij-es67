package com.intellij.lang.ecmascript6.parsing;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import com.intellij.lang.ecmascript6.psi.ES7AnnotationName;
import com.intellij.lang.ecmascript6.psi.impl.ES6ModuleImportImpl;
import com.intellij.lang.ecmascript6.psi.impl.ES7AnnotationImpl;
import com.intellij.lang.ecmascript6.psi.impl.ES7AnnotationNameImpl;
import com.intellij.lang.ecmascript6.psi.impl.ES7AnnotationsImpl;
import com.intellij.lang.javascript.JSCompositeElementType;
import com.intellij.lang.javascript.psi.JSElement;
import com.intellij.psi.tree.IElementType;

/**
 * Created by Sergey on 12/24/14.
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
