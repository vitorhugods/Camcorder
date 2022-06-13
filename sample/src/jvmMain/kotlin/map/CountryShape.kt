package map

class CountryShape(
    val vertices: List<Vertex>
) {

    val extremes: Vertex
        get() {
            val points = vertices.flatMap { it.toList() }.distinct()
            // TODO: Optimize (reduce unnecessary iterations)
            // TODO: Handle pacific islands
            //       Shapes that cross from +180 to -180 longitude?
            val maximums = points.maxOf(Coordinate::lat) coord points.maxOf(Coordinate::lon)
            val minimums = points.minOf(Coordinate::lat) coord points.minOf(Coordinate::lon)
            return minimums to maximums
        }

    companion object {
        fun sample() = CountryShape(
            listOf(
                (-25f coord -25f) to (-25f coord 25f),
                (-25f coord 25f) to (25f coord 25f),
                (25f coord 25f) to (25f coord -25f),
                (25f coord -25f) to (-25f coord -25f)
            )
        )
    }
}

typealias Vertex = Pair<Coordinate, Coordinate>

data class Coordinate(val lat: Float, val lon: Float)

infix fun Float.coord(other: Float) = Coordinate(this, other)