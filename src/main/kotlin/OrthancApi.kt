import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.Url
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.StringSerializer

/**
 * Represents API which uses [httpClient] for downloading and uploading DICOM files
 * from and to Orthanc PACS which takes place on [url].
 */
class OrthancApi(private val pacsUrl: Url, private val httpClient: HttpClient) {
    /** Represents ids of all patients on the Server. */
    val patients: List<String>
        get() = TODO("Patients are not required")

    /** Represents ids of all studies on the Server. */
    val studies: List<String>
        get() = TODO("Studies are not required")

    /** Represents ids of all series on the Server. */
    val series: List<String>
        get() = TODO("Series are not required")

    /** Returns [List] of [RemoteInstance] objects filtered by [filter]. */
    suspend fun getInstances(filter: InstancesFilter? = null): List<RemoteInstance> {
        httpClient.config {
            install(JsonFeature) {
                serializer = KotlinxSerializer().apply {
                    register(ArrayListSerializer(StringSerializer))
                }
            }
        }
        if (filter != null) {
            TODO()
        } else {
            return httpClient.get<List<String>> {
                url(pacsUrl.copy(encodedPath = "/instances"))
            }.map { RemoteInstance(it, httpClient, pacsUrl) }
        }
    }
}