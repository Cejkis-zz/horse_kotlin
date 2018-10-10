package com.company

import java.util.*

class Position(val x: Int, val y: Int) // Simple container for coordinates
class Node(val predecessor: Node?, val position: Position) // Represents a node in a BFS search tree

object Main {

    private val MAX_SIZE = 10000 // maximal height or width of the board

    // finds the shortest path for a horse on a chessboard using BFS tree.
    @JvmStatic
    fun main(args: Array<String>) {

        // get input from user
        val m = readOneInteger("board width", 1, MAX_SIZE) // width and height of the board
        val n = readOneInteger("board height", 1, MAX_SIZE)
        val start = Position(readOneInteger("x position of the start",0,  m), readOneInteger("y position of the start", 0, n))
        val goal = Position(readOneInteger("x position of the goal",0,  m), readOneInteger("y position of the goal", 0, n))

        // initialize search variables
        val board = Array(m){arrayOfNulls<Node?>(n)} // maps position to Node in search tree
        board[start.x][start.y] = Node(null, start) // starting node

        val searchQueue = LinkedList<Position>() // BFS queue for nodes.
        searchQueue.add(start)

        val steps = intArrayOf(-2, -1, 1, 2) // possible changes of coordinates for horse
        var pos: Position

        do {
            pos = searchQueue.pop()
            val parentNode = board[pos.x][pos.y] // tree node of the position I'm expanding

            if (pos.x == goal.x && pos.y == goal.y) { // Did we reach the goal state?
                printResult(parentNode)
                return
            }

            // do all possible moves in distance 1 or 2 and expand the search
            for (i in steps) {
                for (j in steps) {

                    if (Math.abs(i) == Math.abs(j)) // filter only horse's moves
                        continue

                    val newX = pos.x + i
                    val newY = pos.y + j

                    if (newX < 0 || newX >= m || // filter moves outside chessboard
                            newY < 0 || newY >= n)
                        continue

                    val newPos = Position(newX, newY)

                    if (board[newX][newY] == null) { // create a new node and set its parent
                        board[newX][newY] = Node(parentNode, newPos)
                        searchQueue.add(newPos)
                    } // else: node already exists in some shorter branch
                }
            }
        } while (searchQueue.isNotEmpty())

        // if the queue is empty, we have no valid moves and no space to expand.
        println("\nThere is no solution.")
    }

    // asks user to input a integer denoted by "text" smaller than max
    private fun readOneInteger(text: String, min: Int = 0, max: Int = MAX_SIZE): Int {

        println("Please enter $text:")

        while (true) {
            val str = readLine() ?: ""

            if (str.matches("\\d{1,7}".toRegex()) && Integer.parseInt(str) >= min && Integer.parseInt(str) < max) {
                return Integer.parseInt(str)
            } else {
                println("Please enter $text again. It has to be integer >= $min and < $max.")
            }
        }
    }

    // Backtracks nodes from goal to start and prints their positions
    private fun printResult(goalPosition:Node?) {

        val path = ArrayList<Position>()
        var previous: Node? = goalPosition

        while (previous != null) {
            path.add(previous.position)
            previous = previous.predecessor
        }

        println("\nThe path to the goal is ${(path.size - 1)} steps long:")

        for (i in path.indices.reversed()) {
            println("${path[i].x} ${path[i].y}")
        }
    }
}
