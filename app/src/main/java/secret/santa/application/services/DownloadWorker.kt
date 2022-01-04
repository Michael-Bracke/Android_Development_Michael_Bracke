

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import java.io.File
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.util.Log
import secret.santa.application.interfaces.Listener
import java.io.InputStream
import java.lang.Exception
import java.net.URL


class DownloadWorker(var context: Context) {


    fun downloadImage(imageUrl: String, onCompleteListener: Listener<Bitmap?>) {
        Log.e("RUNNABLE", "DOWNLOAD IMAGE VOOR FUN...")
        Runnable {
            Log.e("RUNNABLE", "DOWNLOAD IMAGE NA FUN...")
            var bitmap: Bitmap? = null
            var fileName: String? = null
            try {
                val inputStream: InputStream = URL(imageUrl).openStream()
                Log.e("RUNNABLE", "inputstream"+inputStream)
                bitmap = BitmapFactory.decodeStream(inputStream)
                Log.e("RUNNABLE", "bitmap from factory"+bitmap)
                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            onCompleteListener.DownloadComplete(bitmap)

        }.run()
    }
}

