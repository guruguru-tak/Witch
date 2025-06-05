package com.ssafy.witch.data.model.response

import com.google.gson.annotations.SerializedName

data class PresignedUrl(
    @SerializedName("presignedUrl")  var presignedUrl: String,
    @SerializedName("objectKey")  var objectKey: String?
)
