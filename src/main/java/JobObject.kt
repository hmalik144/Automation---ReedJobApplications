import java.util.*

data class JobObject(
        var jobId: String? = null,
        var website: String? = null,
        var jobTitle: String? = null,
        var location: String? = null,
        var company: String? = null,
        var url: String? = null,
        var dateApplied : String? = null
){

    override fun toString(): String {
        return super.toString()
    }
}