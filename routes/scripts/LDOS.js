console.log("LDOS");
let processCount = 0;
let tabCount = 0;
const chromeid = document.getElementById("chrome");
const terminal = document.getElementById("terminal");
const searchBar = document.getElementById("searchbar");
const inputBar = document.getElementById("inputBar");
const profileButton = document.getElementById("profilebutton");
let processes = [];
const apps = [
  "terminal",
  "photos",
  "videos",
  "file explorer",
  "chrome",
  "discord",
];

window.addEventListener("keydown", (event) => {
  if (event.ctrlKey && event.code === "Space") {
    if (searchBar.style.display !== "flex") {
      openSearchBar();
    } else {
      closeSearchBar(searchBar);
    }
  } else if (searchBar.style.display === "flex" && event.key === "Escape") {
    closeSearchBar(searchBar);
  } else if (event.ctrlKey && event.altKey) {
    openTerminal(terminal.cloneNode(true));
  } else if (event.ctrlKey && event.key === "l") {
    event.preventDefault();
    focusRight();
  } else if (event.ctrlKey && event.key === "h") {
    event.preventDefault();
    focusLeft();
  } else if (event.ctrlKey && event.key === "b") {
    event.preventDefault();
    openChrome(chromeid.cloneNode(true));
  }
});

profileButton.addEventListener("click", () => {
  openSearchBar();
});

inputBar.addEventListener("input", function (e) {
  let searchInput = inputBar.value;
  let regex = new RegExp(searchInput, "i");
});
inputBar.addEventListener("keydown", function (event) {
  if (event.key === "Enter") {
    interpretSearchValue(inputBar.value.toLowerCase());
    inputBar.value = "";
    closeSearchBar(searchBar);
  }
});

function focusRight() {
  if (processes.length < 2) return;
  for (let i = 0; i < processes.length; i++) {
    if (
      processes[i] === document.activeElement.parentElement.id &&
      i !== processes.length - 1
    ) {
      document.getElementById(processes[i + 1]).querySelector("input").focus();
    }
  }
}

function focusLeft() {
  if (processes.length < 2) return;
  for (let i = 0; i < processes.length; i++) {
    if (processes[i] === document.activeElement.parentElement.id && i !== 0) {
      document.getElementById(processes[i - 1]).querySelector("input").focus();
    }
  }
}

async function interpretSearchValue(value) {
  await sleep(750);
  if (value === "terminal") {
    openTerminal(terminal.cloneNode(true));
  } else if (value === "chrome") {
    openChrome(chromeid.cloneNode(true));
  }
}

function openChrome(chromeInstance) {
  chromeInstance.id = "process" + processCount++;
  chromeInstance.style.position = "relative";
  chromeInstance.style.backgroundColor = "rgba(25,26,37,0.7)";
  chromeInstance.style.borderRadius = "6px";
  chromeInstance.style.border = "2px solid white";
  chromeInstance.style.color = "white";
  chromeInstance.style.zIndex = "2";
  chromeInstance.style.margin = "3px";
  document.body.appendChild(chromeInstance);
  processes.push(chromeInstance.id);
  tileProcess(document.getElementById(chromeInstance.id));
  chromeInstance.querySelector("iframe").style.width = "99%";
  chromeInstance.querySelector("iframe").style.height = "93%";
  chromeInstance.querySelector("iframe").focus();
  openTab(chromeInstance);
  chromeInstance.querySelector("input").addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
      chromeInstance.querySelector("iframe").src = "https://" +
        chromeInstance.querySelector("input").value;
      chromeInstance.querySelector("input").placeholder =
        chromeInstance.querySelector("input").value;
      chromeInstance.querySelector("input").value = "";
    }
  });
}

function openTab(chromeInstance) {
  let tabs = chromeInstance.querySelector("#tabs");
  console.log(tabs);
  let tabButton = document.createElement("button");
  tabButton.id = "tabcount" + tabCount;
  tabButton.textContent = "tab";
  console.log(tabButton);
  console.log(tabButton.textContent);
  tabButton.style.width = "50px";
  tabButton.style.height = "90%";
  tabButton.style.backgroundColor = "rgba(76, 80, 98, .5)";
  tabButton.style.borderRadius = "30px";
  tabButton.style.color = "white";
  tabs.appendChild(tabButton);
}

function openTerminal(terminalInstance) {
  terminalInstance.id = "process" + processCount++;
  terminalInstance.style.position = "relative";
  terminalInstance.style.backgroundColor = "rgba(25,26,37,0.7)";
  terminalInstance.style.borderRadius = "6px";
  terminalInstance.style.border = "2px solid white";
  terminalInstance.style.color = "white";
  terminalInstance.style.zIndex = "2";
  terminalInstance.style.margin = "3px";
  document.body.appendChild(terminalInstance);
  processes.push(terminalInstance.id);
  tileProcess(document.getElementById(terminalInstance.id));
  const terminalText = document.getElementById(terminalInstance.id)
    .querySelector("input");
  terminalText.focus();
  terminalText.addEventListener("input", (event) => {
  });
  terminalText.addEventListener("keydown", (enter) => {
    if (enter.key === "Enter") {
      interpretTerminalCommand(
        document.getElementById(terminalInstance.id),
        terminalText.value,
        terminalInstance.querySelector("#terminaloutput"),
      );
      terminalText.value = "";
    } else if (enter.ctrlKey && enter.key === "0") {
      enter.preventDefault();
      //fadeOutBar(document.getElementById(terminalInstance.id));
      closeTerminal(document.getElementById(terminalInstance.id));
    }
  });

  terminalInstance.addEventListener("click", () => {
    terminalText.focus();
  });
  terminalText.addEventListener("focus", () => {
    document.getElementById(terminalInstance.id).style.border =
      "2px solid white";
  });
  terminalText.addEventListener("blur", () => {
    document.getElementById(terminalInstance.id).style.border =
      "1px solid transparent";
  });
}

function closeTerminal(id) {
  processes = processes.filter((item) => item !== id.id);
  fadeOutBar(id);
  const focus = document.getElementById(processes[processes.length - 1]);
  focus.querySelector("input").focus();
  console.log(focus);
  console.log(id);
}

function cd(directory) {
  if (directory === "..") {
    if (currentDirectory.name !== "root") {
      currentDirectory = currentDirectory.parent;
    }
  } else if (directory.split("/" > 1)) {
  }
}

function ls(directory) {
  if (directory === ".." && currentDirectory.name !== "root") {
    console.log(currentDirectory.parent);
  } else if (directory.split("/") > 1) {
    //handle multiple files
  }
}

function sleep(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

function interpretTerminalCommand(process, command, output) {
  let ps1 = document.getElementById("ps1");
  let clone = ps1.cloneNode(true);
  output.appendChild(clone);
  if (command === "hi") {
    commandOutput("hello", output);
  } else if (command === "clear") {
    output.innerHTML = "";
  } else if (command === "whoami") {
    commandOutput("Guestuser", output);
  } else if (command === "clock") {
    clockMethod(process, output);
  } else if (command === "neofetch") {
    neofetch(process, output);
  } else if (command.split(" ")[0] === "wallpaper") {
    const walpcmd = command.split(" ");
    if (walpcmd.length === 1) {
      commandOutput("1, 2, 3, 4, 5, 6, rainbow, cpu, 80", output);
    } else {
      document.body.style.backgroundImage = "url('./images/" + walpcmd[1] +
        ".jpg')";
      commandOutput("wallpaper updated.", output);
    }
  } else {
    commandOutput("Unknown command", output);
  }
}

function neofetch(process, output) {
  const logo = `
                   -\`
                  .o+\`
                 \`ooo/
                \`+oooo:
               \`+oooooo:                 OS: <span style='color:white;'> LDOS x86_64</span>
               -+oooooo+:                Host: <span style='color:white;'>RGSTROG Laptop 12</span>
             \`/:-:++oooo+:               Kernel: <span style='color:white;'>DOM Vkernel</span>
            \`/++++/+++++++:              Uptime: <span style='color:white;'>143 hours, 43 minutes</span>
           \`/++++++++++++++:             Packages: <span style='color:white;'>3 (Pseudo Apt)</span>
          \`/+++ooooooooooooo/\`           Shell: <span style='color:white;'>Lash 0.0.1</span>
         ./ooosssso++osssssso+\`          Resolution: <span style='color:white;'>1920x1080</span>
        .oossssso-\`\`\`\`/ossssss+\`         WM: <span style='color:white;'>LD Window Manager [Version 1.0]</span>
       -osssssso.      :ssssssso.        Terminal: <span style='color:white;'>LD Terminal [Version 2.0]</span>
      :osssssss/        osssso+++.       GPU: <span style='color:white;'>Intel TigerLake-H GT1 [UHD Graphics]</span>
     /ossssssss/        +ssssooo/-       GPU: <span style='color:white;'>Geforce RTX 4090 TI</span>
   \`/ossssso+/:-        -:/+osssso+-     Memory: <span style='color:white;'>4596Mib / 16000 Mb </span>
  \`+sso+:-\`                 \`.-/+oso:
 \`++:.                           \`-/+/
 .\`                                 \`
`;
  console.log(logo);
  //commandOutput(logo, process);
  output.innerHTML += "<div style='color:lightblue; font-size:9px;'>" + logo +
    "</div>";
}

function commandOutput(outputs, process) {
  process.innerHTML += "<br>" + outputs + "<br>";
}

function clockMethod(process, output) {
  output.innerHTML = "";
  //output.style.fontFamily = "ttyclock";
  //output.style.fontSize = "50px";
  //output.innerHTML = "5:57";
  const clock = document.createElement("div");
  clock.style.position = "absolute";
  clock.style.top = "50%";
  clock.style.transform = "translate(-50%, -50%)";
  clock.style.left = "50%";
  clock.textContent = getTime();
  clock.style.fontFamily = "ttyclock";
  clock.style.fontSize = "100px";
  process.appendChild(clock);
}

function getTime() {
  let date = new Date();
  let hours = date.getHours();
  if (hours > 12) hours -= 12;
  let minutes = date.getMinutes();
  if (minutes < 10) minutes = "0" + minutes;
  return hours + ":" + "" + minutes;
}

function tileProcess(process) {
  process.style.display = "block";
  let width = 0;
  let height = 0;
  let getBigger = setInterval(function () {
    if (width > window.innerWidth - 21 || height > window.innerHeight - 21) {
      clearInterval(getBigger);
      process.style.width = window.innerWidth - 20 + "px";
      process.style.height = window.innerHeight - 60 + "px";
    } else {
      process.style.width = width + "px";
      process.style.height = height + "px";
      width += 20;
      height += 20;
    }
  }, 3);
}

function openSearchBar() {
  searchBar.style.display = "flex";
  searchBar.style.animationPlayState = "running";
  inputBar.focus();
}

function closeSearchBar(element) {
  fadeOutBar(element);
}

function fadeOutBar(element) {
  let opacity = 0.7;
  let fadeOut = setInterval(function () {
    if (opacity <= 0) {
      clearInterval(fadeOut);
      element.style.display = "none";
      element.style.opacity = "1";
    } else {
      opacity -= 0.01;
      element.style.opacity = opacity;
    }
  }, 10);
}
