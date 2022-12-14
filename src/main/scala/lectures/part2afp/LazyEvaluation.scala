package lectures.part2afp

object LazyEvaluation extends App {

  // lazy DELAYS the evaluation of values
  lazy val x: Int = {
    println("hello")
    42
  }
  println(x)
  println(x)

  // examples of implications:
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  // Since simpleCondition already false and there is no need to evaluate the next one (lazy)
  println(if (simpleCondition && lazyCondition) "yes" else "no")
  println(if (!simpleCondition && lazyCondition) "yes_recheck" else "no_recheck")

  // in conjunction with call by name
  def byNameMethod(n: => Int): Int = n + n + n + 2

  // Use lazy val because its evaluated once
  // Waiting only happen once
  def byNameMethodLazy(n: => Int): Int = {
    lazy val t = n
    t + t + t + 1
  }

  def retrieveMagicValue = {
    //side effect or a long computation
    Thread.sleep(1000)
    println("waiting")
    42
  }

  println(byNameMethod(retrieveMagicValue))
  println(byNameMethodLazy(retrieveMagicValue))
  // use lazy vals

  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30) // lazy vals under the hood
  val gt20Lazy = lt30Lazy.withFilter(greaterThan20)
  println()
  println(gt20Lazy)
  println()
  // gt20Lazy is an expression that will lazily evaluate number.
  // It will evaluate the number together, if the first condition failed eg 40, it will skip and save
  // processing time
  gt20Lazy.foreach(println)

  // for-comprehensions use withFilter with guards
  for {
    a <- List(1, 2, 3) if a % 2 == 0 // use lazy vals !
  } yield a + 1
  List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int]

  /*
      Exercise: implement a lazily evaluated singly linked STREAM of elements.
      STREAM - The head is always evaluated then the tail is evaluated on demand
      naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potential infinite!)
      naturals.take(100).foreach(println) // lazily evaluated stream of first 100 naturals (finite stream)
      naturals.foreach(println) // will crash - infinite!
      naturals.map(_ * 2) // stream of all even numbers ( potentially infinite)
   */

  abstract class MyStream[+A] {
    def isEmpty: Boolean

    def head: A

    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] // prepend operator

    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams

    def foreach(f: A => Unit): Unit

    def map[B](f: A => B): MyStream[B]

    def flatMap[B](f: A => MyStream[B]): MyStream[B]

    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes the first n elements out of this stream

    def takeAsList(n: Int): List[A]
  }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }
  
}
