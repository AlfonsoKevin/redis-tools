package io.github.alfonsokevin.core.base.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @description: SpringEl表达式工具类
 * @create: 2025-04-22 15:01
 * @author: TangZhiKai
 **/
public class SpElUtils {

    private static final DefaultParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    /**
     * 创建解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    /**
     * 解析SpringEl表达式
     * 创建解析器
     * 构造上下文
     * 解析表达式
     * 求值
     * @param method 原有方法
     * @param args 方法的参数
     * @param el 需要解析的EL
     * @return 解析后的EL表达式
     */
    public static String parseEl(Method method, Object[] args, String el){
        //解析方法之后的参数名
        String[] parameterNames = NAME_DISCOVERER.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        for(int i =0 ;i < parameterNames.length;i++){
            //方法名，参数名
            context.setVariable(parameterNames[i],args[i]);
        }
        Expression expression = PARSER.parseExpression(el);
        return expression.getValue(context,String.class);
    }

    /**
     * 获取方法的完全限定名
     * @param method 方法
     * @return key
     */
    public static String getMethodPrefix(Method method){
        return method.getDeclaringClass() + "#" + method.getName();
    }

}
