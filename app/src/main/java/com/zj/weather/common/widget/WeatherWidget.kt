package com.zj.weather.common.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast
import com.google.gson.Gson
import com.zj.weather.R

const val TOAST_ACTION = "com.zj.weather.common.widget.TOAST_ACTION"
const val EXTRA_ITEM = "com.zj.weather.common.widget.EXTRA_ITEM"

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [WeatherWidgetConfigureActivity]
 */
class WeatherWidget : AppWidgetProvider() {

    // Called when the BroadcastReceiver receives an Intent broadcast.
    // Checks to see whether the intent's action is TOAST_ACTION. If it is, the app widget
    // displays a Toast message for the current item.
    override fun onReceive(context: Context, intent: Intent) {
        val mgr: AppWidgetManager = AppWidgetManager.getInstance(context)
        if (intent.action == TOAST_ACTION) {
            val appWidgetId: Int = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            val viewIndex: Int = intent.getIntExtra(EXTRA_ITEM, 0)
            Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
            mgr.updateAppWidget(
                appWidgetId,
                RemoteViews(context.packageName, R.layout.weather_widget)
            )
        }
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val cityInfo = loadTitlePref(context, appWidgetId)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.weather_widget)
    // views.setTextViewText(R.id.appwidget_text, cityInfo?.city)
    // Set up the intent that starts the StackViewService, which will
    // provide the views for this collection.
    val intent = Intent(context, WeatherWidgetService::class.java).apply {
        // Add the app widget ID to the intent extras.
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        putExtra(CITY_INFO,Gson().toJson(cityInfo))
        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
    }
    // Instantiate the RemoteViews object for the app widget layout.
    views.apply {
        // Set up the RemoteViews object to use a RemoteViews adapter.
        // This adapter connects
        // to a RemoteViewsService  through the specified intent.
        // This is how you populate the data.
        setRemoteAdapter(R.id.stack_view, intent)

        // The empty view is displayed when the collection has no items.
        // It should be in the same layout used to instantiate the RemoteViews
        // object above.
        setEmptyView(R.id.stack_view, R.id.empty_view)
    }

    // This section makes it possible for items to have individualized behavior.
    // It does this by setting up a pending intent template. Individuals items of a
    // collection cannot set up their own pending intents. Instead, the collection as a
    // whole sets up a pending intent template, and the individual items set a fillInIntent
    // to create unique behavior on an item-by-item basis.
//    val toastPendingIntent: PendingIntent = Intent(
//        context,
//        WeatherWidgetService::class.java
//    ).run {
//        // Set the action for the intent.
//        // When the user touches a particular view, it will have the effect of
//        // broadcasting TOAST_ACTION.
//        action = TOAST_ACTION
//        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
//        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
//
//        PendingIntent.getBroadcast(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT)
//    }
//    views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)


    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}