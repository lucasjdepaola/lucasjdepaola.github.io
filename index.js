const gid = (string) => { return document.getElementById(string) } // abstraction
const gcl = (string) => { return document.querySelector(string) }
const input = gid("input")
const output = gid("output");
const cursor = gid("cursor");
const prmpt = gcl(".prompt");
const mainterminal = gid("mainterminal");
let id = 0;
let activeId = false;

const TEXTSPEED = 25;
const introduction = "Lucas DePaola 2024``Type 'commands' for all listed commands."
slowText(introduction);


document.addEventListener("keydown", (key) => { // terminal listener
  updateCursor("input");
  input.textContent += key.key.length > 1 ? "" : key.key;
  updateCursor("input");
  if (key.key === "Enter") {
    interpretText(input.textContent);
    input.textContent = "";
  }
  else if (key.key === "Escape") input.textContent = "";
  else if (key.ctrlKey && key.key === "l") { event.preventDefault(); input.textContent = "" }
  else if (key.ctrlKey && key.key === "Backspace") ctrlBack();
  else if (key.key === "Backspace") input.textContent = input.textContent.slice(0, -1); updateCursor("input");
});

function interpretText(string) {
  string = string.trim();
  if (string === "test") slowText("this is a test");
  else if (string === "c" || string === "clear") clear();
  else slowText("unknown command");
  return "";
}

function updateCursor(boundelement) {
  const el = document.getElementById(boundelement);
  const coords = el.getBoundingClientRect();
  cursor.style.left = coords.right + "px";
  cursor.style.top = coords.top + "px";
  cursor.style.height = coords.height + "px";
}

const clear = () => { output.innerHTML = "" }

function slowText(text) {
  if (activeId) return;
  activeId = true;
  let i = 0;
  id = setInterval(() => {
    if (text[i] === "`") { output.innerHTML += "<br>"; i++ }
    else { output.innerHTML += text[i++]; }
    updateCursor("input");
    if (i > text.length - 1) { output.innerHTML += "<br><br>"; updateCursor("input"); clearInterval(id); activeId = false }
  }, TEXTSPEED);
}

function ctrlBack() {
  let count = 0;
  for (let i = input.textContent.length; i >= 0; i--) {
    if (input.textContent[i] === " ") {
      input.textContent = input.textContent.slice(0, count * -1);
      return;
    } else {
      count++;
    }
  }
  input.textContent = "";
}
