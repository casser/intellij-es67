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

package com.intellij.lang.ecmascript6.parsing;


import com.intellij.lang.PsiBuilder;
import com.intellij.lang.atscript.AtScriptParser;

/**
 * ES7 Parser
 */
public class ES7Parser extends AtScriptParser {
    public ES7Parser(PsiBuilder builder) {
        super(builder);
        /*builder.setTokenTypeRemapper(new ITokenTypeRemapper() {
            @Override
            public IElementType filter(IElementType source, int start, int end, CharSequence text) {
                if(source == JSTokenTypes.BAD_CHARACTER && '@'==text.charAt(start)) {
                    return JSTokenTypes.AT;
                }
                return source;
            }
        });*/
        this.myStatementParser  = new ES7StatementParser(this);
        this.myFunctionParser   = new ES7FunctionParser(this);
        this.myExpressionParser = new ES7ExpressionParser(this);
    }
}
