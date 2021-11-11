import java.util.*

private val scanner = Scanner(System.`in`)
typealias Line = MutableList<Cell>
typealias Field = MutableList<Line>

private const val SIZE = 8

fun main() {
    println("How many mines do you want on the field?")
    val numberOfMines = scanner.nextInt()

    val emptyField = buildField(SIZE).apply {
        addGreed()
    }

    printField(emptyField)

    val field = buildAndPrepareField(numberOfMines)
    startGame(field, numberOfMines)
}

private fun buildAndPrepareField(numberOfMines: Int): Field {
    val (x, y, command) = readCoordinates()

    return buildField(SIZE).apply {
        addMines(numberOfMines, x - 1, y - 1, command)
        addNumbersOfMines()
        addGreed()
        openCellsAndPrintField(x, y, command)
    }
}

private fun startGame(field: Field, numberOfMines: Int) {
    var gameEnds = false

    while (!gameEnds) {
        val (x, y, command) = readCoordinates()

        when {
            field.isMineStepped(x + 1, y + 1, command) -> {
                field.blowUpMines()
                gameEnds = true
                field.openCellsAndPrintField(x, y, command)
                println("You stepped on a mine and failed!")
            }

            field.isOpenedAllMines(numberOfMines) || field.isFullFieldClearlyOpen(numberOfMines) -> {
                gameEnds = true
                field.openCellsAndPrintField(x, y, command)
                println("Congratulations! You found all the mines!")
            }

            else -> field.openCellsAndPrintField(x, y, command)
        }

    }
}

private fun readCoordinates(): Triple<Int, Int, String> {
    println("Set/unset mine marks or claim a cell as free:")
    val x = scanner.nextInt()
    val y = scanner.nextInt()
    val command = scanner.next()
    return Triple(y, x, command)
}

private fun Field.isMineStepped(x: Int, y: Int, command: String): Boolean {
    return this[x][y] is Cell.Bomb && command == "free"
}

private fun Field.blowUpMines() {
    for (i in 0 until size) {
        for (j in 0 until size) {
            if (this[i][j] is Cell.Bomb) {
                this[i][j].isOpen = true
            }
        }
    }
}

private fun Field.isOpenedAllMines(numberOfMines: Int): Boolean {
    var countOfOpenMines = 0

    for (i in 0 until size) {
        for (j in 0 until size) {
            if (this[i][j] is Cell.Bomb && this[i][j].isFlagged) {
                countOfOpenMines++
            }
        }
    }

    return countOfOpenMines == numberOfMines
}

private fun Field.isFullFieldClearlyOpen(numberOfMines: Int): Boolean {
    val cellsWithoutMines = (SIZE + 1 * SIZE + 1) - numberOfMines
    var openCells = 0

    for (i in 0 until size) {
        for (j in 0 until size) {
            if (this[i][j].isOpen) {
                openCells++
            }
        }
    }

    return cellsWithoutMines == openCells
}

private fun Field.openCellsAndPrintField(x: Int, y: Int, command: String) {
    openCell(x + 1, y + 1, command)
    printField(this)
}

private fun printField(field: Field) {
    for (i in 0 until field.size) {
        for (j in 0 until field[i].size) {
            print(field[i][j].getChar())
        }

        println()
    }
}