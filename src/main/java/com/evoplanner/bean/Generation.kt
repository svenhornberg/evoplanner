package com.evoplanner.bean

import org.apache.commons.collections4.ListUtils
import java.util.*

data class Generation(val uuid: UUID, var desks: List<Desk>) {

    enum class SELECTIONMODE {TOURNAMENT, RANDOM, TOP }
    enum class MUTATIONMODE {MUTATE, CROSSOVER }

    private fun selection(SELECTIONMODE: SELECTIONMODE): List<Desk> {
        val numbersToPick = 6
        when (SELECTIONMODE) {
            Generation.SELECTIONMODE.RANDOM -> return selectionRandom(numbersToPick)
            Generation.SELECTIONMODE.TOP -> return selectionTop(numbersToPick)
            Generation.SELECTIONMODE.TOURNAMENT -> return selectionTournament(numbersToPick)
        }
    }

    private fun selectionTournament(numbersToPick: Int): List<Desk> {
        var newDesks = mutableListOf<Desk>()
        newDesks.addAll(desks)

        while (newDesks.size > numbersToPick) {
            newDesks = tournament(newDesks)
        }
        return newDesks

    }

    private fun tournament(desks: List<Desk>): MutableList<Desk> {
        val firstRound = ListUtils.partition(desks, 2)
        val result = firstRound.map {
            if (it.first().fitness() > it.last().fitness()) {
                it.first()
            } else {
                it.last()
            }
        }
        return result.toMutableList()
    }

    private fun selectionRandom(numbersToPick: Int): List<Desk> {
        val newdesks = mutableListOf<Desk>()
        newdesks.addAll(desks)
        return newdesks.take(numbersToPick)
    }

    private fun selectionTop(numbersToPick: Int): List<Desk> {
        return desks.sortedBy { -it.fitness() }.take(numbersToPick)
    }

    fun newGeneration(selectionMode: SELECTIONMODE, mutationMode: MUTATIONMODE): Generation {

        //Take
        val selectedDesks = selection(selectionMode)
        val otherDesks = mutableListOf<Desk>()

        when (mutationMode) {

            MUTATIONMODE.MUTATE -> otherDesks.addAll(selectedDesks.map {
                it.mutate()
            })

            MUTATIONMODE.CROSSOVER -> otherDesks.addAll(genereatePairs(selectedDesks).map {
                it.first.pmxCrossover(it.second)
            })

        }

        val combinedDesks = mutableListOf<Desk>()
        combinedDesks.addAll(selectedDesks)
        combinedDesks.addAll(otherDesks)
        return Generation(UUID.randomUUID(), combinedDesks)
    }

    private fun genereatePairs(persons: List<Desk>): MutableList<Pair<Desk, Desk>> {

        val liste = mutableListOf<Pair<Desk, Desk>>()
        for ((index, element) in persons.withIndex()) {
            try {
                liste.add(Pair(element, persons.get(index + 1)))
                // liste.add(Pair(persons.get(index + 1),element))
            } catch (ex: IndexOutOfBoundsException) {
                liste.add(Pair(element, persons.first()))
                // liste.add(Pair(persons.first(),element))

            }
        }
        return liste
    }
}
