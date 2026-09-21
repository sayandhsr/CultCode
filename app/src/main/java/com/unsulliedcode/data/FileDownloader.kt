package com.unsulliedcode.data

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class FileDownloader(private val context: Context) {

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun downloadFileRobustly(url: String, fileName: String): Boolean = withContext(Dispatchers.IO) {
        val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.filesDir
        val tempFile = File(downloadDir, "$fileName.part")
        val finalFile = File(downloadDir, fileName)

        var connection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null

        try {
            val downloadUrl = URL(url)
            connection = downloadUrl.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                return@withContext false
            }

            val fileLength = connection.contentLength

            inputStream = connection.inputStream
            outputStream = FileOutputStream(tempFile)

            val buffer = ByteArray(8192)
            var totalBytesRead: Long = 0
            var bytesRead: Int

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead
            }

            outputStream.flush()
            outputStream.fd.sync() // Ensure all bytes are physically written to disk
            outputStream.close()
            outputStream = null // Mark closed

            // VERIFY FILE SIZE
            if (fileLength != -1 && tempFile.length() != fileLength.toLong()) {
                tempFile.delete()
                return@withContext false
            }

            // ATOMIC RENAME
            if (finalFile.exists()) {
                finalFile.delete()
            }
            val renamed = tempFile.renameTo(finalFile)

            if (renamed && finalFile.exists() && finalFile.length() > 0) {
                return@withContext true
            } else {
                tempFile.delete()
                return@withContext false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        } finally {
            try { outputStream?.close() } catch (e: Exception) { /* ignore */ }
            try { inputStream?.close() } catch (e: Exception) { /* ignore */ }
            try { connection?.disconnect() } catch (e: Exception) { /* ignore */ }
            
            // Clean up partial file on failure
            if (tempFile.exists()) {
                tempFile.delete()
            }
        }
    }
}
