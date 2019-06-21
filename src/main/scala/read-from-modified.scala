import java.io.File

object readFromModified extends App {
  val edited = WavFile.openWavFile(new File("epicsaxguy-edited.wav"))
  val orig = WavFile.openWavFile(new File("epicsaxguy.wav"))

    edited.display()

  val numChannels = edited.getNumChannels

  val bufferSize = 50 * 8

  val bufferOg = new Array[Double](bufferSize * 2)
  val bufferEdited = new Array[Double](bufferSize * 1)

  var framesRead = 0
  var origFramesRead = 0

  do { // Read frames into buffer

    origFramesRead = orig.readFrames(bufferOg, bufferSize)
    framesRead = edited.readFrames(bufferEdited, bufferSize)

    val groups: List[Array[Double]] = bufferEdited.zipWithIndex.map {
      case (value, index) => {
        Math.abs(value - bufferOg(index))
      }
    }.grouped(50).toList


    val sum = groups.map {
      _.reduce(_ + _)
    }.map(_ / 50).map(_ > 0.0001)
      .map(b => if (b) "1" else "0").reduce(_ + _).take(8)

    val letter = (Integer.parseInt(sum, 2)).toChar

    print(letter)

  } while ( {
    framesRead != 0
  })

  // Close the wavFile
  edited.close()
}