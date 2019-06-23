import { Midi } from '@tonejs/midi'
var parseMidi = require('midi-file').parseMidi
import * as fs from "fs";

async function read() {
    // load a midi file in the browser
    var input = fs.readFileSync('test.midi.mid')
    var parsed = parseMidi(input)
    
    console.log(parsed)

    // const midi = new Midi(parsed)

    // //the file name decoded from the first track
    // const name = midi.name
    // //get the tracks
    // midi.tracks.forEach(track => {
    //     //tracks have notes and controlChanges

    //     //notes are an array
    //     const notes = track.notes

    //     console.log(notes.map(n => n.name))
        
    // })

}
read().then()