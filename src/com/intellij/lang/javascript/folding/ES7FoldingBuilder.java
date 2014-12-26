package com.intellij.lang.javascript.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import com.intellij.lang.ecmascript6.parsing.ES7ElementTypes;
import com.intellij.lang.ecmascript6.psi.ES7Annotations;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.lang.typescript.TypeScriptElementTypes;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Sergey on 12/25/14.
 */
public class ES7FoldingBuilder extends JavaScriptFoldingBuilder {
    static final TokenSet TRAIT = TokenSet.create(
        ES6ElementTypes.TRAIT
    );
    static final TokenSet EXPORT_IMPORT = TokenSet.create(
        ES6ElementTypes.IMPORT_DECLARATION,
        ES6ElementTypes.EXPORT_DECLARATION
    );
    protected ASTNode appendDescriptors(ASTNode node, Document document, List<FoldingDescriptor> descriptors) {
        if(EXPORT_IMPORT.contains(node.getElementType())){
            ASTNode rc = node.findChildByType(JSTokenTypes.RBRACE);
            ASTNode lc =node.findChildByType(JSTokenTypes.LBRACE);
            if(rc!=null && lc!=null){
                descriptors.add(new FoldingDescriptor(node,
                    new TextRange(lc.getStartOffset(),rc.getStartOffset()+1)
                ));
            }
        }else
        if(TRAIT.contains(node.getElementType())){
            ASTNode lb = node.findChildByType(JSTokenTypes.LBRACE);
            if(lb!=null){
                descriptors.add(new FoldingDescriptor(node,
                    new TextRange(lb.getStartOffset(),node.getStartOffset()+node.getTextLength())
                ));
            }
        }else
        if(ES7ElementTypes.ANNOTATIONS.equals(node.getElementType())){
            descriptors.add(new FoldingDescriptor(node,node.getTextRange()));
        }
        return super.appendDescriptors(node, document, descriptors);
    }
    protected String getLanguagePlaceholderText(@NotNull ASTNode node, @NotNull TextRange range) {
        if(ES7ElementTypes.ANNOTATIONS.equals(node.getElementType())){
            return ((ES7Annotations)node.getPsi()).getFoldingText();
        }else
        if(EXPORT_IMPORT.contains(node.getElementType())){
            return "{...}";
        }else
        if(TRAIT.contains(node.getElementType())){
            return "{...}";
        }
        return super.getLanguagePlaceholderText(node, range);
    }
}
