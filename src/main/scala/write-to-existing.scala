import java.io.File

object writeToExisting extends App {

  val listOfBin: Seq[String] =
    """data:audio/midi;base64,TVRoZAAAAAYAAAABAIBNVHJrAAAA1gDAeQCQUUCBAIBRQACQZUCBAIBlQACQZUCBAIBlQACQX0CBAIBfQACQOUCBAIA5QACQJkCBAIAmQACQJkCBAIAmQACQbECBAIBsQACQWUCBAIBZQACQR0CBAIBHQACQJECBAIAkQACQWECBAIBYQACQU0CBAIBTQACQJkCBAIAmQACQSECBAIBIQACQR0CBAIBHQACQSkCBAIBKQACQRUCBAIBFQACQbkCBAIBuQACQK0CBAIArQACQKECBAIAoQACQKUCBAIApQACQN0CBAIA3QAD/LwA=
      |
      |["C", "D", "E", "F", "G", "A", "B"]
      |32
    """.stripMargin
  .map(_.toByte).map(_.toBinaryString).map("0000000" + _).map(_.takeRight(8))


  val text=
    listOfBin.flatMap(_.toCharArray).map {
      case '0' => false
      case _ => true
    }

  println(listOfBin)


  val wavFile = WavFile.openWavFile(new File("epicsaxguy.wav"))
  wavFile.display()
  val wavToWrite = WavFile.newWavFile(new File("epicsaxguy-edited.wav"), wavFile.getNumChannels, wavFile.getNumFrames, wavFile.getValidBits, wavFile.getSampleRate)
  wavToWrite.display()

  val duration = wavFile.getNumFrames / wavFile.getSampleRate
  println("duration: " + duration)


  // Get the number of audio channels in the wav file
  val numChannels = wavFile.getNumChannels
  // Create a buffer of 100 frames
  val buffer: Array[Array[Double]] = Array.ofDim(2, 100)
  var framesRead = 0
  val hz = 20000

  var workedOnFrames = 0

  do { // Read frames into buffer
    framesRead = wavFile.readFrames(buffer, 100)
    val modifiedBuffer: Array[Double] = buffer(0).zipWithIndex.map {
      case (value, frameCounter) => {
        val meep = (workedOnFrames + frameCounter) / 50
        val changedValue = 0.1 * Math.sin(2.0 * Math.PI * hz * frameCounter / wavFile.getSampleRate) + value

        if (meep < text.length && text(
          meep
        )) {
          if (changedValue > 1 || changedValue < -1) println(changedValue)
          changedValue
        }
        else{
          value
        }

      }
    }
    workedOnFrames += framesRead

    buffer(0) = modifiedBuffer

    wavToWrite.writeFrames(buffer, framesRead)
  } while ( {
    framesRead != 0
  })

  println("wrote file")
  // Close the wavFile

  wavToWrite.close()
  wavFile.close()
}