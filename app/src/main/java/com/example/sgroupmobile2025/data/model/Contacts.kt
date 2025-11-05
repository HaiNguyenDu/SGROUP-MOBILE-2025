package com.example.sgroupmobile2025.data.model

data class Contacts (
    val name: String,
    val phone: String
){
    companion object{
        val listContacts = listOf(
            Contacts("T Phương", "0987564567"),
            Contacts("Ngọc Nhi", "0980004567"),
            Contacts("Hoa Hoa", "0956787133"),
            Contacts("Minh Quân", "0328909098"),
            Contacts("Trọng Nam", "0417829367"),
            Contacts("Huyền Trang", "0345664567"),
            Contacts("Thu Nguyễn", "0309874567"),
            Contacts("Hằng Hằng", "0987566786"),
            Contacts("Huy Trung", "0983457167"),
            Contacts("Quỳnh Nga", "0987095386"),
            Contacts("Nhung Hồng", "0987009764"),
            Contacts("Ánh Tuyết", "0989999386"),
            Contacts("Sỹ Xuân", "0987045666"),
        )
    }
}