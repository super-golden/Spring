package com.sr.el;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public class Interpreter {

	public static void main(String[] args) {
		SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
		Expression expression = spelExpressionParser.parseExpression("100*(2+400)");
		Object value = expression.getValue();
		System.out.println(value);
	}


//	private String parseExpression(String expressionString, Method method, Object[] args) {
//
//		LocalVariableTableParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
//		String[] parameterNames = nameDiscoverer.getParameterNames(method);
//		//Spel 解析
//		SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
//
//		StandardEvaluationContext context = new StandardEvaluationContext();
//		for (int i = 0; i < parameterNames.length; i++) {
//			context.setVariable(parameterNames[i],args[i]);
//		}
//		String value = spelExpressionParser.parseExpression(expressionString).getValue(context, String.class);
//		return value;
//	}


	public void getUserName(String name){

	}
}
