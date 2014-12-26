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

package com.intellij.lang.javascript.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import com.intellij.lang.ecmascript6.parsing.ES7ElementTypes;
import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.lang.javascript.JavaScriptSupportLoader;
import com.intellij.lang.javascript.formatter.blocks.JSBlock;
import com.intellij.lang.javascript.formatter.blocks.SubBlockVisitor;
import com.intellij.lang.javascript.types.JSFileElementType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ES7Formatting Model Builder
 */
public class ES7FormattingModelBuilder extends JavascriptFormattingModelBuilder {
    @Override
    public JSBlock createSubBlock(@NotNull ASTNode child, Alignment childAlignment, Indent childIndent, Wrap wrap, @NotNull CodeStyleSettings topSettings, @NotNull Language dialect) {
        return new ES7Block(child, childAlignment, childIndent, wrap, topSettings, JavaScriptSupportLoader.ECMA_SCRIPT_6);
    }

    public static class ES7BlockVisitor extends SubBlockVisitor {
        public ES7BlockVisitor(@Nullable JSBlock block, @NotNull CodeStyleSettings settings) {
            super(block, settings, JavaScriptSupportLoader.ECMA_SCRIPT_6);
        }

        @Nullable
        @Override
        protected Indent getIndent(ASTNode node, ASTNode child) {
            Indent indent = super.getIndent(node, child);
            if (indent == null) {
                indent = Indent.getNoneIndent();
            }
            if(
                ES6ElementTypes.IMPORT_SPECIFIER.equals(child.getElementType()) ||
                ES6ElementTypes.MIXIN_STATEMENT.equals(child.getElementType())||
                ES6ElementTypes.AWAIT_STATEMENT.equals(child.getElementType())
            ){
                indent = Indent.getNormalIndent();
            }
            if(ES7ElementTypes.ANNOTATIONS.equals(child.getElementType())){
                indent = (node.getElementType() instanceof JSFileElementType)?Indent.getNoneIndent():Indent.getNormalIndent();
            }
            if(ES6ElementTypes.TRAIT.equals(node.getElementType())){
                indent = (
                    ES6ElementTypes.REFERENCE_EXPRESSION.equals(child.getElementType())||
                    JSTokenTypes.TRAIT_KEYWORD.equals(child.getElementType())||
                    JSTokenTypes.RBRACE.equals(child.getElementType())||
                    JSTokenTypes.LBRACE.equals(child.getElementType())
                )?Indent.getNoneIndent():Indent.getNormalIndent();
            }
            //System.out.println(node.getElementType()+" < "+child.getElementType()+" "+indent);
            return indent;
        }
    }

    public static class ES7Block extends JSBlock {
        public ES7Block(@NotNull ASTNode node, Alignment alignment, Indent indent, Wrap wrap, @NotNull CodeStyleSettings settings, Language dialect) {
            super(node, alignment, indent, wrap, settings, dialect);
        }

        @Override
        protected SubBlockVisitor createSubBlockVisitor() {
            return new ES7BlockVisitor(this, this.getSettings());
        }
    }
}
