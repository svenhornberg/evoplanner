package com.evoplanner

import com.evoplanner.bean.Desk
import com.evoplanner.bean.Generation
import com.evoplanner.bean.Person
import com.evoplanner.bean.Sex
import java.util.*

fun main(args: Array<String>) {

    // Id, Name, Alter, Sex, Gastgeber
    val p1  = Person( 1, "Anna",        18, Sex.female,  true )
    val p2  = Person( 2, "Sophie",      18, Sex.female,  false)
    val p3  = Person( 3, "Erik",        18, Sex.male,    false)
    val p4  = Person( 4, "Marie",       18, Sex.female,  false)
    val p5  = Person( 5, "Thorsten",    18, Sex.male,    false)
    val p6  = Person( 6, "Stefanie",    18, Sex.female,  false)
    val p7  = Person( 7, "Markus",      18, Sex.male,    false)
    val p8  = Person( 8, "Karolin",     18, Sex.female,  false)
    val p9  = Person( 9, "Ralph",       18, Sex.male,    false)
    val p10 = Person(10, "Martina",     18, Sex.female,  false)
    val p11 = Person(11, "Felix",       18, Sex.male,    false)
    val p12 = Person(12, "Lena",        18, Sex.female,  false)

    p4.dislikes.addAll(listOf(p1.id, p8.id))
    p5.dislikes.addAll(listOf(p2.id, p6.id, p10.id))


    val persons = listOf(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
    val initialDesk = Desk(UUID.randomUUID(), persons)
    val desks = mutableListOf<Desk>()
    for(i in 1..12) {
        desks.add(initialDesk.mutate())
    }

    val firstGen = Generation(UUID.randomUUID(), desks)

    val numberGenerations = 10
    var nextGen = firstGen
    for (i in 1..numberGenerations) {

        println()
        println("Generation:" + i)
        nextGen.desks.sortedBy { -it.fitness() }.forEach {
            println(it)
        }

        //care TOURNAMENT , CROSSOVER
        nextGen = nextGen.newGeneration(Generation.SELECTIONMODE.TOP, Generation.MUTATIONMODE.MUTATE)


    }

}