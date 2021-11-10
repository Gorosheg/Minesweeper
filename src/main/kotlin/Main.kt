import java.util.Scanner
import kotlin.random.Random.Default.nextInt

val scanner = Scanner(System.`in`)
typealias Line = MutableList<Cell>
typealias Field = MutableList<Line>

const val SIZE = 8

fun main() {

    println("How many mines do you want on the field?")
    val numberOfMines = scanner.nextInt()

    val emptyField = createField().apply {
        addGreed()
    }
    printField(emptyField)
    prepareFieldForGame(numberOfMines)

}

private fun createField(): Field {
    val outerList = mutableListOf<Line>()
    for (i in 0..SIZE) {
        outerList.add(createLine())
    }
    return outerList
}

private fun createLine(): Line {
    return mutableListOf<Cell>().also { innerList ->
        for (j in 0..SIZE) {
            innerList.add(Cell.Empty(false))
        }
    }
}

private fun prepareFieldForGame(numberOfMines: Int) {
    val (x, y, command) = readCoordinates()

    val field = createField().apply {
        addMines(numberOfMines, x - 1, y - 1, command)
        addNumbersOfMines()
        addGreed()
    }

    openCellsAndPrintField(field, x, y, command)
    gameProcess(field, numberOfMines)
}

private fun gameProcess(field: Field, numberOfMines: Int) {
    var gameEnds = false
    while (!gameEnds) {
        val (x, y, command) = readCoordinates()

        when {
            field.isMineStepped(x + 1, y + 1, command) -> {
                field.blowUpMines()
                gameEnds = true
                openCellsAndPrintField(field, x, y, command)
                println("You stepped on a mine and failed!")
            }
            field.isOpenedAllMines(numberOfMines) ||
                    field.isFullFieldClearlyOpen(numberOfMines) -> {
                gameEnds = true
                openCellsAndPrintField(field, x, y, command)
                println("Congratulations! You found all the mines!")
            }
            else -> openCellsAndPrintField(field, x, y, command)
        }
    }
}

private fun openCellsAndPrintField(field: Field, x: Int, y: Int, command: String) {
    field.openCell(x + 1, y + 1, command)
    printField(field)
}

private fun Field.addMines(
    minesCount: Int,
    a: Int,
    b: Int,
    command: String
) {
    var existMines = 0

    while (existMines != minesCount) {
        val x = nextInt(size)
        val y = nextInt(size)

        if (command == "free") {
            if (isFirstChoice(a, b, x, y)) continue
        }

        if (this[x][y] is Cell.Empty) {
            this[x][y] = Cell.Bomb()
            existMines++
        }
    }
}

private fun isFirstChoice(a: Int, b: Int, x: Int, y: Int): Boolean {
    return (x == a && y == b)
            || (x == (a - 1) && y == b)
            || (x == (a - 1) && y == (b + 1))
            || (x == a && y == (b + 1))
            || (x == (a + 1) && y == (b + 1))
            || (x == (a + 1) && y == b)
            || (x == (a + 1) && y == (b - 1))
            || (x == a && y == (b - 1))
            || (x == (a - 1) && y == (b - 1))
}

private fun Field.addNumbersOfMines() {
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

private fun Field.openCell(x: Int, y: Int, command: String) {
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

private fun Field.changeSymbolsForMine(x: Int, y: Int) {
    val cell = this[x][y]
    cell.isFlagged = !cell.isFlagged
}

fun Field.isMineStepped(x: Int, y: Int, command: String): Boolean {
    if (this[x][y] is Cell.Bomb && command == "free") {
        return true
    }
    return false
}

fun Field.blowUpMines() {
    for (i in 0 until size) {
        for (j in 0 until size) {
            if (this[i][j] is Cell.Bomb) {
                this[i][j].isOpen = true
            }
        }
    }
}

fun Field.isOpenedAllMines(numberOfMines: Int): Boolean {
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

fun Field.isFullFieldClearlyOpen(numberOfMines: Int): Boolean {
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

private fun readCoordinates(): Triple<Int, Int, String> {
    println("Set/unset mine marks or claim a cell as free:")
    val x = scanner.nextInt()
    val y = scanner.nextInt()
    val command = scanner.next()
    return Triple(y, x, command)
}

private fun printField(field: Field) {
    for (i in 0 until field.size) {
        for (j in 0 until field[i].size) {
            print(field[i][j].getChar())
        }

        println()
    }
}