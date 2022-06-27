package com.dicoding.courseschedule.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.util.*
import java.util.*

class DailyReminder : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        executeThread {
            val repository = DataRepository.getInstance(context)
            val courses = repository?.getTodaySchedule()

            courses?.let {
                if (it.isNotEmpty()) showNotification(context, it)
            }
        }
    }

    //TODO 12 : Implement daily reminder for every 06.00 a.m using AlarmManager
    @SuppressLint("InlinedApi")
    fun setDailyReminder(context: Context) {
        val intent = Intent(context, DailyReminder::class.java)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 6)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, 101, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    @SuppressLint("InlinedApi")
    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 101, intent, PendingIntent.FLAG_IMMUTABLE)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(context: Context, content: List<Course>) {
        //TODO 13 : Show today schedules in inbox style notification & open HomeActivity when notification tapped
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationStyle = NotificationCompat.InboxStyle()
        val channelId = NOTIFICATION_CHANNEL_ID
        val channelName = NOTIFICATION_CHANNEL_NAME
        val homeIntent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, ID_REPEATING, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(context.getString(R.string.today_schedule))
            .setSmallIcon(R.drawable.ic_notifications)
            .setStyle(notificationStyle)
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)
        val timeString = context.resources.getString(R.string.notification_message_format)
        content.forEach {
            val courseData = String.format(timeString, it.startTime, it.endTime, it.courseName)
            notificationStyle.addLine(courseData)
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(NOTIFICATION_ID, notification)




    }
}