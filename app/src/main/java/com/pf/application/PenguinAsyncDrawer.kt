package com.pf.application

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.Date

class PenguinAsyncDrawer(
    private val context: Context,
    private val n_j: Byte,
    private val n_b: Byte,
    private val n_e: Byte,
    private val rec: Float,
    private val time: Date,
    private val tu: Float,
    private val listener: MainListener
) : Penguin(
    context,
    n_j,
    n_b,
    n_e,
    rec,
    time,
    tu,
    listener
) {

    var defMatrixLegs2: Deferred<Matrix> ? = null

    var defMatrixLegs: Deferred<Matrix> ? = null

    var defMatrixBod2: Deferred<Matrix> ? = null

    var defMatrixHand2: Deferred<Matrix> ? = null

    var defMatrixHead: Deferred<Matrix> ? = null

    var defMatrixBody: Deferred<Matrix> ? = null

    var defMatrixHand: Deferred<Matrix>? = null

   var defMatrixHeader: Deferred<Matrix>? = null

    val matrixL2 = Matrix()
    val matrixL = Matrix()
    val matrixB2 = Matrix()
    val matrixB = Matrix()
    val matrixH2 = Matrix()
    val matrixH = Matrix()
    val matrixHe2 = Matrix()
    val matrixHe = Matrix()
    val mtrx = Matrix()


    override fun update() {
        super.update()
        var draw_y = y
        if (y <= dh / 8.0) draw_y = (dh / 8.0).toFloat()

        var `as` = anim_step
        if (anima_type == "jump" || anima_type == "bust") {
            `as` = (`as` * 2 + exp).toByte()
        }

        mtrx.setTranslate(x, draw_y)

        defMatrixLegs2 = CoroutineScope(Dispatchers.Default).async {

            val grad: Double
            grad = if (legs2_grad.containsKey(anima_type)) {
                legs2_grad[anima_type]!![anim_step.toInt()]
            } else {
                0.0
            }
            matrixL2.set(mtrx)
            matrixL2.preRotate(
                grad.toFloat(),
                (dw * 215).toFloat() / 850,
                (dw * 369).toFloat() / 850
            )
            return@async matrixL2
        }

         defMatrixLegs = CoroutineScope(Dispatchers.Default).async(Dispatchers.Default) {

            val grad: Double
            grad = if (legs_grad.containsKey(anima_type)) {
                legs_grad[anima_type]!![anim_step.toInt()]
            } else {
                0.0
            }
            matrixL.set(mtrx)
            matrixL.preRotate(
                grad.toFloat(),
                (dw * 215).toFloat() / 850,
                (dw * 369).toFloat() / 850
            )
            return@async matrixL
        }

         defMatrixBod2 = CoroutineScope(Dispatchers.Default).async(Dispatchers.Default) {

            val grad: Double
            grad = if (bod2_grad.containsKey(anima_type)) {
                bod2_grad[anima_type]!![anim_step.toInt()]
            } else {
                0.0
            }
            matrixB2.set(mtrx)
            matrixB2.preRotate(
                grad.toFloat(),
                (dw * 174).toFloat() / 850,
                (dw * 127).toFloat() / 850
            )
            return@async matrixB2
        }

         defMatrixHand2 = CoroutineScope(Dispatchers.Default).async(Dispatchers.Default) {
            val grad: Double
            grad = if (hand2_grad.containsKey(anima_type)) {
                hand2_grad[anima_type]!![`as`.toInt()]
            } else {
                0.0
            }

            matrixH2.set(mtrx)
            matrixH2.preRotate(
                grad.toFloat(),
                (dw * 236).toFloat() / 850,
                (dw * 138).toFloat() / 850
            )

            return@async matrixH2
        }

        defMatrixHead = CoroutineScope(Dispatchers.Default).async(Dispatchers.Default) {

            val grad: Double
            grad = if (head_grad.containsKey(anima_type)) {
                head_grad[anima_type]!![anim_step.toInt()]
            } else {
                0.0
            }
            matrixHe2.set(mtrx)
            matrixHe2.preRotate(
                grad.toFloat(),
                (dw * 203).toFloat() / 850,
                (dw * 47).toFloat() / 850
            )
            return@async matrixHe2
        }

         defMatrixBody = CoroutineScope(Dispatchers.Default).async(Dispatchers.Default) {
            var grad: Double

            matrixB.set(mtrx)
            return@async matrixB
        }

         defMatrixHand = CoroutineScope(Dispatchers.Default).async(Dispatchers.Default) {

            val grad: Double
            grad = if (hand_grad.containsKey(anima_type)) {
                hand_grad[anima_type]!![`as`.toInt()]
            } else {
                0.0
            }

            matrixH.set(mtrx)
            matrixH.preRotate(
                grad.toFloat(),
                (dw * 236).toFloat() / 850,
                (dw * 138).toFloat() / 850
            )

            return@async matrixH
        }

         defMatrixHeader = CoroutineScope(Dispatchers.Default).async(Dispatchers.Default) {
            val grad: Double

            grad = if (head_grad.containsKey(anima_type)) {
                head_grad[anima_type]!![anim_step.toInt()]
            } else {
                0.0
            }
            matrixHe.set(mtrx)
            matrixHe.preRotate(
                grad.toFloat(),
                (dw * 203).toFloat() / 850,
                (dw * 122).toFloat() / 850
            )

            return@async matrixHe
        }
    }


    override fun draw_penguin(paint: Paint?, canvas: Canvas?) {

        runBlocking (Dispatchers.Main) {
            defMatrixLegs2?.let { canvas!!.drawBitmap(legs, it.await(), paint) }
            defMatrixLegs?.let { canvas!!.drawBitmap(legs, it.await(), paint) }
            defMatrixBod2?.let { canvas!!.drawBitmap(bod_2, it.await(), paint) }
            defMatrixHand2?.let { canvas!!.drawBitmap(hand, it.await(), paint) }
            defMatrixHead?.let { canvas!!.drawBitmap(head, it.await(), paint) }
            defMatrixBody?.let { canvas!!.drawBitmap(body, it.await(), paint) }
            defMatrixHand?.let { canvas!!.drawBitmap(hand, it.await(), paint) }
            defMatrixHeader?.let { canvas!!.drawBitmap(header, it.await(), paint) }

        }
    }

//    @Override
//    override fun draw_penguin(paint: Paint?, canvas: Canvas?) {
//        var draw_y = y
//        if (y <= dh / 8.0) draw_y = (dh / 8.0).toFloat()
//
//        var `as` = anim_step
//        if (anima_type == "jump" || anima_type == "bust") {
//            `as` = (`as` * 2 + exp).toByte()
//        }
//        runBlocking (Dispatchers.Main) {
//
//                val defMatrixLegs2: Deferred<Matrix> = async(Dispatchers.Default) {
//                    val matrix = Matrix()
//                    val grad: Double
//                    grad = if (legs2_grad.containsKey(anima_type)) {
//                        legs2_grad[anima_type]!![anim_step.toInt()]
//                    } else {
//                        0.0
//                    }
//                    matrix.setTranslate(x, draw_y)
//                    matrix.preRotate(
//                        grad.toFloat(),
//                        (dw * 215).toFloat() / 850,
//                        (dw * 369).toFloat() / 850
//                    )
//                    return@async matrix
//                }
//
//                val defMatrixLegs: Deferred<Matrix> = async(Dispatchers.Default) {
//                    val matrix = Matrix()
//                    val grad: Double
//                    grad = if (legs_grad.containsKey(anima_type)) {
//                        legs_grad[anima_type]!![anim_step.toInt()]
//                    } else {
//                        0.0
//                    }
//                    matrix.setTranslate(x, draw_y)
//                    matrix.preRotate(
//                        grad.toFloat(),
//                        (dw * 215).toFloat() / 850,
//                        (dw * 369).toFloat() / 850
//                    )
//                    return@async matrix
//                }
//
//                val defMatrixBod2: Deferred<Matrix> = async(Dispatchers.Default) {
//                    val matrix = Matrix()
//                    val grad: Double
//                    grad = if (bod2_grad.containsKey(anima_type)) {
//                        bod2_grad[anima_type]!![anim_step.toInt()]
//                    } else {
//                        0.0
//                    }
//                    matrix.setTranslate(x, draw_y)
//                    matrix.preRotate(
//                        grad.toFloat(),
//                        (dw * 174).toFloat() / 850,
//                        (dw * 127).toFloat() / 850
//                    )
//                    return@async matrix
//                }
//
//                val defMatrixHand2: Deferred<Matrix> = async(Dispatchers.Default) {
//                    val matrix = Matrix()
//                    val grad: Double
//                    grad = if (hand2_grad.containsKey(anima_type)) {
//                        hand2_grad[anima_type]!![`as`.toInt()]
//                    } else {
//                        0.0
//                    }
//
//                    matrix.setTranslate(x, draw_y)
//                    matrix.preRotate(
//                        grad.toFloat(),
//                        (dw * 236).toFloat() / 850,
//                        (dw * 138).toFloat() / 850
//                    )
//
//                    return@async matrix
//                }
//
//                val defMatrixHead: Deferred<Matrix> = async(Dispatchers.Default) {
//                    val matrix = Matrix()
//                    val grad: Double
//                    grad = if (head_grad.containsKey(anima_type)) {
//                        head_grad[anima_type]!![anim_step.toInt()]
//                    } else {
//                        0.0
//                    }
//                    matrix.setTranslate(x, draw_y)
//                    matrix.preRotate(
//                        grad.toFloat(),
//                        (dw * 203).toFloat() / 850,
//                        (dw * 47).toFloat() / 850
//                    )
//                    return@async matrix
//                }
//
//                val defMatrixBody: Deferred<Matrix> = async(Dispatchers.Default) {
//                    val matrix = Matrix()
//                    var grad: Double
//
//                    matrix.setTranslate(x, draw_y)
//                    return@async matrix
//                }
//
//                val defMatrixHand: Deferred<Matrix> = async(Dispatchers.Default) {
//                    val matrix = Matrix()
//                    val grad: Double
//                    grad = if (hand_grad.containsKey(anima_type)) {
//                        hand_grad[anima_type]!![`as`.toInt()]
//                    } else {
//                        0.0
//                    }
//
//                    matrix.setTranslate(x, draw_y)
//                    matrix.preRotate(
//                        grad.toFloat(),
//                        (dw * 236).toFloat() / 850,
//                        (dw * 138).toFloat() / 850
//                    )
//
//                    return@async matrix
//                }
//
//                val defMatrixHeader: Deferred<Matrix> = async(Dispatchers.Default) {
//                    val matrix = Matrix()
//                    val grad: Double
//
//                    grad = if (head_grad.containsKey(anima_type)) {
//                        head_grad[anima_type]!![anim_step.toInt()]
//                    } else {
//                        0.0
//                    }
//                    matrix.setTranslate(x, draw_y - (dw * 75).toFloat() / 850)
//                    matrix.preRotate(
//                        grad.toFloat(),
//                        (dw * 203).toFloat() / 850,
//                        (dw * 122).toFloat() / 850
//                    )
//
//                    return@async matrix
//                }
//
//                canvas!!.drawBitmap(legs, defMatrixLegs2.await(), paint)
//                canvas!!.drawBitmap(legs, defMatrixLegs.await(), paint)
//                canvas!!.drawBitmap(bod_2, defMatrixBod2.await(), paint)
//                canvas!!.drawBitmap(hand, defMatrixHand2.await(), paint)
//                canvas!!.drawBitmap(head, defMatrixHead.await(), paint)
//                canvas!!.drawBitmap(body, defMatrixBody.await(), paint)
//                canvas!!.drawBitmap(hand, defMatrixHand.await(), paint)
//                canvas!!.drawBitmap(header, defMatrixHeader.await(), paint)
//
//            }
//        }
    }
