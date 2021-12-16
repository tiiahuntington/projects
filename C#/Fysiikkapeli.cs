using System;
using System.Collections.Generic;
using Jypeli;
using Jypeli.Assets;
using Jypeli.Controls;
using Jypeli.Widgets;

/// @author Tiia Huntington
/// @version 6.4.2020
/// <summary>
/// Avaruuspeli
/// </summary>

public class Avaruustaisto : PhysicsGame
{
    private List<GameObject> liikutettavat = new List<GameObject>();
    private List<GameObject> taustakuvat;
    private List<Action> aliohjelmat = new List<Action>();
    private PhysicsObject boss;
    private PhysicsObject alus;
    private GameObject ekaTaustakuva;
    private int bossElamat = 20;
    private int suuntaNopeus = -5;
    private Timer ampuminen;
    private Timer taustaAjastin;
    private Timer liikutusAjastin;

    public override void Begin()
    {
        MultiSelectWindow alkuValikko = new MultiSelectWindow("Avaruustaisto \nPelissä taistelet avaruuden herruudesta. \nAluksesi liikkuu nuolinäppäimillä ja ampuu välilyönnillä.\nOnnea taisteluun!",
        "Aloita peli", "Poistu");
        Add(alkuValikko);
        alkuValikko.ItemSelected += NappiaPainettiin;

        Level.Width *= 2;
        Level.Background.CreateStars();
        Keyboard.Listen(Key.Escape, ButtonState.Pressed, ConfirmExit, "Lopeta peli");
    }


    /// <summary>
    /// Alkuvalikon nappien toimintojen asettaminen.
    /// </summary>
    /// <param name="nappi">Alkuvalikon napin indeksi.</param>
    private void NappiaPainettiin(int nappi)
    {
            switch (nappi)
            {
                case 0:
                    AloitaPeli();
                    break;
                case 1:
                    Exit();
                    break;
            }
        
    }

    
    /// <summary>
    /// Pelin käynnistävä aliohjelma.
    /// </summary>
    private void AloitaPeli()
    {
        liikutusAjastin = new Timer(0.05, LiikutaOlioita);
        liikutusAjastin.Start();

        LuoTaustakuvat();
        Aluksenluonti(90, 0);
        HyokkausJarjestys();
    }


    /// <summary>
    /// Luo ajastimen taustakuvien liikuttamiseen,  
    /// määrittelee taustakuvien listan ja
    /// lisää taustakuvat listalle kutsumalla LisaaTaustakuva aliohjelmaa.
    /// </summary>
    private void LuoTaustakuvat()
    {
        taustaAjastin = new Timer(0.1, LiikutaTaustaa);
        taustaAjastin.Start();
        int taustojenMaara = 4;
        taustakuvat = new List<GameObject>();
        for (int i = 0; i < taustojenMaara; i++)
            LisaaTaustakuva();
    }

    
    /// <summary>
    /// Luo taustakuva olion,
    /// määrittelee taustakuvan sijainnin niin, että 
    /// taustakuvat ovat vierekkäin ja 
    /// lisää olion taustakuvat listaan.
    /// </summary>
    private void LisaaTaustakuva()
    {
        GameObject olio = new GameObject(Screen.Width, Screen.Height);
        olio.Image = LoadImage("taustakuva.png");
        olio.Y = 0;
        Add(olio, -3);

        if (taustakuvat.Count > 0)
        {
            double left = taustakuvat[taustakuvat.Count - 1].Left;
            olio.Right = left;
        }
        else
        {
            olio.Right = Level.Right;
            ekaTaustakuva = olio;
        }

        taustakuvat.Add(olio);
    }


    /// <summary>
    /// Liikuttaa taustakuvia taustakuvat listalla siirtämällä niiden x asemaa suuntaNopeuden verran,
    /// jos taustakuva poistuu pelinäytöltä seuraava taustakuva on ekaTaustakuva.
    /// </summary>
    private void LiikutaTaustaa()
    {
        foreach (GameObject taustakuva in taustakuvat)
        {
            taustakuva.X += suuntaNopeus;

            if (suuntaNopeus < 0 && taustakuva.Right < Level.Left)
            {
                taustakuva.Left = ekaTaustakuva.Right;
                ekaTaustakuva = taustakuva;
            }
        }
    }


    /// <summary>
    /// Liikuttaa olioita liikutettavat listalla
    /// muuttamalla olioden x asemaa suuntaNopeuden verran.
    /// Tuhoaa ja poistaa olion liikutettavat listasta jos olio on ammuttu tai poistuu näytöltä.
    /// </summary>
    private void LiikutaOlioita()
    {
        double tuhoamisX = Screen.Left - 70;
        for (int i = 0; i < liikutettavat.Count; i++)
        {
            GameObject olio = liikutettavat[i];
            olio.X += suuntaNopeus;
            if (olio.IsDestroyed == true || olio.X <= tuhoamisX)
            {
                olio.Destroy();
                liikutettavat.Remove(olio);
            }
        }
    }


    /// <summary>
    /// Aluksen luominen, 
    /// näppäinten määritteleminen aluksen liikuttamista varten.
    /// </summary>
    /// <param name="x">Aluksen x sijainti.</param>
    /// <param name="y">Aluksen y sijainti.</param>
    private void Aluksenluonti(int x, int y)
    {
        alus = new PhysicsObject(70, 70);
        alus.X = Screen.Left + x;
        alus.Y = y;
        alus.CanRotate = false;
        alus.Tag = "alus";
        Add(alus, 2);
        alus.Image = LoadImage("alus.png");

        Keyboard.Listen(Key.Up, ButtonState.Down, Ohjaimet, "", alus, new Vector(0, 400));
        Keyboard.Listen(Key.Up, ButtonState.Released, Ohjaimet, null, alus, Vector.Zero);
        Keyboard.Listen(Key.Down, ButtonState.Down, Ohjaimet, "", alus, new Vector(0, -400));
        Keyboard.Listen(Key.Down, ButtonState.Released, Ohjaimet, null, alus, Vector.Zero);
        Keyboard.Listen(Key.Left, ButtonState.Down, Ohjaimet, "", alus, new Vector(-400, 0));
        Keyboard.Listen(Key.Left, ButtonState.Released, Ohjaimet, null, alus, Vector.Zero);
        Keyboard.Listen(Key.Right, ButtonState.Down, Ohjaimet, "", alus, new Vector(400, 0));
        Keyboard.Listen(Key.Right, ButtonState.Released, Ohjaimet, null, alus, Vector.Zero);
        Keyboard.Listen(Key.Space, ButtonState.Released, Ampuminen, "", alus, new Vector(450, 0));
    }


    /// <summary>
    /// Aliohjelma ampumiseen.
    /// Tarkistaa onko hahmo elossa,
    /// luo luodin,
    /// määrittelee luodin x sijainnin hahmon tyypin perusteella,
    /// "ampuu" luodin,
    /// tuhoaa luodin jos osuu pelikentän laitaan,
    /// määrittelee luodin suunnnan perusteella törmäyksenkäsittelijät.
    /// </summary>
    /// <param name="hahmo">Hahmo, joka ampuu.</param>
    /// <param name="suunta">Suunta, johon ammutaan.</param>
    private void Ampuminen(PhysicsObject hahmo, Vector suunta)
    {
        if (hahmo.IsDestroyed == true) return;
        PhysicsObject luoti = new PhysicsObject(20, 10, Shape.Rectangle);
        if (hahmo.X < 0) luoti.X = hahmo.X + 10;
        else if (hahmo == boss) luoti.X = hahmo.X - 220;
        else { luoti.X = hahmo.X - 10; };
        luoti.Y = hahmo.Y;
        luoti.Color = Color.White;
        luoti.Tag = "luoti";
        luoti.CanRotate = false;
        luoti.Hit(suunta);
        if (luoti.X > Screen.Right || luoti.X < Screen.Left) luoti.Destroy();
        if (suunta.X > 0)
        {
            AddCollisionHandler(luoti, "olio", CollisionHandler.DestroyBoth);
        }
        else
        {
            //AddCollisionHandler(luoti, "alus", AlusOsuma);
            luoti.IgnoresCollisionResponse = true;
        }
        Add(luoti);
    }


    /// <summary>
    /// Aluksen ja luodin törmäyskäsittelijä.
    /// Tuhoaa aluksen ja luodin, 
    /// pyäyttää ajastimet ja 
    /// laittaa näytölle "Game over" tekstin.
    /// </summary>
    /// <param name="kohde">Alus.</param>
    /// <param name="ammus">Luoti.</param>
    private void AlusOsuma(PhysicsObject kohde, PhysicsObject ammus)
    {
        kohde.Destroy();
        ammus.Destroy();
        taustaAjastin.Stop();
        liikutusAjastin.Stop();
        ampuminen.Stop();
        GameObject gameOver = new GameObject(Screen.Width, Screen.Height, 0, 0);
        gameOver.Image = LoadImage("gameover.png");
        Add(gameOver, 4);
    }


    /// <summary>
    /// Määrittelee näppäimen aliohjelmakutsun perusteella suunnan ja nopeuden.
    /// Rajoittaa aluksen liikettä, niin että alus ei pääse poistumaan pelinäkymästä tai 
    /// menemään liian lähelle vihollisia.
    /// </summary>
    /// <param name="alus">Ohjattava eli alus.</param>
    /// <param name="suunta">Näppäimen määrittelemä suunta.</param>
    private void Ohjaimet(PhysicsObject alus, Vector suunta)
    {
        if ((alus.X < Screen.Left + 50 && suunta.X < 0))
        {
            alus.Velocity = new Vector(0, suunta.Y);
            return;
        }
        if (alus.X > -50 && suunta.X > 0)
        {
            alus.Velocity = new Vector(0, suunta.Y);
            return;
        }
        if (alus.Y > Screen.Top - 80 && suunta.Y > 0)
        {
            alus.Velocity = new Vector(suunta.X, 0);
            return;
        }
        if (alus.Y < Screen.Bottom + 120 && suunta.Y < 0)
        {
            alus.Velocity = new Vector(suunta.X, 0);
            return;
        }
        alus.Velocity = suunta;
    }


    /// <summary>
    /// Olioiden luontiohjelma kaikille oliotyypeille.
    /// Luo olion,
    /// määrittää ampumisvälin ja 
    /// kutsuu ampuminen aliohelmaa sen mukaan.
    /// </summary>
    /// <param name="x">Olion x sijainti näytön oikeasta reunasta.</param>
    /// <param name="y">Olion y sijainti.</param>
    /// <param name="korkeus">Olion korkeus.</param>
    /// <param name="leveys">Olion leveys.</param>
    /// <param name="kuvannimi">Oliotyypin kuvan nimi.</param>
    private void OlionLuonti(int x, int y, double korkeus, double leveys, string kuvannimi)
    {
        PhysicsObject olio = new PhysicsObject(leveys, korkeus);
        olio.X = Screen.Right - x;
        olio.Y = y;
        olio.Tag = "olio";
        olio.IgnoresCollisionResponse = true;
        liikutettavat.Add(olio);
        Add(olio);
        olio.Image = LoadImage(kuvannimi);
        double ampumisvali = RandomGen.NextDouble(1, 5);
        ampuminen = new Timer(ampumisvali, delegate
        {
            if (olio.IsDestroyed == true) ampuminen.Stop();
            Ampuminen(olio, new Vector(-350, 0)); }) ;
        ampuminen.Start();
    }


    /// <summary>
    /// Bossin luontiohjelma.
    /// Luo bossin, 
    /// pyäyttää taustakuvan liikutusajastimen,
    /// luo ajastimen ampumiselle,
    /// luo törmäyksenkäsittelijän osumalle,
    /// antaa bossin liikkua 6 sekuntia kunnes pysäyttää liikkeen,
    /// luo vertikaalista liikettä, joka kääntyy kun raja tulee vastaan.
    /// </summary>
    private void Boss()
    {
        boss = new PhysicsObject(267, 400);
        boss.X = Level.Right - 80;
        boss.Y = 0;
        liikutettavat.Add(boss);
        Add(boss);
        boss.Image = LoadImage("boss.png");
        taustaAjastin.Stop();
        Timer ampuminen = new Timer(1, delegate { Ampuminen(boss, new Vector(-350, 0)); });
        ampuminen.Start();
        boss.IgnoresCollisionResponse = true;

        AddCollisionHandler(boss, "luoti", Osuma);
        Timer luonti = new Timer(6, delegate { liikutettavat.Remove(boss); });
        luonti.Start(1);
        int suunta = 5;
        Timer vertikaaliLiike = new Timer(0.05, delegate {
            if (boss.Y < -120 || boss.Y > 120) suunta *= -1;
            boss.Y += suunta;
        });
        vertikaaliLiike.Start();
    }


    /// <summary>
    /// Bossin törmäyksenkäsittelijä.
    /// Ammus tuhoutuu ja 
    /// jos bossilla on elämiä jäljellä, ne vähenevät.
    /// Jos elämät loppu boss tuhoutuu.
    /// Alus poistuu pelinäkymästä ja peli päättyy.
    /// </summary>
    /// <param name="kohde">Boss.</param>
    /// <param name="ammus">Luoti.</param>
    private void Osuma(PhysicsObject kohde, PhysicsObject ammus)
    {
        ammus.Destroy();
        if (bossElamat > 0)
        {
            bossElamat--;
        }
        else
        {
            kohde.Destroy();
            ClearControls();
            alus.Velocity = new Vector(500, 0);
            Timer tarkistus = new Timer(2.5, delegate {
                if (alus.X > Screen.Right)
                {
                    bossElamat = 20;
                    aliohjelmat.Clear();
                    taustakuvat.Clear();
                    liikutettavat.Clear();
                    ClearAll();
                    Begin();
                }
            });
            tarkistus.Start(1);            
        }
    }


    /// <summary>
    /// Olio2 muodostelman luominen.
    /// Olioita syntyy 2.5 sekunnin välein random paikkaan näytölle.
    /// </summary>
    private void Muodostelma1()
    {
        int Maara = 5;
        Timer muodostelmaAjoitus = new Timer(2.5, delegate { OlionLuonti(-80, RandomGen.NextInt(-250, 300), 100, 100, "olio2.png"); });
        muodostelmaAjoitus.Start(Maara);
    }


    /// <summary>
    /// Olio1 auramuodostelmassa.
    /// Ensimmäisen y sijainti arvotaan,
    /// y sijaintiin lisätään korkeusero,
    /// seuraava aalto syntyy 1.5 sekunnin päästä,
    /// kutsuu VastakkainenPaikka aliohjelmaa, 
    /// joka laskee samaan altoon tulevan toisen olion paikan.
    /// </summary>
    private void Muodostelma2()
    {
        int y = RandomGen.NextInt(-160, 220);
        int korkeusero = 40;
        int kierros = 0;
        int KolmioRivit = 3;
        Timer muodostelmaAjoitus = new Timer(1.5, delegate 
        {
            OlionLuonti(-80, y, 70, 70, "olio1.png");
            if (VastakkainenPaikka(y, korkeusero, kierros) != y)
            {
                OlionLuonti(-80, VastakkainenPaikka(y, korkeusero, kierros), 70, 70, "olio1.png");
            }
            y += korkeusero;
            kierros++;
        });
        muodostelmaAjoitus.Start(KolmioRivit);
    }


    /// <summary>
    /// Laskee ensimmäiseen olioon peilaten
    /// vastakkaisen y sijainnin auran alemmalle oliolle.
    /// </summary>
    /// <param name="y">Ylemmän olion y sijainti.</param>
    /// <param name="h">Auran aaltojen korkeusero.</param>
    /// <param name="kierros">Monesko aalto menossa.</param>
    /// <returns>Alemman olion y sijainnin.</returns>
    private int VastakkainenPaikka(int y, int h, int kierros)
    {
        if (kierros == 0) return y;
        int vastakkainen = y - h * kierros * 2;
        return vastakkainen;
    }

    /// <summary>
    /// Olio3 rintama muodostelma.
    /// Ylimmän olion sijainti arvodaan perustuen olioden määrään ja oliden väliin.
    /// </summary>
    private void Muodostelma3()
    {
        int montakoOlioo = 4;
        int olioVali = 100;
        int y = RandomGen.NextInt(AlinSijainti(montakoOlioo, olioVali), 300);
        for (int i = 0; i < montakoOlioo; i++)
        {
            OlionLuonti(-80, y, 75, 112.5, "olio3.png");
            y -= olioVali;
        }
    }


    /// <summary>
    /// Laskee alimman mahdollisen y sijainnin ensimmäiselle oliolle
    /// perustuen olioden määrään ja
    /// oliden korkeuseroon.
    /// </summary>
    /// <param name="MontakoOlioo">Olioden määrä rintamassa.</param>
    /// <param name="OlioVali">Olioden välin korkeusero.</param>
    /// <returns>Alimman mahdollisen y sijainnin rintamalle.</returns>
    private int AlinSijainti(int MontakoOlioo, int OlioVali)
    {
        int AlinY = -250 + OlioVali * (MontakoOlioo - 1);
        return AlinY;
    }


    /// <summary>
    /// Olio3 matomaisessa muodostelmassa.
    /// Ensimmäisen y sijainti randomoitu,
    /// kierroksesta riipuen lisää seuraavan olion koreuseron
    /// verran ylös tai alas.
    /// </summary>
    private void Muodostelma4()
    {
        int maara = 6;
        int korkeusero = 50;
        int y = RandomGen.NextInt(-250 + korkeusero, 220);
        int kierros = 0;
        Timer muodostelmaAjoitus = new Timer(1.0, delegate
        {
            OlionLuonti(-80, y, 70, 70, "olio1.png");
            if (kierros % 2 == 0)
            {
                y -= korkeusero;
            }
            else y += korkeusero;
            kierros++;
        });
        muodostelmaAjoitus.Start(maara);
    }


    /// <summary>
    /// Lisää ensin muodostelmien aliohjelmakutsuja listalle ja lähtee kutsumaan niitä 6 sekunnin välein.
    /// </summary>
    private void HyokkausJarjestys()
    {
        aliohjelmat.Add(Muodostelma3);
        aliohjelmat.Add(Muodostelma2);
        aliohjelmat.Add(Muodostelma2);
        aliohjelmat.Add(Muodostelma1);
        aliohjelmat.Add(Muodostelma4);
        aliohjelmat.Add(Muodostelma2);
        aliohjelmat.Add(Muodostelma3);
        aliohjelmat.Add(Muodostelma2);
        aliohjelmat.Add(Muodostelma1);
        aliohjelmat.Add(Muodostelma4);
        aliohjelmat.Add(Boss);

        int i = 0;
        Timer hyokkaysvali = new Timer(6, delegate 
        {
            aliohjelmat[i]();            
            i++;
        });
        hyokkaysvali.Start(aliohjelmat.Count);


    }
}
    