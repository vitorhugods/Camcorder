package map

import java.lang.Float.max

class CountryShape(
    val vertices: List<Vertex>
) {

    val extremes: Vertex by lazy {
        val points = vertices.flatMap { it.toList() }.distinct()
        // TODO: Optimize (reduce unnecessary iterations)
        // TODO: Handle pacific islands
        //       Shapes that cross from +180 to -180 longitude?
        val maximums = points.maxOf(Coordinate::lat) coord points.maxOf(Coordinate::lon)
        val minimums = points.minOf(Coordinate::lat) coord points.minOf(Coordinate::lon)
        minimums to maximums
    }

    fun transposeCoordinateToCanvas(coordinate: Coordinate, canvasMaxSize: Float): Coordinate {
        val verticalCenter = extremes.second.lat - extremes.first.lat
        val horizontalCenter = extremes.second.lon - extremes.first.lon
        val biggest = max(verticalCenter, horizontalCenter)
        val scale = canvasMaxSize / biggest

        val newLon = (coordinate.lon - extremes.first.lon) * scale
        val newLat = (coordinate.lat - extremes.first.lon) * scale
        return newLat coord newLon
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