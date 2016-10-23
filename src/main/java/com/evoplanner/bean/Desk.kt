package com.evoplanner.bean

import org.apache.commons.collections4.ListUtils
import java.util.*

data class Desk(val uuid: UUID, val chairs: List<Person>) {

    /**
     * Mutiert Desk (shuffle chairs)
     */
    fun mutate(): Desk {
        val shuffledChairs = mutableListOf<Person>()
        shuffledChairs.addAll(chairs)
        Collections.shuffle(shuffledChairs)
        return Desk(UUID.randomUUID(), shuffledChairs)
    }

    override fun toString(): String {
        return java.lang.String.format("%03d : %s", fitness(), chairs.map { it.name + " (" + it.sex.toString().get(0).toUpperCase() + ")" })
    }

    fun fitness(): Int {
        var fitness = 0
        //apply fitness rules

        //+10 Gastgeber sizt auf platz 1 (pos 0)
        if (chairs.first().host) {
            fitness += 50
        }

        // Frau sizt neben Mann
        val pairs = genereatePairs(chairs)
        pairs.forEach {
            if(it.first.sex != it.second.sex) {
                fitness += 30
            }
        }
        // Dislikes
        chairs.forEach {
            if(it.dislikes.contains(it.id)) {
                fitness -= 20
            }
        }

        return fitness
    }

    fun pmxCrossover(other: Desk): Desk {
        val partionSize = Math.ceil(chairs.size.toDouble() / 3).toInt()
        val partitionTable1 = ListUtils.partition(chairs, partionSize)
        val partitionTable2 = ListUtils.partition(other.chairs, partionSize)

        val midTable1 = partitionTable1[1]
        val midTable2 = partitionTable2[1]

        //mapping
        val mappingCrossover = mutableMapOf<Person, Person>()
        for (i in 0..midTable1.size - 1) {
            mappingCrossover.put(midTable2[i], midTable1[i] )
        }

        val child = mutableListOf<Person>()

        // first Part
        for (element in partitionTable1[0]) {
            if (mappingCrossover.containsKey(element)) {
                var person = element
                while (mappingCrossover.containsKey(person)) {
                    person = mappingCrossover[person]
                }
                child.add(person)
            } else {
                child.add(element)
            }
        }

        // mid
        child.addAll(midTable2)

        // last
        for (element in partitionTable1[2]) {
            if (mappingCrossover.containsKey(element)) {
                var person = element
                while (mappingCrossover.containsKey(person)) {
                    person = mappingCrossover[person]
                }
                child.add(person)
            } else {
                child.add(element)
            }
        }

        return Desk(UUID.randomUUID(), child)
    }

    private fun genereatePairs(persons: List<Person>): MutableList<Pair<Person, Person>> {

        val liste = mutableListOf<Pair<Person,Person>>()
        for((index, element) in persons.withIndex()) {
            try {
                liste.add(Pair(element, persons.get(index + 1)))
            } catch (ex : IndexOutOfBoundsException) {
                liste.add(Pair(element, persons.first()))
            }
        }
        return liste
    }
}