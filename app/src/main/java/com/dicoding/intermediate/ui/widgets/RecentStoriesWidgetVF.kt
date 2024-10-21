package com.dicoding.intermediate.ui.widgets

import android.content.Context
import android.os.Binder
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.room.Room
import com.bumptech.glide.Glide
import com.dicoding.intermediate.BuildConfig
import com.dicoding.intermediate.R
import com.dicoding.intermediate.data.db.StoryDatabase
import com.dicoding.intermediate.data.md.Story
import com.dicoding.intermediate.data.network.ApiResponse
import com.dicoding.intermediate.data.network.HeaderInterceptor
import com.dicoding.intermediate.data.network.services.StoryServ
import com.dicoding.intermediate.data.resource.StoryDS
import com.dicoding.intermediate.utils.PreferManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class RecentStoriesWidgetVF internal constructor(
    private val mContext: Context,
) : RemoteViewsService.RemoteViewsFactory {

    private var storyList: MutableList<Story?> = mutableListOf()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        val identifyToken: Long = Binder.clearCallingIdentity()
        runBlocking(Dispatchers.IO) {
            try {
                provideDataSource().fetchStories().collect {
                    if (it is ApiResponse.Success) {
                        storyList.clear()
                        storyList.addAll(it.data)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Binder.restoreCallingIdentity(identifyToken)
    }

    override fun onDestroy() {}

    override fun getCount(): Int = storyList.size

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(mContext.packageName, R.layout.widget_item_story)
        if (storyList.isNotEmpty()) {
            val urlImg = storyList[position]?.photoUrl
            if (urlImg != null) {
                try {
                    val bitmap = Glide.with(mContext).asBitmap().load(urlImg).submit().get()
                    remoteViews.setImageViewBitmap(R.id.img_story, bitmap)
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        val extras = Bundle()
        extras.putString(RecentStoriesWidget.EXTRA_PHOTO_URL, storyList[position]?.photoUrl)
        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    private fun provideDataSource(): StoryDS {
        return StoryDS(provideDatabase(), provideStoryService(createRetrofit()))
    }

    private fun provideDatabase(): StoryDatabase {
        return Room.databaseBuilder(
            mContext.applicationContext,
            StoryDatabase::class.java,
            BuildConfig.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createClient())
            .build()
    }

    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(getHeaderInterceptor(providePreferenceManager()))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    private fun providePreferenceManager() = PreferManager(mContext)

    private fun provideStoryService(retrofit: Retrofit): StoryServ =
        retrofit.create(StoryServ::class.java)

    private fun getHeaderInterceptor(preferenceManager: PreferManager): Interceptor {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        return HeaderInterceptor(headers, preferenceManager)
    }
}