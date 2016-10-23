package com.evoplanner.bean

data class Person(val id: Int,
                  val name: String,
                  val age: Int,
                  val sex: Sex,
                  val host: Boolean) : Comparable<Person> {

    var dislikes: MutableList<Int> = mutableListOf()



    override fun compareTo(other: Person): Int {
        return this.id.compareTo(other.id)
    }
}