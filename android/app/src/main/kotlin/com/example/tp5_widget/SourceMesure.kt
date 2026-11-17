
package com.example.tp5_widget

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

object SourceMesure {

    // Lire la température de la batterie
    fun temperatureBatterie(context: Context): Double? {

        val etat = context.applicationContext
            .registerReceiver(
                null,
                IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            ) ?: return null

        val dixiemes = etat.getIntExtra(
            BatteryManager.EXTRA_TEMPERATURE,
            Int.MIN_VALUE
        )

        return if (dixiemes == Int.MIN_VALUE) {
            null
        } else {
            dixiemes / 10.0
        }
    }

    // Publier la mesure dans le widget
    suspend fun lireEtPublier(context: Context) {

        val t = temperatureBatterie(context) ?: return

        WidgetMaj.enregistrer(
            context,
            t,
            titre = "Batterie (démo)",
            unite = "°C"
        )
    }
}
