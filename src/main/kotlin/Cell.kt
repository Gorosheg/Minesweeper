sealed class Cell(var isOpen: Boolean, var isFlagged: Boolean = false) {

    class Bomb(isOpen: Boolean = false) : Cell(isOpen)

    class Empty(isOpen: Boolean = false, val numberOfMines: Int = 0) : Cell(isOpen)

    class Greed(val greedChar: Char) : Cell(true)

    fun getChar(): Char {
        return when {
            this.isFlagged -> '*'
            this is Empty && numberOfMines > 0 && isOpen -> numberOfMines.digitToChar()
            this is Empty && isOpen -> '/'
            this is Bomb && isOpen -> 'X'
            this is Greed -> greedChar
            else -> '.'
        }
    }
}