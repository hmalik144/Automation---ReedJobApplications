package api.network.responses
data class ReedJobObject(
        val date: String?,
        val employerProfileId: String?,
        val locationName: String?,
        val maximumSalary: String?,
        val jobTitle: String?,
        val employerName: String?,
        val employerProfileName: String?,
        val jobId: Int?,
        val employerId: String?,
        val minimumSalary: String?,
        val jobUrl: String?,
        val jobDescription: String?,
        val expirationDate: String?,
        val applications: Int?
)