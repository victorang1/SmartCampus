package com.example.smartcampus.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.smartcampus.R
import kotlin.math.min

class IPKProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 50f
        color = Color.parseColor("#404040")
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 50f
        color = Color.parseColor("#C23177") // Primary color
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = Color.BLACK
        textSize = 80f
    }

    private val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = Color.parseColor("#C23177")
        textSize = 120f
        isFakeBoldText = true
    }

    private val arcRect = RectF()
    private var progress: Float = 0f
    private var maxProgress: Float = 4.0f

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IPKProgressView)
            progress = typedArray.getFloat(R.styleable.IPKProgressView_progress, 0f)
            maxProgress = typedArray.getFloat(R.styleable.IPKProgressView_maxProgress, 4.0f)
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = min(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
        setMeasuredDimension(size, size / 2 + 100) // Add extra space for text
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = 60f
        arcRect.set(
            padding,
            padding,
            width.toFloat() - padding,
            width.toFloat() - padding
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw background arc
        canvas.drawArc(arcRect, 180f, 180f, false, backgroundPaint)

        // Draw progress arc
        val sweepAngle = (progress / maxProgress) * 180f
        canvas.drawArc(arcRect, 180f, sweepAngle, false, progressPaint)

        // Calculate center point of the arc
        val centerX = width / 2f
        val centerY = (arcRect.top + arcRect.bottom) / 2f - 20f

        // Draw label text slightly above center
        val labelOffset = 200f
        canvas.drawText(
            "IPK",
            centerX,
            centerY - labelOffset,
            labelPaint
        )

        // Draw value text slightly below center
        val valueOffset = -30f
        canvas.drawText(
            String.format("%.2f", progress),
            centerX,
            centerY + valueOffset,
            valuePaint
        )
    }

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, maxProgress)
        invalidate()
    }

    fun setMaxProgress(value: Float) {
        maxProgress = value
        progress = progress.coerceIn(0f, maxProgress)
        invalidate()
    }
} 