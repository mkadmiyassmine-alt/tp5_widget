
package com.example.tp5_widget

import android.content.Context
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll

// Les clés de l'état du widget
object CleWidget {
    val TITRE = stringPreferencesKey("titre")
    val VALEUR = floatPreferencesKey("valeur")
    val UNITE = stringPreferencesKey("unite")
    val SEUIL = floatPreferencesKey("seuil")
    val HEURE = longPreferencesKey("heure")
}

object WidgetMaj {

    // Compter les widgets installés
    suspend fun nombre(context: Context): Int =
        GlanceAppWidgetManager(context)
            .getGlanceIds(MesureWidget::class.java).size

    // Enregistrer la mesure et actualiser les widgets
    suspend fun enregistrer(
        context: Context,
        valeur: Double,
        titre: String? = null,
        unite: String? = null,
        seuil: Double? = null
    ): Int {
        val ids = GlanceAppWidgetManager(context)
            .getGlanceIds(MesureWidget::class.java)

        ids.forEach { id ->
            updateAppWidgetState(context, id) { prefs ->
                prefs[CleWidget.VALEUR] = valeur.toFloat()
                prefs[CleWidget.HEURE] = System.currentTimeMillis()

                if (titre != null) prefs[CleWidget.TITRE] = titre
                if (unite != null) prefs[CleWidget.UNITE] = unite
                if (seuil != null) prefs[CleWidget.SEUIL] = seuil.toFloat()
            }
        }

        MesureWidget().updateAll(context)
        return ids.size
    }
}
