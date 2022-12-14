package lectures.part3concurrency

import java.util.concurrent.Executors

object Intro extends App {

  /*
      interface Runnable {
        public void run()
      }
   */
  //JVM threads
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel")
  })

  aThread.start() // gives the signal to the JVM to start a JVM thread
  // create a JVM thread => OS thread

  val runnable = new Runnable {
    override def run(): Unit = println("Running in parallel")
  }
  val newThread = new Thread(runnable)
  runnable.run() // doesn't do anything

  aThread.join() // blocks until aThread finishes running
  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  threadHello.start()
  threadGoodbye.start()
  // different runs produce different results!

  //executors
  //Thread is expensive to create and kill so we use the pool
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })

  pool.shutdown()
  //  pool.execute(() => println("shoud not appear")) // throws an exception in the calling thread

  //  pool.shutdownNow()
  println(pool.isShutdown) // true
}
