import java.io._

object wav extends App{
  try { // Open the wav file specified as the first argument
    val wavFile = WavFile.openWavFile(new File("8k16bitpcm.wav"))
    // Display information about the wav file
    wavFile.display
    // Get the number of audio channels in the wav file
    val numChannels = wavFile.getNumChannels
    // Create a buffer of 100 frames
    val buffer = new Array[Double](100 * numChannels)
    var framesRead = 0
    var min = Double.MaxValue
    var max = Double.MinValue
    do { // Read frames into buffer
      framesRead = wavFile.readFrames(buffer, 100)
      // Loop through frames and look for minimum and maximum value
      var s = 0
      while ( {
        s < framesRead * numChannels
      }) {
        if (buffer(s) > max) max = buffer(s)
        if (buffer(s) < min) min = buffer(s)

        {
          s += 1; s - 1
        }
      }
    } while ( {
      framesRead != 0
    })
    // Close the wavFile
    wavFile.close
    // Output the minimum and maximum value
    printf("Min: %f, Max: %f\n", min, max)
  } catch {
    case e: Exception =>
      println(e)
  }
}