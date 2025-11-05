package com.example.sgroupmobile2025.data.model

import com.google.gson.annotations.SerializedName

data class AutoCompleteResponse(
    @SerializedName("predictions")
    val predictions: List<Prediction>,
    @SerializedName("execution_time")
    val executionTime: String?,
    val status: String
)

data class Prediction(
    val description: String,
    @SerializedName("matched_substrings")
    val matchedSubstrings: List<MatchedSubstring>?,
    @SerializedName("place_id")
    val placeId: String,
    val reference: String,
    @SerializedName("structured_formatting")
    val structuredFormatting: StructuredFormatting?,
    @SerializedName("has_children")
    val hasChildren: Boolean,
    @SerializedName("plus_code")
    val plusCode: PlusCode?,
    val compound: Compound?,
    val terms: List<Term>?,
    val types: List<String>?,
    @SerializedName("distance_meters")
    val distanceMeters: Int?,
    @SerializedName("deprecated_description")
    val deprecatedDescription: String?,
    @SerializedName("deprecated_compound")
    val deprecatedCompound: DeprecatedCompound?
)

data class MatchedSubstring(
    val length: Int,
    val offset: Int
)

data class StructuredFormatting(
    @SerializedName("main_text")
    val mainText: String?,
    @SerializedName("main_text_matched_substrings")
    val mainTextMatchedSubstrings: List<MatchedSubstring>?,
    @SerializedName("secondary_text")
    val secondaryText: String?,
    @SerializedName("secondary_text_matched_substrings")
    val secondaryTextMatchedSubstrings: List<MatchedSubstring>?
)

data class PlusCode(
    @SerializedName("compound_code")
    val compoundCode: String?,
    @SerializedName("global_code")
    val globalCode: String?
)

data class Compound(
    val commune: String?,
    val province: String?
)

data class DeprecatedCompound(
    val district: String?,
    val commune: String?,
    val province: String?
)

data class Term(
    val offset: Int,
    val value: String
)
