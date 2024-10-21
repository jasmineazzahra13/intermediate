package com.dicoding.intermediate.ui.widgets

import android.content.Intent
import android.widget.RemoteViewsService

class RecentStoriesWidgetServ : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        RecentStoriesWidgetVF(this.applicationContext)
}