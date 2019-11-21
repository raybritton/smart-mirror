package app.raybritton.smartmirror.ext


import io.reactivex.*
import io.reactivex.functions.BiFunction

//Usage

/**
 * observableFromPair(view.textChanges(), view.editorActions())
 * ....
 *
 * or
 *
 * Observable.zip(view.flowable(), view.editorActions(), BiFunctionPairer())
 * ....
 */

class BiFunctionPairer<I1, I2> : BiFunction<I1, I2, Pair<I1, I2>> {
    override fun apply(t1: I1, t2: I2): Pair<I1, I2> {
        return t1 to t2
    }
}

fun <I1, I2> singleFromPair(input1: Single<I1>, input2: Single<I2>): Single<Pair<I1, I2>> {
    return  Single.zip(input1, input2, BiFunctionPairer())
}

fun <I1, I2> observableFromPair(input1: Observable<I1>, input2: Observable<I2>): Observable<Pair<I1, I2>> {
    return  Observable.zip(input1, input2, BiFunctionPairer())
}

fun <I1, I2> flowableFromPair(input1: Flowable<I1>, input2: Flowable<I2>): Flowable<Pair<I1, I2>> {
    return  Flowable.zip(input1, input2, BiFunctionPairer())
}

fun <I1, I2> maybeFromPair(input1: Maybe<I1>, input2: Maybe<I2>): Maybe<Pair<I1, I2>> {
    return  Maybe.zip(input1, input2, BiFunctionPairer())
}