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
import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.lang.javascript.formatter.blocks.JSBlock;
import com.intellij.lang.javascript.formatter.blocks.SubBlockVisitor;
import com.intellij.lang.javascript.formatter.blocks.alignment.ASTNodeBasedAlignmentFactory;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ES7Formatting Model Builder
 */
public class ES7FormattingModelBuilder extends JavascriptFormattingModelBuilder {
    static final TokenSet EXPORT_IMPORT = TokenSet.create(
            ES6ElementTypes.IMPORT_DECLARATION,
            ES6ElementTypes.EXPORT_DECLARATION
    );
    @Override
    public JSBlock createSubBlock(@NotNull ASTNode child, Alignment childAlignment, Indent childIndent, Wrap wrap, @NotNull CodeStyleSettings topSettings, @NotNull Language dialect, @Nullable ASTNodeBasedAlignmentFactory sharedAlignmentFactory) {
        return new ES7Block(child, childAlignment, childIndent, wrap, topSettings, dialect, sharedAlignmentFactory);
    }

    public static class ES7BlockVisitor extends SubBlockVisitor {
        public ES7BlockVisitor(@Nullable JSBlock block, @NotNull CodeStyleSettings settings, @NotNull Language dialect, ASTNodeBasedAlignmentFactory alignmentFactory) {
            super(block, settings,dialect,alignmentFactory);
        }

        @Nullable
        @Override
        protected Indent getIndent(ASTNode node, ASTNode child) {
            if(EXPORT_IMPORT.contains(child.getElementType())){
                return Indent.getAbsoluteNoneIndent();
            }
            if(
                JSTokenTypes.RBRACE.equals(child.getElementType()) ||
                JSTokenTypes.LBRACE.equals(child.getElementType())
            ){
                if(child.getTreeParent()!=null && EXPORT_IMPORT.contains(child.getTreeParent().getElementType())){
                    return Indent.getNoneIndent();
                }
            }
            if(ES6ElementTypes.ATTRIBUTE_LIST.equals(node.getElementType())){
                return Indent.getNoneIndent();
            }
            if(ES6ElementTypes.VARIABLE.equals(child.getElementType())){
                return Indent.getNoneIndent();
            }
            return super.getIndent(node, child);

        }
    }

    public static class ES7Block extends JSBlock {
        private final Language dialect;
        private final ASTNodeBasedAlignmentFactory sharedAlignmentFactory;

        public ES7Block(@NotNull ASTNode node, Alignment alignment, Indent indent, Wrap wrap, @NotNull CodeStyleSettings settings, Language dialect,@Nullable ASTNodeBasedAlignmentFactory sharedAlignmentFactory) {
            super(node, alignment, indent, wrap, settings, sharedAlignmentFactory, dialect);
            this.sharedAlignmentFactory = sharedAlignmentFactory;
            this.dialect = dialect;
        }

        @Override
        protected SubBlockVisitor createSubBlockVisitor() {
            return new ES7BlockVisitor(this, this.getSettings(),this.dialect,this.sharedAlignmentFactory);
        }
    }
}
