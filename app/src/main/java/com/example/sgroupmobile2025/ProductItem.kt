data class ProductItem(
    val imageRes: Int,
    val brand: String,
    val description: String,
    val price: String,
    var isFavorite: Boolean = false
)