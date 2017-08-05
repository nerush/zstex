package com.zstex

import org.tensorflow._

object Main extends App {

  autoClose(new Graph) {
    case g => {
      val version = s"Hello TensorFlow: ${TensorFlow.version}"

      autoClose(Tensor.create(version.getBytes("UTF-8"))) {
        case t => {
          g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build()
        }
      }

      autoClose(new Session(g)) {
        case s => autoClose(s.runner().fetch("MyConst").run().get(0)) {
          case output => println(new String(output.bytesValue(), "UTF-8"))
        }
      }

    }
  }

  def autoClose[A <: AutoCloseable, B](closable: A)(f: A => B): Unit = {
    try {
      f(closable)
    } finally {
      closable.close()
    }
  }

}
