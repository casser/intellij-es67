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
import com.intellij.lang.javascript.JSBundle;
import com.intellij.lang.javascript.JSTokenTypes;

/**
 * ES7 Expression Parser
 */
public class ES7ExpressionParser extends ES6ExpressionParser{
    private ES7FunctionParser getFunctionParser(){
        return (ES7FunctionParser) this.myJavaScriptParser.getFunctionParser();
    }
    private ES7StatementParser getStatementParser(){
        return (ES7StatementParser) this.myJavaScriptParser.getStatementParser();
    }

    protected ES7ExpressionParser(ES6Parser parser) {
        super(parser);
    }

    @Override
    public void parseArgumentList() {
        super.parseArgumentList();
    }


    @Override
    protected void parseParenthesizedExpression() {
        super.parseParenthesizedExpression();
    }

    @Override
    public void parsePropertyName() {
        if(this.isPropertyToken(this.builder.getTokenType())) {
            this.builder.advanceLexer();
        } else {
            assert this.builder.getTokenType() == JSTokenTypes.LBRACKET : JSBundle.message("javascript.parser.message.expected.rbracket", new Object[0]);

            this.builder.advanceLexer();
            this.parseAssignmentExpression(false, false);
            checkMatches(this.builder, this.builder.getTokenType(), "javascript.parser.message.expected.rbracket");
        }
    }

    @Override
    public boolean parseAssignmentExpression(boolean allowIn, boolean withinPropertyInitializer) {
        getStatementParser().parseAnnotations();
        if(this.builder.getTokenType()== JSTokenTypes.IDENTIFIER && "async".equals(this.builder.getTokenText())){
            PsiBuilder.Marker marker = this.builder.mark();
            getFunctionParser().parseAttributesList();
            if(this.builder.getTokenType()== JSTokenTypes.FUNCTION_KEYWORD){
               getFunctionParser().parseFunctionNoMarker(true, marker);
                return true;
            }else{
                marker.rollbackTo();
            }
        }
        if(this.builder.getTokenType()== JSTokenTypes.AWAIT_KEYWORD){
            PsiBuilder.Marker marker = this.builder.mark();
            this.builder.advanceLexer();
            super.parseAssignmentExpression(allowIn, withinPropertyInitializer);
            marker.done(ES6ElementTypes.PREFIX_EXPRESSION);
            return true;
        }else{
            return super.parseAssignmentExpression(allowIn, withinPropertyInitializer);
        }

    }
}
