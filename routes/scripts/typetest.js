const test = document.getElementById("typetest");
let prevTime = 0;
let prevWpm = 0;
const text = document.getElementById("text");
const words = "the be of and a to in he have it that for they with as not on she at by this we you do but from or which one would all will there say who make when can more if no man out other so what time up go about than into could state only new year some take come these know see use get like then first any work now may such give over think most even find day also after way many must look before great back through long where much should well people down own just because good each those feel seem how high too place little world very still nation hand old life tell write become here show house both between need mean call develop under last right move thing general school never same another begin while number part turn real leave might want point form off child few small since against ask late home interest large person end open public follow during present without again hold govern around possible head consider word program problem however lead system set order eye plan run keep face fact group play stand increase early course change help line "
let wordlen = 25;
const cursor = document.getElementById("cursor");
let typeIndex = 0;
let startTime;
let start = false;
let wpmlen = 0;
let singleton = false;
cursor.style.border = "1px solid black";

document.addEventListener("keydown", (key) => {
  if (!start) {
    startTime = new Date;
    startTime = startTime.getTime();
    start = true;
  }
  if (typeIndex === text.children.length - 2) {
    if (singleton) return;
    singleton = true;
    const stop = new Date();
    document.getElementById("wpm").textContent = "Your wpm is: " + calculateWPM(stop);
    changeCursor(typeIndex);
    return;
  }
  if (key.key === text.children[typeIndex].textContent) {
    typeIndex++;
    changeCursor(typeIndex);
    smoothwpm();
  }
});

function calculateWPM(stop) {
  console.log(stop.getTime() + " " + startTime);
  return Math.round((wpmlen / ((stop.getTime() - startTime) / 1000) * 60) / 5);
}

function restartTest() {
  text.innerHTML = "";
  initializeTest(wordlen);
}

function initializeTest(length) {
  let data = words.split(" ");
  let arr = [];
  for (let i = 0; i < length; i++) {
    arr.push(data[generateRandomNumber(199)]); // generate length amount of random words, for the test
  }

  for (let j = 0; j < length; j++) {
    for (let k = 0; k < arr[j].length; k++) {
      const spn = document.createElement("span");
      spn.textContent = arr[j][k];
      text.appendChild(spn);
    }
    const space = document.createElement("span");
    space.textContent = " ";
    text.appendChild(space);
    wpmlen += arr[j].length + 1;
  }
  cursor.style.left = changeCursor(0);
  return arr;
}
const testWords = initializeTest(wordlen);

function generateRandomNumber(max) { // get random number for english words.
  return Math.ceil(Math.random() * max);
}

function changeCursor(index) {
  const item = text.children[index].getBoundingClientRect();
  cursor.style.width = item.width + "px";
  cursor.style.height = item.height + "px";
  cursor.style.left = item.left + "px";
  cursor.style.top = item.top + "px";
}

function smoothwpm() {
  console.log("hello");
  const date = Date.now();
  const diff = date - startTime;
  // const wpm = Math.round((diff / 1000) / 5 * 60);
  const wpm = Math.round(6000 / diff);
  console.log(wpm + "diff");
  if (prevTime === 0) { prevTime = date; return; }
  prevTime = date;
  document.getElementById("wpm").innerText = wpm;
  prevWpm = wpm;
  return wpm;
}
