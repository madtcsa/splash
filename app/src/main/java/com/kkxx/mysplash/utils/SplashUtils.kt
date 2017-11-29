package com.kkxx.mysplash.utils

import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * @author zsmj
 * @date 2017/11/30
 */
class SplashUtils {

    companion object {
        fun isExternalStorageWritable(): Boolean {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

        fun copyInputStreamToFile(inputStream: InputStream, file: File) = try {
            val out = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len = inputStream.read(buf)
            while (len > 0) {
                out.write(buf, 0, len)
                len = inputStream.read(buf)
            }
            out.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        fun getAlbumStorageDir(albumName: String): File {
            return File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName)
        }

    }
}