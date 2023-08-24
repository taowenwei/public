package com.example.odata.odata4

import org.apache.olingo.server.api.uri.queryoption.expression.Expression

class FilterExpressionSalesforce {
    fun evaluate(expression: Expression?) =
        expression?.let {
            expression.toString().replace("[", "")
                .replace("]", "")
                .replace("{", "(")
                .replace("}", ")")
                .replace(" EQ ", "=")
                .replace(" NE ", "!=")
                .replace(" GE ", ">")
                .replace(" GT ", ">=")
                .replace(" LE ", "<=")
                .replace(" LT ", "<")
        }
}