import java.io.File

import scala.collection.immutable

object writeToExisting extends App {

  val listOfBin: Seq[String] =  "ha".map(_.toByte).map(_.toBinaryString).map("0000000" + _).map(_.takeRight(8))


  val text=
    listOfBin.flatMap(_.toCharArray).map {
      case '0' => false
      case _ => true
    }

  println("ha".map(_.toByte).map(_.toBinaryString))




  val wavFile = WavFile.openWavFile(new File("epicsaxguy.wav"))
  wavFile.display()
  val wavToWrite = WavFile.newWavFile(new File("epicsaxguy-edited.wav"), wavFile.getNumChannels, wavFile.getNumFrames, wavFile.getValidBits, wavFile.getSampleRate)
  wavToWrite.display()

  val duration = wavFile.getNumFrames / wavFile.getSampleRate
  println("duration: " + duration)


  // Get the number of audio channels in the wav file
  val numChannels = wavFile.getNumChannels
  // Create a buffer of 100 frames
  val buffer = new Array[Double](100 * numChannels)
  var framesRead = 0
  val hz = 400

  var workedOnFrames = 0

  do { // Read frames into buffer
    framesRead = wavFile.readFrames(buffer, 100)
    workedOnFrames += framesRead
    val modifiedBuffer: Array[Double] = buffer.zipWithIndex.map {
      case (value, frameCounter) => {
        val meep = (workedOnFrames + frameCounter) / 50
        if (meep < text.length && text(
          meep
        ))
          0.1 * Math.sin(2.0 * Math.PI * hz * frameCounter / wavFile.getSampleRate) + 0
        else
          0
      }
    }

    wavToWrite.writeFrames(modifiedBuffer, framesRead)
  } while ( {
    framesRead != 0
  })

  println("wrote filde3")
  // Close the wavFile

  wavToWrite.close()
  wavFile.close()
}