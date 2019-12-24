import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream

/** Represents an abstraction of DICOM file which takes place on the Orthanc PACS with [url] and uses [httpClient]. */
data class RemoteInstance(
    /** Unique id of the instance on the Server. */
    val id: String,
    private val httpClient: HttpClient,
    private val url: Url
) {

    private val tagIdRegex = Regex("[0-9,a-f]{4},[0-9,a-f]{4}")

    /** Represents attributes of the instance as [Map]. */
    val attrMap: Map<String, String> by lazy {
        runBlocking<Map<String, String>> {
            TODO()
        }
    }

    /** Returns attribute value by its DICOM [tag]. */
    suspend fun getAttrByTag(tag: String): String {
        if (!tagIdRegex.matches(tag)) {
            throw IllegalArgumentException("Given tag string ($tag) is not a tag.")
        }
        TODO()
    }

    /** Returns attribute value by it DICOM tag [name]. */
    suspend fun getAttrByName(name: String): String {
        TODO()
    }

    /** Downloads DICOM file which instance represents to directory [absolutePath]
     * as file named [fileName].
     * @param fileName If it is null or blank file name will be its [id].
     * @return [LocalInstance] which represents the downloaded file.
     */
    suspend fun downloadDicom(absolutePath: String, fileName: String? = null): LocalInstance {
        val absoluteFileName = buildString {
            if (fileName.isNullOrBlank()) {
                append("$absolutePath/$id.dcm")
            } else {
                append("$absolutePath/$fileName.dcm")
            }
        }

        if (File(absoluteFileName).exists()) {
            return LocalInstance(File(absoluteFileName), httpClient, url)
        }
        val downloadFileUrl = url.copy(encodedPath = "${url.encodedPath}/${id}/file")
        val downloadedBytes = httpClient.get<ByteArray> {
            url(downloadFileUrl)
        }
        GlobalScope.launch(Dispatchers.IO) {
            val downloadDirectory = File("$absolutePath/")
            if (!downloadDirectory.exists()) {
                downloadDirectory.mkdir()
            }
            FileOutputStream(absoluteFileName).use {
                it.write(downloadedBytes)
            }
        }.join()

        return LocalInstance(File(absoluteFileName), httpClient, url)
    }
}