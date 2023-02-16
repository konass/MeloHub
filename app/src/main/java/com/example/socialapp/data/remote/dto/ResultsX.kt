package com.example.socialapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ResultsX(
    @SerializedName("@attr")
    val attr: Attr,
    val artistmatches: Artistmatches,
    val opensearchQuery: OpensearchQuery,
    @SerializedName("opensearch:itemsPerPage")
    val opensearchitemsPerPage: String,
    @SerializedName("opensearch:startIndex")
    val opensearchstartIndex: String,
    @SerializedName("opensearch:totalResults")
    val opensearchtotalResults: String
)