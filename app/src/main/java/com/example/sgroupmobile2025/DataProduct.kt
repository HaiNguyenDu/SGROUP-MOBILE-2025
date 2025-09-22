package com.example.sgroupmobile2025

data class DataProduct(private val imgSrc : Int, private val name : String, private val description : String, private val price : Double) {
    fun getImgSrc(): Int = imgSrc
    fun getName() : String = name
    fun getDescription() : String = description
    fun getPrice() : Double = price
}