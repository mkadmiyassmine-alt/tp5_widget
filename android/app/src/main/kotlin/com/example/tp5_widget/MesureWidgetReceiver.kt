
package com.example.tp5_widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class MesureWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = MesureWidget()

    // Appelée lorsque le premier widget est ajouté
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WidgetWorker.planifier(context)
    }

    // Appelée lorsque le dernier widget est supprimé
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WidgetWorker.annuler(context)
    }
}
