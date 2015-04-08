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
import com.intellij.lang.atscript.AtScriptElementTypes;
import com.intellij.lang.atscript.AtScriptFunctionParser;
import com.intellij.lang.javascript.JSStubElementTypes;
import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.lang.javascript.parsing.FunctionParser;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

/**
 * ES7 Function Parser
 */
public class ES7FunctionParser extends AtScriptFunctionParser {

    protected ES7FunctionParser(ES6Parser parser) {
        super(parser);
    }
    public void tryParseAttributesWithoutBrackets() {
        while(this.builder.getTokenType() == JSTokenTypes.AT) {
            this.parseAttributeWithoutBrackets();
        }
    }

    public void parseAttributeWithoutBrackets() {
        PsiBuilder.Marker attribute = this.builder.mark();
        this.builder.advanceLexer();
        if(!this.myJavaScriptParser.getExpressionParser().parseQualifiedTypeName()) {
            attribute.drop();
        } else {
            if(this.builder.getTokenType() == JSTokenTypes.LPAR) {
                this.myJavaScriptParser.getExpressionParser().parseArgumentList();
            }
            attribute.done(AtScriptElementTypes.ATTRIBUTE);
        }
    }
    public boolean parseFunctionNoMarker(FunctionParser.Context context, @NotNull PsiBuilder.Marker functionMarker) {
        String oldKey = this.builder.getUserData(methodsEmptinessKey);
        this.builder.putUserData(methodsEmptinessKey,METHODS_EMPTINESS_POSSIBLY);
        Boolean result = super.parseFunctionNoMarker(context, functionMarker);
        this.builder.putUserData(methodsEmptinessKey,oldKey);
        return result;
    }
}
