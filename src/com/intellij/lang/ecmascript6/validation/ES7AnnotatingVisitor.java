package com.intellij.lang.ecmascript6.validation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.ecmascript6.psi.ES6FromClause;
import com.intellij.lang.ecmascript6.psi.ES7AnnotationName;
import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.lang.javascript.highlighting.ECMAL4Highlighter;
import com.intellij.lang.javascript.psi.ecmal4.JSAttributeList;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Sergey on 12/25/14.
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
            lineMarker(element, ECMAL4Highlighter.ECMAL4_METADATA, "attribute", this.myHolder);
        }else
        if(element instanceof JSAttributeList){
            lineMarker(element, ECMAL4Highlighter.ECMAL4_KEYWORD, "keyword", this.myHolder);
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
