package cc.easyandroid.easyutils;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;

public class NotifiUtil {
	@SuppressLint("NewApi")
	public void show(Context mContext , Bitmap bitmap) {

//		Intent resultIntent = new Intent(mContext, VideoPlayer.class);
//		Bundle bundle = new Bundle();
//		bundle.putBoolean("isTipOff", false);
//		bundle.putSerializable("videoinfo", new VideoInfo(Integer.parseInt(messageHolder.videoId)));
//		resultIntent.putExtras(bundle);
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
//		stackBuilder.addParentStack(MainActivity.class);
//		stackBuilder.addNextIntentWithParentStack(new Intent(mContext, MainActivity.class));
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent pendingIntent = stackBuilder.getPendingIntent(NOTIF_CONNECTED++, PendingIntent.FLAG_UPDATE_CURRENT);
//		Notification bigpictureNoti = new NotificationCompat.Builder(mContext)//
//				.setAutoCancel(true)//
//				.setContentTitle(messageHolder.name)//
//				.setContentText(messageHolder.description)//
//				.setDefaults(Notification.DEFAULT_ALL)//
//				.setSmallIcon(R.drawable.icon_s)//
//				.setContentIntent(pendingIntent)//
//				.setAutoCancel(true)//
//				.build();
//		bigpictureNoti.flags |= Notification.FLAG_AUTO_CANCEL; // 点击后消失
//		RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notificationlayout);
//		remoteViews.setTextViewText(R.id.notificationtitle, messageHolder.name);
//		remoteViews.setTextViewText(R.id.notificationdesc, messageHolder.description);
//		remoteViews.setImageViewBitmap(R.id.notificationimage, bitmap);
//		remoteViews.setOnClickPendingIntent(R.id.notificationimage, pendingIntent);
//		remoteViews.setOnClickPendingIntent(R.id.notificationdesc, pendingIntent);
//		bigpictureNoti.bigContentView = remoteViews;
		NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//		manager.notify(1, bigpictureNoti);
	}
}
