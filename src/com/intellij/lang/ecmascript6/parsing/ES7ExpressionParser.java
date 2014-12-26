package com.intellij.lang.ecmascript6.parsing;

import com.intellij.codeInsight.template.LiveTemplateBuilder;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import com.intellij.lang.ecmascript6.parsing.ES6ExpressionParser;
import com.intellij.lang.ecmascript6.parsing.ES6Parser;
import com.intellij.lang.javascript.JSBundle;
import com.intellij.lang.javascript.JSTokenTypes;

/**
 * Created by Sergey on 11/26/14.
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
