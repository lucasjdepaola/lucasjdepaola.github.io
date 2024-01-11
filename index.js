const gid = (string) => {
  return document.getElementById(string);
}; // abstraction
const gcl = (string) => {
  return document.querySelector(string);
};
const input = gid("input");
const output = gid("output");
console.log = (str) => {
  if (str === undefined) return;
  output.innerText += str + "\n";
};
const cursor = gid("cursor");
const prmpt = gcl(".prompt");
const mainterminal = gid("mainterminal");
const throwaway = gid("throwaway");
const file = gid("file");
const mobileInput = gid("mobileinput");
let cmpstr = "";
let editBool = false;
let fzfBool = false;
let id = 0;
let sudo = false; // if you really want to change this manually, I'm not stopping you.
const vvvvvvvvvvvv = "void";
let activeId = false;
const commandBuffer = [];
let bufferIndex = 0;
let whoami = "Guest";

const TEXTSPEED = 25;
lbl("Lucas DePaola 2024");
const introduction =
  // "Lucas DePaola 2024`Type 'commands' for all listed commands.";
  "Type 'commands' for all listed commands.";
slowText(introduction);

class File {
  constructor(name, type, contents) {
    this.name = name;
    this.type = type;
    this.contents = contents;
  }
}

class Directory {
  constructor(name, contents, prev) {
    this.name = name;
    this.contents = contents;
    this.prev = prev;
  }
}

function initFileSystem() {
  const rootDir = new Directory("root", null, null);
  const test =
    "You seem to have figured your way around this website, can your terminal skills find more within?";
  rootDir.contents = [
    new File("null.txt", "txt", test),
    // new File("script.js", "js", "console.log('hi')"),
    new Directory("documentation", [
      new File(
        "doc1.doc",
        "doc",
        (() => {
          let st =
            "You can use this terminal to do almost anything a regular terminal would do.`";
          st +=
            "You can view all the functions of this program in the ../scripts directory.`";
          st +=
            "to run and execute scripts, use 'node' like you would a javascript runtime.";
          return st;
        })(),
      ),
    ], null),
    new Directory("private", [
      new File(
        "secret.txt",
        "txt",
        sudo
          ? "github password: lucas100!77`network password: shinyelement75`instagram password: lucas!!77lucas"
          : "You need sudo permission to access this files contents.",
      ),
    ], null),
    new Directory("scripts", [], null),
    new Directory("aboutme", [
      new File("information.txt", "txt", "some information about me."),
    ], null),
  ];
  return rootDir;
}
const root = initFileSystem();
let currentDir = root;

const keyDownFunction = (key) => { // terminal listener
  updateCursor("input");
  scrollDown();
  if (editBool) return;
  input.textContent += key.key.length > 1 ? "" : key.key;
  updateCursor("input");
  if (key.key === "Enter") {
    interpretText(input.textContent);
    input.textContent = "";
  } else if (key.key === "Escape") input.textContent = "";
  else if (key.ctrlKey && key.key === "l") {
    event.preventDefault();
    input.textContent = "";
    output.textContent = "";
  } else if (key.ctrlKey && key.key === "Backspace") ctrlBack();
  else if (key.key === "Backspace") {
    input.textContent = input.textContent.slice(0, -1);
  } else if (key.key === "ArrowUp") {
    if (bufferIndex > commandBuffer.length) bufferIndex--;
    const len = commandBuffer.length - 1 - bufferIndex;
    input.textContent = commandBuffer[len];
    bufferIndex++;
  } else if (key.key === "ArrowDown") {
    if (bufferIndex < 0) bufferIndex++;
    const len = commandBuffer.length - 1 - bufferIndex;
    input.textContent = commandBuffer[len];
    bufferIndex--;
  } else if (key.ctrlKey && key.key === "a") {
    event.preventDefault();
    input.innerText = "";
  } else if (key.key === "Tab") {
    event.preventDefault();
    autoCompleteCommand();
  }
  updateCursor("input");
};

document.addEventListener("keydown", keyDownFunction);

document.addEventListener("touchstart", () => {
  mobileInput.focus();
});

// mobileInput.addEventListener("keydown", keyDownFunction);

window.addEventListener("resize", () => {
  updateCursor(); // TODO: have the resize update work properly, event doesn't capture it
});

function interpretText(string) {
  if (string === "") return;
  if (string.includes(";")) {
    const splt = string.split(";");
    for (s of splt) interpretText(s);
  } else if (string.includes("|")) {
    const splt = string.split("|");
    if (string.includes("fzf")) {
      fzfBool = true;
      const o = interpretText(splt[0]).split("`");
      initfzf(o);
    } else if (string.includes("grep")) {
      const o = interpretText(splt[0]).split("`");
      grep(splt[1].split(" ")[2], o);
    }
  }
  if (!fzfBool) clonePrompt();
  commandBuffer.push(string);
  string = string.trim();
  string = string.toLowerCase();
  string = string.replaceAll("./", "");
  const ss = string.split(" ");
  if (string === "test") slowText("this is a test");
  else if (string === "c" || string === "clear") clear();
  else if (string === "commands") {
    let commands = "c: clear screen`ls: list contents of the current directory";
    commands += "`cd: change directory`cat: display contents of a file";
    commands +=
      "`mkdir: create a directory`touch: create a file`edit: edit a file";
    commands +=
      "`whoami: display user (change via cmd 'script whoami=\"USER\"')";
    commands += "`weather: display local weather information.";
    slowText(commands);
  } else if (string === "ls") return ls();
  else if (ss[0] === "cat") return cat(ss[1]);
  else if (ss[0] === "cd" || ss[0] === "d") cd(ss[1]);
  else if (ss[0] === "mkdir") mkdir(ss[1]);
  else if (ss[0] === "touch") touch(ss[1]);
  else if (ss[0] === "edit" || ss[0] === "e") edit(ss[1]);
  else if (ss[0] === "node") node(ss[1]);
  else if (ss[0] === "exec") exec(ss[1]);
  else if (ss[0] === "echo") echo(ss[1]);
  else if (string === "ip") ip();
  else if (string === "b") cd("..");
  else if (string === "whoami") slowText(whoami);
  else if (ss[0] === "script") {
    interpretScript(string.slice(6, string.length));
  } else if (string === "weather") weather();
  else if (ss[0] === "mv") {
    mv(ss[1], ss[2]);
  } else if (ss[0] === "lbl") lbl(ss[1]);
  else slowText("unknown command");
  return "";
}

function node(fileName) {
  let code = "";
  for (const element of currentDir.contents) {
    if (element.name === fileName) {
      code += element.contents;
    }
  }
  try {
    const result = eval(code);
    // slowText(result);
    console.log(result);
  } catch (error) {
    console.log("error " + error.message);
  }
  if (code === "") slowText("could not find file");
}

function interpretScript(script) {
  try {
    const result = eval(script);
    // slowText(result);
    console.log(result);
  } catch (error) {
    console.log("error " + error.message);
  }
}

function autoCompleteCommand() {
  const outputArr = input.innerHTML.split(" ");
  if (outputArr.length <= 1) {
    for (e of functions) {
      if (textaligns(e.name, outputArr[0])) {
        input.innerHTML = e.name;
        cmpstr = e.name;
      }
    }
  } else {
    for (e of currentDir.contents) {
      if (textaligns(e.name, outputArr[1])) {
        input.innerHTML = outputArr[0] + " ./" + e.name;
        cmpstr = e.name;
      }
    }
  }
}

function textaligns(elementName, input) {
  for (let i = 0; i < input.length; i++) {
    if (elementName[i] !== input[i]) return false;
  }
  return true;
}

function echo(somestr) {
  slowText(somestr.replaceAll('"', ""));
}

function mv(str1, str2) {
  for (e of currentDir.contents) {
    if (e.name === str1) {
      e.name = str2;
      return;
    }
  }
  slowText("File not found.");
}

function ls() {
  if (currentDir.contents === null) return;
  let string = "";
  for (element of currentDir.contents) {
    string += element.name;
    string += element instanceof Directory ? " (Directory)`" : "`";
  }
  return slowText(string);
}

function ip() {
  fetch("https://httpbin.org/ip").then((response) => response.json()).then((
    data,
  ) => slowText(data.origin));
}

async function weather() {
  try {
    const response = await fetch("https://wttr.in");
    const text = await response.text();
    output.innerHTML += text;
    updateCursor();
    scrollDown();
  } catch (error) {
    console.log(error);
  }
}

function exec(scriptName) {
  if (currentDir.contents === null) return;
  for (element of currentDir.contents) {
    if (element.name === scriptName) {
      const contentArr = element.contents.split(/\r?\n/);
      for (const ct of contentArr) {
        interpretText(ct);
      }
    }
  }
  slowText("Could not find file.");
}

function cat(fileName) {
  for (const element of currentDir.contents) {
    if (element.name === fileName) {
      slowText(element.contents);
    }
  }
}

function cd(dirName) {
  dirName = dirName.replaceAll("./", "");
  if (dirName === "..") {
    currentDir = currentDir.prev;
    file.innerText = "~" +
      (currentDir.name === "root" ? "" : "/" + currentDir.name);
    return;
  }
  for (const element of currentDir.contents) {
    if (element instanceof Directory && element.name === dirName) {
      element.prev = currentDir;
      currentDir = element;
      file.innerText += "/" + currentDir.name;
      return;
    }
  }
  slowText(
    "Directory not found, run 'ls' to find the current files/directories.",
  );
}

function initfzf(arr) {
  const el = document.createElement("span");
  el.id = "fzf";
  el.innerText = arr.join("\r");
  throwaway.appendChild(el);

  function keydownFzf(key) {
    el.innerText = "";
    if (key.key === "Enter") {
      document.removeEventListener("keydown", keydownFzf);
      throwaway.removeChild(el);
      fzfBool = false;
      return fzf(input.innerText, arr);
    } else if (key.ctrlKey && key.key === "c") {
      document.removeEventListener("keydown", keydownFzf);
      throwaway.removeChild(el);
      fzfBool = false;
    } else {
      // el.innerText = fzf(input.innerText, arr);
      for (const z of fzf(input.innerText, arr)) el.innerText += z + "\r";
    }
    updateCursor();
  }
  document.addEventListener("keydown", keydownFzf);
}

function fzf(string, arr) {
  if (string === "") return arr;
  const a = [];
  let num = 0;
  let cache = 0;
  let flag = false;
  for (const e of arr) {
    let str = string;
    for (s of string) {
      num = e.indexOf(s);
      if (num === -1 || num < cache) {
        flag = true;
      } else {
        str = str.replace(s, "");
      }
    }
    if (!flag) a.push(e);
    flag = false;
  }
  return a;
}

function grep(string, arr) {
  const a = [];
  const reg = new RegExp(string);
  for (e of arr) {
    if (reg.test(e)) {
      a.push(e);
    }
  }
  return a;
}

function lbl(string) {
  const d = document.createElement("div");
  d.style.animation = "scrollText 5s linear infinite";
  d.innerText = string;
  output.appendChild(d);
}

function vim() {
  let state = "insert";
  let vimRow = 0;
  let vimColumn = 0;

  function vimKeyDown(key) {
    if (state === "insert") {
      insertState(key);
    } else if (state === "normal") {
      normalState(key);
    } else if (state === "command") {
      commandState(key);
    } else if (state === "visual") {
      visualState(key);
    }
    renderVimCursor();
  }
  function insertState(key) {
    if (key.key === "Escape") {
      state = "normal";
    } else if (key.key === "Enter") {
      appendRow();
    } else if (key.key.length <= 1 && !key.ctrlKey) {
      txt.innerText += key.key;
      insertText(key.key);
    }
  }
  const insertText = (text) => {
    const s = document.createElement("span");
    s.innerText = text;
    txt.children.item(vimRow).insertBefore(
      text,
      txt.children.item(vimRow).children.item(vimColumn),
    );
  };
  function normalState(key) {
    if (key.key === "i" && !key.ctrlKey) {
      state = "insert";
    } else if (key.key === ";" || key.key === ":") {
      state = "command";
    } else if (key.key === "v") {
      state = "visual";
    } else if (key.key === "V") {
      state = "visual";
      //highlight total;
    } else if (key.key === "j") {
      vimRow--;
    } else if (key.key === "k") {
      vimRow++;
    } else if (key.key === "h") {
      vimColumn--;
    } else if (key.key === "l") {
      vimColumn++;
    }
  }
  function commandState(key) {
    if (key.key === "Enter") {
      interpretVimCommand(cmd.innerText);
    } else if (key.key.length <= 1) {
      cmd.innerText += key.key;
    }
  }
  function visualState(key) {
    if (key.key === "Escape") {
      state = "normal";
    } else if (key.key === "v") {
      state = "normal";
    }
  }
  function renderVimCursor(spanPos) {
    const pos = spanPos.getBoundingClientRect();
    if (state === "normal") {
    } else if (state === "insert") {
    }
  }
  function appendRow() {
    const rowAppend = document.createElement("span");
    txt.insertBefore(txt.children.item(vimRow++));
  }
  function initText(fileName) {
    let f;
    if (currentDir.contents !== null) {
      for (const element of currentDir.contents) {
        if (element.name === fileName) {
          fileContents += element.contents;
          f = element;
        }
      }
    }
    for (c of f.contents) {
      txt.innerHTML += "<span>" + c + "</span>";
    }
  }
  const typeBox = document.createElement("div");
  const txt = document.createElement("div");
  const cmd = document.createElement("div");
  const crsr = document.createElement("div");
  typeBox.id = "vim";
  throwaway.appendChild(typeBox);
  typeBox.appendChild(txt);
  typeBox.appendChild(cmd);
  typeBox.appendChild(crsr);
  document.addEventListener("keydown", vimKeyDown);
}

function typeTest() {
  function testKeyDown(key) {
  }
  const typeBox = document.createElement("div");
  typeBox.id = "typetest";
  throwaway.appendChild(typeBox);
  document.addEventListener("keydown", testKeyDown);
}

function mkdir(dirName) {
  for (const element of currentDir.contents) {
    if (dirName === element.name) {
      slowText("Cannot create directory, name already exists.");
      return;
    }
  }
  currentDir.contents.push(new Directory(dirName, null, currentDir));
  slowText("Directory: " + dirName + " created.");
}

function edit(fileName) {
  const outputbuffer = output.innerHTML;
  output.innerHTML = "";
  editBool = true;
  let fileContents = "";
  let f;
  if (currentDir.contents !== null) {
    for (const element of currentDir.contents) {
      if (element.name === fileName) {
        fileContents += element.contents;
        f = element;
      }
    }
  }
  const box = document.createElement("textarea");
  box.value = fileContents;
  const c = mainterminal.getBoundingClientRect();
  box.id = "edit";
  box.style.backgroundColor = "black";
  box.style.color = "white";
  box.style.width = (c.width - 40) + "px";
  box.style.height = (c.height - 40) + "px";
  box.style.backgroundColor = "rgb(1,1,1,0.2)";
  box.style.fontSize = "15px";
  throwaway.appendChild(box);
  box.focus();
  box.addEventListener("keydown", (key) => {
    if (key.ctrlKey && key.key === "s") {
      event.preventDefault();
      if (f !== undefined) f.contents = box.value;
      else {
        const fn = fileName === undefined ? "untitled.txt" : fileName;
        currentDir.contents.push(
          new File(fn, fn.split(".")[1], box.value),
        );
      }
      throwaway.removeChild(box);
      editBool = false;
      output.innerHTML = outputbuffer;
      updateCursor();
    } else if (key.key === "Tab") event.preventDefault();
  });
}

function clone() {
  const cl = mainterminal.cloneNode(true);
  cl.style.marginLeft = "5px";
  document.body.appendChild(cl);
}

function touch(fileName) {
  if (currentDir.contents === null) {
    currentDir.contents = [new File(fileName, fileName.split(".")[1], "")];
    return;
  }
  for (const element of currentDir.contents) {
    if (element.name === fileName) {
      slowText("Cannot create file, name already exists.");
    }
  }
  currentDir.contents.push(new File(fileName, fileName.split(".")[1], ""));
  slowText("File: " + fileName + " created.");
}

function updateCursor(boundelement) {
  const el = document.getElementById(boundelement);
  const coords = el.getBoundingClientRect();
  cursor.style.left = coords.right + "px";
  cursor.style.top = coords.top + "px";
  cursor.style.height = coords.height + "px";
}

const clear = () => {
  if (activeId) {
    clearInterval(id);
    activeId = false;
  }
  output.innerHTML = "";
};

function slowText(text) {
  if (activeId) return;
  if (fzfBool) return text;
  activeId = true;
  if (text === undefined) return;
  let i = 0;
  id = setInterval(() => {
    if (text[i] === "`") {
      output.innerHTML += "<br>";
      i++;
    } else output.innerHTML += text[i++];
    updateCursor("input");
    scrollDown();
    if (i > text.length - 1) {
      output.innerHTML += "<br><br>";
      updateCursor("input");
      clearInterval(id);
      activeId = false;
      scrollDown();
    }
  }, TEXTSPEED);
}

function clonePrompt() {
  const p = prmpt.cloneNode(true);
  // const foo = document.getElementById("test");
  // foo.querySelector;
  const nput = p.querySelector("#input");
  const cr = p.querySelector("#cursor");
  cr.style.display = "none";
  nput.id = "foo";
  output.appendChild(p);
}

function scrollDown() {
  mainterminal.scrollTop = mainterminal.scrollHeight;
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

function writeFunctions() {
  for (const elemnt of currentDir.contents) {
    if (elemnt.name === "scripts" && elemnt instanceof Directory) {
      for (const f of functions) {
        elemnt.contents.push(new File(f.name + ".js", "js", f));
      }
      elemnt.contents.push(
        new File("whoami.js", ".js", "//whoami = 'YOURUSERHERE';"),
      );
    }
  }
}

function fnListener(fn, timeInSeconds) {
  setInterval(fn, timeInSeconds * 1000);
}

function sudoListener() {
  if (sudo) {
    slowText("Administrative permissions granted."); // User has changed a boolean value, can now access the mainframe.
  }
}
// fnListener(sudoListener, 1);

function logFunctions() {
  const functions = [];
  for (const i in window) {
    if (
      (typeof window[i]).toString() == "function" &&
      window[i].toString().indexOf("native") == -1
    ) {
      functions.push(window[i]);
    }
  }
  return functions;
}
const functions = logFunctions();
writeFunctions();
