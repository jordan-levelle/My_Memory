package edu.bu.jlevelle.mymemory.models

import edu.bu.jlevelle.mymemory.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize) {

    val cards: List<MemoryCard>
    var numPairsFound = 0 // var because it will change over time

    private var numCardFlips = 0
    private var indexOfSingleSelectedCard: Int? = null

    init {
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it) }
    }

    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card  = cards[position]
        var foundMatch = false
        // 0 cards previously flipped over => flip over selected card & flip over the selected card ---
        // 1 card previously flipped over => flip over selected card & check if images match          | - 0 and 2 are the same
        // 2 cards previously flipped over => restore cards & flip over the selected card -------------
        if (indexOfSingleSelectedCard == null) {
            // 0 or 2 cards selected previously
            restoreCards()
            indexOfSingleSelectedCard = position
        } else{
            // exactly 1 card was selected previously
            foundMatch  = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards){
            if(!card.isMatched) {
                card.isFaceUp = false // restore to its default state
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int  {
       return numCardFlips / 2
    }

}