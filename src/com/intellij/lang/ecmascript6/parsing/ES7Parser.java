//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.lang.ecmascript6.parsing;


import com.intellij.lang.ITokenTypeRemapper;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.javascript.JSTokenTypes;
import com.intellij.psi.tree.IElementType;

public class ES7Parser extends ES6Parser {
    public ES7Parser(PsiBuilder builder) {
        super(builder);
        builder.setTokenTypeRemapper(new ITokenTypeRemapper() {
            @Override
            public IElementType filter(IElementType source, int start, int end, CharSequence text) {
                if(source == JSTokenTypes.BAD_CHARACTER && '@'==text.charAt(start)) {
                    return JSTokenTypes.AT;
                }
                return source;
            }
        });
        this.myStatementParser = new ES7StatementParser(this);
        this.myFunctionParser = new ES7FunctionParser(this);
        this.myExpressionParser = new ES7ExpressionParser(this);
    }
}
