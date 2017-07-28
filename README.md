#shapeless-applied

shapeless-applied is a tiny library for doing typelevel computations on literal types. It's intended to be used with
[shapeless](https://github.com/milessabin/shapeless), so it includes that as a dependency.

## Usage

In contrast to [singleton-ops](https://github.com/fthomas/singleton-ops), shapeless-applied doesn't provide any
pre-defined type functions. Instead, it defines a single (for now) general-purpose type function, `Applied`.

`Applied` lets you apply an arbitrary function to a literal type. The caveat is that your function must be an object
(for similar reasons as the same constraint on `Poly`) and it has to be annotated with `@literalFn` (so the macro that
materializes `Applied` can recover the AST of the implementation). Unfortunately, this results in a little bit of
repetitive boilerplate, as you'll see below - this could be alleviated with macro annotations, but I didn't want to
require the paradise plugin in order to use this.

Here's an example of usage (you can see examples for other types in the test):

```scala
import applied._
import shapeless.Witness

@literalFn((s: String) => """(?:^|_)([a-z])""".r.replaceAllIn(s, m => m.group(1).toUpperCase))
object camelCase extends (String => String) {
  def apply(s: String): String = """(?:^|_)([a-z])""".r.replaceAllIn(s, m => m.group(1).toUpperCase)
}

def camelize[S <: Symbol, CS <: Symbol](s: Witness.Lt[S])(implicit
  applied: Applied.Aux[camelCase.type, S, CS],
  result: Witness.Aux[CS]
): Symbol = result.value

camelize('smorgas_bord) shouldEqual 'SmorgasBord
```

Here, we're performing camel casing on a singleton symbol type *at compile time* and getting the result back as a
singleton symbol type. You could imagine using this, for example, to transform labels from `LabelledGeneric` to
mangle them for external service interop.

As you can see, we had to repeat the function body in that annotation so that we could get it out. This is because the
actual implementation isn't compiled yet when we go to use it, and so we need the tree around so we can evaluate it
inline. This also means you can't use any closures or non-stable values in there. But it's still pretty useful!

You can also do computations on other constant types: `String`, `Boolean`, `Byte`, `Short`, `Int`, `Long`, `Float`, and `Double`.

## Future

This is just a tiny library I whipped up in a few minutes. It's limited by:

* Functions must be `A => A`, i.e. you can't go `String => Float` even though they're both constant types. There's no
  reason for this limitation; it would only take a few minutes to overcome it if there's interest.
* Only unary functions are supported. Again, this could be easily overcome; it's just a question of creating the API.

The macro machinery that does the work is pretty straightforward (the whole library is only 84 lines of code), so go
check out how it works!