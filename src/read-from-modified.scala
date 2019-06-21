import java.io.File
import util.control.Breaks._

object readFromModified extends App {
  val edited = WavFile.openWavFile(new File("epicsaxguy-edited.wav"))
  val orig = WavFile.openWavFile(new File("epicsaxguy.wav"))

  edited.display()


  // Get the number of audio channels in the wav file
  val numChannels = edited.getNumChannels

  val bufferSize = 50 * 8

  val bufferOg = new Array[Double](bufferSize * numChannels)
  val bufferEdited = new Array[Double](bufferSize * numChannels)

  var framesRead = 0
  var origFramesRead = 0
  breakable {
    do { // Read frames into buffer

      origFramesRead = orig.readFrames(bufferOg, bufferSize)
      framesRead = edited.readFrames(bufferEdited, bufferSize)

      val groups: List[Array[Double]] = bufferEdited.zipWithIndex.map {
        case (value, index) => {
          Math.abs(value - bufferOg(index))
          value
        }
      }.grouped(50).toList

//      bufferEdited.foreach(print)
//      break



      val sum = groups.map {
        _.reduce(_ + _)
      }.map(_ / 50).map(_ > 0.0001)
        .map(b => if (b) "1" else "0").reduce(_ + _).take(8).reverse

      val letter = (Integer.parseInt(sum, 2)).toChar

      //      .tapEach(_.reduce((_ + _))).tapEach(_.map(_ > 0.1E-14)).toList
      //    print(sum)
      //
          print(letter)
      //


      //      .reduce(_ + _) > 0.1E-14


      //    val modifiedBuffer: Array[Double] = buffer.map(_ + 10000)

    } while ( {
      framesRead != 0
    })
  }
  // Close the wavFile
  edited.close()
}