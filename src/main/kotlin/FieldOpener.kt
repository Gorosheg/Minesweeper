fun Field.openCell(x: Int, y: Int, command: String) {
    when (command) {
        "free" -> changeSymbolsForFree(x, y)
        "mine" -> changeSymbolsForMine(x, y)
    }
}

private fun Field.changeSymbolsForFree(x: Int, y: Int) {
    val cell = this[x][y]
    if (cell !is Cell.Empty || cell.isOpen) return
    cell.isOpen = true
    cell.isFlagged = false

    if (cell.numberOfMines > 0) return

    changeSymbolsForFree(x - 1, y)
    changeSymbolsForFree(x - 1, y + 1)
    changeSymbolsForFree(x, y + 1)
    changeSymbolsForFree(x + 1, y + 1)
    changeSymbolsForFree(x + 1, y)
    changeSymbolsForFree(x + 1, y - 1)
    changeSymbolsForFree(x, y - 1)
    changeSymbolsForFree(x - 1, y - 1)
}

fun Field.changeSymbolsForMine(x: Int, y: Int) {
    val cell = this[x][y]
    cell.isFlagged = !cell.isFlagged
}
