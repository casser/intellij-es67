//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.lang.javascript.dialects;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.ecmascript6.parsing.ES7Parser;
import com.intellij.lang.javascript.JSFlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class ES7ParserDefinition extends ECMA6ParserDefinition {
    @NotNull
    public Lexer createLexer(Project project) {
        return new JSFlexAdapter(ECMA6LanguageDialect.DIALECT_OPTION_HOLDER);
    }

    @NotNull
    public PsiParser createParser(Project project) {


        return new PsiParser() {
            @NotNull
            public ASTNode parse(IElementType root, PsiBuilder builder) {
                (new ES7Parser(builder)).parseJS(root);
                return builder.getTreeBuilt();
            }
        };
    }
}