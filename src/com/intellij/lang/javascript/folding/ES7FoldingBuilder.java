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

package com.intellij.lang.javascript.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * ES7 Folding Builder
 */
public class ES7FoldingBuilder extends JavaScriptFoldingBuilder {
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
        if(ES6ElementTypes.ATTRIBUTE_LIST.equals(node.getElementType())){
            if(node.getTextRange().getLength()>0){
                descriptors.add(new FoldingDescriptor(node,node.getTextRange()));
            }
        }else
        if(ES6ElementTypes.PARAMETER_LIST.equals(node.getElementType())){
            ASTNode parent = node.getTreeParent();
            if(
                ES6ElementTypes.FUNCTION_DECLARATION.equals(parent.getElementType()) &&
                ES6ElementTypes.CLASS.equals(parent.getTreeParent().getElementType())
            ){
                descriptors.add(new FoldingDescriptor(node,node.getTextRange()));
            }
        }
        return super.appendDescriptors(node, document, descriptors);
    }
    protected String getLanguagePlaceholderText(@NotNull ASTNode node, @NotNull TextRange range) {
        if(ES6ElementTypes.ATTRIBUTE_LIST.equals(node.getElementType())){
            return "@(..)";
        }else
        if(ES6ElementTypes.PARAMETER_LIST.equals(node.getElementType())){
            return "(...)";
        }else
        if(EXPORT_IMPORT.contains(node.getElementType())){
            return "{...}";
        }/*else
        if(TRAIT.contains(node.getElementType())){
            return "{...}";
        }*/
        return super.getLanguagePlaceholderText(node, range);
    }
}
