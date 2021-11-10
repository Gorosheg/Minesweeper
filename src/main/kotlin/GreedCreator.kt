fun Field.addGreed() {
    add(0, buildHorizontalSeparatorLine(size))
    add(0, buildHorizontalNumbersLine())
    add(buildHorizontalSeparatorLine(size))
    addVerticalSeparatorLine()
    addVerticalNumbersLine()
}

private fun buildHorizontalSeparatorLine(size: Int): Line {
    return mutableListOf<Cell>().also { innerList ->
        for (i in 0..size) {
            innerList.add(Cell.Greed('—'))
        }
    }
}

private fun buildHorizontalNumbersLine(): Line {
    return mutableListOf<Cell>().also { innerList ->
        for (i in 1..9) {
            innerList.add(Cell.Greed((i).digitToChar()))
        }
    }
}

private fun Field.addVerticalSeparatorLine() {
    for (i in 0 until size) {
        for (j in 0 until size) {
            if (j == 0 || j == size - 2) {
                this[i].add(j, Cell.Greed('|'))
            }
        }
    }
}

private fun Field.addVerticalNumbersLine() {
    for (i in 0 until size) {
        for (j in 0 until size) {
            if (j == 0) {
                this[i].add(j, buildSideNumber(i, size))
            }
        }
    }
}

private fun buildSideNumber(i: Int, sizeOfField: Int): Cell {
    return when (i) {
        0 -> Cell.Greed(' ')
        1, sizeOfField - 1 -> Cell.Greed('—')
        else -> Cell.Greed((i - 1).digitToChar())
    }
}