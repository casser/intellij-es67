package com.intellij.lang.ecmascript6.parsing;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.WhitespacesBinders;
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import com.intellij.lang.ecmascript6.parsing.ES6Parser;
import com.intellij.lang.ecmascript6.parsing.ES6StatementParser;
import com.intellij.lang.javascript.*;
import com.intellij.lang.javascript.parsing.ExpressionParser;
import com.intellij.lang.javascript.parsing.StatementParser;
import com.intellij.lang.javascript.psi.JSElement;
import com.intellij.lang.javascript.psi.impl.JSCallExpressionImpl;
import com.intellij.lang.javascript.psi.impl.JSExpressionStatementImpl;
import com.intellij.psi.tree.IElementType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Sergey on 11/26/14.
 */
public class ES7StatementParser extends ES6StatementParser{
    private ES7ExpressionParser getExpressionParser(){
        return (ES7ExpressionParser) this.myJavaScriptParser.getExpressionParser();
    }
    private ES7FunctionParser getFunctionParser(){
        return (ES7FunctionParser) this.myJavaScriptParser.getFunctionParser();
    }
    protected ES7StatementParser(ES6Parser parser) {
        super(parser);
    }

    @Override
    protected void parseClassOrInterfaceNoMarker(PsiBuilder.Marker clazz) {
        super.parseClassOrInterfaceNoMarker(clazz);
    }

    @Override
    public void parseStatement() {
         IElementType tokenType = this.builder.getTokenType();
        if(tokenType == JSTokenTypes.AT) {
            this.parseAnnotation();
        } else {
            super.parseStatement();
        }
    }

    @Override
    protected void doParseStatement(boolean canHaveClasses) {
        this.parseAnnotations();
        if(this.builder.getTokenType() == JSTokenTypes.AWAIT_KEYWORD){
            this.parseAwaitStatement();
        }else {
            super.doParseStatement(canHaveClasses);
        }
    }
    private void parseAwaitStatement() {
        PsiBuilder.Marker block = this.builder.mark();
        LOG.assertTrue(this.builder.getTokenType() == JSTokenTypes.AWAIT_KEYWORD);
        this.builder.advanceLexer();
        if(!getExpressionParser().parseAssignmentExpression(true, false)) {
            this.builder.error(JSBundle.message("javascript.parser.message.expected.expression", new Object[0]));
        }
        boolean hasNewLine = hasSemanticLinefeedBefore(this.builder);
        if(!hasNewLine) {
            getExpressionParser().parseExpressionOptional();
            this.checkForSemicolon();
        }
        block.done(JSElementTypes.AWAIT_STATEMENT);
    }
    @Override
    public void parseBlockOrFunctionBody(BlockType type) {
        super.parseBlockOrFunctionBody(type);
    }

    protected void parseAnnotations() {
        PsiBuilder.Marker marker = this.builder.mark();
        boolean has = false;
        while (JSTokenTypes.AT.equals(this.builder.getTokenType())){
            if(this.parseAnnotation()){
                has=true;
            }
        }
        if(has){
            marker.done(ES7ElementTypes.ANNOTATIONS);
        }else{
            marker.drop();
        }
    }
    protected void parseES6ImportStatement() {
        PsiBuilder.Marker marker = this.builder.mark();
        LOG.assertTrue(this.builder.getTokenType() == JSTokenTypes.IMPORT_KEYWORD);
        this.builder.advanceLexer();
        boolean parsedAnything = false;
        if(JSTokenTypes.STRING_LITERALS.contains(this.builder.getTokenType())) {
            parsedAnything = true;
            this.builder.advanceLexer();
        } else {
            boolean expectedComma = false;
            if(this.builder.getTokenType() == JSTokenTypes.IDENTIFIER) {
                parsedAnything = true;
                PsiBuilder.Marker importedBinding = this.builder.mark();
                this.builder.advanceLexer();
                importedBinding.done(ES6ElementTypes.IMPORTED_BINDING);
                if(this.builder.getTokenType() == JSTokenTypes.COMMA) {
                    this.builder.advanceLexer();
                } else {
                    expectedComma = true;
                }
            }else
            if(this.builder.getTokenType() == JSTokenTypes.MULT){
                parsedAnything = true;
                PsiBuilder.Marker allMarker = this.builder.mark();
                this.builder.advanceLexer();
                if(this.builder.getTokenType() == JSTokenTypes.AS_KEYWORD){
                    this.builder.advanceLexer();
                    if(this.isIdentifierToken(this.builder.getTokenType())) {
                        this.builder.advanceLexer();
                        allMarker.done(ES6ElementTypes.IMPORT_SPECIFIER);
                    } else {
                        allMarker.drop();
                        this.builder.error(JSBundle.message("javascript.parser.message.expected.name", new Object[0]));
                    }
                }else{
                    allMarker.done(ES6ElementTypes.IMPORT_SPECIFIER);
                }
                if(this.builder.getTokenType() == JSTokenTypes.COMMA) {
                    this.builder.advanceLexer();
                } else {
                    expectedComma = true;
                }
            }

            if(this.builder.getTokenType() == JSTokenTypes.LBRACE) {
                parsedAnything = true;
                if(expectedComma) {
                    this.builder.error(JSBundle.message("javascript.parser.message.expected.comma", new Object[0]));
                }

                this.builder.advanceLexer();
                this.parseES6ImportOrExportClause(false);
            }

            this.parseES6FromDeclaration();
        }

        if(!parsedAnything) {
            this.builder.error(JSBundle.message("javascript.parser.message.expected.identifier.string.literal.or.lbrace", new Object[0]));
        }

        this.forceCheckForSemicolon();
        marker.done(ES6ElementTypes.IMPORT_DECLARATION);
    }

    protected boolean parseAnnotation() {
        PsiBuilder.Marker marker = this.builder.mark();

        PsiBuilder.Marker annoName = this.builder.mark();
        LOG.assertTrue(this.builder.getTokenType() == JSTokenTypes.AT);
        this.builder.advanceLexer();
        if(!this.isIdentifierToken(this.builder.getTokenType())) {
            this.builder.error(JSBundle.message("javascript.parser.message.expected.name", new Object[0]));
            marker.drop();
            annoName.drop();
            return false;
        }else{
            getExpressionParser().buildTokenElement(JSElementTypes.REFERENCE_EXPRESSION);
            annoName.done(ES7ElementTypes.ANNOTATION_NAME);
        }
        if(JSTokenTypes.LPAR.equals(this.builder.getTokenType())){
            ((ES7ExpressionParser) getExpressionParser()).parseArgumentList();
        }
        marker.done(ES7ElementTypes.ANNOTATION);
        return true;
    }

    @Override
    protected void parseClassNoMarker(PsiBuilder.Marker block) {
        super.parseClassNoMarker(block);
    }

    @Override
    protected void parseClassMember() {
        this.parseAnnotations();
        PsiBuilder.Marker classMember = this.builder.mark();
        if(this.builder.getTokenType() == JSTokenTypes.SEMICOLON) {
            classMember.drop();
            this.parseEmptyStatement();
        } else if(this.builder.getTokenType() == JSTokenTypes.CONST_KEYWORD) {
            this.parseVarStatementNoMarker(false, classMember);
        } else {
            boolean lexerAdvanced = false;
            PsiBuilder.Marker attrList = this.builder.mark();
            while(isModifier() && isPropertyNameStart(this.builder.lookAhead(1))) {
                this.builder.advanceLexer();
                lexerAdvanced = true;
            }
            attrList.done(JSElementTypes.ATTRIBUTE_LIST);

            IElementType nextToken = this.builder.getTokenType();
            if(nextToken == JSTokenTypes.MIXIN_KEYWORD) {
                this.builder.advanceLexer();
                if(this.builder.getTokenType() == JSTokenTypes.IDENTIFIER) {
                    getExpressionParser().buildTokenElement(JSElementTypes.REFERENCE_EXPRESSION);
                }

                this.forceCheckForSemicolon();
                classMember.done(JSElementTypes.MIXIN_STATEMENT);
            } else if(nextToken == JSTokenTypes.VAR_KEYWORD) {
                this.parseVarStatementNoMarker(false, classMember);
            } else if(nextToken != JSTokenTypes.GET_KEYWORD && nextToken != JSTokenTypes.SET_KEYWORD && nextToken != JSTokenTypes.MULT && nextToken != JSTokenTypes.FUNCTION_KEYWORD) {
                if(isPropertyNameStart(nextToken)) {
                    if(this.isPropertyToken(nextToken) && JSTokenTypes.LPAR == this.builder.lookAhead(1)) {
                        getFunctionParser().parseFunctionNoMarker(false, classMember);
                        this.forceCheckForSemicolon();
                    } else if(nextToken == JSTokenTypes.LBRACKET) {
                        getExpressionParser().parsePropertyName();
                        getFunctionParser().parseFunctionNoMarker(true, classMember);
                        this.forceCheckForSemicolon();
                    } else {
                        this.parseVarList(false);
                        this.forceCheckForSemicolon();
                        classMember.done(JSStubElementTypes.VAR_STATEMENT);
                        classMember.setCustomEdgeTokenBinders(INCLUDE_DOC_COMMENT_AT_LEFT, WhitespacesBinders.DEFAULT_RIGHT_BINDER);
                    }
                } else {
                    this.builder.error(JSBundle.message("javascript.parser.message.expected.statement", new Object[0]));
                    if(!lexerAdvanced) {
                        this.builder.advanceLexer();
                    }

                    classMember.drop();
                }
            } else {
                if(nextToken == JSTokenTypes.MULT) {
                    this.builder.advanceLexer();
                }

                getFunctionParser().parseFunctionNoMarker(false, classMember);
            }

        }
    }
    private boolean isModifier() {
        IElementType modifier = this.builder.getTokenType();
        return (
           modifier == JSTokenTypes.STATIC_KEYWORD ||
           modifier == JSTokenTypes.PUBLIC_KEYWORD ||
           modifier == JSTokenTypes.PRIVATE_KEYWORD ||
           modifier == JSTokenTypes.IDENTIFIER && "async".equals(this.builder.getTokenText())
        );
    }

    private static boolean isPropertyNameStart(IElementType elementType) {
        return JSTokenTypes.IDENTIFIER_NAMES.contains(elementType) || elementType == JSTokenTypes.LBRACKET;
    }
}
