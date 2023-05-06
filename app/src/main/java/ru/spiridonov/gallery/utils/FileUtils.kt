package ru.spiridonov.gallery.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object FileUtils {

    fun createFileFromByteArray(context: Context, byteArray: ByteArray, fileName: String): File? {
        val file = File(context.cacheDir, fileName)
        file.createNewFile()

        val fos: FileOutputStream?
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }
        try {
            fos.write(byteArray)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return file
    }

    fun copyFile(context: Context, oldPlace: String, newName: String) {
        val file = File(oldPlace)
        val newFile = File(context.cacheDir, newName)
        file.copyTo(newFile, true)
    }

    fun getFileFromCache(context: Context, mediaPath: String): File? {
        val file = File(context.cacheDir, mediaPath)
        if (file.exists())
            return file
        return null
    }

    fun getPath(context: Context, uri: Uri): String? {
        val data = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context, uri, data, null, null, null)
        val cursor = loader.loadInBackground()
        cursor?.let {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        return null
    }

}