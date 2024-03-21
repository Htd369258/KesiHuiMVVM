package com.htd.mymvvm.utils.file

import android.content.Context
import android.os.Environment
import java.io.*
import java.nio.channels.FileChannel

/**
 * SDCardUtil 是一个有关文件在SD卡上面存取的工具类
 */
object SDCardUtil {
    var SDPATH = Environment.getExternalStorageDirectory()
        .toString() + "/Photo_LJ/"

    /**
     * 创建文件
     * @param dirName
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun createSDDir(dirName: String): File {
        val dir = File(SDPATH + dirName)
        if (Environment.getExternalStorageState() ==
            Environment.MEDIA_MOUNTED
        ) {
            println("createSDDir:" + dir.absolutePath)
            println("createSDDir:" + dir.mkdir())
        }
        return dir
    }

    /**
     * 文件是否存在
     * @param fileName
     * @return
     */
    fun isFileExist(fileName: String): Boolean {
        val file = File(SDPATH + fileName)
        file.isFile
        return file.exists()
    }

    /**
     * 根据文件名字删除文件
     * @param fileName
     */
    fun delFile(fileName: String) {
        val file = File(SDPATH + fileName)
        if (file.isFile) {
            file.delete()
        }
        file.exists()
    }

    /**
     * 删除文件
     */
    fun deleteDir() {
        val dir = File(SDPATH)
        if (dir == null || !dir.exists() || !dir.isDirectory) return
        for (file in dir.listFiles()) {
            if (file.isFile) file.delete() else if (file.isDirectory) deleteDir()
        }
        dir.delete()
    }

    /**
     * 判断是否存在文件
     * @param path
     * @return
     */
    fun fileIsExists(path: String?): Boolean {
        try {
            val f = File(path)
            if (!f.exists()) {
                return false
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }
    /**
     * 复制文件
     * @param srcFile
     * @param destFile
     * @param preserveFileDate
     * @throws IOException
     */
    /**
     * 复制文件
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    @JvmOverloads
    @Throws(IOException::class)
    fun copyFile(
        srcFile: File?,
        destFile: File?,
        preserveFileDate: Boolean = true
    ) {
        if (srcFile == null) {
            throw NullPointerException("Source must not be null")
        } else if (destFile == null) {
            throw NullPointerException("Destination must not be null")
        } else if (!srcFile.exists()) {
            throw FileNotFoundException("Source \'\' does not exist")
        } else if (srcFile.isDirectory) {
            throw IOException("Source \'\' exists but is a directory")
        } else if (srcFile.canonicalPath == destFile.canonicalPath) {
            throw IOException("Source \'\' and destination \'\' are the same")
        } else {
            val parentFile = destFile.parentFile
            if (parentFile != null && !parentFile.mkdirs() && !parentFile.isDirectory) {
                throw IOException("Destination \'\' directory cannot be created")
            } else if (destFile.exists() && !destFile.canWrite()) {
                throw IOException("Destination \'\' exists but is read-only")
            } else {
                doCopyFile(srcFile, destFile, preserveFileDate)
            }
        }
    }

    @Throws(IOException::class)
    private fun doCopyFile(
        srcFile: File,
        destFile: File,
        preserveFileDate: Boolean
    ) {
        if (destFile.exists() && destFile.isDirectory) {
            throw IOException("Destination \'\' exists but is a directory")
        } else {
            var fis: FileInputStream? = null
            var fos: FileOutputStream? = null
            var input: FileChannel? = null
            var output: FileChannel? = null
            try {
                fis = FileInputStream(srcFile)
                fos = FileOutputStream(destFile)
                input = fis.channel
                output = fos.channel
                val size = input.size()
                var pos = 0L
                var count = 0L
                while (pos < size) {
                    count = if (size - pos > 31457280L) 31457280L else size - pos
                    pos += output.transferFrom(input, pos, count)
                }
            } finally {
                closeQuietly(output)
                closeQuietly(fos)
                closeQuietly(input)
                closeQuietly(fis)
            }
            if (srcFile.length() != destFile.length()) {
                throw IOException("Failed to copy full contents from \'\' to \'\'")
            } else {
                if (preserveFileDate) {
                    destFile.setLastModified(srcFile.lastModified())
                }
            }
        }
    }

    fun closeQuietly(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (var2: IOException) {
        }
    }

    /**
     * 取得文件大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getFileSizes(f: File): Long { // 取得文件大小
        var s: Long = 0
        if (f.exists()) {
            var fis: FileInputStream? = null
            fis = FileInputStream(f)
            s = fis.available().toLong()
        } else {
            f.createNewFile()
            println("文件不存在")
        }
        return s
    }

    // 检查扩展名，得到视频格式的文件
    private fun checkIsVideoFile(fName: String): Boolean {
        var isImageFile = false

        // 获取扩展名
        val FileEnd = fName.substring(
            fName.lastIndexOf(".") + 1,
            fName.length
        ).toLowerCase()
        isImageFile =
            FileEnd == "mp4" || FileEnd == "3gp" || FileEnd == "mkv" || FileEnd == "avi" || FileEnd == "rm" || FileEnd == "rmvb" || FileEnd == "m3u8"
        return isImageFile
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            //递归删除目录中的子目录下
            for (i in children.indices) {
                val success =
                    deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete()
    }

    /**
     * Check the SD card state whether it is available
     *
     * @return
     */
    fun checkSDCardAvailable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * Check if the file is exists
     *
     * @param filePath
     * @param fileName
     * @return
     */
    fun isFileExistsInSDCard(filePath: String?, fileName: String?): Boolean {
        var flag = false
        if (checkSDCardAvailable()) {
            val file = File(filePath, fileName)
            if (file.exists()) {
                flag = true
            }
        }
        return flag
    }

    /**
     * Write file to SD card
     *
     * @param filePath
     * @param filename
     * @param content
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun saveFileToSDCard(
        filePath: String,
        filename: String,
        content: String
    ): Boolean {
        var flag = false
        if (checkSDCardAvailable()) {
            val file = File(filePath + filename)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            val outStream = FileOutputStream(file)
            outStream.write(content.toByteArray())
            outStream.close()
            flag = true
        }
        return flag
    }

    /**
     * Read file as stream from SD card
     *
     * @param fileAbsPath
     * @return
     */
    fun readFileFromSDCard(fileAbsPath: String?): ByteArray? {
        var buffer: ByteArray? = null
        try {
            if (checkSDCardAvailable()) {
                val fin = FileInputStream(fileAbsPath)
                val length = fin.available()
                buffer = ByteArray(length)
                fin.read(buffer)
                fin.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return buffer
    }

    /**
     * Read file as stream from SD card
     *
     * @param fileName String PATH =
     * Environment.getExternalStorageDirectory().getAbsolutePath() +
     * "/dirName";
     * @return
     */
    fun readFileFromSDCard(filePath: String, fileName: String): ByteArray? {
        return readFileFromSDCard("/")
    }

    /**
     * Delete file
     *
     * @param filePath
     * @param fileName filePath =
     * android.os.Environment.getExternalStorageDirectory().getPath()
     * @return
     */
    fun deleteSDFile(filePath: String, fileName: String): Boolean {
        val file = File("/")
        return if (file == null || !file.exists() || file.isDirectory) false else file.delete()
    }

    /**
     * Temp file
     *
     * @param filePath
     * @param fileName filePath =
     * android.os.Environment.getExternalStorageDirectory().getPath()
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getTemFile(filePath: String?, fileName: String?): File {
        return if (checkSDCardAvailable()) {
            val file = File(filePath, fileName)
            if (!file.exists()) {
                file.parentFile.mkdirs() //创建缺少 的父路径
            }
            file.createNewFile() //创建文件
            file
        } else {
            File.createTempFile(null, ".jpg")
        }
    }

    /**
     * root  path
     *
     * @return
     */
    val rootPath: String
        get() = Environment.getExternalStorageDirectory().absolutePath
    //********************************文 件 名:  DataCleanManager.java  * 描    述:  主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录 **********************************************
    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context
     */
    fun cleanInternalCache(context: Context) {
        deleteFilesByDirectory(context.cacheDir)
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context
     */
    fun cleanDatabases(context: Context) {
        deleteFilesByDirectory(
            File(
                "/data/data/"
                        + context.packageName + "/databases"
            )
        )
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
     * context
     */
    fun cleanSharedPreference(context: Context) {
        deleteFilesByDirectory(
            File(
                "/data/data/"
                        + context.packageName + "/shared_prefs"
            )
        )
    }

    /**
     * 按名字清除本应用数据库 * * @param context * @param dbName
     */
    fun cleanDatabaseByName(context: Context, dbName: String?) {
        context.deleteDatabase(dbName)
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context
     */
    fun cleanFiles(context: Context) {
        deleteFilesByDirectory(context.filesDir)
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    fun cleanExternalCache(context: Context) {
        if (Environment.getExternalStorageState() ==
            Environment.MEDIA_MOUNTED
        ) {
            deleteFilesByDirectory(context.externalCacheDir)
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath
     */
    fun cleanCustomCache(filePath: String?) {
        deleteFilesByDirectory(File(filePath))
    }

    /**
     * 清除本应用所有的数据 * * @param context * @param filepath
     */
    fun cleanApplicationData(
        context: Context,
        vararg filepath: String?
    ) {
        cleanInternalCache(context)
        cleanExternalCache(context)
        cleanDatabases(context)
        cleanSharedPreference(context)
        cleanFiles(context)
        for (filePath in filepath) {
            cleanCustomCache(filePath)
        }
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
     */
    private fun deleteFilesByDirectory(directory: File?) {
        if (directory != null && directory.exists() && directory.isDirectory) {
            for (item in directory.listFiles()) {
                item.delete()
            }
        }
    }
}