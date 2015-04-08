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
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import com.intellij.lang.javascript.JSTokenTypes;

/**
 * ES7 Expression Parser
 */
public class ES7ExpressionParser extends ES6ExpressionParser{
    protected ES7ExpressionParser(ES6Parser parser) {
        super(parser);
    }
    protected boolean parseQualifiedTypeNameTail(PsiBuilder.Marker expr) {
        expr.drop();
        return this.builder.getTokenType() != JSTokenTypes.LT || this.tryParseTypeArgumentList(false, ES6ElementTypes.TYPE_ARGUMENT_LIST);
    }
}
