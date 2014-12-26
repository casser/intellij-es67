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
import com.intellij.lang.ecmascript6.parsing.ES6FunctionParser;
import com.intellij.lang.ecmascript6.parsing.ES6Parser;
import com.intellij.lang.javascript.JSBundle;
import com.intellij.lang.javascript.JSElementTypes;
import com.intellij.lang.javascript.JSStubElementTypes;
import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.lang.javascript.parsing.JSParsingResult;
import com.intellij.lang.javascript.parsing.StatementParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * ES7 Function Parser
 */
public class ES7FunctionParser extends ES6FunctionParser{
    private ES7ExpressionParser getExpressionParser(){
        return (ES7ExpressionParser) this.myJavaScriptParser.getExpressionParser();
    }
    private ES7StatementParser getStatementParser(){
        return (ES7StatementParser) this.myJavaScriptParser.getStatementParser();
    }

    protected ES7FunctionParser(ES6Parser parser) {
        super(parser);
    }

    @Override
    public boolean parseAttributesList() {
        Boolean parsed = false;
        PsiBuilder.Marker modifierList = this.builder.mark();
        while(this.builder.getTokenType() == JSTokenTypes.EXPORT_KEYWORD || (this.builder.getTokenType() == JSTokenTypes.IDENTIFIER && "async".equals(this.builder.getTokenText()))) {
            this.builder.advanceLexer();
            parsed = true;
        }
        modifierList.done(JSElementTypes.ATTRIBUTE_LIST);

        return parsed;
    }

    @Override
    public boolean parseArrowFunction() {
        PsiBuilder.Marker arrowFunction = this.builder.mark();
        this.parseAttributesList();
        IElementType firstToken = this.builder.getTokenType();
        boolean isDefinitelyArrowFunction = false;
        if(this.isIdentifierToken(firstToken)) {
            PsiBuilder.Marker arrow = this.builder.mark();
            PsiBuilder.Marker parameter = this.builder.mark();
            this.builder.advanceLexer();
            parameter.done(JSStubElementTypes.FORMAL_PARAMETER);
            arrow.done(JSStubElementTypes.PARAMETER_LIST);
        } else {
            JSParsingResult arrow1 = this.parseParameterList(true);
            if(arrow1.hasImprint()) {
                isDefinitelyArrowFunction = true;
            } else if(arrow1.hasErrors()) {
                arrowFunction.rollbackTo();
                return false;
            }

            getExpressionParser().tryParseType();
        }

        IElementType arrow2 = this.builder.getTokenType();
        if(JSTokenTypes.ARROWS.contains(arrow2)) {
            this.builder.advanceLexer();
            getStatementParser().parseBlockOrFunctionBody(StatementParser.BlockType.ARROW_FUNCTION_BODY);
            isDefinitelyArrowFunction = true;
        } else if(isDefinitelyArrowFunction) {
            this.builder.error(JSBundle.message("javascript.parser.message.expected.eqgt", new Object[0]));
        }

        if(isDefinitelyArrowFunction) {
            arrowFunction.done(this.getFunctionExpressionElementType());
        } else {
            arrowFunction.rollbackTo();
        }

        return isDefinitelyArrowFunction;
    }
    @Override
    protected boolean parseParameterAttributeList() {
        getStatementParser().parseAnnotations();
        return true;
    }

}
