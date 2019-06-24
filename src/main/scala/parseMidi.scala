package main.scala

import java.io.File

import javax.sound.midi.{MidiSystem, ShortMessage}

import scala.collection.immutable


object Test extends App {
  val NOTE_ON = 0x90
  val NOTE_OFF = 0x80
  val NOTE_NAMES = Array("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")


  val listOfNotes: immutable.Seq[(String, Int)] = (for (letter <- List("C", "D", "E", "F", "G", "A", "B"))
    yield for (i <- 0 to 8)
      yield letter + i).flatten.sortWith(_.charAt(1) < _.charAt(1)).zipWithIndex


  val sequence = MidiSystem.getSequence(new File("midi/jb.mid"))
  var trackNumber = 0
  for (track <- sequence.getTracks) {
    trackNumber += 1
    var i = 0
    while ( {
      i < track.size
    }) {
      val event = track.get(i)
      val message = event.getMessage
      if (message.isInstanceOf[ShortMessage]) {
        val sm = message.asInstanceOf[ShortMessage]
        if (sm.getCommand == NOTE_ON) {
          val key = sm.getData1
          val octave = (key / 12) - 1
          val note = key % 12
          val noteName = NOTE_NAMES(note)

          listOfNotes.find(_._1 == noteName + octave).map(_._2 + 32).map(_.toChar).foreach(print)
        }
      }
      i += 1;
    }
  }

}