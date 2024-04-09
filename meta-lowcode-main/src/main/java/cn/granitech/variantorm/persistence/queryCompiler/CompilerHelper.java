package cn.granitech.variantorm.persistence.queryCompiler;

import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.persistence.queryCompiler.antlr4.MySqlLexer;
import cn.granitech.variantorm.persistence.queryCompiler.antlr4.MySqlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Arrays;

public class CompilerHelper {
    public static SelectStatement compileEasySql(MetadataManager metadata, String eSql) {
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(eSql));
        MySqlParser parser = new MySqlParser(new CommonTokenStream(lexer));
        SelectStatement select = new SelectStatement(parser.selectStatement(), Arrays.asList(parser.getRuleNames()));
        select.compiler(metadata);
        return select;
    }
}
