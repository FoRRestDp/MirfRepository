import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.Url
import io.ktor.http.content.LocalFileContent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.File
import java.io.FileNotFoundException

/** 
 *  Represents an abstraction of DICOM [file] which takes place locally and cab be uploaded to Orthanc PACS
 *  which takes place on [url] and uses [httpClient].
 */
data class LocalInstance(
    /** File that should be represented. */
    val file: File,
    private val httpClient: HttpClient,
    private val url: Url
) {
    @Serializable
    private data class DicomUploadResponse(
        @SerialName("ID") val id: String,
        @SerialName("Path") val path: String,
        @SerialName("Status") val status: String
    )
    
    /** Uploads DICOM file which instance represents to the Server and returns [RemoteInstance] which represents uploaded file. */
    suspend fun uploadDicom(): RemoteInstance {
        if (!file.exists() || !file.isFile) {
            throw FileNotFoundException("File is not found")
        }
        val response = httpClient.post<DicomUploadResponse> {
            headers.append("Expect", "")
            url {
                port = 8042
                encodedPath = "/instances"
            }
            body = LocalFileContent(file)
        }
        return RemoteInstance(response.id, httpClient, url)
    }
}