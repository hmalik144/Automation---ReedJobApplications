

import api.network.responses.ReedJobObject

data class ReedResponse (
    val results : List<ReedJobObject>?,
    val totalResults : Int?
)