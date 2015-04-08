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

package com.intellij.lang.javascript.highlighting;

import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.lang.javascript.dialects.ECMA6LanguageDialect;
import com.intellij.lang.javascript.dialects.ECMA6SyntaxHighlighterFactory;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.tree.IElementType;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * ES7 Syntax Highlighter Factory
 */
public class ES7SyntaxHighlighterFactory extends ECMA6SyntaxHighlighterFactory {
    public ES7SyntaxHighlighterFactory() {
        super();
    }

    @NotNull
    protected SyntaxHighlighter createHighlighter() {
        return new ES7SyntaxHighlighterFactory.ES7SyntaxHighlighter();
    }

    private static class ES7SyntaxHighlighter extends JSHighlighter {
        private final Map<IElementType, TextAttributesKey> myKeysMap = new THashMap();

        public ES7SyntaxHighlighter() {
            super(ECMA6LanguageDialect.DIALECT_OPTION_HOLDER);
            this.myKeysMap.put(JSTokenTypes.STRING_TEMPLATE_PART, DefaultLanguageHighlighterColors.STRING);
            this.myKeysMap.put(JSTokenTypes.AT, DefaultLanguageHighlighterColors.METADATA);
            this.myKeysMap.put(JSTokenTypes.FROM_KEYWORD, DefaultLanguageHighlighterColors.KEYWORD);
            this.myKeysMap.put(JSTokenTypes.ASYNC_KEYWORD, DefaultLanguageHighlighterColors.KEYWORD);
            this.myKeysMap.put(JSTokenTypes.AWAIT_KEYWORD, DefaultLanguageHighlighterColors.KEYWORD);
        }

        @NotNull
        public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
            return this.myKeysMap.containsKey(tokenType)?pack((TextAttributesKey)this.myKeysMap.get(tokenType)):super.getTokenHighlights(tokenType);
        }

        @Override
        public TextAttributesKey getMappedKey(TextAttributesKey original) {
            return super.getMappedKey(original);
        }
    }

}