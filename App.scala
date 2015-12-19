package littleprograms

import littleprograms._

object App {
  def main(args: Array[String]) {
    val user = args.headOption.getOrElse("beraboris")
    OptionExample.run(user)
  }
}
