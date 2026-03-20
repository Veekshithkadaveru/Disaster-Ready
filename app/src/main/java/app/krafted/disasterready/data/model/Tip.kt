package app.krafted.disasterready.data.model

import com.google.gson.annotations.SerializedName

data class Tip(
    val id: Int,
    @SerializedName("phase") val phase: Phase,
    @SerializedName("severity") val severity: Severity,
    val title: String,
    val body: String
)
