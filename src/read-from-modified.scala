import java.io.File

object readFromModified extends App {
  val wavFile = WavFile.openWavFile(new File("8k16bitpcm-edited.wav"))
  wavFile.display()


  // Get the number of audio channels in the wav file
  val numChannels = wavFile.getNumChannels
  // Create a buffer of 100 frames
  val buffer = new Array[Double](100 * numChannels)
  var framesRead = 0
  var min = Double.MaxValue
  var max = Double.MinValue

  var i = 0
  do { // Read frames into buffer
    framesRead = wavFile.readFrames(buffer, 100)

    buffer.zipWithIndex.map {
      case (value, frameCounter) => {
        value
//        println(value + text(frameCounter).toDouble / 1000)
//        value + text(frameCounter).toDouble / 1000
      }
    }

//    val modifiedBuffer: Array[Double] = buffer.map(_ + 10000)

    val remaining = wavFile.getFramesRemaining

  } while ( {
    framesRead != 0
  })
  // Close the wavFile
  wavFile.close()
}