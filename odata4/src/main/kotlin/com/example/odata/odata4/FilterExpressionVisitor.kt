package com.example.odata.odata4

import org.apache.olingo.commons.api.edm.EdmEnumType
import org.apache.olingo.commons.api.edm.EdmType
import org.apache.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind
import org.apache.olingo.server.api.uri.queryoption.expression.Expression
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitor
import org.apache.olingo.server.api.uri.queryoption.expression.Literal
import org.apache.olingo.server.api.uri.queryoption.expression.Member
import org.apache.olingo.server.api.uri.queryoption.expression.MethodKind
import org.apache.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind


class FilterExpressionVisitor : ExpressionVisitor<Any> {
    var expressionString = ""

    private fun evaluateBooleanOperation(operator: BinaryOperatorKind, left: Any, right: Any) {
        expressionString += when (operator) {
            BinaryOperatorKind.AND -> " AND "
            BinaryOperatorKind.OR -> "OR"
            else -> throw ODataRuntimeException("unknown operation")
        }
    }

    private fun evaluateComparisonOperation(operator: BinaryOperatorKind, left: Any, right: Any) {
        expressionString += when (operator) {
            BinaryOperatorKind.EQ -> "="
            BinaryOperatorKind.NE -> "!="
            BinaryOperatorKind.GE -> ">="
            BinaryOperatorKind.GT -> ">"
            BinaryOperatorKind.LE -> "<="
            BinaryOperatorKind.LT -> "<"
            else -> throw ODataRuntimeException("not supported")
        }
    }

    private fun evaluateArithmeticOperation(operator: BinaryOperatorKind, left: Any, right: Any) {
        expressionString +=
            when (operator) {
                BinaryOperatorKind.ADD -> "+"
                BinaryOperatorKind.SUB -> "-"
                BinaryOperatorKind.MUL -> "*"
                BinaryOperatorKind.DIV -> "/"
                BinaryOperatorKind.MOD -> "%"
                else -> throw ODataRuntimeException("not supported")
            }
    }

    override fun visitBinaryOperator(operator: BinaryOperatorKind?, left: Any?, right: Any?) {
        expressionString += when (operator) {
            BinaryOperatorKind.ADD -> "+"
            BinaryOperatorKind.MOD -> "%"
            BinaryOperatorKind.MUL -> "*"
            BinaryOperatorKind.DIV -> "/"
            BinaryOperatorKind.SUB -> "-"
            BinaryOperatorKind.EQ -> "="
            BinaryOperatorKind.NE -> "!="
            BinaryOperatorKind.GE -> ">="
            BinaryOperatorKind.GT -> ">"
            BinaryOperatorKind.LE -> "<"
            BinaryOperatorKind.LT -> "<="
            BinaryOperatorKind.AND -> " AND "
            BinaryOperatorKind.OR -> " OR "
            else -> throw ODataRuntimeException("not supported")
        }
    }

    override fun visitBinaryOperator(
        operator: BinaryOperatorKind?,
        left: Any?,
        right: MutableList<Any>?,
    ): Any {
        TODO("Not yet implemented")
    }

    override fun visitUnaryOperator(operator: UnaryOperatorKind?, operand: Any?) {
        expressionString += when (operator) {
            UnaryOperatorKind.NOT -> " NOT "
            UnaryOperatorKind.MINUS -> "-"
            else -> throw ODataRuntimeException("not supported")
        }
    }

    override fun visitMethodCall(methodCall: MethodKind?, parameters: MutableList<Any>?): Any =
        TODO("Not yet implemented")

    override fun visitLambdaExpression(
        lambdaFunction: String?,
        lambdaVariable: String?,
        expression: Expression?,
    ): Any {
        TODO("Not yet implemented")
    }

    override fun visitLiteral(literal: Literal?) {
        expressionString += literal!!.text
    }

    override fun visitMember(member: Member?) {
        val uriResourceParts = member!!.resourcePath.uriResourceParts
        if (uriResourceParts.size == 1) {
            expressionString += uriResourceParts[0].segmentValue
        } else {
            throw ODataRuntimeException("not supported")
        }
    }

    override fun visitAlias(aliasName: String?): Any {
        TODO("Not yet implemented")
    }

    override fun visitTypeLiteral(type: EdmType?): Any {
        TODO("Not yet implemented")
    }

    override fun visitLambdaReference(variableName: String?): Any {
        TODO("Not yet implemented")
    }

    override fun visitEnum(type: EdmEnumType?, enumValues: MutableList<String>?): Any {
        TODO("Not yet implemented")
    }
}