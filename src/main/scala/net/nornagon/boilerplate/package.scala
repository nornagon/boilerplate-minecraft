package net.nornagon

import scala.collection.mutable

package object boilerplate {
  type Coord = (Int, Int, Int)

  val cardinals = Seq(
    (1,0,0),
    (-1,0,0),
    (0,1,0),
    (0,-1,0),
    (0,0,1),
    (0,0,-1)
  )

  def bfsFrom(c: Coord)(cb: (Coord) => Boolean): Unit = {
    val visited = mutable.Set.empty[Coord]
    val toExplore = mutable.Queue[Coord](c)

    def hmm(to: Coord) = {
      if (!visited(to)) {
        visited += to
        toExplore.enqueue(to)
      }
    }

    while (toExplore.nonEmpty) {
      val to = toExplore.dequeue()
      if (cb(to)) {
        val (x, y, z) = to
        for ((dx, dy, dz) <- cardinals) {
          hmm((x+dx, y+dy, z+dz))
        }
      }
    }
  }

}
