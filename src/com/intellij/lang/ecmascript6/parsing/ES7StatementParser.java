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
import com.intellij.lang.WhitespacesBinders;
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import com.intellij.lang.javascript.*;
import com.intellij.lang.javascript.parsing.FunctionParser;
import com.intellij.psi.tree.IElementType;

/**
 * ES7 Statement Parser
 */
public class ES7StatementParser extends ES6StatementParser{
    private static boolean isPropertyNameStart(IElementType elementType) {
        return JSKeywordSets.IDENTIFIER_NAMES.contains(elementType) || elementType == JSTokenTypes.LBRACKET;
    }
    protected ES7StatementParser(ES6Parser parser) {
        super(parser);
    }
    public void parseSourceElement() {
        this.parseSourceElementAttributesList();
        IElementType firstToken = this.builder.getTokenType();
        if(firstToken == JSTokenTypes.EXPORT_KEYWORD &&(this.builder.lookAhead(1)== JSTokenTypes.MULT || this.builder.lookAhead(1) == JSTokenTypes.LBRACE)) {
            this.parseExportDeclaration(this.builder.mark());
        }else{
            super.parseSourceElement();
        }

    }
    public void parseSourceElementAttributesList() {
        PsiBuilder.Marker attrList = this.builder.mark();
        Boolean attrs = false;
        while(this.builder.getTokenType() == JSTokenTypes.AT) {
            attrs = true;
            this.myJavaScriptParser.getFunctionParser().parseAttributeWithoutBrackets();
        }
        if(attrs){
            attrList.done(JSStubElementTypes.ATTRIBUTE_LIST);
        }else{
            attrList.drop();
        }
    }
    public void parseClassMember() {
        PsiBuilder.Marker classMember = this.builder.mark();
        int classMemberOffset = this.builder.getCurrentOffset();
        if(this.builder.getTokenType() == JSTokenTypes.SEMICOLON) {
            classMember.drop();
            this.parseEmptyStatement();
        } else {
            boolean lexerAdvanced = false;
            PsiBuilder.Marker attrList = this.builder.mark();
            this.myJavaScriptParser.getFunctionParser().tryParseAttributesWithoutBrackets();
            IElementType modifier = this.builder.getTokenType();
            while((
                modifier == JSTokenTypes.STATIC_KEYWORD         ||
                modifier == JSTokenTypes.PUBLIC_KEYWORD         ||
                modifier == JSTokenTypes.PRIVATE_KEYWORD        ||
                modifier == JSTokenTypes.ASYNC_KEYWORD          ||
                modifier == JSTokenTypes.CONST_KEYWORD          ||
                modifier == JSTokenTypes.VAR_KEYWORD            ||
                modifier == JSTokenTypes.PROTECTED_KEYWORD
            )) {
                this.builder.advanceLexer();
                modifier = this.builder.getTokenType();
                lexerAdvanced = true;
            }

            attrList.done(JSStubElementTypes.ATTRIBUTE_LIST);
            IElementType nextToken = this.builder.getTokenType();
            if(nextToken == JSTokenTypes.CLASS_KEYWORD) {
                this.parseClassNoMarker(classMember);
            } else
            if(nextToken == JSTokenTypes.MIXIN_KEYWORD) {
                this.builder.advanceLexer();
                if(this.builder.getTokenType() == JSTokenTypes.IDENTIFIER) {
                    this.myJavaScriptParser.getExpressionParser().buildTokenElement(JSElementTypes.REFERENCE_EXPRESSION);
                }

                this.forceCheckForSemicolon();
                classMember.done(JSElementTypes.MIXIN_STATEMENT);
            } else if(nextToken == JSTokenTypes.VAR_KEYWORD || nextToken == JSTokenTypes.CONST_KEYWORD || nextToken == JSTokenTypes.LET_KEYWORD) {
                this.parseVarStatementNoMarker(false, classMember);
            } else if(nextToken != JSTokenTypes.GET_KEYWORD && nextToken != JSTokenTypes.SET_KEYWORD && nextToken != JSTokenTypes.MULT && nextToken != JSTokenTypes.FUNCTION_KEYWORD) {
                if(isPropertyNameStart(nextToken)) {
                    if(JSKeywordSets.IDENTIFIER_NAMES.contains(nextToken) && JSTokenTypes.LPAR == this.builder.lookAhead(1)) {
                        this.myJavaScriptParser.getFunctionParser().parseFunctionNoMarker(FunctionParser.Context.SOURCE_ELEMENT, classMember);
                        this.forceCheckForSemicolon();
                    } else if(nextToken == JSTokenTypes.LBRACKET) {
                        this.myJavaScriptParser.getExpressionParser().parsePropertyName();
                        this.myJavaScriptParser.getFunctionParser().parseFunctionNoMarker(FunctionParser.Context.EXPRESSION, classMember);
                        this.forceCheckForSemicolon();
                    } else {
                        this.parseVarList(false);
                        this.forceCheckForSemicolon();
                        classMember.done(JSStubElementTypes.VAR_STATEMENT);
                        classMember.setCustomEdgeTokenBinders(INCLUDE_DOC_COMMENT_AT_LEFT, WhitespacesBinders.DEFAULT_RIGHT_BINDER);
                    }
                } else {
                    this.builder.error(JSBundle.message("javascript.parser.message.expected.statement"));
                    if(!lexerAdvanced) {
                        this.builder.advanceLexer();
                    }

                    classMember.drop();
                }
            } else {
                if(nextToken == JSTokenTypes.MULT) {
                    this.builder.advanceLexer();
                }

                this.myJavaScriptParser.getFunctionParser().parseFunctionNoMarker(FunctionParser.Context.SOURCE_ELEMENT, classMember);
            }

            assert this.builder.getCurrentOffset() > classMemberOffset;

        }
    }


    @Override
    protected boolean parseDialectSpecificSourceElements(PsiBuilder.Marker marker) {
        if(this.builder.getTokenType() == JSTokenTypes.IMPORT_KEYWORD){
            this.parseES6ImportStatement(marker);
            return true;
        }else
        if(this.builder.getTokenType() == JSTokenTypes.EXPORT_KEYWORD){
            this.parseExportDeclaration(marker);
            return true;
        }else{
            return super.parseDialectSpecificSourceElements(marker);
        }
    }


    protected void parseExportDeclaration(PsiBuilder.Marker marker) {
        LOG.assertTrue(this.builder.getTokenType() == JSTokenTypes.EXPORT_KEYWORD);
        this.builder.advanceLexer();
        if(this.builder.getTokenType() == JSTokenTypes.MULT) {
            this.builder.advanceLexer();
            if(this.builder.getTokenType() == JSTokenTypes.FROM_KEYWORD){
                this.parseES6FromDeclaration();
                this.forceCheckForSemicolon();
            }
            this.forceCheckForSemicolon();
            marker.done(ES6ElementTypes.EXPORT_DECLARATION);
        } else if(this.builder.getTokenType() == JSTokenTypes.LBRACE) {
            this.builder.advanceLexer();
            this.parseES6ImportOrExportClause(true);
            if(this.builder.getTokenType() == JSTokenTypes.FROM_KEYWORD) {
                this.parseES6FromDeclaration();
            }

            this.forceCheckForSemicolon();
            marker.done(ES6ElementTypes.EXPORT_DECLARATION);
        } else {
            LOG.error("* or { expected");
            marker.drop();
        }

    }
    protected void parseES6ImportStatement() {
        parseES6ImportStatement(this.builder.mark());
    }
    protected void parseES6ImportStatement(PsiBuilder.Marker marker) {
        LOG.assertTrue(this.builder.getTokenType() == JSTokenTypes.IMPORT_KEYWORD);
        this.builder.advanceLexer();
        boolean parsedAnything;
        if(JSTokenTypes.STRING_LITERALS.contains(this.builder.getTokenType())) {
            parsedAnything = true;
            this.builder.advanceLexer();
            if(this.builder.getTokenType() == JSTokenTypes.MULT || this.builder.getTokenType() == JSTokenTypes.LBRACE ){
                this.parseES6ImportClause();
            }
        } else {
            parsedAnything = this.parseES6ImportClause();
            this.parseES6FromDeclaration();
        }

        if(!parsedAnything) {
            this.builder.error(JSBundle.message("javascript.parser.message.expected.identifier.string.literal.or.lbrace"));
        }

        this.forceCheckForSemicolon();
        marker.done(ES6ElementTypes.IMPORT_DECLARATION);
    }
    protected void parseClassNoMarker(PsiBuilder.Marker block) {
        if(!(this instanceof ES6StatementParser) && !this.isIdentifierToken(this.builder.lookAhead(1))) {
            block.drop();
            this.builder.error(JSBundle.message("javascript.parser.message.expected.statement"));
            this.builder.advanceLexer();
        } else {
            LOG.assertTrue(JSTokenTypes.CLASS_KEYWORD == this.builder.getTokenType());
            this.builder.advanceLexer();
            if(!this.isIdentifierToken(this.builder.getTokenType())) {
                this.builder.error(JSBundle.message("javascript.parser.message.expected.name"));
                block.drop();
            } else {
                //this.myJavaScriptParser.getExpressionParser().buildTokenElement(JSElementTypes.REFERENCE_EXPRESSION);
                //((ES7ExpressionParser)this.myJavaScriptParser.getExpressionParser()).parseQualifiedTypeNameTail(this.builder.mark());

                this.myJavaScriptParser.getExpressionParser().parseQualifiedTypeName();
                if(this.builder.getTokenType() == JSTokenTypes.EXTENDS_KEYWORD) {
                    this.parseReferenceList();
                }

                if(this.builder.getTokenType() == JSTokenTypes.IMPLEMENTS_KEYWORD) {
                    this.parseReferenceList();
                }

                if(this.builder.getTokenType() != JSTokenTypes.LBRACE) {
                    this.builder.error(JSBundle.message("javascript.parser.message.expected.lbrace"));
                } else {
                    this.builder.advanceLexer();

                    while(this.builder.getTokenType() != JSTokenTypes.RBRACE) {
                        if(this.builder.eof()) {
                            this.builder.error(JSBundle.message("javascript.parser.message.missing.rbrace"));
                            break;
                        }

                        this.parseClassMember();
                    }

                    this.builder.advanceLexer();
                }

                block.done(this.getClassElementType());
                block.setCustomEdgeTokenBinders(INCLUDE_DOC_COMMENT_AT_LEFT, WhitespacesBinders.DEFAULT_RIGHT_BINDER);
            }
        }
    }


}
