package applied

import org.scalatest.{FlatSpec, FreeSpec, Matchers}
import shapeless.Witness

class AppliedSpec extends FreeSpec with Matchers {

  "Symbols" - {
    "addFoo" in {
      def result[In <: Symbol, Out <: Symbol](in: Witness.Lt[In])(implicit
        applied: Applied.Aux[addFoo.type, In, Out],
        witnessResult: Witness.Aux[Out]
      ): Symbol = witnessResult.value

      result('blah) shouldEqual 'blahFoo
    }

    "camelCase" in {
      def result[In <: Symbol, Out <: Symbol](in: Witness.Lt[In])(implicit
        applied: Applied.Aux[camelCase.type, In, Out],
        witnessResult: Witness.Aux[Out]
      ): Symbol = witnessResult.value

      result('smorgas_bord) shouldEqual 'SmorgasBord
    }
  }

  "Strings" - {
    "addFoo" in {
      def result[In <: String, Out <: String](in: Witness.Lt[In])(implicit
        applied: Applied.Aux[addFoo.type, In, Out],
        witnessResult: Witness.Aux[Out]
      ): String = witnessResult.value

      result("blah") shouldEqual "blahFoo"
    }

    "camelCase" in {
      def result[In <: String, Out <: String](in: Witness.Lt[In])(implicit
        applied: Applied.Aux[camelCase.type, In, Out],
        witnessResult: Witness.Aux[Out]
      ): String = witnessResult.value

      result("smorgas_bord") shouldEqual "SmorgasBord"
    }
  }


  "Ints" in {
    def result[In <: Int, Out <: Int](in: Witness.Lt[In])(implicit
      applied: Applied.Aux[numDigits.type, In, Out],
      witnessResult: Witness.Aux[Out]
    ): Int = witnessResult.value

    result(123) shouldEqual 3
  }

  "Booleans" in {
    def result[In <: Boolean, Out <: Boolean](in: Witness.Lt[In])(implicit
      applied: Applied.Aux[notb.type, In, Out],
      witnessResult: Witness.Aux[Out]
    ): Boolean = witnessResult.value

    result(true) shouldEqual false
    result(false) shouldEqual true
  }

  "Floats" in {
    def result[In <: Float, Out <: Float](in: Witness.Lt[In])(implicit
      applied: Applied.Aux[timesTwoPointTwo.type, In, Out],
      witnessResult: Witness.Aux[Out]
    ): Float = witnessResult.value

    result(10.10f) shouldEqual (10.10f * 2.2f)
  }

  "Doubles" in {
    def result[In <: Double, Out <: Double](in: Witness.Lt[In])(implicit
      applied: Applied.Aux[exp.type, In, Out],
      witnessResult: Witness.Aux[Out]
    ): Double = witnessResult.value

    result(10.10) shouldEqual math.exp(10.10)
  }

}

@literalFn((s: String) => s + "Foo")
object addFoo extends (String => String) {
  def apply(s: String): String = s + "Foo"
}

@literalFn((s: String) => """(?:^|_)([a-z])""".r.replaceAllIn(s, m => m.group(1).toUpperCase))
object camelCase extends (String => String) {
  def apply(s: String): String = """(?:^|_)([a-z])""".r.replaceAllIn(s, m => m.group(1).toUpperCase)
}

@literalFn((i: Int) => i.toString.length)
object numDigits extends (Int => Int) {
  def apply(i: Int): Int = i.toString.length
}

@literalFn((b: Boolean) => !b)
object notb extends (Boolean => Boolean) {
  def apply(b: Boolean): Boolean = !b
}

@literalFn((f: Float) => f * 2.2f)
object timesTwoPointTwo extends (Float => Float) {
  def apply(f: Float): Float = f * 2.2f
}

@literalFn((d: Double) => math.exp(d))
object exp extends (Double => Double) {
  def apply(d: Double): Double = math.exp(d)
}