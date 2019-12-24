import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.StringSerializer

@Serializable
data class MainDicomTags(
    @SerialName("ImageComments")
    val imageComments: String,
    @SerialName("InstanceCreationDate")
    val instanceCreationDate: String = "",
    @SerialName("InstanceCreationTime")
    val instanceCreationTime: String = "",
    @SerialName("InstanceNumber")
    val instanceNumber: String,
    @SerialName("NumberOfFrames")
    val numberOfFrames: String,
    @SerialName("SOPInstanceUID")
    val SOPInstanceUID: String
)

@Serializable
data class InstanceShort(
    @SerialName("FileSize")
    val fileSize: Int,
    @SerialName("FileUuid")
    val fileUuid: String,
    @SerialName("ID")
    val id: String,
    @SerialName("IndexInSeries")
    val indexInSeries: Int,
    @SerialName("MainDicomTags")
    val mainDicomTags: MainDicomTags,
    @SerialName("ParentSeries")
    val parentSeries: String,
    @SerialName("Type")
    val type: String
)

fun main() = runBlocking {
    val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer().apply {
                register(ArrayListSerializer(StringSerializer))
            }
        }
    }

    val instancesIds = client.get<List<String>> {
        url("http://localhost:8042/instances")
    }

    /*for (id in instancesIds) {
        val instance = client.get<InstanceShort> {
            url {
                port = 8042
                encodedPath = "/instances/$id"
            }
        }
        println(instance)
    }*/

    println(instancesIds.joinToString())

//    val instance = Instance(instancesIds[1], client, Url(URLBuilder(port = 8042, encodedPath = "/instances")))
//    instance.downloadDicom("/Users/egorponomaryov/IdeaProjects/MirfRepository/dicom")

//    val uploadInstance = Instance(
//        File("/Users/egorponomaryov/IdeaProjects/MirfRepository/dicom_to_upload/image-000001.dcm"),
//        client,
//        Url(URLBuilder(port = 8042, encodedPath = "/instances"))
//    )
//    uploadInstance.uploadDicom()
    println(client.get<List<String>> {
        url("http://localhost:8042/instances")
    }.joinToString())

    client.close()
}