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

/**
 * ES7 Parser Definition
 */
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