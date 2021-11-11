fun Field.addNumbersOfMines() {
    for (i in 0 until size) {
        for (j in 0 until size) {
            if (this[i][j] is Cell.Bomb) continue
            val minesCount = countMines(i, j)

            this[i][j] =
                if (minesCount == 0) Cell.Empty()
                else Cell.Empty(numberOfMines = minesCount)
        }
    }
}

private fun Field.countMines(i: Int, j: Int): Int {
    var minesCount = 0
    val size = size - 1

    if (i != 0 && j != 0 && this[i - 1][j - 1] is Cell.Bomb) minesCount++
    if (i != 0 && this[i - 1][j] is Cell.Bomb) minesCount++
    if (i != 0 && j != size && this[i - 1][j + 1] is Cell.Bomb) minesCount++
    if (j != 0 && this[i][j - 1] is Cell.Bomb) minesCount++
    if (j != size && this[i][j + 1] is Cell.Bomb) minesCount++
    if (i != size && j != 0 && this[i + 1][j - 1] is Cell.Bomb) minesCount++
    if (i != size && this[i + 1][j] is Cell.Bomb) minesCount++
    if (i != size && j != size && this[i + 1][j + 1] is Cell.Bomb) minesCount++

    return minesCount
}