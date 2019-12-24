import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.Url

class Instances(private val httpClient: HttpClient, private val url: Url) {
    suspend fun listAll(): List<RemoteInstance> =
        httpClient.get(url.copy(encodedPath = "/instances"))

    suspend fun listFiltered(filter: InstancesFilter): List<String> {
        TODO()
    }

    suspend fun getInstanceAttrsById(id: String): RemoteInstance {
        TODO()
    }
}