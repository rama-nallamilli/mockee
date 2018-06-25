package org.mockee.func

@Suppress("UNCHECKED_CAST")
sealed class Either<T, out R> {

    fun <N> leftMap(fn: (T) -> N): Either<N, R> {
        return when (this) {
            is Left -> Left(fn(leftValue))
            is Right -> this as Either<N, R>
        }
    }

    fun <N> map(fn: (R) -> N): Either<T, N> {
        return when (this) {
            is Right -> Right(fn(rightValue))
            is Left -> this as Either<T, N>
        }
    }

    fun <N> flatMap(fn: (R) -> Either<T, N>): Either<T, N> {
        return when (this) {
            is Right -> fn(rightValue)
            is Left -> this as Either<T, N>
        }
    }

    fun left(): T {
        return when (this) {
            is Left -> leftValue
            else ->
                throw NoSuchElementException("leftValue projection does not exist")
        }
    }

    fun right(): R {
        return when (this) {
            is Right -> rightValue
            else ->
                throw NoSuchElementException("rightValue projection does not exist")
        }
    }
}

data class Right<L, out R>(val rightValue: R) : Either<L, R>()
data class Left<L, out R>(val leftValue: L) : Either<L, R>()