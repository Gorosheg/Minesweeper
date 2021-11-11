import kotlin.random.Random

fun Field.addMines(minesCount: Int, a: Int, b: Int, command: String) {
    var existMines = 0

    while (existMines != minesCount) {
        val x = Random.nextInt(size)
        val y = Random.nextInt(size)

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