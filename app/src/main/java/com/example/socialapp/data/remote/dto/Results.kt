package com.example.socialapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("@attr")
    val attr: Attr,
    val opensearch: OpensearchQuery,
    @SerializedName("opensearch:itemsPerPage")
    val opensearchitemsPerPage: String,
    @SerializedName("opensearch:startIndex")
    val opensearchstartIndex: String,
    @SerializedName("opensearch:totalResults")
    val opensearchtotalResults: String,
    val trackmatches: Trackmatches
)