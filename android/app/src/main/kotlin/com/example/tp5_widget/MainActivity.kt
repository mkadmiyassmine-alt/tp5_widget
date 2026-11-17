
package com.example.tp5_widget

import androidx.glance.appwidget.GlanceAppWidgetManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : FlutterActivity() {

    // Gestion des opérations asynchrones
    private val scope = MainScope()

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "widget/maj"
        ).setMethodCallHandler { call, result ->

            when (call.method) {

                "mettreAJour" -> {
                    val valeur = call.argument<Double>("valeur")

                    if (valeur == null) {
                        result.error(
                            "ARGUMENT",
                            "valeur manquante",
                            null
                        )
                    } else {
                        scope.launch {
                            val n = WidgetMaj.enregistrer(
                                applicationContext,
                                valeur,
                                titre = call.argument<String>("titre"),
                                unite = call.argument<String>("unite"),
                                seuil = call.argument<Double>("seuil")
                            )

                            result.success(n)
                        }
                    }
                }

                "nombreWidgets" -> scope.launch {
                    result.success(
                        WidgetMaj.nombre(applicationContext)
                    )
                }

                "epingler" -> scope.launch {
                    val ok = GlanceAppWidgetManager(applicationContext)
                        .requestPinGlanceAppWidget(
                            MesureWidgetReceiver::class.java
                        )

                    result.success(ok)
                }

                else -> result.notImplemented()
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}
