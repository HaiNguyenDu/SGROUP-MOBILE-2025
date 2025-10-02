package com.example.sgroupmobile2025.data.model

data class DataProduct(
    private val imgSrc : Int,
    private val name : String,
    private val description : String,
    private val price : Double,
    private val detailDes: String
    ) {
    fun getImgSrc(): Int = imgSrc
    fun getName() : String = name
    fun getDescription() : String = description
    fun getPrice() : Double = price

    fun getDetailDes(): String = detailDes
}