package codes.jrave.aoc2025

fun <T, S> findAllRelations(nodes: Collection<T>, valueFunction: (T, T) -> S): Collection<Relation<T, S>> {
    val queue = nodes.toMutableList()
    val relations = listOf<Relation<T, S>>().toMutableList()
    while (queue.isNotEmpty()) {
        val cur = queue.removeLast()
        val newRelations = queue.map { other -> Relation(cur, other, valueFunction(cur, other)) }
        relations.addAll(newRelations)
    }
    return relations
}


data class Relation<T, S>(val a: T, val b: T, val value: S)