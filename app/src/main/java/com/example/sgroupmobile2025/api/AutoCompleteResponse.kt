package com.example.sgroupmobile2025.api


data class AutoCompleteResponse(
    val predictions: List<Prediction>,
    val execution_time: String?,
    val status: String?
)

data class Prediction(
    val description: String?,
    val matched_substrings: List<Any>?,
    val place_id: String?,
    val reference: String?,
    val structured_formatting: StructuredFormatting?,
    val has_children: Boolean?,
    val plus_code: PlusCode?,
    val compound: Compound?,
    val deprecated_description: String?,
    val terms: List<Term>?,
    val types: List<String>?,
    val distance_meters: Double?
)

data class StructuredFormatting(
    val main_text: String?,
    val main_text_matched_substrings: List<Any>?,
    val secondary_text: String?,
    val secondary_text_matched_substrings: List<Any>?
)

data class PlusCode(
    val compound_code: String?,
    val global_code: String?
)

data class Compound(
    val district: String?,
    val commune: String?,
    val province: String?
)

data class Term(
    val offset: Int?,
    val value: String?
)