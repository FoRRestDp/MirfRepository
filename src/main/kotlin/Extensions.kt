import io.ktor.client.HttpClient
import io.ktor.http.Url
import java.io.File

/**
 * Downloads all [RemoteInstance] objects from [Collection] to [absoluteDirectoryPath] 
 * and returns [Collection] of [LocalInstance] objects.
 */
suspend fun Collection<RemoteInstance>.downloadDicoms(absoluteDirectoryPath: String): Collection<LocalInstance> =
    map { remote -> remote.downloadDicom(absoluteDirectoryPath) }

/**
 * Downloads all [RemoteInstance] objects from [Collection] filtered by [filter]
 * to [absoluteDirectoryPath] and returns [Collection] of [LocalInstance] objects.
 */
suspend fun Collection<RemoteInstance>.downloadDicomsFiltered(
    absoluteDirectoryPath: String,
    filter: InstancesFilter
): Collection<LocalInstance> =
    map { remote -> remote.downloadDicom(absoluteDirectoryPath) }
        .filter { TODO() }

/**
 * Uploads all [RemoteInstance] objects recursively from directory represented as [File] to server which uses [httpClient] 
 * and takes place on [url] and returns [Collection] of [RemoteInstance] objects.
 */
suspend fun File.uploadDicomsInDirectoryRecursively(httpClient: HttpClient, url: Url): Collection<RemoteInstance> {
    if (!this.isDirectory) {
        throw IllegalArgumentException("Receiver is not a directory")
    }
    val remoteInstanceList = mutableListOf<RemoteInstance>()
    for (file in this.walkBottomUp()) {
        val remoteInstance = LocalInstance(file, httpClient, url).uploadDicom()
        remoteInstanceList.add(remoteInstance)
    }

    return remoteInstanceList
}