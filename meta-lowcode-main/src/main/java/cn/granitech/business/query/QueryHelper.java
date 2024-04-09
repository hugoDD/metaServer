package cn.granitech.business.query;

import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.persistence.queryCompiler.SqlCompiler;
import cn.granitech.variantorm.persistence.queryCompiler.antlr4.MySqlLexer;
import cn.granitech.variantorm.persistence.queryCompiler.antlr4.MySqlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Arrays;
import java.util.List;

/**
 * @author ly-dourx
 */
public class QueryHelper implements SqlCompiler {
    @Override
    public SelectStatement compileEasySql(MetadataManager metadata, String sql) {
        MySqlParser parser = new MySqlParser(new CommonTokenStream(new MySqlLexer(CharStreams.fromString(sql))));
        List<String> list = Arrays.asList(parser.getRuleNames());
        SelectStatement select = new SelectStatement(parser.selectStatement(), list);
        select.compiler(metadata);
        return select;
    }
}
