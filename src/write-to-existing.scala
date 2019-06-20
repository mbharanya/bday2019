import java.io.File

import scala.collection.immutable

object writeToExisting extends App {
  val text: immutable.Seq[Boolean] =
    """
      |import java.io.File
      |
      |object writeToExisting extends App {
      |  val wavFile = WavFile.openWavFile(new File("test.wav"))
      |  val wavToWrite = WavFile.newWavFile(new File("8k16bitpcm-edited.wav"), wavFile.getNumChannels, wavFile.getNumFrames, wavFile.getValidBits, wavFile.getSampleRate)
      |
      |  // Get the number of audio channels in the wav file
      |  val numChannels = wavFile.getNumChannels
      |  // Create a buffer of 100 frames
      |  val buffer = new Array[Double](100 * numChannels)
      |  var framesRead = 0
      |  var min = Double.MaxValue
      |  var max = Double.MinValue
      |
      |  var i = 0
      |  do { // Read frames into buffer
      |    framesRead = wavFile.readFrames(buffer, 100)
      |
      |//    val modifiedBuffer: Array[Double] = buffer.zipWithIndex.map {
      |//      case (value, frameCounter) => value + Math.sin(2.0 * Math.PI * 10000 * frameCounter / 44100)
      |//    }
      |
      |    val modifiedBuffer: Array[Double] = buffer.map(_ + 10000)
      |
      |    val remaining = wavFile.getFramesRemaining
      |    val toWrite: Int = if (remaining > 100) 100 else remaining.toInt
      |
      |    wavToWrite.writeFrames(modifiedBuffer, 0, toWrite)
      |    i += 100
      |  } while ( {
      |    framesRead != 0
      |  })
      |  // Close the wavFile
      |  wavToWrite.close()
      |  wavFile.close()
      |}
    """.stripMargin.map(_.toByte).flatMap(_.toBinaryString.map{
      case '0' => false
      case _ => true
    })

  val wavFile = WavFile.openWavFile(new File("8k16bitpcm.wav"))
  wavFile.display()
  val wavToWrite = WavFile.newWavFile(new File("8k16bitpcm-edited.wav"), wavFile.getNumChannels, wavFile.getNumFrames, wavFile.getValidBits, wavFile.getSampleRate)
  wavToWrite.display()


  // Get the number of audio channels in the wav file
  val numChannels = wavFile.getNumChannels
  // Create a buffer of 100 frames
  val buffer = new Array[Double](100 * numChannels)
  var framesRead = 0

  do { // Read frames into buffer
    framesRead = wavFile.readFrames(buffer, 100)

    val modifiedBuffer: Array[Double] = buffer.zipWithIndex.map {
      case (value, frameCounter) => {
        if(text(frameCounter))
          0.5 * value
        else
          0.2 * value
      }
    }

    wavToWrite.writeFrames(modifiedBuffer, framesRead)
  } while ( {
    framesRead != 0
  })
  // Close the wavFile
  wavToWrite.close()
  wavFile.close()
}