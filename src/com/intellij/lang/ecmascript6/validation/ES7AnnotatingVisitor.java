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

package com.intellij.lang.ecmascript6.validation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.ecmascript6.psi.ES6FromClause;
import com.intellij.lang.ecmascript6.psi.ES7AnnotationName;
import com.intellij.lang.javascript.highlighting.ECMAL4Highlighter;
import com.intellij.lang.javascript.psi.ecmal4.JSAttributeList;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * ES7 Annotating Visitor
 */
public class ES7AnnotatingVisitor extends ES6AnnotatingVisitor {
    public ES7AnnotatingVisitor() {
    }

    public void visitES6FromClause(ES6FromClause fromClause) {
        this.checkReferences(fromClause);
    }

    @Override
    public void visitElement(PsiElement element) {
        if(element instanceof ES7AnnotationName){
            lineMarker(element, DefaultLanguageHighlighterColors.METADATA, "attribute", this.myHolder);
        }else
        if(element instanceof JSAttributeList){
            if(element.getTextLength()>0){
                lineMarker(element, DefaultLanguageHighlighterColors.KEYWORD, "Modifiers", this.myHolder);
            }
        }
        super.visitElement(element);
    }
    private static void lineMarker(@NotNull PsiElement element, TextAttributesKey attrKey, String debugName, AnnotationHolder holder) {
        justLineMarker(element, attrKey, debugName, holder);
    }
    private static void justLineMarker(PsiElement markedNode, TextAttributesKey attrKey, String debugName, AnnotationHolder holder) {
        holder.createInfoAnnotation(markedNode, debugName).setEnforcedTextAttributes(TextAttributes.ERASE_MARKER);
        holder.createInfoAnnotation(markedNode, debugName).setTextAttributes(attrKey);

    }
}
