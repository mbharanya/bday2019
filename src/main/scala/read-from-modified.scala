import java.io.File

object readFromModified extends App {
  val edited = WavFile.openWavFile(new File("epicsaxguy-edited.wav"))
  val orig = WavFile.openWavFile(new File("epicsaxguy.wav"))

    edited.display()

  val numChannels = edited.getNumChannels

  val bufferSize = 10 * 8

  val bufferOg: Array[Array[Double]] = Array.ofDim(2, bufferSize)
  val bufferEdited: Array[Array[Double]] = Array.ofDim(2, bufferSize)

  var framesRead = 0
  var origFramesRead = 0

  do { // Read frames into buffer

    origFramesRead = orig.readFrames(bufferOg, bufferSize)
    framesRead = edited.readFrames(bufferEdited, bufferSize)

    val groups: List[Array[Double]] = bufferEdited(0).zipWithIndex.map {
      case (value, index) => {
        Math.abs(value - bufferOg(0)(index))
      }
    }.grouped(10).toList


    val sum = groups.map {
      _.reduce(_ + _)
    }.map(_ / 10).map(_ > 0.0001)
      .map(b => if (b) "1" else "0").reduce(_ + _).take(8)

    val letter = (Integer.parseInt(sum, 2)).toChar

    if (letter > 0) print(letter)

  } while ( {
    framesRead != 0
  })

  // Close the wavFile
  edited.close()
}