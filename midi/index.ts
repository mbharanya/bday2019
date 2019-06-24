import * as MidiWriter from 'midi-writer-js'

// Start with a new track
var track = new MidiWriter.Track();

// // Define an instrument (optional):
track.addEvent(new MidiWriter.ProgramChangeEvent({instrument : 121}));

const link: string = "Jean-Baptiste Bernus".toUpperCase();

let listOfNotes: string[] = [];
for (const letter of ["C", "D", "E", "F", "G", "A", "B"]) {
    for (let i = 0; i <= 8; i++) {
        listOfNotes.push(letter + i)
    }
}
listOfNotes = listOfNotes.sort((a,b) => a.charCodeAt(1) - b.charCodeAt(1))

const textInNotes: string[] = link.split("").map((char) => {
    return listOfNotes[char.charCodeAt(0) - 32]
})

console.log(textInNotes)


textInNotes
.map(pitch => new MidiWriter.NoteEvent({ pitch: [pitch], duration: '4' }))
.forEach(note => track.addEvent(note))

var write = new MidiWriter.Writer(track);
write.saveMIDI("test");