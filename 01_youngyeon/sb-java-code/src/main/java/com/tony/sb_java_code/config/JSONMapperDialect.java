package com.tony.sb_java_code.config;

import com.google.gson.Gson;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Set;

public class JSONMapperDialect extends AbstractDialect implements IExpressionObjectDialect {

    public JSONMapperDialect() {
        super("JSONMapperDialect");
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        // TODO Auto-generated method stub
        return new IExpressionObjectFactory() {
            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton("JSONMapper");
            }

            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                return new Gson();
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return false;
            }

        };
    }
}
