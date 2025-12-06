package com.example.sgroupmobile2025.ui.fragment.model

data class Poster(
    val title: String,
    val images: List<String>
) {
    companion object {
        val listPosters = listOf(
            Poster(
                "Free Design",
                listOf(
                    "https://img.freepik.com/free-vector/graphic-design-geometric-lettering_23-2148470664.jpg?semt=ais_hybrid&w=740&q=80",
                    "https://img.freepik.com/free-vector/design-process-concept-landing-page_23-2148313670.jpg?semt=ais_hybrid&w=740&q=80",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZ6GMl807SVgqee9iAtotYwlP13MLzMyJ6AjqRzcYL&s",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTq5vFRo2tUzsLWVCawgJhyVufMTqs1pQHJVPHEY_rN&s",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ3p2B1yhKtGUiQPDQbr1N1PexL9AJCRwXsGFM7gWavCw&s",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSD-NbOJa-04BefoTwsmBpYkfUSve2n-n27mgCy49Xi&s"
                )
            ),
            Poster(
                "Hot Design",
                listOf(
                    "https://img.freepik.com/free-vector/graphic-design-geometric-lettering_23-2148470664.jpg?semt=ais_hybrid&w=740&q=80",
                    "https://img.freepik.com/free-vector/design-process-concept-landing-page_23-2148313670.jpg?semt=ais_hybrid&w=740&q=80",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZ6GMl807SVgqee9iAtotYwlP13MLzMyJ6AjqRzcYL&s",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTq5vFRo2tUzsLWVCawgJhyVufMTqs1pQHJVPHEY_rN&s",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ3p2B1yhKtGUiQPDQbr1N1PexL9AJCRwXsGFM7gWavCw&s",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSD-NbOJa-04BefoTwsmBpYkfUSve2n-n27mgCy49Xi&s"
                )
            ),
            Poster(
                "Flexible",
                listOf(
                    "https://img.freepik.com/free-vector/graphic-design-geometric-lettering_23-2148470664.jpg?semt=ais_hybrid&w=740&q=80",
                    "https://img.freepik.com/free-vector/design-process-concept-landing-page_23-2148313670.jpg?semt=ais_hybrid&w=740&q=80",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZ6GMl807SVgqee9iAtotYwlP13MLzMyJ6AjqRzcYL&s",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTq5vFRo2tUzsLWVCawgJhyVufMTqs1pQHJVPHEY_rN&s",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ3p2B1yhKtGUiQPDQbr1N1PexL9AJCRwXsGFM7gWavCw&s",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSD-NbOJa-04BefoTwsmBpYkfUSve2n-n27mgCy49Xi&s"
                )
            ),
            Poster(
                "Perfect",
                listOf(
                    "https://img.freepik.com/free-vector/graphic-design-geometric-lettering_23-2148470664.jpg?semt=ais_hybrid&w=740&q=80",
                    "https://img.freepik.com/free-vector/design-process-concept-landing-page_23-2148313670.jpg?semt=ais_hybrid&w=740&q=80",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZ6GMl807SVgqee9iAtotYwlP13MLzMyJ6AjqRzcYL&s",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTq5vFRo2tUzsLWVCawgJhyVufMTqs1pQHJVPHEY_rN&s",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ3p2B1yhKtGUiQPDQbr1N1PexL9AJCRwXsGFM7gWavCw&s",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSD-NbOJa-04BefoTwsmBpYkfUSve2n-n27mgCy49Xi&s"
                )
            )
        )
        fun getIndexByTitle(title: String): Int {
            for(i in listPosters.indices){
                if (title == listPosters[i].title){
                    return i
                }
            }
            return -1
        }
    }

}
