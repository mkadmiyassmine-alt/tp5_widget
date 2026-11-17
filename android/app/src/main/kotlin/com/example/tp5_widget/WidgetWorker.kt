
package com.example.tp5_widget

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class WidgetWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        SourceMesure.lireEtPublier(applicationContext)
        return Result.success()
    }

    companion object {
        private const val NOM = "maj_widget"

        fun planifier(context: Context) {
            val requete =
                PeriodicWorkRequestBuilder<WidgetWorker>(
                    15, TimeUnit.MINUTES
                ).build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    NOM,
                    ExistingPeriodicWorkPolicy.KEEP,
                    requete
                )
        }

        fun annuler(context: Context) {
            WorkManager.getInstance(context)
                .cancelUniqueWork(NOM)
        }
    }
}
