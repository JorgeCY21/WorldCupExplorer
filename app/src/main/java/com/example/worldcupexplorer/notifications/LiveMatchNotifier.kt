package com.example.worldcupexplorer.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.worldcupexplorer.MainActivity
import com.example.worldcupexplorer.R
import com.example.worldcupexplorer.domain.model.Match
import com.example.worldcupexplorer.domain.model.MatchTeam
import com.example.worldcupexplorer.presentation.common.toCountryFlagUrl
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveMatchNotifier @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun notifyLiveMatch(match: Match) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        createChannel()

        val homeFlag = loadFlag(match.homeTeam)
        val awayFlag = loadFlag(match.awayTeam)
        val scoreboard = drawScoreboard(match, homeFlag, awayFlag)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(NotificationHelper.EXTRA_DESTINATION, NotificationHelper.DESTINATION_MATCHES)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            match.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val score = match.score ?: "vs"
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_soccer)
            .setColor(ACCENT_COLOR)
            .setContentTitle("🔴 LIVE · ${match.homeTeam.name} vs ${match.awayTeam.name}")
            .setContentText("⚽ $score · ${match.stage.toStageDisplay()}")
            .setSubText("World Cup 2026")
            .setLargeIcon(homeFlag)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(scoreboard)
                    .bigLargeIcon(null as Bitmap?)
                    .setBigContentTitle("🔴 ${match.homeTeam.name} $score ${match.awayTeam.name}")
                    .setSummaryText(match.stage.toStageDisplay())
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // Un id por partido: cada chequeo actualiza el marcador en la misma notificacion.
        NotificationManagerCompat.from(context).notify(match.id, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Live matches",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Live World Cup scores every 5 minutes"
                enableVibration(true)
            }
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    private suspend fun loadFlag(team: MatchTeam): Bitmap? = withContext(Dispatchers.IO) {
        val url = (team.countryCode ?: team.name).toCountryFlagUrl()
            ?.replace("/w80/", "/w160/")
            ?: return@withContext null
        runCatching {
            URL(url).openStream().use { BitmapFactory.decodeStream(it) }
        }.getOrNull()
    }

    private fun drawScoreboard(match: Match, homeFlag: Bitmap?, awayFlag: Bitmap?): Bitmap {
        val width = 1000
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Fondo con el degradado azul -> carmesi de las tarjetas de la app.
        val background = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                Color.parseColor("#141E46"), Color.parseColor("#8E2440"),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), background)

        val glow = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.argb(14, 255, 255, 255) }
        canvas.drawCircle(width * 0.9f, height * 0.1f, 220f, glow)
        canvas.drawCircle(width * 0.08f, height * 0.95f, 180f, glow)

        // Pildora "EN VIVO".
        val pillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#E0264C") }
        val pill = RectF(width / 2f - 120f, 36f, width / 2f + 120f, 96f)
        canvas.drawRoundRect(pill, 30f, 30f, pillPaint)
        val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }
        canvas.drawCircle(pill.left + 40f, pill.centerY(), 10f, dotPaint)
        val pillText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 34f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("LIVE", pill.left + 82f, pill.centerY() + 12f, pillText)

        drawFlag(canvas, homeFlag, centerX = 190f, centerY = 235f)
        drawFlag(canvas, awayFlag, centerX = width - 190f, centerY = 235f)

        val codePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 44f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(match.homeTeam.displayCode(), 190f, 360f, codePaint)
        canvas.drawText(match.awayTeam.displayCode(), width - 190f, 360f, codePaint)

        // Marcador central.
        val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 120f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(match.score ?: "vs", width / 2f, 280f, scorePaint)

        val stagePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(210, 255, 255, 255)
            textSize = 38f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(match.stage.toStageDisplay(), width / 2f, 360f, stagePaint)

        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(150, 255, 255, 255)
            textSize = 32f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("FIFA World Cup 2026 · World Cup Explorer", width / 2f, height - 40f, footerPaint)

        return bitmap
    }

    private fun drawFlag(canvas: Canvas, flag: Bitmap?, centerX: Float, centerY: Float) {
        val flagWidth = 230f
        val flagHeight = 155f
        val rect = RectF(
            centerX - flagWidth / 2, centerY - flagHeight / 2,
            centerX + flagWidth / 2, centerY + flagHeight / 2
        )
        if (flag != null) {
            val clip = Path().apply { addRoundRect(rect, 22f, 22f, Path.Direction.CW) }
            canvas.save()
            canvas.clipPath(clip)
            canvas.drawBitmap(flag, null, rect, Paint(Paint.FILTER_BITMAP_FLAG))
            canvas.restore()
        } else {
            val placeholder = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.argb(60, 255, 255, 255) }
            canvas.drawRoundRect(rect, 22f, 22f, placeholder)
        }
        val border = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 5f
            color = Color.argb(200, 255, 255, 255)
        }
        canvas.drawRoundRect(rect, 22f, 22f, border)
    }

    private fun MatchTeam.displayCode(): String =
        countryCode?.uppercase() ?: name.take(3).uppercase()

    private fun String?.toStageDisplay(): String = when (this) {
        "GROUP_STAGE" -> "Group stage"
        "LAST_32" -> "Round of 32"
        "LAST_16", "ROUND_OF_16" -> "Round of 16"
        "QUARTER_FINALS" -> "Quarter-finals"
        "SEMI_FINALS" -> "Semi-finals"
        "THIRD_PLACE" -> "Third place"
        "FINAL" -> "Final"
        null -> "Live match"
        else -> replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
    }

    companion object {
        const val CHANNEL_ID = "live_matches"
        private val ACCENT_COLOR = android.graphics.Color.parseColor("#E0264C")
    }
}
