"use strict"; // pidä tämä ensimmäisenä rivinä
//@ts-check

let jasenetFieldset = {};

window.onload = function () {
  alusta();
};

function alusta() {
  jasenetFieldset = document.getElementsByTagName("fieldset")[1];
  alustaRadionapit();
  alustaLeimausnapit();
  var p1 = document.getElementById("j0");
  var p2 = document.getElementById("j1");
  p1.addEventListener("input", muokkaaInput);
  p2.addEventListener("input", muokkaaInput);
  var napit = document.getElementById("tallennanappi");
  napit.addEventListener("click", lisaaJoukkue);
  napit.muokkaus = false;
  var nappi2 = document.getElementById("tallennanappileimaus");
  nappi2.addEventListener("click", lisaaLeimaustapa);
  listaaJoukkueet();
}

/**
 * Tapahtumankäsittelijä jäsen nimi inputeille
 * @param {Object} e input tapahtuma
 */
function muokkaaInput(e) {
  var i = 1;
  var inputit = jasenetFieldset.getElementsByTagName("input");
  var joukkuenimi = document.getElementById("joukkuenimi");
  for (var input of inputit) {
    var jid = "j" + i;
    if (
      joukkuenimi.value != "" &&
      inputit[0].value != "" && //jos tarpeelliset kentät täytetty
      inputit[1].value != ""
    ) {
      //document.getElementById("lisaaJoukkueNappi").disabled = false;
    }
    if (
      e.currentTarget.id == jid &&
      jasenetFieldset.lastElementChild == e.currentTarget //lisää jos viimeistä muokataan
    ) {
      i++;
      jasenetFieldset.appendChild(
        luoJasenInput("Jäsen " + (i + 1) + " ", "j" + i)
      );
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
  var id = teksti.toLowerCase();
  id = teksti.replace(/\s+/g, "");
  var label = document.createElement("label");
  var labelinput = document.createElement("input");
  var labeltext = document.createTextNode(teksti);
  label.appendChild(labeltext);
  labelinput.setAttribute("type", "text");
  labelinput.setAttribute("value", "");
  labelinput.setAttribute("id", id);
  label.appendChild(labelinput);
  label.setAttribute("for", id);
  return label;
}

/**
 * Leimaustavan lisäämisen tallenna-napin tapahtumankäsittelijä.
 * Tekee tarvittavat tarkastuksen ja lisää leimaustavan tietorakenteeseen
 * @param {Object} e
 */
function lisaaLeimaustapa(e) {
  e.preventDefault();
  var nimiInput = document.getElementById("leimaustapanimi");
  var nimi = nimiInput.value.trim();
  if (nimi.length < 2) {
    nimiInput.setCustomValidity("Nimen on oltava vähintään 2 merkkiä");
    nimiInput.reportValidity();
    return;
  }
  if (!uniikkiLeimausNimi(nimi)) {
    nimiInput.setCustomValidity("Nimi on jo olemassa, keksi uusi");
    nimiInput.reportValidity();
    return;
  }
  data.leimaustapa.push(nimi);
  nimiInput.value = "";
  alustaLeimausnapit();
}

/**
 * Poistaa joukkueen tietorakenteesta
 * @param {Object} joukkue
 */
function poistaJoukkue(joukkue) {
  data.joukkueet.remove(joukkue);
}

/**
 * Muokkaa tai poistaa olemassa olevan joukkueen tietoja
 * @param {Object} joukkue
 */
function muokkaaJoukkue(joukkue) {
  var table = document.getElementById("rastit");
  if (table.muokkaus) {
    if (!muokkaaRastit(joukkue)) {
      tyhjennaJoukkueLomake();
      listaaJoukkueet();
      return;
    }
  }
  var jasenet = jasenetFieldset.getElementsByTagName("input");
  var nimiInput = document.getElementById("joukkuenimi");
  var nimi = nimiInput.value;
  if (nimi != joukkue.nimi) {
    if (nimi == "") {
      poistaJoukkue(joukkue);
      return;
    } else if (!nimiPituus(nimi)) {
      nimiInput.setCustomValidity("Nimen on oltava vähintään 2 merkkiä");
      nimiInput.reportValidity();
      return;
    } else if (!uniikkiNimi(nimi)) {
      nimiInput.setCustomValidity("Nimi käytössä, keksi uusi");
      nimiInput.reportValidity();
      return;
    } else {
      joukkue.nimi = nimi;
    }
  }
  var leimat = document.getElementById("leimaustapa");
  var leimaInputit = leimat.getElementsByTagName("input");
  var leimausvalinnat = [];
  var chekattu = false;
  for (var ruutu of leimaInputit) {
    if (ruutu.checked) {
      chekattu = true;
      leimausvalinnat.push(ruutu.value);
    }
  }
  if (!chekattu) {
    leimaInputit[0].setCustomValidity(
      "Joukkueella on oltava vähintään 1 leimaustapa"
    );
    leimaInputit[0].reportValidity();
    return;
  }
  if (jasenet[0].value == "" || jasenet[1].value == "") {
    jasenet[1].setCustomValidity("Joukkueella on oltava vähintään 2 jäsentä!");
    jasenet[1].reportValidity();
    return;
  }
  var jasenNimet = [];
  for (var i = 0; i < jasenet.length - 1; i++) {
    var n = jasenet[i].value;
    if (!uniikkiJasenNimi(jasenNimet, n)) {
      jasenet[1].setCustomValidity(
        "Joukkueen jäsenillä oltava erilaiset nimet!"
      );
      jasenet[1].reportValidity();
      tyhjennaJoukkueLomake();
      return;
    }
    jasenNimet.push(n);
    jasenet[i].value = "";
  }
  leimausvalinnat = haeLeimatavat(leimausvalinnat);
  if (leimausvalinnat != joukkue.leimaustapa) {
    joukkue.leimaustapa = leimausvalinnat;
  }
  var sarja = haeSarjaValinta();
  if (sarja != joukkue.sarja) {
    joukkue.sarja = sarja;
  }
  if (jasenNimet != joukkue.jasenet) {
    joukkue.jasenet = jasenNimet;
  }
  var tallennanappi = document.getElementById("tallennanappi");
  tallennanappi.muokkaus = false;
  tyhjennaJoukkueLomake();
  listaaJoukkueet();
}

/**
 * Tekeen rastien muokkauksen lomakkeen tarkistukset ja tallentaa muuttuneet tiedot
 * @param {Object} joukkue
 * @returns boolean, onnistuiko rastien muokkaus
 */
function muokkaaRastit(joukkue) {
  var table = document.getElementById("rastit");
  var rivit = table.getElementsByTagName("tr");
  var muutRastit = rivit[rivit.length - 1].muutRastit;
  var uusi = false;
  for (var rivi of rivit) {
    if (rivi == rivit[0]) {
      continue;
    }
    if (rivi.leimaus != undefined) {
      var leimaus = rivi.leimaus;
    } else {
      uusi = true;
    }
    if (rivi.muokattu) {
      var inputit = rivi.getElementsByTagName("input");
      if (inputit[0].muokattu) {
        //jos koodia muokattu
        if (!loytyykoRasti(inputit[0].value, muutRastit)) {
          inputit[0].setCustomValidity("Rasti on jo leimauttu");
          inputit[0].reportValidity();
          poistaLeimausRivit(rivit);
          return false;
        }
        if (!rastiOn(inputit[0].value)) {
          inputit[0].setCustomValidity("Annetulla koodilla ei löydy rastia");
          inputit[0].reportValidity();
          return false;
        }
        var uudenRastinId = etsiRastiKoodilla(data, inputit[0].value).id;
        if (!uusi) {
          leimaus.rasti = uudenRastinId;
        }
        for (var i = 0; i < muutRastit.length; i++) {
          var r = etsiRastiKoodilla(data, inputit[0].value);
          if (r == muutRastit[i]) {
            muutRastit.splice(i, 1);
          }
        }
      }
      if (inputit[1].muokattu) {
        //jos aikaa muokattu
        var aika = parseAika(inputit[1].value);
        if (aika == 0) {
          inputit[1].setCustomValidity(
            "Ajan on oltava muodossa: pp.kk.vvvv hh:mm:ss"
          );
          inputit[1].reportValidity();
          return false;
        }
        if (!aikaOnValilta(aika, table.aloitusaika, table.lopetusaika)) {
          inputit[1].setCustomValidity(
            "Ajan on oltava sarjan aloitus- ja lopetusaikojen väliltä"
          );
          inputit[1].reportValidity();
          return false;
        }
        if (!uusi) {
          leimaus.aika = aika;
        }
      }
      if (inputit[2].muokattu) {
        if (inputit[2].checked) {
          if (!uusi) {
            var vastaus = window.confirm("Haluatko varmasti poistaa?");
            if (vastaus) {
              poistaJoukkueeltaLeimaus(joukkue, leimaus);
              muutRastit.push(etsiRastiKoodilla(data, leimaus.koodi2));
            }
          }
        }
      }
      if (uusi) {
        if (inputit[0].muokattu && inputit[1].muokattu) {
          lisaaJoukkueelleLeimaus(joukkue, uudenRastinId, aika);
        } else if (!inputit[2].muokattu) {
          inputit[1].setCustomValidity("Täytä kaikki vaadittavat kentät!");
          inputit[1].reportValidity();
          return false;
        }
      }
    }
  }
  poistaLeimausRivit(rivit);
  return true;
}

/**
 * Lisää joukkueelle uuden leimauksen annettujen tietojen pohjalta
 * @param {Object} joukkue
 * @param {Number} uudenRastinId
 * @param {String} aika
 */
function lisaaJoukkueelleLeimaus(joukkue, uudenRastinId, aika) {
  var uusiRasti = {
    aika: aika,
    rasti: uudenRastinId,
  };
  joukkue.rastit.push(uusiRasti);
}

/**
 * Lisaa joukkue - napin tapahtumankäsittelijä. Luo uuden joukkueen tietojen pohjalta ja lisää sen tietorakenteeseen
 * @param {Object} e click eventti
 */
function lisaaJoukkue(e) {
  e.preventDefault();
  if (e.target.muokkaus === true) {
    muokkaaJoukkue(e.target.joukkue);
    return;
  }
  var jasenet = jasenetFieldset.getElementsByTagName("input");
  var nimiInput = document.getElementById("joukkuenimi");
  var nimi = nimiInput.value;
  if (nimi == "") {
    nimiInput.setCustomValidity("Anna joukkueelle nimi!");
    nimiInput.reportValidity();
    return;
  }
  var leimat = document.getElementById("leimaustapa");
  var leimaInputit = leimat.getElementsByTagName("input");
  var leimausvalinnat = [];
  var chekattu = false;
  for (var ruutu of leimaInputit) {
    if (ruutu.checked) {
      chekattu = true;
      leimausvalinnat.push(ruutu.value);
    }
  }
  if (!chekattu) {
    leimaInputit[0].setCustomValidity(
      "Joukkueella on oltava vähintään 1 leimaustapa"
    );
    leimaInputit[0].reportValidity();
    return;
  }
  if (jasenet[0].value == "" || jasenet[1].value == "") {
    jasenet[1].setCustomValidity("Joukkueella on oltava vähintään 2 jäsentä!");
    jasenet[1].reportValidity();
    return;
  }
  var jasenNimet = [];
  for (var i = 0; i < jasenet.length - 1; i++) {
    var n = jasenet[i].value;
    if (!uniikkiJasenNimi(jasenNimet, n)) {
      jasenet[1].setCustomValidity(
        "Joukkueen jäsenillä oltava erilaiset nimet!"
      );
      jasenet[1].reportValidity();
      tyhjennaJoukkueLomake();
      return;
    }
    jasenNimet.push(n);
    jasenet[i].value = "";
  }
  if (!nimiPituus(nimi)) {
    nimiInput.setCustomValidity("Nimen on oltava vähintään 2 merkkiä");
    nimiInput.reportValidity();
    tyhjennaJoukkueLomake();
    return;
  }
  if (!uniikkiNimi(nimi)) {
    nimiInput.setCustomValidity("Nimi käytössä, keksi uusi");
    nimiInput.reportValidity();
    tyhjennaJoukkueLomake();
    return;
  }
  var sarja = haeSarjaValinta();
  var uusiJoukkue = luoJoukkue(
    nimiInput.value,
    jasenNimet,
    sarja,
    leimausvalinnat
  );
  data.joukkueet.push(uusiJoukkue);
  tyhjennaJoukkueLomake();
  listaaJoukkueet();
}

/**
 * Korjaa joukkue lomakkeen inputien määrän
 */
function tyhjennaJoukkueLomake() {
  var form = document.getElementsByTagName("form")[0];
  var event = document.createEvent("Event");
  event.initEvent("input", true, true);
  var pt = form.getElementsByTagName("p");
  var joukkuenimi = document.getElementById("joukkuenimi");
  joukkuenimi.value = "";
  var inputit = jasenetFieldset.getElementsByTagName("input");
  for (var input of inputit) {
    input.value = "";
  }
  var tallennanappi = document.getElementById("tallennanappi");
  tallennanappi.value = "Tallenna";
  var j1 = document.getElementById("j1");
  while (pt.length > 3) {
    if (jasenetFieldset.lastElementChild == j1) {
      break;
    }
    pt[2].dispatchEvent(event);
  }
}

/**
 * Luo joukkueen annettujen tietojen pohjalta
 * @param {String} nimi joukkueen nimi
 * @param {Object} jasenet joukkueen jäsenet taulukossa
 * @param {Object} leimaustavat
 * @returns luodun joukkueen
 */
function luoJoukkue(nimi, jasenet, sarja, leimaustavat) {
  var joukkue = {
    nimi: nimi,
    id: uusiId("joukkueet"),
    sarja: sarja,
    jasenet: jasenet,
    rastit: [],
    leimaustapa: haeLeimatavat(leimaustavat),
  };
  return joukkue;
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
 * Tarkistaa onko annettu nimi olemassa jo jollain joukkueella
 * @param {String} nimi
 * @returns boolean
 */
function uniikkiNimi(nimi) {
  for (var joukkue of data.joukkueet) {
    nimi = nimi.trim();
    nimi = nimi.toUpperCase();
    if (joukkue.nimi.toUpperCase() == nimi) {
      return false;
    }
  }
  return true;
}

/**
 * Tarkistaa onko jäsenillä uniikit nimet
 * @param {Object} jasennimet
 * @param {String} jasen
 * @returns boolean
 */
function uniikkiJasenNimi(jasennimet, jasen) {
  for (var nimi of jasennimet) {
    var pnimi = nimi.toLowerCase();
    pnimi = pnimi.trim();
    var pjasen = jasen.toLowerCase();
    pjasen = pjasen.trim();
    if (pnimi == pjasen) {
      return false;
    }
  }
  return true;
}

/**
 * Tarkistaa onko annettu leimaustapasyöte uniikki
 * @param {String} syote
 * @returns boolean
 */
function uniikkiLeimausNimi(syote) {
  var nimi = syote.toUpperCase();
  for (var leimaustapa of data.leimaustapa) {
    var leimanimi = leimaustapa.toUpperCase();
    if (nimi == leimanimi) {
      return false;
    }
  }
  return true;
}

/**
 * Tarkistaa nimen pituuden
 * @param {String} nimi
 * @returns boolean
 */
function nimiPituus(nimi) {
  nimi = nimi.trim();
  if (nimi.length < 2) {
    return false;
  }
  return true;
}

/**
 * Hakee sarjan valinnan ja palauttaa sarjan id:n
 * @returns sarjan id
 */
function haeSarjaValinta() {
  var div = document.getElementById("sarja");
  var napit = div.getElementsByTagName("input");
  for (var nappi of napit) {
    if (nappi.checked) {
      var id = parseInt(nappi.value);
      return id;
    }
  }
}

/**
 * hakee sarjan id:n sen nimen perusteella
 * @param {String} nimi
 * @returns
 */
function haeSarjaIdNimella(nimi) {
  for (var sarja of data.sarjat) {
    if (sarja.nimi == nimi) {
      return sarja.id;
    }
  }
}

/**
 * Hekaa sarjan nimen sen id:n perusteella
 * @param {Number} id
 * @returns sarjan nimen
 */
function haesarjaNimiIdlla(id) {
  for (var sarja of data.sarjat) {
    if (sarja.id == id) {
      return sarja.nimi;
    }
  }
}

/**
 * Otaa parametrina taulukon leimaustapojen nimistä ja palauttaa taulukon
 * nimeä vastaavasta paikoista data tietorakenteessa
 * @param {Object} leimanimet
 * @returns taulukon nimiä vastaavaista indekseistä
 */
function haeLeimatavat(leimanimet) {
  var leimauskoodit = [];
  for (var nimi of leimanimet) {
    for (var i = 0; i < data.leimaustapa.length; i++) {
      if (nimi == data.leimaustapa[i]) {
        leimauskoodit.push(i);
      }
    }
  }
  return leimauskoodit;
}

/**
 * Hakee data tietorakenteesta leimaustavan nimellä sen indeksin paikan
 * @param {String} leimaustapa
 * @returns leimaustavan indeksin
 */
function haeleimaIndx(leimaustapa) {
  for (var i = 0; i < data.leimaustapa.length; i++) {
    if (leimaustapa == data.leimaustapa[i]) {
      return i;
    }
  }
}

/**
 * Alustaa sarjan valintaa varten radionapit tietorakenteessa olevien sarjojen mukaan
 */
function alustaRadionapit() {
  var sarjatJarjestykseen = [];
  for (var sarja of data.sarjat) {
    sarjatJarjestykseen.push(sarja);
  }
  sarjatJarjestykseen.sort(function (a, b) {
    if (a.nimi < b.nimi) {
      return -1;
    } else {
      return 1;
    }
  });
  var i = 1;
  for (var s of sarjatJarjestykseen) {
    var div = document.createElement("div");
    div.setAttribute("class", "labeldiv");
    var p = document.getElementById("sarja");
    p.appendChild(div);
    var label = document.createElement("label");
    div.appendChild(label);
    var labelTeksti = document.createTextNode(s.nimi);
    label.appendChild(labelTeksti);
    label.setAttribute("for", "label" + i);
    var input = document.createElement("input");
    div.appendChild(input);
    input.setAttribute("name", "sarja");
    input.setAttribute("type", "radio");
    input.setAttribute("value", s.id);
    input.setAttribute("class", "sarjaradio");
    input.setAttribute("id", "label" + i);
    p.appendChild(document.createElement("br"));
    i++;
  }
  var sarjadiv = document.getElementById("sarja");
  var ipt = sarjadiv.getElementsByTagName("input");
  ipt[0].checked = "checked";
}

/**
 * Alustaa leimaustavan valintaan napit aakkosjärjestyksessä
 */
function alustaLeimausnapit() {
  var p = document.getElementById("leimaustapa");
  var pLapset = p.childNodes;
  if (pLapset.length > 0) {
    poistaLapset(p);
  }
  var leimatJarjestykseen = [];
  for (var leimaustapa of data.leimaustapa) {
    leimatJarjestykseen.push(leimaustapa);
  }
  leimatJarjestykseen.sort(function (a, b) {
    if (a < b) {
      return -1;
    } else {
      return 1;
    }
  });
  var i = 11;
  for (var l of leimatJarjestykseen) {
    var div = document.createElement("div");
    div.setAttribute("class", "labeldiv");
    p.appendChild(div);
    var label = document.createElement("label");
    div.appendChild(label);
    var labelTeksti = document.createTextNode(l);
    label.appendChild(labelTeksti);
    label.setAttribute("for", "label" + i);
    var input = document.createElement("input");
    div.appendChild(input);
    input.setAttribute("name", "leimaus");
    input.setAttribute("type", "checkbox");
    input.setAttribute("value", l);
    input.setAttribute("id", "label" + i);
    p.appendChild(document.createElement("br"));
    i++;
  }
}

/**
 * Listaa joukkueet ja niiden sarjan ja jäsenet aakkosjärjestyksessä
 */
function listaaJoukkueet() {
  var body = document.getElementsByTagName("body")[0];
  document.getElementById("joukkueet").remove();
  var ul = document.createElement("ul");
  ul.setAttribute("id", "joukkueet");
  body.appendChild(ul);
  var jarjestamattomat = [];
  for (var joukkue of data.joukkueet) {
    var jasenetjarjestykseen = [];
    for (var jarjestamatonjasen of joukkue.jasenet) {
      jasenetjarjestykseen.push(jarjestamatonjasen);
    }
    jarjestamattomat.push({
      nimi: joukkue.nimi,
      sarja: haesarjaNimiIdlla(joukkue.sarja),
      sarjaNimi: joukkue.sarja,
      id: joukkue.id,
      jasenet: jasenetjarjestykseen.sort(function (a, b) {
        if (a < b) {
          return -1;
        } else {
          return 1;
        }
      }),
      rastit: jarjestaLeimaukset(joukkue),
    });
  }
  var jarjestetyt = jarjestamattomat.sort(function (a, b) {
    if (a.sarja < b.sarja) {
      return -1;
    }
    if (a.sarja > b.sarja) {
      return 1;
    }
    if (a.nimi < b.nimi) {
      return -1;
    }
    if (a.nimi > b.nimi) {
      return 1;
    }
  });
  for (var j of jarjestetyt) {
    var li1 = document.createElement("li");
    ul.appendChild(li1);
    var jTeksti = document.createTextNode(j.nimi + " ");
    var a = document.createElement("a");
    li1.appendChild(a);
    a.appendChild(jTeksti);
    a.setAttribute("href", "#form");
    a.addEventListener("mouseup", muokkaaJoukkueTila);
    a.jid = j.id;
    var strong = document.createElement("strong");
    var sarja = document.createTextNode(haesarjaNimiIdlla(j.sarjaNimi));
    li1.appendChild(strong);
    strong.appendChild(sarja);
    var ul2 = document.createElement("ul");
    li1.appendChild(ul2);
    var jasenetLi = document.createElement("li");
    ul2.appendChild(jasenetLi);
    jasenetLi.appendChild(document.createTextNode("Jäsenet"));
    var jasenetUl = document.createElement("ul");
    jasenetLi.appendChild(jasenetUl);
    for (var jasen of j.jasenet) {
      var li = document.createElement("li");
      jasenetUl.appendChild(li);
      li.appendChild(document.createTextNode(jasen));
    }
    var rastitLi = document.createElement("li");
    ul2.appendChild(rastitLi);
    var details = document.createElement("details");
    rastitLi.appendChild(details);
    var summary = document.createElement("summary");
    details.appendChild(summary);
    summary.appendChild(document.createTextNode("Rastit"));
    var ol = document.createElement("ol");
    details.appendChild(ol);
    for (var leimaus of j.rastit) {
      var leimausLi = document.createElement("li");
      ol.appendChild(leimausLi);
      leimausLi.appendChild(
        document.createTextNode(
          "Koodi: " + leimaus.koodi2 + " | Aika: " + formatoiAika(leimaus.aika)
        )
      );
    }
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
  var form = document.getElementById("form");
  var jasenetFieldset = form.getElementsByTagName("fieldset")[1];
  var inputit = jasenetFieldset.getElementsByTagName("input");
  if (inputit.length > 3) {
    tyhjennaJoukkueLomake();
  }
  var nimi = document.getElementById("joukkuenimi");
  nimi.value = joukkue.nimi;
  var leimaustapa = document.getElementById("leimaustapa");
  var leimaustavat = leimaustapa.getElementsByTagName("input");
  for (var tapa of leimaustavat) {
    for (var t of joukkue.leimaustapa) {
      if (haeleimaIndx(tapa.value) == t) {
        tapa.checked = "checked";
      }
    }
  }
  var sarja = document.getElementById("sarja");
  var sarjaInputit = sarja.getElementsByTagName("input");
  for (var sarjainput of sarjaInputit) {
    //palauta tähän sarjan value
    if (sarjainput.value == joukkue.sarja) {
      sarjainput.checked = "checked";
    }
  }
  var pt = jasenetFieldset.getElementsByTagName("p");
  for (var i = 0; i < joukkue.jasenet.length; i++) {
    inputit[i].value = joukkue.jasenet[i];
    var event = document.createEvent("Event");
    event.initEvent("input", true, true);
    pt[i].dispatchEvent(event);
  }
  var tallennanappi = document.getElementById("tallennanappi");
  tallennanappi.joukkue = joukkue;
  tallennanappi.muokkaus = true;
  alustaLeimaukset(joukkue);
}

/**
 * Alustaa leimausten muokkausnäkymän
 * @param {Object} joukkue
 */
function alustaLeimaukset(joukkue) {
  var table = document.getElementById("rastit");
  table.muokkaus = false;
  var rivit = table.getElementsByTagName("tr");
  if (rivit.length > 2) {
    poistaLeimausRivit(rivit);
  }
  var leimauksetJarjestyksessa = jarjestaLeimaukset(joukkue);
  var i = 1;
  var muutRastit = puuttuvatRastit(leimauksetJarjestyksessa);
  if (leimauksetJarjestyksessa.length > 0) {
    table.aloitusaika = leimauksetJarjestyksessa[0];
    table.lopetusaika =
      leimauksetJarjestyksessa[leimauksetJarjestyksessa.length - 1];
  } else {
    table.aloitusaika = data.alkuaika;
    table.lopetusaika = data.loppuaika;
  }
  for (var leimaus of leimauksetJarjestyksessa) {
    var uusiRivi = document.createElement("tr");
    uusiRivi.leimaus = leimaus;
    table.appendChild(uusiRivi);
    var s1 = document.createElement("th");
    uusiRivi.appendChild(s1);
    var koodiInput = document.createElement("input");
    koodiInput.addEventListener("input", muokkaaLeimausInput);
    s1.appendChild(koodiInput);
    koodiInput.setAttribute("list", "vapaatrastit" + i);
    koodiInput.setAttribute("value", leimaus.koodi2);
    koodiInput.muokattu = false;
    var datalist = document.createElement("datalist");
    s1.appendChild(datalist);
    datalist.setAttribute("id", "vapaatrastit" + i);
    listaaVapaatRastit(datalist, muutRastit);
    var s2 = document.createElement("th");
    uusiRivi.appendChild(s2);
    var aikaInput = document.createElement("input");
    aikaInput.addEventListener("input", muokkaaLeimausInput);
    s2.appendChild(aikaInput);
    aikaInput.setAttribute("type", "text");
    aikaInput.setAttribute("value", formatoiAika(leimaus.aika));
    aikaInput.aika = Date.parse(leimaus.aika);
    aikaInput.muokattu = false;
    var s3 = document.createElement("th");
    uusiRivi.appendChild(s3);
    var poista = document.createElement("input");
    s3.appendChild(poista);
    poista.setAttribute("type", "checkbox");
    poista.addEventListener("click", muokkaaLeimausInput);
    poista.setAttribute("class", "poistotOikealle");
    i++;
  }
  luoRastiInput(muutRastit, i);
}

/**
 * Formatoi ajan muotoon missä sen kuuluu käyttäjälle näkyä
 * @param {String} aika
 */
function formatoiAika(aika) {
  //2017-03-18 12:00:00
  //18.03.2017 12:00:00
  var vuosi = aika.slice(0, 4);
  var kuukausi = aika.slice(5, 7);
  var paiva = aika.slice(8, 10);
  var kello = aika.slice(11);
  var uusiAika = paiva + "." + kuukausi + "." + vuosi + " " + kello;
  return uusiAika;
}

/**
 * Muuttaa inputin esitysmuodon tallennusmuotoon ja tarkistaa onko se sallitussa muodossa
 * @param {String} aika inputin value
 * @returns
 */
function parseAika(aika) {
  //18.03.2017 12:00:00
  var paiva = aika.slice(0, 2);
  var kuukausi = aika.slice(3, 5);
  var vuosi = aika.slice(6, 10);
  var kello = aika.slice(11);
  var uusiAika = vuosi + "-" + kuukausi + "-" + paiva + " " + kello;
  var date = Date.parse(uusiAika);
  if (isNaN(date)) {
    return 0;
  }
  return uusiAika;
}

/**
 * Luo rivin rastin lisäystä varten
 * @param {Object} muutRastit rastit joita käyttäjä ei ole leimannut
 * @param {Number} i rivien määrä
 */
function luoRastiInput(muutRastit, i) {
  var table = document.getElementById("rastit");
  var uusiRivi = document.createElement("tr");
  table.appendChild(uusiRivi);
  var s1 = document.createElement("th");
  uusiRivi.appendChild(s1);
  uusiRivi.muutRastit = muutRastit;
  var koodiInput = document.createElement("input");
  s1.appendChild(koodiInput);
  koodiInput.setAttribute("list", "vapaatrastit" + i);
  koodiInput.addEventListener("input", muokkaaLeimausInput);
  koodiInput.rivit = i;
  koodiInput.muokattu = false;
  var datalist = document.createElement("datalist");
  s1.appendChild(datalist);
  datalist.setAttribute("id", "vapaatrastit" + i);
  listaaVapaatRastit(datalist, muutRastit);
  var s2 = document.createElement("th");
  uusiRivi.appendChild(s2);
  var aikaInput = document.createElement("input");
  aikaInput.addEventListener("input", muokkaaLeimausInput);
  s2.appendChild(aikaInput);
  aikaInput.setAttribute("type", "text");
  aikaInput.setAttribute("value", "pp.kk.vvvv --:--:--");
  aikaInput.muokattu = false;
  var s3 = document.createElement("th");
  uusiRivi.appendChild(s3);
  var poista = document.createElement("input");
  s3.appendChild(poista);
  poista.setAttribute("type", "checkbox");
  poista.addEventListener("click", muokkaaLeimausInput);
  poista.setAttribute("class", "poistotOikealle");
}

/**
 * Leimaustaulukon viimeisten inputien tapahtumankäisttelijä
 * @param {Object} e input tapahtuma
 */
function muokkaaLeimausInput(e) {
  var table = document.getElementById("rastit");
  table.muokkaus = true;
  var rivi = e.target.parentNode.parentNode;
  var rivinInputit = rivi.getElementsByTagName("input");
  rivi.muokattu = true;
  e.target.muokattu = true;
  var rivit = table.getElementsByTagName("tr");
  var vikaRivi = rivit[rivit.length - 1];
  var inputit = vikaRivi.getElementsByTagName("input");
  if (e.target == inputit[0]) {
    //muokataan koodi riviä ja ei ole enempää rivejä
    if (inputit[0].value.length == 1 && rivit.length == inputit[0].rivit + 1) {
      luoRastiInput(vikaRivi.muutRastit, rivit.length);
    }
  }
  if (rivinInputit[0].value.length == 0 && rivinInputit[1].value.length == 0) {
    if (e.target.value.length == 0) {
      vikaRivi.remove();
    }
  }
}

/**
 * Luo annettuun datalistaparetriin listan muutRastit parametrin rasteista
 * @param {Object} datalist
 * @param {Object} muutRastit
 */
function listaaVapaatRastit(datalist, muutRastit) {
  var rastit = [];
  rastit = muutRastit.sort(function (a, b) {
    if (a.koodi < b.koodi) {
      return -1;
    } else {
      return 1;
    }
  });
  for (var rasti of rastit) {
    var option = document.createElement("option");
    datalist.appendChild(option);
    option.setAttribute("value", rasti.koodi);
  }
}

/**
 * Palauttaa listan rasteista jotka eivät esiinny annetulla listalla
 * @param {Object} leimaukset lista johon verrataan
 * @returns lista muista rasteista
 */
function puuttuvatRastit(leimaukset) {
  var rastit = [];
  for (var rasti of data.rastit) {
    var loytyyko = false;
    for (var leimaus of leimaukset) {
      if (rasti.koodi == leimaus.koodi2) {
        loytyyko = true;
      }
    }
    if (!loytyyko) {
      rastit.push(rasti);
    }
  }
  return rastit;
}

/**
 * Poistaa leimaustaulukosta kaikki muut rivit paitsi ensimmäisen
 * @param {Object} rivit
 */
function poistaLeimausRivit(rivit) {
  for (var i = rivit.length - 1; i >= 1; i--) {
    rivit[i].remove();
  }
}

/**
 * Järjestää joukkueen leimaukset aikajärjestykseen ja palauttaa listan
 * @param {Object} joukkue
 */
function jarjestaLeimaukset(joukkue) {
  var rastit = huonotPois(joukkue);
  rastit.sort(function (a, b) {
    if (a.aika < b.aika) {
      return -1;
    }
    if (a.aika > b.aika) {
      return 1;
    }
    if (a.koodi2 < b.koodi2) {
      return -1;
    } else {
      return 1;
    }
  });
  return rastit;
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
 * Tarkistaa onko annettua koodia annetussa listassa muita rasteja
 * @param {String} koodi
 * @param {Object} muutRastit
 * @returns
 */
function loytyykoRasti(koodi, muutRastit) {
  if (muutRastit == undefined) {
    return true;
  }
  for (var rasti of muutRastit) {
    if (rasti == undefined) {
      continue;
    }
    if (koodi == rasti.koodi) {
      return true;
    }
  }
  muutRastit.push(etsiRastiKoodilla(data, koodi));
  return false;
}

/**
 * Etsii rastin sen koodin perusteella
 * @param {Number} koodi
 * @returns rastiobjektin jolla on haluttu koodi
 */
function etsiRastiKoodilla(data, koodi) {
  for (var r of data.rastit) {
    if (r.id == koodi) {
      return r;
    } else if (r.koodi == koodi) {
      return r;
    }
  }
}

/**
 * Tarkistaa onko rastit tietorakenteessa annettua objektia
 * @param {Objekt} rasti objekti
 * @returns onko objektia
 */
function rastiOn(tunnus) {
  for (var r of data.rastit) {
    if (r.id == tunnus) {
      return true;
    }
    if (r.koodi == tunnus) {
      return true;
    }
  }
  return false;
}

/**
 * Tarkistaa onko annettu aika annetus aloitus- ja lopetusajan väliltä
 * @param {String} aika
 * @param {String} aloitusaika
 * @param {String} lopetusaika
 * @returns boolean loytyyko aika väliltä
 */
function aikaOnValilta(aika, aloitusaika, lopetusaika) {
  var aikanumerona = Date.parse(aika);
  var aloitusnumerona = Date.parse(aloitusaika);
  var lopetusnumerona = Date.parse(lopetusaika);
  if (aikanumerona < aloitusnumerona || aikanumerona > lopetusnumerona) {
    return false;
  }
  return true;
}

/**
 * Poistaa annetun elementin lapset
 * @param {Object} elementti html elementti
 */
function poistaLapset(elementti) {
  var lapset = elementti.childNodes;
  for (var i = lapset.length - 1; i >= 0; i--) {
    lapset[i].remove();
  }
}

/**
 * Poistaa annetun leimauksen joukkueen leimauksista
 * @param {Object} joukkue
 * @param {Object} leimaus
 */
function poistaJoukkueeltaLeimaus(joukkue, leimaus) {
  for (var i = 0; i < joukkue.rastit.length; i++) {
    if (leimaus.rasti == joukkue.rastit[i].rasti) {
      if (leimaus.aika == joukkue.rastit[i].aika) {
        joukkue.rastit.splice(i, 1);
      }
    }
  }
}

console.log(data);
