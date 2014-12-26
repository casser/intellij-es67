package com.intellij.lang.javascript.highlighting;

import com.intellij.lang.ecmascript6.parsing.ES7ElementTypes;
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
 * Created by Sergey on 12/25/14.
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
            this.myKeysMap.put(ES7ElementTypes.ANNOTATION_NAME, DefaultLanguageHighlighterColors.METADATA);
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