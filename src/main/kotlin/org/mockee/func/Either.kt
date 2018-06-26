package org.mockee.func

@Suppress("UNCHECKED_CAST")
sealed class Either<out L, out R> {

    inline fun <N> leftMap(crossinline fn: (L) -> N): Either<N, R> = when (this) {
        is Left -> Left(fn(leftValue))
        is Right -> this as Either<N, R>
    }

    inline fun <N> map(crossinline fn: (R) -> N): Either<L, N> = when (this) {
        is Right -> Right(fn(rightValue))
        is Left -> this as Either<L, N>
    }

    fun left(): L = when (this) {
        is Left -> leftValue
        else ->
            throw NoSuchElementException("leftValue projection does not exist")
    }

    fun right(): R = when (this) {
        is Right -> rightValue
        else ->
            throw NoSuchElementException("rightValue projection does not exist")
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <L, R, N> Either<L, R>.flatMap(crossinline fn: (R) -> Either<L, N>): Either<L, N> = when (this) {
    is Right -> fn(rightValue)
    is Left -> this as Either<L, N>
}

data class Right<out L, out R>(val rightValue: R) : Either<L, R>()
data class Left<out L, out R>(val leftValue: L) : Either<L, R>()