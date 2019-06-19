import java.io.File

object writeToExisting extends App {
  val wavFile = WavFile.openWavFile(new File("8k16bitpcm.wav"))
  val wavToWrite = WavFile.newWavFile(new File("8k16bitpcm-edited.wav"), wavFile.getNumChannels, wavFile.getNumFrames, wavFile.getValidBits, wavFile.getSampleRate)

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
    // Loop through frames and look for minimum and maximum value

    val modifiedBuffer: Array[Double] = buffer.map(Math.sin(_) * Math.PI)
    val remaining = wavFile.getFramesRemaining
    val toWrite: Int = if (remaining > 100) 100 else remaining.toInt

    wavToWrite.writeFrames(modifiedBuffer, 0, toWrite)
    i += 100
  } while ( {
    framesRead != 0
  })
  // Close the wavFile
  wavToWrite.close()
  wavFile.close()
}