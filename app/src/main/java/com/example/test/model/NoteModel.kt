package com.example.test.model

data class CustomDate(val day:Int , val month : Int , val year : Int): Comparable<CustomDate> {

    constructor() : this(0, 0, 0)
    override fun compareTo(other: CustomDate): Int {
        if (year != other.year) {
            return year.compareTo(other.year)
        } else if (month != other.month) {
            return month.compareTo(other.month)
        }
        return day.compareTo(other.day)
    }
}


data class NoteModel (
    val date: CustomDate,
    val weekDay :String,
    var task:String,
    val status: String,
    var id: String? = null,
    var isChecked: Boolean = false,
    var isSelected: Boolean = false,
    var description : String? = null){

    constructor() : this( CustomDate(0,0,0), "", "", "", "", false, false , null)
}
