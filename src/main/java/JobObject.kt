import api.network.responses.ReedJobObject
import java.time.LocalDateTime
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

    constructor(job: ReedJobObject): this(
            jobId = job.jobId.toString(),
            website = job.jobUrl,
            jobTitle = job.jobTitle,
            location = job.locationName,
            company = job.employerName,
            dateApplied = LocalDateTime.now().toString()
    )
}