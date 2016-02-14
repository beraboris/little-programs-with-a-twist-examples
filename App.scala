package littleprograms

import littleprograms._

object App {
  def main(args: Array[String]) {
    val user = args.headOption.getOrElse("dotboris")
    OptionExample.run(user)
    EitherExample.run(user)
    FutureExample.run(user)
  }
}
