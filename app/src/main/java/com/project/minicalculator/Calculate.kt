package com.project.minicalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun Calculate(viewModel: CalculatorViewModel = viewModel()){
    val calculateBox = viewModel.calculateBox.value
    val answer = viewModel.answer.value
    Column (modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        BasicTextField(
            value = calculateBox,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp).background(Color.White)
                .shadow(elevation = 30.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                .wrapContentSize(),
            textStyle = (MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Right)),
            singleLine = true,
        )

        BasicTextField(
            value = answer,
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth().background(Color.White)
                .shadow(elevation = 30.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp)),
                readOnly = true,
            textStyle = (MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Right, color = Color.Blue)),
            singleLine = true,

//            if (answer.isNotEmpty()) answer else presentNumber
        )
    }

        // Buttons Function

        //Operation function section
//        fun operationButton(selectOperation: Char){
//            if (presentNumber.isNotEmpty()){
//                previousNumber = presentNumber
//                presentNumber = ""
//                operate = selectOperation
//                calculateBox = "$previousNumber $selectOperation"
//            }
//            if (calculateBox.isNotEmpty() && calculateBox.last() !in "+-*/") {
//                calculateBox += selectOperation
//            }
//        }

        fun evaluateExpression(expression: String): String {
            return try {
                // Replace with an Expression Evaluator
                val result = object : Any() {
                    var index = -1
                    var ch = 0

                    fun nextChar() {
                        ch = if (++index < expression.length) expression[index].code else -1
                    }

                    fun eat(charToEat: Int): Boolean {
                        while (ch == ' '.code) nextChar()
                        if (ch == charToEat) {
                            nextChar()
                            return true
                        }
                        return false
                    }

                    fun parse(): Double {
                        nextChar()
                        val x = parseExpression()
                        if (index < expression.length) throw RuntimeException("Unexpected: " + ch.toChar())
                        return x
                    }

                    // Grammar:
                    // expression = term | expression `+` term | expression `-` term
                    private fun parseExpression(): Double {
                        var x = parseTerm()
                        while (true) {
                            when {
                                eat('+'.code) -> x += parseTerm() // Addition
                                eat('-'.code) -> x -= parseTerm() // Subtraction
                                else -> return x
                            }
                        }
                    }

                    // term = factor | term `*` factor | term `/` factor
                    private fun parseTerm(): Double {
                        var x = parseFactor()
                        while (true) {
                            when {
                                eat('*'.code) -> x *= parseFactor() // Multiplication
                                eat('/'.code) -> x /= parseFactor() // Division
                                else -> return x
                            }
                        }
                    }

                    // factor = `+` factor | `-` factor | `(` expression `)` | number
                    //        | functionName `(` expression `)` | functionName factor | factor `^` factor
                    private fun parseFactor(): Double {
                        when {
                            eat('+'.code) -> return parseFactor() // Unary plus
                            eat('-'.code) -> return -parseFactor() // Unary minus
                        }
                        var x: Double
                        val startPos = index
                        when {
                            eat('('.code) -> { // Parentheses
                                x = parseExpression()
                                eat(')'.code)
                            }

                            ch in '0'.code..'9'.code || ch == '.'.code -> { // Numbers
                                while (ch in '0'.code..'9'.code || ch == '.'.code) nextChar()
                                x = expression.substring(startPos, index).toDouble()
                            }

                            else -> throw RuntimeException("Unexpected: " + ch.toChar())
                        }
                        return x
                    }
                }.parse()

                // Remove trailing .0 for whole numbers
                if (result % 1.0 == 0.0) result.toInt().toString() else result.toString()
            } catch (e: Exception) {
                "Error"
            }
        }


        Column (modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom){
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
                horizontalArrangement = Arrangement.SpaceBetween){

                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 30.dp, spotColor = Color.Black,shape = RoundedCornerShape(30.dp))
                    .height(70.dp), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
//
                    onClick = { viewModel.onClearButton() }) { Text("C", color = Color.Blue, fontSize = 30.sp, fontWeight = FontWeight.SemiBold)}

                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.operationButton('/') }) { Text("/", color = Color.Blue, fontSize = 30.sp, fontWeight = FontWeight.Bold)}

                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.operationButton('*') }) { Text("x", color = Color.Blue, fontSize = 30.sp, fontWeight = FontWeight.Bold)}

                Button(modifier = Modifier
                    .width(90.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp).size(64.dp), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.cancelLastNumber() }) {Text("DEL", color = Color.Blue, fontSize = 20.sp, fontWeight = FontWeight.Bold)}
            }

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.onClickButton("7")}) { Text("7", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)}

                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.onClickButton("8")}) { Text("8", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)}

                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.onClickButton("9")}) { Text("9", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)}

                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.operationButton('-') }) { Text("-", color = Color.Blue, fontSize = 40.sp, fontWeight = FontWeight.Bold)}
            }


            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.onClickButton("4")}) { Text("4", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)}
                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.onClickButton("5")}) { Text("5", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)}
                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.onClickButton("6")}) { Text("6", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)}
                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.operationButton('+') }) { Text("+", color = Color.Blue, fontSize = 40.sp, fontWeight = FontWeight.Bold)}
            }

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.onClickButton("1") }) { Text("1", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)}
                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.onClickButton("2") }) { Text("2", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)}
                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.onClickButton("3") }) { Text("3", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)}
                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.operationButton('-') }) { Text("-", color = Color.Blue, fontSize = 30.sp, fontWeight = FontWeight.Bold)}
            }

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
                horizontalArrangement = Arrangement.SpaceBetween){

                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = {  }) { Text("%", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)}
                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = { viewModel.onClickButton("0") }) { Text("0", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)}

                Button(modifier = Modifier
                    .width(70.dp).shadow(elevation = 10.dp, spotColor = Color.Black,shape = RoundedCornerShape(20.dp))
                    .height(70.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White),
                    onClick = {  }) { Text(",", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)}

                Button(modifier = Modifier
                    .width(60.dp).shadow(elevation = 10.dp, spotColor = Color.Blue,shape = RoundedCornerShape(20.dp))
                    .height(110.dp),colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue),
                    onClick = { viewModel.equalToClick() }) { Text("=", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)}
            }


        }

    }