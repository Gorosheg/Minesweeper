fun buildField(size: Int): Field {
    return mutableListOf<Line>().also { outerList ->
        for (j in 0..size) {
            outerList.add(createLine(size))
        }
    }
}

private fun createLine(size: Int): Line {
    return mutableListOf<Cell>().also { innerList ->
        for (j in 0..size) {
            innerList.add(Cell.Empty(false))
        }
    }
}