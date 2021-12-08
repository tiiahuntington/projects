"use strict";
//@ts-check
// data-muuttuja on lähes sama kuin viikkotehtävässä 1.
//
let jasenetFieldset = {};
let listaTaulukko = {};
let listaustapa = "";
let table = {};

window.onload = function () {
  listaTaulukko = document.getElementsByTagName("table")[0];
  lisaaTaulukkoon();
  luoRastiLomake();
  alustaJoukkueLomake();
};

/**
 * Muokkaa xhtml tiedostossa olevaa valmista form elementtiä siihen muotoon mitä sen halutaan alustavasti olevan
 */
function alustaJoukkueLomake() {
  var jform = haeElementti("form", 1);
  jform.setAttribute("id", "lomake");
  var ekap = jform.getElementsByTagName("p")[0];
  var nimiinput = ekap.getElementsByTagName("input")[0];
  nimiinput.setAttribute("id", "joukkuenimi");
  jasenetFieldset = document.createElement("fieldset");
  var legendjasenet = document.createElement("legend");
  var jasenetnode = document.createTextNode("Jäsenet");
  ekap.after(jasenetFieldset);
  jasenetFieldset.appendChild(legendjasenet);
  legendjasenet.appendChild(jasenetnode);
  for (var i = 0; i < 2; i++) {
    var p = luoJasenInput("Jasen " + (i + 1), "j" + i);
    jasenetFieldset.appendChild(p);
  }
  var napit = jform.getElementsByTagName("button");
  napit[0].addEventListener("click", lisaaJoukkue);
  napit[0].id = "lisaaJoukkueNappi";
  napit[0].disabled = true;
  napit[1].addEventListener("click", muokkaaJoukkue);
  napit[1].setAttribute("class", "piilota");
}

/**
 * Luo p elementin sisään valmiin pohjan inputelementlle
 * @param {String} teksti labelin teksti
 * @param {String} id p elementin id
 * @returns luodun p elementin
 */
function luoJasenInput(teksti, id) {
  var p = document.createElement("p");
  p.setAttribute("id", id);
  var labelp = luoLabel(teksti);
  p.appendChild(labelp);
  p.addEventListener("input", muokkaaInput);
  return p;
}

/**
 * Luo label elementin ja sen alle yksinkertaisen teksti-input elementin
 * @param {String} teksti labelin teksti
 * @returns label elementin
 */
function luoLabel(teksti) {
  var label = document.createElement("label");
  var labelinput = document.createElement("input");
  var labeltext = document.createTextNode(teksti);
  label.appendChild(labeltext);
  labelinput.setAttribute("type", "text");
  labelinput.setAttribute("value", "");
  label.appendChild(labelinput);
  return label;
}

/**
 * Tapahtumankäsittelijä jäsen nimi inputeille
 * @param {Object} e input tapahtuma
 */
function muokkaaInput(e) {
  var i = 1;
  while (i < 100) {
    var jid = "j" + i;
    var inputit = jasenetFieldset.getElementsByTagName("input");
    var joukkuenimi = document.getElementById("joukkuenimi");
    if (
      joukkuenimi.value != "" &&
      inputit[0].value != "" && //jos tarpeelliset kentät täytetty
      inputit[1].value != ""
    ) {
      document.getElementById("lisaaJoukkueNappi").disabled = false;
    }
    if (
      e.currentTarget.id == jid &&
      jasenetFieldset.lastElementChild == e.currentTarget //lisää jos viimeistä muokataan
    ) {
      i++;
      jasenetFieldset.appendChild(luoJasenInput("Jäsen " + (i + 1), "j" + i));
      break;
    }
    if (e.currentTarget.getElementsByTagName("input")[0].value == "") {
      //jos value nollataan
      var vika = jasenetFieldset.lastElementChild;
      if (vika.id != "j1") {
        vika.remove();
        break;
      }
    }
    i++;
    if (e.currentTarget.id == "j0") {
      //jos ensimmäistä muokataan
      if (inputit[1].value != "" && inputit[2] != undefined) {
        jasenetFieldset.appendChild(luoJasenInput("Jäsen " + (i + 1), "j" + i));
        break;
      }
    }
  }
}

/**
 * Lisaa joukkue - napin tapahtumankäsittelijä. Luo uuden joukkueen tietojen pohjalta ja lisää sen tietorakenteeseen
 * @param {Object} e click eventti
 */
function lisaaJoukkue(e) {
  e.preventDefault();
  var jasenet = jasenetFieldset.getElementsByTagName("input");
  var nimiInput = document.getElementById("joukkuenimi");
  var nimi = nimiInput.value;
  nimiInput.value = "";
  if (nimi == "") {
    return;
  }
  if (jasenet[0].value == "" || jasenet[1].value == "") {
    return;
  }
  var jasenNimet = [];
  for (var i = 0; i < jasenet.length - 1; i++) {
    var n = jasenet[i].value;
    jasenNimet.push(n);
    jasenet[i].value = "";
  }
  var uusiJoukkue = luoJoukkue(nimi, jasenNimet);
  data.joukkueet.push(uusiJoukkue);
  listaaJoukkueet(listaustapa);
  tyhjennaJoukkueLomake();
}

/**
 * Tallenna muutokset - napin tapahtumankäsittelijä. Muokkaa annettujen tietojen pohjalta joukkueen tietoja
 * @param {Object} e click eventti
 */
function muokkaaJoukkue(e) {
  e.preventDefault();
  var form = haeElementti("form", 1);
  var inputit = form.getElementsByTagName("input");
  var joukkue = inputit[0].joukkue;
  if (inputit[0].value != joukkue.nimi) {
    joukkue.nimi = inputit[0].value;
    if (joukkue.nimi == "") {
      poistaJoukkue(joukkue);
    }
  }
  for (var i = 0; i < inputit.length - 1; i++) {
    if (inputit[i + 1].value != joukkue.jasenet[i]) {
      if (inputit[i + 1].value == "") {
        if (joukkue.jasenet[i] != undefined) {
          joukkue.jasenet.pop();
          break;
        }
        break;
      }
      joukkue.jasenet[i] = inputit[i + 1].value;
    }
  }
  listaaJoukkueet(listaustapa);
  for (i = 0; i < inputit.length; i++) {
    inputit[i].value = "";
  }
  var napit = form.getElementsByTagName("button");
  napit[0].setAttribute("class", "");
  napit[1].setAttribute("class", "piilota");
  tyhjennaJoukkueLomake();
  var vanhatable = document.getElementById("leimaukset");
  if (vanhatable != null) {
    vanhatable.remove();
  }
}

/**
 * Korjaa joukkue lomakkeen inputien määrän
 */
function tyhjennaJoukkueLomake() {
  var form = document.getElementsByTagName("form")[1];
  var event = document.createEvent("Event");
  event.initEvent("input", true, true);
  var pt = form.getElementsByTagName("p");
  var inputit = form.getElementsByTagName("input");
  for (var input of inputit) {
    input.value = "";
  }
  while (pt.length > 5) {
    pt[1].dispatchEvent(event);
  }
}

/**
 * Tapahtumankäsittelijä joukkueen nimen klikkaukselle. Alustaa lomakkeen joukkueen tiedoilla, tietojen muokkausta varten
 * @param {Object} e a elementin klikkauksen tapahtuma
 */
function muokkaaJoukkueTila(e) {
  e.preventDefault();
  var a = e.target;
  var joukkue = haeJoukkueIdlla(a.jid);
  alustaLeimaukset(joukkue);
  var form = haeElementti("form", 1);
  var inputit = form.getElementsByTagName("input");
  if (inputit.length > 3) {
    tyhjennaJoukkueLomake();
  }
  inputit[0].value = joukkue.nimi;
  inputit[0].joukkue = joukkue;
  var pt = form.getElementsByTagName("p");
  for (var i = 0; i < joukkue.jasenet.length; i++) {
    inputit[i + 1].value = joukkue.jasenet[i];
    var event = document.createEvent("Event");
    event.initEvent("input", true, true);
    pt[i + 1].dispatchEvent(event);
  }
  var napit = form.getElementsByTagName("button");
  napit[0].setAttribute("class", "piilota");
  napit[1].setAttribute("class", "");
}

/**
 * Hakee ja palauttaa joukkueobjektin senid:n perusteella
 * @param {Number} id haettavan joukkueen id
 * @returns joukkueen
 */
function haeJoukkueIdlla(id) {
  for (var joukkue of data.joukkueet) {
    if (joukkue.id === id) {
      return joukkue;
    }
  }
}

/**
 * Luo joukkueen annettujen tietojen pohjalta
 * @param {String} nimi joukkueen nimi
 * @param {Object} jasenet joukkueen jäsenet taulukossa
 * @returns luodun joukkueen
 */
function luoJoukkue(nimi, jasenet) {
  var joukkue = {
    nimi: nimi,
    id: uusiId("joukkueet"),
    sarja: 123456,
    jasenet: jasenet,
    rastit: [],
    leimaustapa: [0],
  };
  return joukkue;
}

/**
 * Lisaa annettuun html taulukkoelementtiin data tietorakenteen joukkueiden sarjan ja nimen halutussa järjestyksessä
 */
function lisaaTaulukkoon() {
  var ekarivi = haeElementti("tr", 0); //eka rivi
  var psarake = document.createElement("th"); //otsikko sarake
  ekarivi.appendChild(psarake);
  var potsikko = document.createTextNode("Pisteet"); //teksti
  psarake.appendChild(potsikko);
  var tht = ekarivi.getElementsByTagName("th");
  tht[0].tapa = "sarja";
  tht[0].addEventListener("click", lajittelu);
  tht[1].tapa = "joukkue";
  tht[1].addEventListener("click", lajittelu);
  tht[2].tapa = "pisteet";
  tht[2].addEventListener("click", lajittelu);
  listaaJoukkueet();
}

/**
 * Tapahtumankäisttelijä otsikoille
 */
function lajittelu(e) {
  listaustapa = e.target.tapa;
  listaaJoukkueet(listaustapa);
}

/**
 * Luo joukkueen rivin tietoineen, lajittelee ja lisaa taulukkoon
 */
function listaaJoukkueet(tapa) {
  tyhjenna();
  if (tapa == undefined || tapa.length < 1) {
    tapa = "sarja";
  }
  let jarjestamattomat = [];
  for (var joukkue of data.joukkueet) {
    var jasenetjono = "";
    var pituus = joukkue.jasenet.length;
    for (var i = 0; i < pituus; i++) {
      jasenetjono = jasenetjono + joukkue.jasenet[i] + ", ";
    }
    jasenetjono = jasenetjono.substring(0, jasenetjono.length - 2);
    var rastitJarjestyksessa = lajitteleRastit(joukkue);
    jarjestamattomat.push({
      sarja: haeSarja(joukkue.sarja),
      nimi: joukkue.nimi.trim(),
      jasenet: jasenetjono,
      pisteet: laskePisteet(rastitJarjestyksessa, 0, false),
      id: joukkue.id,
    });
  }
  var jarjestetyt = [];
  switch (tapa) {
    case "sarja":
      jarjestetyt = sortSarja(jarjestamattomat);
      break;
    case "joukkue":
      jarjestetyt = sortJoukkue(jarjestamattomat);
      break;
    case "pisteet":
      jarjestetyt = sortPisteet(jarjestamattomat);
      break;
  }
  for (var j of jarjestetyt) {
    var rivi = document.createElement("tr"); //rivi
    listaTaulukko.appendChild(rivi);
    var s1 = document.createElement("td"); //1. sarake, sarja
    rivi.appendChild(s1);
    var sarja = document.createTextNode(j.sarja);
    s1.appendChild(sarja);
    var s2 = document.createElement("td"); // 2. saarake, nimi
    rivi.appendChild(s2);
    var a = document.createElement("a");
    a.jid = j.id;
    a.addEventListener("mouseup", muokkaaJoukkueTila);
    s2.appendChild(a);
    a.setAttribute("href", "#lomake");
    var joukkuenimi = document.createTextNode(j.nimi);
    a.appendChild(joukkuenimi);
    s2.appendChild(br());
    var nimet = document.createTextNode(j.jasenet);
    s2.appendChild(nimet);
    var s3 = document.createElement("td"); // 3. sarake pisteet
    rivi.appendChild(s3);
    var piste = document.createTextNode(j.pisteet);
    s3.appendChild(piste);
  }
}

/**
 * Alustaa leimaukset taulukon
 */
function alustaLeimaukset(joukkue) {
  var vanhatable = document.getElementById("leimaukset");
  if (vanhatable != null) {
    vanhatable.remove();
  }
  table = document.createElement("table");
  table.setAttribute("id", "leimaukset");
  var lomake = document.getElementById("lomake");
  lomake.parentNode.appendChild(table);
  var otsikko = document.createElement("caption");
  table.appendChild(otsikko);
  var otsikkoteksti = document.createTextNode("Leimaukset");
  otsikko.appendChild(otsikkoteksti);
  var ekarivi = document.createElement("tr");
  table.appendChild(ekarivi);
  var es1 = document.createElement("th");
  var es2 = document.createElement("th");
  var es3 = document.createElement("th");
  ekarivi.appendChild(es1);
  ekarivi.appendChild(es2);
  ekarivi.appendChild(es3);
  var teksti1 = document.createTextNode("Rastin koodi");
  var teksti2 = document.createTextNode("Leimausaika");
  var teksti3 = document.createTextNode("");
  es1.appendChild(teksti1);
  es2.appendChild(teksti2);
  es3.appendChild(teksti3);
  listaaLeimaukset(joukkue);

  var rivi = document.createElement("tr");
  rivi.joukkue = joukkue;
  table.appendChild(rivi);
  var s1 = document.createElement("th");
  var s2 = document.createElement("th");
  var s3 = document.createElement("th");
  rivi.appendChild(s1);
  rivi.appendChild(s2);
  rivi.appendChild(s3);
  var span = document.createElement("span");
  var select = document.createElement("select");
  select.setAttribute("name", "rastit");
  select.setAttribute("id", "rastivalikko");
  lisaaSelectRastit(select);
  s1.appendChild(span);
  s1.appendChild(select);
  var spanteksti = document.createTextNode("Lisää rasti");
  span.appendChild(spanteksti);
  select.addEventListener("input", leimausMuokkaus);

  var span2 = document.createElement("span");
  var input2 = document.createElement("input");
  s2.appendChild(span2);
  s2.appendChild(input2);
  var spanteksti2 = document.createTextNode("Aika");
  span2.appendChild(spanteksti2);
  input2.type = "text";
  input2.value = "";
  input2.addEventListener("input", leimausMuokkaus);

  var tallenna = document.createElement("button");
  tallenna.disabled = true;
  s3.appendChild(tallenna);
  var tekstiT = document.createTextNode("Tallenna");
  tallenna.appendChild(tekstiT);
  tallenna.addEventListener("click", tallennaUusiRasti);
}

/**
 * Listaa joukkueen leimaukset
 * @param {Object} joukkue
 */
function listaaLeimaukset(joukkue) {
  var rastit = huonotPois(joukkue);
  for (var rasti of rastit) {
    var rivi = document.createElement("tr");
    rivi.rasti = rasti;
    rivi.joukkue = joukkue;
    table.appendChild(rivi);
    var s1 = document.createElement("th");
    var s2 = document.createElement("th");
    var s3 = document.createElement("th");
    rivi.appendChild(s1);
    rivi.appendChild(s2);
    rivi.appendChild(s3);
    var span = document.createElement("span");
    var input = document.createElement("input");
    s1.appendChild(span);
    s1.appendChild(input);
    var spanteksti = document.createTextNode("Rasti");
    span.appendChild(spanteksti);
    input.type = "text";
    if (rasti.koodi == undefined) {
      input.value = rasti.koodi2;
    } else {
      input.value = rasti.koodi;
    }
    input.addEventListener("input", leimausMuokkaus);

    var span2 = document.createElement("span");
    var input2 = document.createElement("input");
    s2.appendChild(span2);
    s2.appendChild(input2);
    var spanteksti2 = document.createTextNode("Aika");
    span2.appendChild(spanteksti2);
    input2.type = "text";
    input2.value = rasti.aika;
    input2.addEventListener("input", leimausMuokkaus);

    var poista = document.createElement("button");
    var tallenna = document.createElement("button");
    tallenna.disabled = true;
    s3.appendChild(poista);
    s3.appendChild(tallenna);
    var tekstiP = document.createTextNode("Poista");
    var tekstiT = document.createTextNode("Tallenna");
    poista.appendChild(tekstiP);
    tallenna.appendChild(tekstiT);
    poista.addEventListener("click", poistaRasti);
    tallenna.addEventListener("click", tallennaRasti);
  }
}

/**
 * Lisää select valikkoon rastit
 * @param {Object} s select valikko
 */
function lisaaSelectRastit(s) {
  for (var rasti of data.rastit) {
    var o = document.createElement("option");
    s.appendChild(o);
    var oTeksti = document.createTextNode(rasti.koodi);
    o.rasti = rasti;
    o.appendChild(oTeksti);
  }
}

/**
 * Karsii joukkueiden rastileimauksista pois ne jotka eivät ole oikeita rasteja
 */
function huonotPois(joukkue) {
  var rastit = [];
  for (var rasti of joukkue.rastit) {
    var id = parseInt(rasti.rasti);
    if (!isNaN(id)) {
      if (id > 0) {
        var r = etsiRastiIdlla(id);
        rasti.koodi2 = r.koodi;
        rastit.push(rasti);
      }
    }
  }
  return rastit;
}

/**
 * Tapahtumankäsittelijä leimaustaulukon inputelementille. Aktivoi tallenna napin.
 * @param {Object} e input tapahtuma
 */
function leimausMuokkaus(e) {
  var tr = e.target.parentNode.parentNode;
  tr.getElementsByTagName("button")[0].disabled = false;
  if (tr.getElementsByTagName("button")[1] != undefined) {
    tr.getElementsByTagName("button")[1].disabled = false;
  }
}

/**
 * Poista rasti napin tapahtumankäistelijä.
 * Muuttaa joukkueen rastin koodin nollaksi jolloin sitä ei listata.
 * @param {Object} e click tapahtuma
 */
function poistaRasti(e) {
  var rasti = e.target.parentNode.parentNode.rasti;
  var joukkue = e.target.parentNode.parentNode.joukkue;
  for (var i = 0; i < joukkue.rastit.length; i++) {
    if (
      rasti.koodi == joukkue.rastit[i].koodi &&
      joukkue.rastit[i].aika == rasti.aika
    ) {
      joukkue.rastit.splice(i, 1);
    }
  }
  alustaLeimaukset(joukkue);
}

function tallennaRasti(e) {
  var inputit = e.target.parentNode.parentNode.getElementsByTagName("input");
  var rasti = e.target.parentNode.parentNode.rasti;
  var joukkue = e.target.parentNode.parentNode.joukkue;
  if (inputit[0].value != rasti.koodi || inputit[1].value != rasti.aika) {
    for (var i = 0; i < joukkue.rastit.length; i++) {
      if (
        rasti.koodi == joukkue.rastit[i].koodi &&
        joukkue.rastit[i].aika == rasti.aika
      ) {
        joukkue.rastit[i].koodi = inputit[0].value;
        joukkue.rastit[i].aika = inputit[1].value;
        break;
      }
    }
  }
  alustaLeimaukset(joukkue);
}

/**
 * Tapahtumankäisttleijä uuden rastin lisäämiselle
 * @param {Object} e click tapahtuma tallenna napista
 */
function tallennaUusiRasti(e) {
  var tr = e.target.parentNode.parentNode;
  var joukkue = tr.joukkue;
  var s = document.getElementById("rastivalikko");
  var rasti = s.options[s.selectedIndex].rasti;
  var input = tr.getElementsByTagName("input")[0];
  var aika = input.value;
  if (!tarkistaaika(aika)) {
    var label = document.createElement("label");
    var virhe = document.createTextNode("Korjaa päivämäärä!");
    label.setAttribute("class", "virhe");
    e.target.parentNode.appendChild(label);
    label.appendChild(virhe);
    return;
  }
  var uusiRasti = {
    aika: aika,
    rasti: rasti.id,
  };
  joukkue.rastit.push(uusiRasti);
  alustaLeimaukset(joukkue);
}

/**
 * Tarkistaa onko annettu aika oikeassa muodossa
 * @param {String} aika käyttäjän syöttämä aika
 */
function tarkistaaika(aika) {
  //2017-03-18 18:52:15
  if (aika.length < 18) {
    return false;
  }
  return true;
}

/**
 * Tyhjentää joukkuelistataulukon jos siinä on rivejä
 * @returns
 */
function tyhjenna() {
  var rivit = listaTaulukko.getElementsByTagName("tr");
  if (rivit.length == 1) {
    return;
  }
  for (var i = rivit.length - 1; i > 0; i--) {
    rivit[i].remove();
  }
}

function sortSarja(jarjestamattomat) {
  var jarjestetyt = jarjestamattomat.sort(function (a, b) {
    if (a.sarja < b.sarja) {
      return -1;
    }
    if (a.sarja > b.sarja) {
      return 1;
    }
    if (a.pisteet > b.pisteet) {
      return -1;
    }
    if (a.pisteet < b.pisteet) {
      return 1;
    } else {
      var animi = a.nimi.toUpperCase();
      var bnimi = b.nimi.toUpperCase();
      if (animi < bnimi) {
        return -1;
      } else {
        return 1;
      }
    }
  });
  return jarjestetyt;
}

function sortJoukkue(jarjestamattomat) {
  var jarjestetyt = jarjestamattomat.sort(function (a, b) {
    var animi = a.nimi.toUpperCase();
    var bnimi = b.nimi.toUpperCase();
    if (animi < bnimi) {
      return -1;
    } else {
      return 1;
    }
  });
  return jarjestetyt;
}

function sortPisteet(jarjestamattomat) {
  var jarjestetyt = jarjestamattomat.sort((a, b) => b.pisteet - a.pisteet);
  return jarjestetyt;
}

/**
 * Hakee data tietorakenteesta sarjan annetun id:n perusteella
 * @param {String} id
 * @returns sarjan nimen
 */
function haeSarja(id) {
  for (var sarja of data.sarjat) {
    if (id == sarja.id) {
      return sarja.nimi;
    }
  }
}

/**
 * Luo lomakkeen jolla voi lisätä tietorakenteeseen uuden rastin
 */
function luoRastiLomake() {
  let lomake = haeElementti("form", 0);
  var fieldset = document.createElement("fieldset");
  lomake.appendChild(fieldset);
  var legend = document.createElement("legend");
  fieldset.appendChild(legend);
  var legendinteksti = document.createTextNode("Rastin tiedot");
  legend.appendChild(legendinteksti);

  var label0 = document.createElement("label");
  fieldset.appendChild(label0);
  var span0 = document.createElement("span");
  label0.appendChild(span0);
  var teksti0 = document.createTextNode("Lat");
  span0.appendChild(teksti0);
  var input0 = document.createElement("input");
  input0.setAttribute("type", "text");
  input0.setAttribute("value", "");
  label0.appendChild(input0);

  var label1 = document.createElement("label");
  fieldset.appendChild(label1);
  var span1 = document.createElement("span");
  label1.appendChild(span1);
  var teksti1 = document.createTextNode("Lon");
  span1.appendChild(teksti1);
  var input1 = document.createElement("input");
  input1.setAttribute("type", "text");
  input1.setAttribute("value", "");
  label1.appendChild(input1);

  var label2 = document.createElement("label");
  fieldset.appendChild(label2);
  var span2 = document.createElement("span");
  label2.appendChild(span2);
  var teksti2 = document.createTextNode("Koodi");
  span2.appendChild(teksti2);
  var input2 = document.createElement("input");
  input2.setAttribute("type", "text");
  input2.setAttribute("value", "");
  label2.appendChild(input2);

  var nappi = document.createElement("button");
  fieldset.appendChild(nappi);
  nappi.addEventListener("click", formNappi);
  nappi.setAttribute("id", "rasti");
  var nappiteksti = document.createTextNode("Lisää rasti");
  nappi.appendChild(nappiteksti);
}

/**
 * Tapahtumankäsittelijä lisää rasti formin napille
 * @param {Object} e click tapahtuma
 */
function formNappi(e) {
  e.preventDefault();
  var lomake = haeElementti("form", 0);
  var inputit = lomake.getElementsByTagName("input");
  var lat = inputit[0].value;
  var lon = inputit[1].value;
  var koodi = inputit[2].value;
  if (onkoTietoja(lat, lon, koodi)) {
    lat = formatoi(lat);
    lon = formatoi(lon);
    luoUusiRasti(lat, lon, koodi);
  }
  inputit[0].value = "";
  inputit[1].value = "";
  inputit[2].value = "";
  tulostaRastitKonsoliin();
}

/**
 * Tulostaa olemassa olevat rastit konsoliin
 */
function tulostaRastitKonsoliin() {
  console.log("Rasti         Lat          Lon");
  var jarjestamattomat = [];
  for (var rasti of data.rastit) {
    jarjestamattomat.push(rasti);
  }
  var jarjestetyt = jarjestamattomat.sort(function (a, b) {
    if (a.koodi < b.koodi) {
      return -1;
    }
    if (a.koodi > b.koodi) {
      return 1;
    }
  });
  for (rasti of jarjestetyt) {
    console.log(rasti.koodi + "            " + rasti.lat + "    " + rasti.lon);
  }
}

/**
 * Formatoi annetun luvun samaan muotoon kuin muut pituus- ja leveyssuunnat
 * @param {String} n numero, joka halutaan formatoida
 * @returns
 */
function formatoi(n) {
  //9 --> 2.6
  if (n.length < 9) {
    var luku = parseFloat(n);
    if (luku < 10) {
      n = luku.toString() + ".";
      for (var i = 0; n.length < 8; i++) {
        n = n + "0";
      }
      return n;
    } else {
      n = luku.toString() + ".";
      for (i = 0; n.length < 9; i++) {
        n = n + "0";
      }
      return n;
    }
  }
  return n;
}

/**
 * Luo uuden rastin tietorakenteeseen
 * @param {String} lat latitude
 * @param {String} lon longitude
 * @param {String} koodi rastin koodi
 */
function luoUusiRasti(lat, lon, koodi) {
  var uusi = {
    lon: lon,
    koodi: koodi,
    lat: lat,
    id: uusiId("rastit"),
  };
  data.rastit.push(uusi);
}

/**
 * Tarkistaa halutun tietorakenteen suurimman id:n ja luo yhtä suuremman uuden id:n
 * @param {String} tietorakenne tietorakenteen nimi
 * @returns uusi uniikki id
 */
function uusiId(tietorakenne) {
  var suurin = 0;
  if (tietorakenne == "rastit") {
    for (var rasti of data.rastit) {
      if (rasti.id > suurin) {
        suurin = rasti.id;
      }
    }
    return suurin + 1;
  } else if (tietorakenne == "joukkueet") {
    for (var joukkue of data.joukkueet) {
      if (joukkue.id > suurin) {
        suurin = joukkue.id;
      }
    }
    return suurin + 1;
  }
}

/**
 * Trkistaa onko tietoja olemassa ja ovatko ne saalitussa muodossa
 * @param {String} lat latitude
 * @param {String} lon longitude
 * @param {String} koodi rastin koodi
 * @returns boolean arvon tietojen oikeellisuuden perusteella
 */
function onkoTietoja(lat, lon, koodi) {
  var latitude = parseFloat(lat);
  var longitude = parseFloat(lon);
  if (isNaN(latitude) || isNaN(longitude)) {
    return false;
  }
  if (koodi.length < 1) {
    return false;
  } else {
    return true;
  }
}

/**
 * Etsii halutun elementin tagin ja teksti sisällön perusteella
 * @param {String} tag elementin tagi
 * @param {String} teksti teksti jonka parent nodea etsitään
 * @returns elementin jos löytyy
 */
function haeElementtiTekstilla(tag, teksti) {
  var et = document.getElementsByTagName(tag);
  for (var i = 0; i < et.length; i++) {
    if (et[i].textContent == teksti) {
      return et[i];
    }
  }
}

/**
 * Hakee elementin tagin ja järjestysnumeron perusteella
 * @param {String} tag tag:n nimi
 * @param {Number} i monesko elementti
 * @returns halutun elementin jos löytyy
 */
function haeElementti(tag, i) {
  return document.getElementsByTagName(tag)[i];
}

/**
 * Etsii tietorakenteesta annetun joukkueen ja poistaa sen
 * @param {Object} joukkue poistettava joukkue
 */
function poistaJoukkue(joukkue) {
  for (var i = 0; i < data.joukkueet.length; i++) {
    if (data.joukkueet[i] == joukkue) {
      data.joukkueet.splice(i);
    }
  }
}

/**
 * Luo br ementin ja palauttaa sen
 * @returns br elementin
 */
function br() {
  return document.createElement("br");
}

/**
 * Lajittelee annetun joukkueen rastit aikajärjestykseen
 * @param {Object} joukkue jonka rastit halutaan lajitella
 * @returns listan rasteista aikajärjestyksessä
 */
function lajitteleRastit(joukkue) {
  var r = [];
  for (var i = 0; i < joukkue.rastit.length; i++) {
    if (joukkue.rastit[i].rasti != "0") {
      r.push(joukkue.rastit[i]);
    }
  }
  r.sort(function (a, b) {
    if (a.aika < b.aika) {
      return -1;
    }
    if (a.aika > b.aika) {
      return 1;
    }
    return 0;
  });
  return r;
}

/**
 * Laskee annetun rastit listan pisteet tiettyjen kriteerien mukaan
 * @param {Object} r rastit aikajärjetyksessä
 * @param {Number} ind indeksi mistä aloitetaan pistelasku
 * @param {boolean} lahdetty kertoo ollaanko "rekisteroity" lähtöruutu
 * @returns joukkueen pistemäärän
 */
function laskePisteet(r, ind, lahdetty) {
  var pisteet = 0;
  var leimatut = [];
  var lahto = etsiRastiKoodilla(data, "LAHTO").id;
  var maali = etsiRastiKoodilla(data, "MAALI").id;
  for (var i = ind; i < r.length; i++) {
    var rastiId = r[i].rasti;
    if (!rastiOn(data, rastiId)) {
      continue;
    }
    if (lahdetty === false && rastiId == lahto) {
      lahdetty = true;
      continue;
    } else if (lahdetty === true && rastiId == lahto) {
      lahdetty = false;
      pisteet = laskePisteet(r, i, lahdetty);
      return pisteet;
    } else if (lahdetty === true) {
      //on lähdetty
      if (rastiId === maali) {
        return pisteet;
      }
      //tarkistetaan että rasti löytyy rastit tiedoista
      if (rastiOn(data, rastiId)) {
        if (onkoLeimattu(leimatut, r[i])) {
          continue;
        } else {
          leimatut.push(r[i]);
          var koodi = etsiRastiKoodilla(data, rastiId).koodi;
          var piste = parseInt(koodi.substring(0, 1), 10);
          if (isNaN(piste)) {
            continue;
          } else {
            pisteet += piste;
          }
        }
      } else {
        continue;
      }
    } else {
      continue;
    }
  }
  return pisteet;
}

/**
 * Tarkistaa onko rastit tietorakenteessa annettua objektia
 * @param {Objekt} data tietorakenne
 * @param {Objekt} rasti objekti
 * @returns onko objektia
 */
function rastiOn(data, rastiId) {
  for (var r of data.rastit) {
    if (r.id == rastiId) {
      return true;
    }
  }
  return false;
}

/**
 * Etsii rastin sen koodin perusteella
 * @param {Number} koodi
 * @returns rastiobjektin jolla on haluttu koodi
 */
function etsiRastiKoodilla(data, koodi) {
  if (data.rastit === undefined) {
    var p = 0;
    for (var r of data) {
      if (r.rasti === undefined) {
        continue;
      }
      if (r.rasti.koodi == koodi) {
        p = r;
      }
    }
    return p;
  }
  for (r of data.rastit) {
    if (r.id == koodi) {
      return r;
    } else if (r.koodi == koodi) {
      return r;
    }
  }
}

/**
 * Etsii rastin id:n perusteella
 * @param {Number} id jolla etsitään
 * @returns rastin jonka id on sama
 */
function etsiRastiIdlla(id) {
  for (var rasti of data.rastit) {
    if (rasti.id == id) {
      return rasti;
    }
  }
}

/**
 * Tarkistaa onko annettua rastia laskettu eli lisätty annettuun tietorakenteeseen.
 * @param {Object} lista lista rasteista jotka on jo laskettu
 * @param {Object} rasti rasti joka tarkistetaan
 * @returns true jos löytyy ja false jos ei löydy
 */
function onkoLeimattu(lista, rasti) {
  for (var leimaus of lista) {
    if (leimaus.rasti === rasti.rasti) {
      return true;
    }
  }
  return false;
}

console.log(data);
