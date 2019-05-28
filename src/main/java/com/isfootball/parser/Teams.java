package com.isfootball.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Evgeniy Pshenitsin on 05.01.2015.
 */
public enum Teams {


    TEAM_UNKNOWN(0, "", "", "UNKNOWN", "UNKNOWN"),

    //ИталияСерияА - ItaliyaSeriyaA

    Juventus(100, "Ювентус;ФК Ювентус", "YUventus;Juventus;Juventus Turin","Italy","Италия"),
    Napoli(101, "Наполи;ФК Наполи", "Napoli;SSC Napoli","Italy","Италия"),
    Fiorentina(102, "Фиорентина;ФК Фиорентина", "Fiorentina;AC Fiorentina","Italy","Италия"),
    Latsio(103, "Лацио;ФК Лацио", "Latsio;Lazio;Lazio Roma","Italy","Италия"),
    Milan(104, "Милан;АС Милан;ФК Милан", "Milan;AC Milan;FC MILAN","Italy","Италия"),
    Cesena(105, "Чезена;Цесена;ФК Чезена;Чесена","CHezena;Cesena;AC Cesena","Italy","Италия"),
    Udineze(106, "Удинезе;ФК Удинезе", "Udineze;Udinese;Udinese Calcio","Italy","Италия"),
    Roma(107, "Рома;ФК Рома;АС Рома", "Roma;AS Roma;Rome","Italy","Италия"),
    Parma(108, "Парма;ФК Парма", "Parma;Parma F.C.","Italy","Италия"),
    Inter(109, "Интер;Интер Милан;Интер М;Интернационале;ФК Интер", "Inter;Inter Milan;Internazionale Milan;Internazionale","Italy","Италия"),
    Palermo(110, "Палермо;ФК Палермо", "Palermo;US Palermo","Italy","Италия"),
    Cagliari(111, "Кальяри;ФК Кальяри;Каглиари", "Kal'yari;Cagliari;Cagliari Calcio","Italy","Италия"),
    Kevo(112, "Кьево", "K'evo;AC Chievo Verona;Chievo Verona;Chievo","Italy","Италия"),
    Atalanta(113, "Аталанта;ФК Аталанта", "Atalanta;Atalanta Bergamasca;Atalanta Bergamo","Italy","Италия"),
    Sampdoria(114, "Сампдория;Сампродия;ФК Сампдория", "Sampdoriya;Sampdoria;Sampdoria Genoa","Italy","Италия"),
    Empoli(115, "Эмполи;ФК Эмполи", "Empoli","Italy","Италия"),
    Torino(116, "Торино;ТУРИН;ФК Турин", "Torino;Torino FC;Turin;Turin F.C.","Italy","Италия"),
    Verona(117, "Верона;Эллас Верона;Хеллас", "Verona;Hellas Verona","Italy","Италия"),
    Dzhenoa(118, "Дженоа;ФК Дженоа", "Dzhenoa;Genoa;Genua","Italy","Италия"),
    Sassuolo(119, "Сассуоло;ФК Сассуоло", "Sassuolo;US Sassuolo","Italy","Италия"),

    //РоссияПремьерЛига - RossiyaPrem'erLiga

    CSKA(1000,"ЦСКА;ЦСКА М.;ЦСКА Москва","TSSKA;CSKA Мoscow;CSKA Moscow;CSKA M;ZSKA Moscow;CSKA Moskva","Russia","Россия"),
    TorpedoM(1000,"Торпедо;Торпедо М;Торпедо Москва;ФК Торпедо Москва","Torpedo;Torpedo Moscow","Russia","Россия"),//russia
    Krasnodar(1000,"Краснодар;ФК Краснодар","Krasnodar;FK Krasnodar;FC Krasnodar","Russia","Россия"),
    Zenit(1000,"Зенит;Зенит С-Пб;Зенит Санкт-Петербург;ФК Зенит","Zenit;Zenit ST Petersburg;Zenit St. Petersburg;Zenit St.Petersb.","Russia","Россия"),
    Rubin(1000,"Рубин;Рубин Казань;ФК Рубин","Rubin;Rubin Kazan","Russia","Россия"),
    DinamoM(1000,"Динамо Москва;Динамо М;Динамо","Dinamo;Dynamo Moscow;Dinamo Moscow;FK Dinamo Moscow","Russia","Россия"),
	DinamoR(1000,"Динамо Рига;Динамо Р","DinamoR","Russia","Россия"),
    Kuban(1000,"Кубань;Кубань Краснодар","Kuban';Kuban;Kuban Krasnodar","Russia","Россия"),
    Spartak(1000,"Спартак Москва;Спартак М;Спартак","Spartak;Spartak M;Spartak Moscow","Russia","Россия"),
    Terek(1000,"Терек;Терек Гроэный","Terek;Terek;Terek Grozny;Terek Groznyi;RFK Terek Grozny","Russia","Россия"),
    Lokomotiv(1000,"Локомотив Москва;Локомотив М.;Локомотив М;Локомотив;ФК Локомотив","Lokomotiv;Lokomotiv Moscow;FC Lokomotiv Moscow;Lok. Moscow;Lokomotiv Moskva","Russia","Россия"),
    Rostov(1000,"Ростов;ФК Ростов","Rostov;Rostov FK;FK Rostov","Russia","Россия"),
    Amkar(1000,"Амкар;ФК Амкар","Amkar;Amkar Perm","Russia","Россия"),
    Ural(1000,"Урал;ФК Урал","Ural;Ural S.R;Ural Sverdlovsk Oblast;Ural Yekaterinburg;Ural Ekaterinburg;Ekaterinburg","Russia","Россия"),
    Mordoviya(1000,"Мордовия;ФК Мордовия;Мордовия Саранск","Mordoviya;Mordovia;Mordovia Saransk;Mordoviya;Mordovya Saransk","Russia","Россия"),
	
	//РоссияФНЛ
	
    ArsenalTula(1000,"Арсенал Тула;Арсенал Т;АРСЕНАЛ Т.;Арсенал Т.","FK Arsenal Tula;Arsenal Tula;ArsenalR;Ar. Tula;Arsenal Tula","Russia","Россия"),
    Ufa(1000,"Уфа;ФК Уфа","Ufa;FC Ufa;FK Ufa","Russia","Россия"),
    Anzhi(1000,"Анжи;ФК Анжи","Anzhi;Anzhi Makhachkala;Anzhi Makh;Anji Makhachkala","Russia","Россия"),
    KrylyaSovetov(1000,"Крылья Советов;КрыльяСоветов;Кр. Советов;КРЫЛЬЯ","Kryl'yaSovetov;Samara;Kryliya Sovetov;Krylya Sovetov Samara;Samara Kryliya Sovetov;Samara Kryliya;Krylya Samara;Samara K. Sovetov","Russia","Россия"),
    Tom(1000,"Томь;ФК Томь","Tom';Tom Tomsk;Tom;Tomsk","Russia","Россия"),
    Shinnik(1000,"Шинник;ФК Шинник","SHinnik;Shinnik Jaroslavl;Shinnik;Shinnik Yaroslavl","Russia","Россия"),
    Gazovik(1000,"Газовик;Газовик (Оренбург);Газовик Оренбург;Газовик Ор;ФК Газовик;Газовик Ор.","Gazovik;Gazovik Orenburg;Gazovik Or;Fk Gazovik Orenburg","Russia","Россия"),
    Sibir(1000,"Сибирь","Sibir',Sibir Novosibirsk;FC Sibir Novosibirsk;FC Sibir Novosibirsk;Sibir;Fk Sibir","Russia","Россия"),
    KHimik(1000,"Химик;Химик Дз;Химик Дзержинск;Химик Дз.","KHimik;Khimik Dzerzhinsk;Dzerzhinsk","Russia","Россия"),
    Enisej(1000,"Енисей","Enisej;Krasnoyarsk;Yenisey;FC Yenisey Krasnoyarsk;Yenisey Krasnoyarsk;FK Yenisey Krasnoyarsk;FK Yenisey","Russia","Россия"),
    Tyumen(1000,"Тюмень;ФК Тюмень","Tyumen';Tyumen;FK Tyumen;Fc	Tyumen","Russia","Россия"),
    SkaEnergiya(1000,"Ска Энергия;СКА-Энергия;ЭНЕРГИЯ;СКАЭнергия","SkaEnergiya;Energiya Khabarovsk;SKA Energiya Khabarovsk;Energia Khabarovsk;SKA Energia;FC Ska-Energiya Khabarovsk","Russia","Россия"),
    DinamoSPB(1000,"Динамо Спб.;Динамо СПб;Динамо Санкт-Петербург;ДИНАМО С-П","DinamoSPB;Dynamo St Peter;Dinamo St;Dinamo St. Petersburg","Russia","Россия"), //russia fnl
    Volgar(1000,"Волгарь;Волгарь-Газ.;Волгарь Астрахань;Волгарь-Газпром (Астрахань);Волгарь-Астрахань;Волгарь-Газпром","Volgar';Volgar-Gazprom Astrahan;Volgar-Gazprom;Volgar Astrakhan ","Russia","Россия"),
    Sakhalin(1000,"Сахалин;ФК Сахалин","Sakhalin;Sakhalin Sakhalinsk;FK Sakhalin","Russia","Россия"),
    Tosno(1000,"Тосно;ФК Тосно;Руан Тосно;Руан;Роан","Tosno;FC Tosno;FK Tosno;Ruan-Tosno","Russia","Россия"),
    Baltika(1000,"Балтика;Балтика (Калининград)","Baltika;Baltika Kaliningrad;Kaliningrad","Russia","Россия"),
    LuchEnergiya(1000,"Луч Энергия;Луч-Энергия;ЛУЧ;ЛучЭнергия","LuchEnergiya;Luch-Energia Vladivostok;Luch-E. Vladivostok;Luch Energiya;Luch Energia Vladivostok;LE.Vladivostok","Russia","Россия"),
    Volga(1000,"Волга;Волга (Нижний Новгород);Волга НН;Волга Нижний Новгород;Волга Н.Н.","Volga;Volga Nizhny Novgorod;FK Volga Nischni Novgorod;Volga Novgorod","Russia","Россия"),
    Sokol(1000,"Сокол;Сокол Саратов;Сокол-Саратов","Sokol;Sokol Saratov;FC Sokol Saratov","Russia","Россия"),
	Fakel(3000,"Факел;ФК Факел;Факел Воронеж;Факел (Воронеж);Факел Воронеж (Россия)","Fakel;FC Fakel;Fakel Voronezh;Worenesch;Fakel Worenesch","Russia","Россия"),
	Spartak2(3000,"Спартак 2;Спартак-2;Спартак2;Спартак II (Москва);Спартак М II;Спартак М 2","Spartak2;Spartak 2;Spartak-2;Spartak 2 Moscow;Spartak Moscow II;Spartak 2 М;Spartak Moskva II","Russia","Россия"),
	Zenit2(3000,"Зенит 2;Зенит-2;Зенит 2;Зенит II;Зенит-2 Санкт-Петербург","Zenit2;Zenit 2;Zenit-2;Zenit2;Zenit St.Pet II","Russia","Россия"),
	TorpedoAr(3000,"Торпедо Армавир;ТорпедоАрмавир;Торпедо Ар.;Торпедо Арм.","TorpedoAr;TorpedoArmavir;Torpedo Armavir","Russia","Россия"),
	Kamaz(3000,"Камаз;ФК Камаз;КамАЗ Наб.Челны;КАМАЗ Набережные Челны;КАМАЗ (Набережные Челны)","KAMAZ;KAMAZ Naberezhnye Chelny;Kamaz FK;Kamaz Nab Chelny;Kamaz Chelny;FC Kamaz Naberezhnye Chelny","Russia","Россия"),
	Baikal(3000,"Байкал;ФК Байкал;Радиан-Байкал (Иркутск)","Baikal;Baykal Irkutsk;FC Baykal Irkutsk;FC Baykal;FC Radian Baikal Irkutsk;Baikal Irkutsk;Radian Baikal Irkutsk;Radian-Baikal","Russia","Россия"),
	
	//АнглияПремьерЛига
	
    ManchesterUnited(1000,"Манчестер Юнайтед;Манчестер Юн.;Манчестер Ю;ФК Манчестер Юнайтед;МАН Ю","ManchesterYUnajted;Man United;Manchester Utd;Man Utd;Manchester United","England","Англия"),
    ManchesterSiti(1000,"Манчестер Сити;Манчестер С;ФК Манчестер Сити;МАН СИТИ","ManchesterSiti;Man City;Manchester City","England","Англия"),
    Everton(1000,"Эвертон","Everton;Everton FC","England","Англия"),
    Chelsea(1000,"Челси","CHelsi;Chelsea;Chelsea FC","England","Англия"),
    Arsenal(1000,"Арсенал;Арсенал Л.;Арсенал Лондон;ФК Арсенал Л.","Arsenal;Arsenal FC;Arsenal London;Arsenal (n)","England","Англия"),
    Tottenkhem(1000,"Тоттенхэм Хотспур;Тоттенхем;Тоттенхэм","Tottenkhem;Tottenham;Tottenham Hotspur","England","Англия"), //Тоттенхэм
    VestBromvich(1000,"Вест Бромвич Альбион;Вест Бромвич","VestBromvich;WBA;West Bromwich;West Bromwich Albion;W.B.A;West Brom","England","Англия"), //Вест Бромвич
    Liverpul(1000,"Ливерпуль;ФК Ливерпуль","Liverpul';Liverpool;Liverpool FC","England","Англия"),
    VestKHem(1000,"Вест Хэм Юнайтед;Вест Хем;Вест Хэм","Vest KHem;West Ham;West Ham united;West Ham Utd","England","Англия"),
    Bernli(1000,"Бернли;Бёрнли;ФК Бернли;Барнли","Bernli;Burnley;Burnley FC","England","Англия"),
    Stok(1000,"Сток Сити;Сток;ФК Сток Сити","Stok;Stoke;Stoke City","England","Англия"),
    Nyukasl(1000,"Ньюкасл Юнайтед;Ньюкасл;ФК Ньюкасл Юнайтед","N'yukasl;Newcastle;Newcastle United;Newcastle United F.C.","England","Англия"), //Ньюкасл
    Suonsi(1000,"Суонси Сити;Суонси;ФК Суонси Сити","Suonsi;Swansea;Swansea City","England","Англия"),
    Sautgempton(1000,"Саутгемптон;ФК Саутгемптон","Sautgempton;Southampton","England","Англия"),
    Sanderlend(1000,"Сандерленд;Сандерлэнд;ФК Сандерленд","Sanderlend;Sunderland;Sunderland AFC","England","Англия"),
    Lester(1000,"Лестер Сити;Лестер;ФК Лестер Сити;ЛЕСТЕР","Lester;Leicester;Leicester City","England","Англия"),
    AstonVilla(1000,"Астон Вилла","AstonVilla;Aston Villa","England","Англия"),
    Hull(1000,"Халл Сити;Халл;ФК Халл Сити","KHall;Hull;Hull City","England","Англия"), //Халл
    Kristall(1000,"Кристал Пэлэс;Кристал Пэлас;Кристал Пэлэс (Лондон);Кристалл Пэлэс;Кр. Пэлэс;КРИСТАЛ;Кристалл Пэлас","Kristall;Crystal Palace;C Palace","England","Англия"), //Кристал
	QuinsParkReigers(2000,"Куинз Парк Рейнджерс;Куинс Парк Рэйнджерс;ФК Куинз Парк;КПР;Квинс Парк Р.;Куинз Парк Рэйнджерс", "Quins Park Rangers;QPR;Queens Park Rangers;KPR;Queens Park R.;Queens Park;Q.P.R.","England","Англия"),
	
	//АнглияЧемпионсшип
	
    Fulkhem(1000,"Фулхэм;ФК Фулхэм;Фулхем","Fulkhem;Fulham","England","Англия"),
	Norvich(1000,"Норвич;Норвич Сити (Норидж);Норвич Сити","Norvich;Norwich;Norwich City;Norwich (n)","England","Англия"),
    Kardiff(1000,"Кардифф;ФК Кардифф Сити;Кардифф Сити","Kardiff;Cardiff;Cardiff City","England","Англия"),
    Reding(1000,"Рединг;Ридинг;ФК Рединг","Reding;Reading","England","Англия"),
    Lids(1000,"Лидс;Лидс Юнайтед;ФК Лидс Юнайтед","Lids;Leeds;Leeds United","England","Англия"),
    Nottingem(1000,"Ноттингем;Ноттингем Ф;Ноттингем Форест;ФК Ноттингем Форест","Nottingem;Nottingham Forest;Nottingham","England","Англия"),
    Uigan(1000,"Уиган;Уиган Атлетик;ФК Уиган Атлетик","Uigan;Wigan;Wigan Athletic","England","Англия"),
    Blekpul(1000,"Блэкпул;ФК Блэкпул","Blekpul;Blackpool;Blackpool","England","Англия"),
    Blekbern(1000,"Блекберн;Блэкберн;Блэкберн Роверс;ФК Блэкберн Роверс","Blekbern;Blackburn;Blackburn Rovers","England","Англия"),
    Ipsvich(1000,"Ипсвич;Ипсвич Таун;ФК Ипсвич Таун","Ipsvich;Ipswich;Ipswich Town","England","Англия"),
    Brajton(1000,"Брайтон;Брайтон и Хоув Альбион;Брайтон энд Хоув Альбион;Бартон Альбион;ФК Брайтон энд Хоув Альбион","Brajton;Brighton;Brighton & Hove Albion;Burton;Burton Albion","England","Англия"),
    Huddersfield(1000,"Хаддерсфилд;Хаддерсфилд Таун;ФК Хаддерсфилд Таун","KHaddersfild;Huddersfield;Huddersfield Town","England","Англия"),
    Bornmut(1000,"Борнмут;ФК Борнмут;Боурнмут","Bornmut;Bournemouth;AFC Bournemouth;Bournemouth AFC","England","Англия"),
    Middlsbro(1000,"Миддлсбро;Мидлсбро;ФК Мидлсбро","Middlsbro;Middlesbrough","England","Англия"),
    Uotford(1000,"Уотфорд;ФК Уотфорд","Uotford;Watford","England","Англия"),
    Milluol(1000,"Миллуол;Миллуолл;Милуол;ФК Миллуол","Milluol;Millwall","England","Англия"),
    Brentford(1000,"Брентфорд;Бредфорд;Бредфорд Сити;ФК Брентфорд;Брэдфорд Сити","Brentford;Bradford City;Bradford","England","Англия"),
    Vulverkhempton(1000,"Вулверхэмптон;ФК Вулвергемптон;Вулверхэмптон Уондерерз;Вулвергемптон Уондерерс","Vulverkhempton;Wolves;Wolverhampton;Wolverhampton Wanderers","England","Англия"),
    Derbi(1000,"Дерби;Дерби Каунти;ФК Дерби Каунти","Derbi;Derby;Derby County","England","Англия"),
    Birmingem(1000,"Бирмингем;Бирмингем Сити;ФК Бирмингем Сити","Birmingem;Birmingham;Birmingham City","England","Англия"),
    Bolton(1000,"Болтон;ФК Болтон Уондерерс;Болтон Уондерерз;ФК Болтон Уондерз","Bolton;Bolton Wanderers","England","Англия"),
    Sheffield(1000,"Шеффилд;ФК Шеффилд Юнайтед;Шеффилд Юн.;Шеффилд Уэнсдей;ФК Шеффилд Уинсдей;Шеффилд Юнайтед;Шеффилд Уэнсдэй;Шеффилд У;ФК Шеффилд Уэнсдей;Шеффилд Уэнс.","SHeffild;Sheffield United;Sheffield Utd;Sheff Utd;Sheffield;Sheff Wed;Sheffield Wednesday;Sheffield Wed","England","Англия"),
    CHarlton(1000,"Чарльтон;Чарльтон Атлетик;Чарльтон Атлетик (Лондон);ФК Чарльтон Атлетик","CHarl'ton;Charlton;Charlton Athletic","England","Англия"),
    Rotterkhem(1000,"Роттерхэм;ФК Ротерхэм Юнайтед;Ротерхэм;Ротерхэм Юнайтед;Ротерем Юнайтед;Ротерэм Юнайтед","Rotterkhem;Rotherham;Rotherham United;Rotherham Utd","England","Англия"),
	
	//ГерманияБундеслига
	
    Bavariya(1000,"Бавария;Бавария (Мюнхен);Байерн Мюнхен;Байерн Мюнхен;Бавария	М","Bavariya;Bayern München;Bayern Munchen;FC Bayern Munchen;Bayern Munich;B. Munich;B Munich","Germany","Германия"),
    Bayer(1000,"Байер;Байер (Леверкузен);Байер Л;Байер 04;Байер Леверкузен;ЛЕВЕРКУЗЕН;Байер Л.","Bajer;Bayer Leverkusen;Bayer Leverkusen;Bayer 04 Leverkusen;Leverkusen","Germany","Германия"),
    BorussiyaD(1000,"Боруссия Д;Боруссия Дортмунд;Боруссия (Дортмунд);Дортмунд","BorussiyaD;Borussia Dortmund;Borussia Dortmund;Dortmund","Germany","Германия"),
    SHalke(1000,"Шальке;Шальке 04","SHal'ke;Schalke 04;Schalke;FC Schalke 04","Germany","Германия"), //шальке 04
    BorussiyaM(1000,"Боруссия М;Боруссия (Мёнхенгладбах);БоруссияМ;Боруссия M;Боруссия Менхенгладбах;Мюнхенгладбах;Боруссия;М'ГЛАДБАХ","BorussiyaM;BorussiyaM;Monchengladbach;Borussia Monchengladbach;Borussia Mönchengladbach;Borussia Mönchengladbach;Borussia Mgladbach;Mönchengladbach;Mgladbach;M'gladbach;Borussia Moenchengladbach","Germany","Германия"),
    Gannover(1000,"Ганновер;Ганновер 96;ФК Ганновер-96;ФК Ганновер 96","Gannover;Hannover 96;Hannover;Gannover","Germany","Германия"),
    Ajntrakht(1000,"Айнтрахт;Айнтрахт (Франкфурт);Айнтрахт Фр.;Айнтрахт Франкфурт;Айнтрахт Ф;Айнтрахт Фр","Ajntrakht;Eintracht Frankfurt;E. Frankfurt","Germany","Германия"),
    Majnts(1000,"Майнц;Майнц 05","Majnts;Mainz;Mainz 05;FSV Mainz 05;1 FSV Mainz 05","Germany","Германия"),
    Frajburg(1000,"Фрайбург;ФК Фрайбург","Frajburg;Freiburg;SC Freiburg;SC Freiburg","Germany","Германия"),
    Gamburg(1000,"Гамбург;ФК Гамбург","Gamburg;Hamburg;Hamburger SV","Germany","Германия"),
    Keln(1000,"Кельн;ФК Кёльн;Кёльн","Kel'n;Koln;FC Koln;1 Cologne;Köln;Cologne;FC Köln;1. FC Köln;1. FC Cologne;FC Cologne","Germany","Германия"),
    Gerta(1000,"Герта;Герта Берлин;Герта (Берлин);ГЕРТА БСК","Gerta;Hertha Berlin;Hertha BSC;Hertha;Hertha BSC Berlin","Germany","Германия"),
    Hoffenheim(1000,"Хоффенхайм;Хоффенхайм (Зинсхайм);Хоффенхейм","KHoffenkhajm;Hoffenheim;TSG Hoffenheim;1899 Hoffenheim;TSG 1899 Hoffenheim","Germany","Германия"),
    Stuttgart(1000,"Штутгарт;Штуттгарт","SHtutgart;Stuttgart;VfB Stuttgart","Germany","Германия"),
    Verder(1000,"Вердер;Вердер Бремен;БРЕМЕН","Verder;Werder Bremen;Bremen;W Bremen","Germany","Германия"),
    Augsburg(1000,"Аугсбург;ФК Аугсбург;АУГСБУРГЕР","Augsburg;FC Augsburg","Germany","Германия"),
    Volfsburg(1000,"Вольфсбург;ФК Вольфсбург","Vol'fsburg;Wolfsburg;VfL Wolfsburg;Volfsburg","Germany","Германия"),
    Pateborn(1000,"Патеборн;ФК Падерборн;Падерборн;Падерборн 07;Падерборон;ПАДЕБОРН","Pateborn;Paderborn;SC Paderborn 07;Paderborn 07;SC Paderborn","Germany","Германия"),
	
	//Германия2Бундеслига
	
    Kajzerslautern(1000,"Кайзерслаутерн;ФК Кайзерслаутерн","Kaiserslautern;1. FC Kaiserslautern;Kaiserslautern;Kajzerslautern;1 FC Kaiserslautern","Germany","Германия"),
    Sandkhauzen(1000,"Сандхаузен;ФК Зандхаузен;Зандхаузен;Сандхаусен","Sandkhauzen;Sandhausen;Sandkhauzen;SV Sandhausen","Germany","Германия"),
    Ergebirge(1000,"Эргебирге;Эрцгебирге Ауе;Эрцгебирге Ауэ;Эрцгебирге;Ауэ","Ergebirge;Erzgebirge Aue;Ауе;Aue","Germany","Германия"),
    Karlsrue(1000,"Карлсруэ;ФК Карлсруэ;КАРЛСРУХЕР СК","Karlsrue;Karlsruher;Karlsruher SC;Karlsruhe","Germany","Германия"),
    SanktPauli(1000,"Санкт-Паули;Ст. Паули","Sankt-Pauli;St. Pauli;FC ST Pauli;St Pauli;St. Pauli;SanktPauli","Germany","Германия"),
    Fortuna(1000,"Фортуна;Фортуна Д;Фортуна Дюссельдорф;Фортуна Дюс;ДЮССЕЛЬДОРФ","Fortuna;Fortuna Dusseldorf;Fortuna Düsseldorf;Fortuna Dusseldorf Am;Dusseldorf","Germany","Германия"),
    Fyurt(1000,"Фюрт;Гройтер Фюрт;Грёйтер Фюрт (Фюрт);ГРЕУТЕР ФЮРТ;Грейтер Фюрт","Fyurt;Greuther Furth;SpVgg Greuther Fürth;Furth;Greuther Fuerth","Germany","Германия"),
    UnionBerlin(1000,"Унион Берлин;УнионБерлин;Унион (Берлин);Юнион Берлин;Унион","UnionBerlin;Union Berlin;1. FC Union Berlin;FC Union Berlin;1. FC Union Berlin","Germany","Германия"),
    Myunkhen(1000,"Мюнхен;Мюнхен 1860;ФК Мюнхен-1860;1860 МЮНХЕН","Myunkhen;TSV 1860 Munchen;TSV 1860 München;1860 Munich;1860 Munchen;1860 Muenchen","Germany","Германия"),
    Nyurnberg(1000,"Нюрнберг;ФК Нюрнберг","Nyurnberg;1.FC Nurnberg;Nurnberg;FC Nurnberg;FC Nürnberg;1. FC Nürnberg;1. FC Nuremberg;Nuremberg","Germany","Германия"),
    Frankfurt(1000,"Франкфурт;ФСВ Франкфурт;FSV Фракнкфурт;ФК Франкфурт","Frankfurt;FSV Frankfurt","Germany","Германия"),
	Braunshvejg(1000,"Брауншвейг;Айнтрахт Бр;Айнтрахт Б.;ФТ Брауншвейг;Айнтрахт Брауншвейг","Eintracht Braunschweig;FT Braunschweig;Braunschweig;Eintracht Braunschweig;Braunshvejg;Eintr. Braunschweig","Germany","Германия"),
    Aalen(1000,"Аален;ФК Аален;Ален","Aalen;VfR Aalen","Germany","Германия"),
    KHaidenkhajm(1000,"Хаиденхайм;кельнхайм;ФК Хейденхайм;ФК Хайденхайм 1846;Хайденхайм;ФК Хайденхам;ХЕЙДЕНХЕЙМ","KHaidenkhajm;FC Heidenheim;FC Heidenheim;Heidenheim;Heidenheim 1846;1. FC Heidenheim","Germany","Германия"),
    Darmshtadt(1000,"Дармштадт;Дармштадт 98;ФК Дармштадт 98","Darmshtadt;SV Darmstadt 98;Darmstadt;Darmstadt 98","Germany","Германия"),
    Bokhum(1000,"Бохум;ФК Бохум","Bokhum;VFL Bochum;Bochum","Germany","Германия"),
    Lejptsig(1000,"Лейпциг;РБ Лейпциг","Lejptsig;RB Leipzig;RasenBallsport Leipzig","Germany","Германия"),
    Ingolshtadt(1000,"Ингольштадт;Ингольштадт 04;ФК Ингольштадт;ФК Ингольштадт-04","Ingol'shtadt;Ingolshtadt;FC Ingolstadt 04;Ingolstadt 04;Ingolstadt;FC Ingolstadt","Germany","Германия"),
	
	//ЮжнаяКорея
	
    Pokhang(1000,"Поханг;ФК Похан Стилерс;Пхохан Стилерс;Похан;ФК Пхохан Стилерс","Pokhang;Pohang;Pohang Steelers","South Korea","Южная Корея"),
    Tag(3000,"Тэгу;ФК Тэгу","Tag;Daegu FC","South Korea","Южная Корея"),
    DaejeonCitizen(3000,"Тэджон;ФК Тэджон Ситизен;Тэчжон;Тэджон Ситизен","DaejeonCitizen;Daejeon Citizen FC;Daejeon Citizen","South Korea","Южная Корея"),
    Dzheonbuk(1000,"Джеонбук;Чонбук;Чонбук Моторс (Чонджу);Чонбук Моторз;Чжонбук;Чонбук Хёндэ Моторс","Dzheonbuk;Jeonbuk;Jeonbuk Motors;Jeonbuk	Hyundai	Motors;Jeonbuk Motors Dzheonbuk;Jeonbuk	Hyundai","South Korea","Южная Корея"),
    CHannam(1000,"Чаннам;Чхоннам Драгонс;Чуннам;Чуннам Драгонс (Гвангянг);Чуннам Драгонс;ДРЭГОНЗ","CHannam","South Korea","Южная Корея"),
    CHzhechzhuYUnajted(1000,"ЧжечжуЮнайтед;ФК Чеджу Юнайтед;Чжэжу Юнайтед;Чеджу Юнайтед;Чжечжу Юнайтед;ЧЕДЖУ ЮН","CHzhechzhuYUnajted;Jeju Utd;Jeju United","South Korea","Южная Корея"),
    Suvon(1000,"Сувон;ФК Сувон Самсунг Блувингз;Сувон Блюуингз;Сувон Самсунг Блюуингз","Suvon;Suwon;Suwon City","South Korea","Южная Корея"),
    Ulsan(1000,"Ульсан;ФК Ульсан Хёндэ;УЛСАН ХЮНДАЙ","Ul'san;Ulsan Hyundai;Ulsan Hyundai Horang-i;Ulsan","South Korea","Южная Корея"),
    Busan(1000,"Бусан;ФК Пусан И-Парк;ФК Пусан Киотонг;Пусан И.Парк;Пусан Ай'Парк;Пусан;Пусан Ай Парк;БУСАН И ПАРК;Бусан И.Парк","Busan;Busan I.Park;Busan lPark","South Korea","Южная Корея"),
    Seongnam(1000,"Сеонгнам;ФК Соннам Ильва Чунма;Соннам Ильва;Сеннам Ильва;ФК Соннам Ильхва Чхонма;СОННАМ","Seongnam;Seongnam Ilhwa","South Korea","Южная Корея"),
    Gieongnam(1000,"Гиеонгнам","Gieongnam;Jeonnam","South Korea","Южная Корея"),
    Sanguj(1000,"Сангуй;Санджу Сангму Феникс;Санджу Cанму","Sanguj;Sangju Sangmu;Sangju Sangmu Phoenix;Sangju Sangmu P.","South Korea","Южная Корея"),
	Seul(1000,"Сеул;ФК Сеул;Сеул Е-Ланд ФК;Сеул ФК","Seul;FC Seoul;Seoul;Seoul E-Land FC","South Korea","Южная Корея"),
	InchonYUnajted(1000,"Инчон Юнайтед;ИнчонЮнайтед;Инчхон Юнайтед;ИНЧХОН ЮН;","InchonYUnajted;Incheon;Incheon Utd;Incheon United","South Korea","Южная Корея"),
	Gwangju(3000,"Кванджу;Кванджу ФК;ФК Кванджу;Кванчжу","Gwangju;Gwangju FC","South Korea","Южная Корея"),
	
	//ИспанияПримера
	
    Barselona(1000,"Барселона;ФК Барселона","Barselona;Barcelona;FC Barcelona","Spain","Испания"),
    Real(1000,"Реал Мадрид;Реал;Реал М;ФК Реал Мадрид","Real;Real Madrid","Spain","Испания"), //Реал
    Atletiko(1000,"Атлетико;Атлетико Мадрид;Атлетико М;Атлетико М.","Atletiko;Atletico Madrid;Atl. Madrid;Atlético Madrid;At. Madrid","Spain","Испания"),
    Valensiya(1000,"Валенсия;ФК Валенсия","Valensiya;Valencia;Valencia CF","Spain","Испания"),
    Sosedad(1000,"Сосьедад;Реал Сосьедад;Реал С","Sos'edad;Real Sociedad;R. Sociedad;Sociedad","Spain","Испания"),
    Sevilya(1000,"Севилья;ФК Севилья","Sevil'ya;Sevilla;FC Sevilla;Sevilla FC;Sevilya;Seville","Spain","Испания"),
    Malaga(1000,"Малага;ФК Малага","Malaga;Málaga;Malaga CF","Spain","Испания"),
    RayoValekano(1000,"РайоВальекано;Райо Вальекано;Р. Вальекано;ВАЛЛЕКАНО","RajoVal'ekano;Rayo Vallecano;Vallecano","Spain","Испания"),
    Khetafe(1000,"Хетафе;ФК Хетафе","KHetafe;Getafe;Getafe CF","Spain","Испания"),
    Levante(1000,"Леванте","Levante;Levante UD","Spain","Испания"),
    Deportivo(1000,"Депортиво;Депортиво Ла Корунья;ФК Депортиво Ла-Корунья;Депортиво Ла-Корунья","Deportivo;Deportivo La Coruña;La Coruna;Deportivo La Coruna","Spain","Испания"),
    Atletik(1000,"Атлетик;Атлетик Б;ФК Атлетик Бильбао;Атлетик Бильбао;АТ. БИЛЬБАО;Атлетик Б.","Atletik;Atletic Bilbao;Athletic Bilbao;Athletic Club Bilbao;Ath.Bilbao;At. Bilbao;Ath Bilbao;Atl. Bilbao","Spain","Испания"),
    Eibar(1000,"Эйбар;Эибар;ФК Эйбар","Ejbar;Eibar;SD Eibar","Spain","Испания"),
    Espanol(1000,"Эспаньол;Эспаньол (Барселона)","Espan'ol;Espanyol;RCD Espanyol;Espanol;Espanyol Barcelona","Spain","Испания"),
    Selta(1000,"Сельта;Сельта Виго;СЕЛЬТА ДЕ ВИГО","Sel'ta;Celta Vigo;Celta de Vigo;Celta","Spain","Испания"),
    Kordoba(1000,"Кордоба;ФК Кордова","Kordoba;Cordoba;Córdoba CF;Cordoba CF;Córdoba;CF Cordoba","Spain","Испания"),
    Vilyarreal(1000,"Вильярреал;ФК Вильярреал;ФК ВИЛЬЯРЕАЛ;Вильярреаль","Vil'yarreal;Villarreal;CF Villarreal;Villarreal CF","Spain","Испания"),
    Elche(1000,"Эльче;ФК Эльче","El'che;Elche;CF Elche;Elche CF","Spain","Испания"),
    Almeriya(1000,"Альмерия;ФК Альмерия;АЛМЕРИЯ","Al'meriya;Almeria;UD Almeria","Spain","Испания"),
    Granada(1000,"Гранада;Гранада (Испания);ФК Гранада","Granada;Granada CF","Spain","Испания"),
	
	//ИспанияСегунда
	
    Osasuna(1000,"Осасуна","Osasuna;CA Osasuna","Spain","Испания"),
    Valyadolid(1000,"Вальядолид;Реал Вальядолид","Val'yadolid;Real Valladolid;Valladolid","Spain","Испания"),
    Rekreativo(1000,"Рекреативо;Рекреативо Уэльва","Rekreativo;Recreativo Huelva;Recreativo de Huelva;Recreativo","Spain","Испания"),
    Lugo(1000,"Луго;ФК Луго","Lugo;CD Lugo;Lugo","Spain","Испания"),
    LasPalmas(1000,"Лас Пальмас;ЛасПальмас;ФК Лас-Пальмас;Лас-Пальмас;ЛАС ПАЛМАС","LasPal'mas;Las Palmas;Deportiva Las Palmas;Las Palmas UD","Spain","Испания"),
    Malorka(1000,"Мальорка;Мальорка (Пальма-де-Мальорка);ФК Мальорка","Mal'orka;Mallorca;RCD Mallorca","Spain","Испания"),
    Tenerife(1000,"Тенерифе;Тенерифа;Гранадилла Тенерифе","Tenerife;Tenerife CD;CD Tenerife;Teneriffa","Spain","Испания"),
    Ponferradina(1000,"Понферрадина;ФК Понферрадина","Ponferradina","Spain","Испания"),
    Numansiya(1000,"Нумансия","Numansiya;Numancia;Numancia CD","Spain","Испания"),
	SportingGijon(1000,"Спортинг Х;Спортинг Хихон;Реал Спортинг (Хихон);Спортинг;ХИХОН;Спортинг Х.","Sporting;Sporting Gijon;Sporting de Gijon;Gijon;Sporting G;Sp Gijon","Spain","Испания"),
    Sabadell(1000,"Сабаделл;Сабадел;ФК Сабадель;Сабадель","Sabadell;CE Sabadell;Sabadell FC;Sabadell","Spain","Испания"),
    Albasete(1000,"Альбасете;ФК Альбасете;АЛБАСЕТЕ","Al'basete;Albacete Balompie;Albacete","Spain","Испания"),
    Leganes(1000,"Леганес;ФК Леганес","Leganes","Spain","Испания"),
    Alkorkon(1000,"Алькоркон;ФК Алькоркон;Алкоркон","Al'korkon;AD Alcorcon;Alcorcon;Alcorcón","Spain","Испания"),
    Mirandes(1000,"Мирандес;ФК Мирандес","CD Mirandes;Mirandes;Mirandés","Spain","Испания"),
    Llagostera(1000,"Ллагостера;Льягостера","Llagostera;UE Llagostera","Spain","Испания"),
    Betis(1000,"Бетис;Реал Бетис;БЕТИС СЕВИЛЬЯ","Betis;Real Betis;Betis Sevilla","Spain","Испания"),
    Saragosa(1000,"Сарагоса;ФК Реал Сарагоса;ФК Сарагоса","Saragosa;Real Zaragoza;Zaragoza","Spain","Испания"),
    Alaves(1000,"Алавес;ФК Алавес","Alaves;CD Alaves;Deportivo Alaves","Spain","Испания"),
    BarselonaB(1000,"БарселонаБ;Барселона II;Барселона B;Барселона Б;ФК Барселона II;Барселона В;Барселона 2","BarselonaB;Barcelona-B;FC Barcelona B;Barcelona B;Barcelona B FC;Barcelona II;Barcelona Atl.","Spain","Испания"),
    Zhirona(1000,"Жирона;ФК Жирона;ГИРОНА","ZHirona;Girona;Girona FC","Spain","Испания"),
    Rasing(1000,"Расинг;Расинг Сантандер","Rasing;Racing Santander;R. Santander","Spain","Испания"),
	Huesca(1000,"Уэска;ФК Уэска","Huesca;FC Huesca;SD Huesca","Spain","Испания"),
	Oviedo(1000,"Овьедо;ФК Овьедо;Реал Овьедо","Oviedo;FC Oviedo;Real Oviedo","Spain","Испания"),
	
	//Австралия
	
    Sydney(1000,"Сидней;Сидней ФК;ФК Сидней","Sidnej;Sydney;Sydney FC","Australia","Австралия"),
    MelburnViktori(1000,"МельбурнВиктори;Мельбурн Виктори;ФК Мельбурн Виктори;МЕЛЬБУРН В","Mel'burnViktori;Melbourne Victory","Australia","Австралия"),
    Brisben(1000,"Брисбен;Брисбен Роар;ФК Брисбен Роар","Brisben;Brisbane Roar;Brisbane Roar FC","Australia","Австралия"),
    PertGlori(1000,"Перт Глори;ПертГлори;ФК Перт Глори;ПЕРТ","PertGlori;Perth Glory;Perth","Australia","Австралия"),
    CentralKoast(1000,"Централ Коаст;Централ Кост;Сентрал Кост;ЦентралКоаст;Централ Кост Маринерс Академи","TSentralKoast;Central Coast;Central Coast Mariners","Australia","Австралия"),
    NyukaslDzhets(1000,"Ньюкасл Джетс;НьюкаслДжетс;ФК Ньюкасл Джетс","N'yukaslDzhets;Newcastle Jets;Newcastle Jets FC;NyukaslDzhets","Australia","Австралия"),
    VestSydney(1000,"Вест Сидней;Вестерн Сидней Уондерерз;ФК Вестерн Сидней Уондерерс;ВЕСТЕРН СИДНЕЙ;Вестерн Сидней Уондерерс","Vest Sidnej;Western Sydney;W. Sydney;Western Sydney Wanderers FC","Australia","Австралия"),
    AdelaidaUnited(1000,"АделаидаЮнайтед;Аделаида Юнайтед;Аделаида Юн;ФК Аделаида Юнайтед;АДЕЛАИДА;Аделаида Юн.","AdelaidaYUnajted;Adelaide United;Adelaide Utd;Adelaide","Australia","Австралия"),
    Vellington(1000,"Веллингтон;Веллингтон Феникс;ФК Веллингтон Феникс;Веллингтон Финикс","Vellington;Wellington Phoenix FC;Wellington Phoenix;Wellington","Australia","Австралия"),
    MelburnHart(1000,"Мельбурн Харт;Мельбурн Сити;МельбурнХарт;ФК Мельбурн Сити;МЕЛЬБУРН","Mel'burnKHart;Melbourne City;Melbourne City FC","Australia","Австралия"),
	
	//ИталияСерияБ
	
    Katanya(1000,"Катанья;Катания;Катаниа;ФК Катания","Katan'ya;Catania;Catania Calcio","Italy","Италия"),
    Bolonya(1000,"Болонья;ФК Болонья","Bolon'ya;Bologna;Boulogne","Italy","Италия"),
    Lansiano(1000,"Лансиано;Ланчиано;Виртус;Виртус Ланчано;Ланчано","Lansiano;Lanciano;Virtus Lanciano;SS Virtus Lanciano 1924;","Italy","Италия"),
    Avellino(1000,"Авеллино;ФК Авеллино","Avellino;US Avellino;AS Avellino 1912;Avellino Calcio 1912","Italy","Италия"),
    Krotone(1000,"Кротоне;ФК Кротоне","Krotone;Crotone","Italy","Италия"),
    Latina(1000,"Латина;Латино;ФК Латина","Latina;US Latina Calcio;Latina Calcio","Italy","Италия"),
    Livorno(1000,"Ливорно;ФК Ливорно","Livorno;AS Livorno","Italy","Италия"),
    Vareze(1000,"Варезе;Варесе;ФК Варезе","Vareze;Varese;AS Varese;S Varese 1910","Italy","Италия"),
    Spetsiya(1000,"Специя;ФК Специа;Специя 1906;Специа;ФК Специя","Spetsiya;Spezia;Spezia Calcio","Italy","Италия"),
    Peskara(1000,"Пескара;ФК Пескара","Peskara;Pescara;US Pescara;Pescara Calcio","Italy","Италия"),
    Frozinone(1000,"Фрозиньоне;Фрозиноне;Фросиноне;ФК Фрозиноне","Frozin'one;Frosinone;Frozinone;Frosinone Calcio","Italy","Италия"),
    Modena(1000,"Модена;ФК Модена","Modena","Italy","Италия"),
    Trapani(1000,"Трапани;ФК Трапани","Trapani;Trapani Calcio","Italy","Италия"),
    Breshiya(1000,"Брешия;Брешиа;Брешиа Кальчо;ФК Брешиа","Breshiya;Brescia;Brescia Calcio","Italy","Италия"),
    Kapri(1000,"Капри;Карпи;Карпи ФК;ФК Капри;ФК Карпи","Kapri;Carpi;Carpi FC 1909","Italy","Италия"),
    CHittadella(1000,"Читтаделла;Ситтадела;ФК Читтаделла","CHittadella;Cittadella;AS Cittadella","Italy","Италия"),
    Ternana(1000,"Тернана","Ternana;Ternana Calcio","Italy","Италия"),
    Bari(1000,"Бари;ФК Бари","Bari;AS Bari","Italy","Италия"),
    Verchelli(1000,"Верчелли;Про Верчелли;ФК Про Верчелли;Верцелли","Verchelli;Pro Vercelli;FC Pro Vercelli 1892","Italy","Италия"),
    Entella(1000,"Энтелла;Виртус Энтелла (Кьявари);Виртус Энтелла","Entella;Virtus Entella","Italy","Италия"),
    Perudzha(1000,"Перуджа;ФК Перуджа","Perudzha;Perugia;Perugia Calcio Spa","Italy","Италия"),
    Vichentsa(1000,"Виченца;ФК Виченца Кальчо","Vichentsa;Vicenza;Vicenza Calcio","Italy","Италия"),
	Ascoli(1000,"Асколи;ФК Асколи","Ascoli;FC Ascoli","Italy","Италия"),
	Novara(1000,"Новара;ФК Новара","Novara,FC Novara;Novara Calcio","Italy","Италия"),
	Lumezzane(1000,"Люмеццане;ФК Люмеццане;Люмеззане","Lumezzane;FC Lumezzane;C Lumezzane","Italy","Италия"),
	Akragas(1000,"Акрагас;ФК Акрагас","Akragas;FC Akragas","Italy","Италия"),
	Como(1000,"Комо;ФК Комо","Como;FC Como;Calcio Como;Como Calcio","Italy","Италия"),
	Salernitana(1000,"Салернитана;ФК Салернитана","Salernitana;FC Salernitana;Salernitana Calcio 1919","Italy","Италия"),
	
	//ФранцияЛига1
	
    PSZH(1000,"ПСЖ;Пари Сен-Жермен","PSZH;PSG;Paris SG;Paris Saint Germain;Paris Saint-Germain;Paris St Germain;Paris St-G;Paris St.-Germain","France","Франция"),
    Nitstsa(1000,"Ницца;Ница","Nitstsa;Nice;OGC Nice","France","Франция"),
    Monpele(1000,"Монпелье","Monpel'e;Montpellier;Montpellier HSC","France","Франция"),
    SentEten(1000,"Сент-Этьен;Сент;Сент Этьен;Сэнт-Этьен;СТ. ЭТЬЕН;Ст.Этьен","Sent-Et'en;Saint-Etienne;St Etienne;St. Etienne;Saint-Étienne;AS Saint-Etienne;Saint Etienne;AS St. Etienne","France","Франция"),
    Lion(1000,"Лион;ФК Лион","Lion;Lyon;Olympique Lyonnais;Olympique Lyon;Ol. Lyon","France","Франция"),
    Loryan(1000,"Лорьян;Лорьен;Лориент","Lor'yan;Lorient","France","Франция"),
    Marsel(1000,"Марсель;Олимпик Марсель","Marsel';Marseille;Olympique de Marseille;Ol. Marseille","France","Франция"),
    Lill(1000,"Лилль","Lill';Lille;OSC Lille;Lille OSC","France","Франция"),
    Lans(1000,"Ланс;ФК Ланс","Lans;Lens;RC Lens","France","Франция"),
    Rejms(1000,"Реймс;Рейм","Rejms;Reims;Stade Reims;Stade de Reims","France","Франция"),
    Bastiya(1000,"Бастия;Бастиа;СК Бастия","Bastiya;SC Bastia;Bastia","France","Франция"),
    Kan(1000,"Кан;Каен","Kan;Caen;SM Caen","France","Франция"),
    Bordo(1000,"Бордо","Bordo;Bordeaux;Girondins de Bordeaux","France","Франция"),
    Tuluza(1000,"Тулуза;ФК Тулуза","Tuluza;Toulouse;Toulouse HB","France","Франция"),
    Renn(1000,"Ренн","Renn;Rennes;Stade Rennais;Stade Rennes","France","Франция"),
    Nant(1000,"Нант","Nant;Nantes","France","Франция"),
    Metts(1000,"Метц;Мец;ФК Мец","Metts;Metz","France","Франция"),
    Gengam(1000,"Генгам;Гуингам","Gengam;Guingamp;EA Guingamp","France","Франция"),
    Evian(1000,"Эвиан;Эвьян;Эвиан Тонон Гальяр;ЭВИАН ТГФК","Evian;Evian Thonon Gaillard;Evian TGFC;Evian TG;Evian TG FC;Evian Thonon G.","France","Франция"),
    Monako(1000,"Монако","Monako;Monaco;AS Monaco;AS Monaco FC","France","Франция"),
	Paris(3000,"ФК Париж;Париж;Париж 98","Paris;Paris FC","France","Франция"),
	
	//ФранцияЛига2
	
    Sosho(1000,"Сошо","Sosho;Sochaux;FC Sochaux Montbeliard","France","Франция"),
    Valansen(1000,"Валансьен;Валенсьен","Valans'en;Valenciennes","France","Франция"),
    Oser(1000,"Осер","Oser;Auxerre;AJ Auxerre","France","Франция"),
    Klermon(1000,"Клермон","Klermon;Clermont;Clermont Foot","France","Франция"),
    Dizhon(1000,"Дижон","Dizhon;Dijon","France","Франция"),
    Nansi(1000,"Нанси;Нанси-Лоррен","Nansi;Nancy;AS Nancy","France","Франция"),
    Trua(1000,"Труа","Trua;Troyes;ESTAC Troyes","France","Франция"),
    Ayachcho(1000,"Аяччо","Ayachcho;Ajaccio;AC Ajaccio","France","Франция"),
    Arl(1000,"Арль;Арль-Авиньон;Арле;АРЛЕС","Arl';Arles;AC Arles Avignon;Arles Avignon;AC Arles;Arles-Avignon","France","Франция"),
    Anzher(1000,"Анжер;Анже","Anzher;Angers;SCO Angers","France","Франция"),
    Orlean45(1000,"Орлеан45;Орлеан;Орлеан 45;ОРЛЕАНС","Orlean45;Orleans;US Orleans;US Orleans 45;Orléans","France","Франция"),
    Nor(1000,"Ньор","N'or;Niort;Chamois Niortais;Nor;Chamois","France","Франция"),
    Tur(1000,"Тур","Tur;Tours;Tour","France","Франция"),
    Nim(1000,"Ним;Ним Олимпик (Ним);Ним Олимпик (Ним)","Nim;Nimes;Nimes Olympique;Nîmes Olympique;Olympique Nimes","France","Франция"),
    Kretel(1000,"Кретель;Кретей-Люзитано;ФК Кретей;КРЕТЕЙЛ","Kretel';Creteil;Créteil","France","Франция"),
    Laval(1000,"Лаваль;ЛАВАЛ;ФК Лаваль","Laval';Laval;Stade Lavallois;Stade Lavallois MFC;Stade Laval","France","Франция"),
    Gavr(1000,"Гавр;ХАВРЕ;ФК Гавр","Gavr;Le Havre","France","Франция"),
    GFKAyachcho(1000,"ГФКАяччо;Газелек Аяччо;Газелек (Аяччо);ГФКО Аяччо","GFKAyachcho;Gazelec Ajaccio;Gazélec Ajaccio;GFC Ajaccio;Ajaccio GFCO;GFCO Ajaccio","France","Франция"),
    Brest(1000,"Брест;Стаде Брест;Стад Брест 29","Brest;Brest;Stade Brestois 29","France","Франция"),
    Shatoru(1000,"Шатору","SHatoru;Chateauroux;Châteauroux;LB Chateauroux","France","Франция"),
	RedStar(1000,"Ред Стар;ФК Ред Стар;Ред Стар Париж","Red Star;FC Red Star;Red Star FC 93;Red Star Saint Ouen;Red Star 93","France","Франция"),
	
	//ПортугалияСуперлига
	
    Akademika(1000,"Академика;Академика К;Академика Куимбра","Akademika;Academica;Academica Coimbra","Portugal","Португалия"),
    Aruka(1000,"Арука;Арока;Ароука;ФК Арока;АРО","Aruka;Arouca;FC Arouca","Portugal","Португалия"),
    Belenensesh(1000,"Белененсеш;Белененсиш Лиссабон;Белененсиш","Belenensesh;Belenenses Lissabon;CF Belenenses;Belenenses","Portugal","Португалия"),
    Benfika(1000,"Бенфика","Benfika;Benfica Lissabon;Benfica;Benfica (n)","Portugal","Португалия"),
    Boavishta(1000,"Боавишта;ФК Боавишта Порту;Боавишта	Порту","Boavishta;Boavista Porto;Boavista","Portugal","Португалия"),
    Eshtoril(1000,"Эшторил;ФК Эшторил","Eshtoril;Estoril","Portugal","Португалия"),
    Portu(1000,"Порту;ФК Порто;Порто;ФК Порту","Portu;FC Porto;Porto","Portugal","Португалия"),
    ZHilVisente(1000,"Жил Висенте;ЖилВисенте","ZHilVisente;Gil Vicente","Portugal","Португалия"),
    Maritimu(1000,"Маритиму;Маритимо;Маритимо (Фуншал)","Maritimu;Maritimo Funchal;Maritimo","Portugal","Португалия"),
    Morejrenshe(1000,"Морейренше;Морейренсе","Morejrenshe;Moreirense FC;Moreirense","Portugal","Португалия"),
    Nasonal(1000,"Насьональ;Националь;Национал;Насьонал Мадейра;НАСЬОНАЛ;Национал М.","Nas'onal';Nacional Montevideo;Nacional Madeira;CD Nacional;Nacional","Portugal","Португалия"),
    PasushFerrejra(1000,"ПасушФеррейра;Пасуш Ферейра;Пасош Феррейра;Пасуш ди Феррейра;Феррейра;ПАКОШ ФЕРРЕЙРА","PasushFerrejra;Pacos de Ferreira;Pacos Ferreira;FC Pacos Ferreira;Ferreira","Portugal","Португалия"),
    Penafiel(1000,"Пенафиель;Пенафиэль;Пенафил;Пенафьель;Пенафиел;Пеньяфиль","Penafiel';Penafiel","Portugal","Португалия"),
    RioAve(1000,"Рио Аве;Риу Аве;Риу-Аве (Вила-ду-Конди);Риу Ави","Rio Ave;RioAve;Rio Ave FC","Portugal","Португалия"),
    Braga(1000,"Спортинг Брага;Брага;ФК Брага;СпортингБрага","SportingBraga;Sporting Braga;Braga","Portugal","Португалия"),
	Sporting(3000,"Спортинг;Спортинг Л;ФК Спортинг Л;Спортинг Л.","Sporting Lissabon;Sporting Lisbon;Sporting","Portugal","Португалия"),
    VitoriyaG(1000,"ВиторияГ.;Гимараенш;Витория Гимарайнш;ВИТОРИЯ Г;Гимараеш", "VitoriyaG.;Vitoria Guimaraes;V. Guimaraes;Guimaraes;Vitoria de Guimaraes","Portugal","Португалия"),
    VitoriyaS(1000,"ВиторияС.;Сетубал;Витория Сетубал;Витория (Сетубал);ВИТОРИЯ С", "VitoriyaS.;Vitoria Setúbal;Vitoria Setubal;Setubal;Vitoria de Setubal","Portugal","Португалия"),
	
	//Португалия2девизион
	
    AkademikoVizeo(1000,"Академико Визео;АкадемикоВизео;Академика Визеу;Академику Визеу;ВИЗЕУ","AkademikoVizeo;Ac. Viseu;Viseu;Academico Viseu;Academico de Viseu","Portugal","Португалия"),
    Avesh(1000,"Авеш;Авиш;Авес","Avesh;Aves;CD Aves;Deportivo Aves","Portugal","Португалия"),
    Atletiku(1000,"Атлетику;Атлетико КП;Атлетику Л","Atletiku;Atletico CP;Atletico Clube de Portugal;Atlético CP","Portugal","Португалия"),
    BeiraMar(1000,"Бейра-Мар;Бейра Мар","Bejra-Mar;Beira Mar;Beira-Mar","Portugal","Португалия"),
    BenfikaB(1000,"БенфикаБ;Бенфика Б;Бенфика II;Бенфика Лиссабон II","BenfikaB;Benfica B;Benfica II;Benfica (R)","Portugal","Португалия"),
    BragaB(1000,"БрагаБ;Брага Б;Спортинг Брага II;Брага II","BragaB;Sporting Braga B;Braga B","Portugal","Португалия"),
    Kovilya(1000,"Ковилья;Ковильян;Спортинг Ковильян","Kovil'ya;Covilha;Sporting Covilha","Portugal","Португалия"),
    Leyshoesh(1000,"Лейшоеш;Лейшойнш (Матозиньюш);Лейкшоеш;Лейшойнш;ЛЕЙКШЕС","Lejshoesh;Leixões;Leixoes","Portugal","Португалия"),
    MaritimuB(1000,"МаритимуБ;Маритиму Б;Маритиму II;МАРИТИМО Б","MaritimuB;Maritimo B","Portugal","Португалия"),
    Oliveyrenshe(1000,"Оливейренше;Оливейренсе","Olivejrenshe;Oliveirense","Portugal","Португалия"),
    Olhanense(1000,"Ольяненше;Олханенсе","Ol'yanenshe;Olhanense","Portugal","Португалия"),
    OrientalLissabon(1000,"ОриенталЛиссабон;Ориентал;Ориентал Лиссабон","OrientalLissabon;Clube de Lisboa;Oriental","Portugal","Португалия"),
    Portimonenshe(1000,"Портимоненше;Портимоненсе","Portimonenshe;Portimonense","Portugal","Португалия"),
    PortuB(1000,"ПортуБ;ФК Порто II;Порто II;Порту Б;Порту II;ПОРТО Б","PortuB;FC Porto B;Porto B","Portugal","Португалия"),
    SantaKlara(1000,"Санта Клара;Санта-Клара","Santa Klara;Santa Clara;CD Santa Clara","Portugal","Португалия"),
    SportingB(1000,"СпортингБ;Спортинг II (Лиссабон);Спортинг Л II;Спортинг Б;Спортинг Лиссабон II;Спортинг II","SportingB;Sporting Lisbon B;Sporting L II;Sporting Lisbon B;Sporting L. B;Sporting CP B;Sp Lisbon B","Portugal","Португалия"),
    Tondela(1000,"Тондела","Tondela;CD de Tondela;CD Tondela","Portugal","Португалия"),
    Trofense(1000,"Трофенсе","Trofense","Portugal","Португалия"),
    UniauMadejra(1000,"Униау Мадейра;Унио Мадейра;Мадейра;Униан Мадейра;УНИАО;Униао Мадейра","Uniau Madejra;Uniao Madeira;Uniao da Madeira","Portugal","Португалия"),
    Farenshe(1000,"Фаренше;Фаренсе;Фейренсе","Farenshe;Sporting Farense;Farense","Portugal","Португалия"),
    Feyrenshe(1000,"Фейренше","Fejrenshe;Feirense","Portugal","Португалия"),
    Freamunde(1000,"Фреамунде;Фримунде","Freamunde","Portugal","Португалия"),
    Chavesh(1000,"Чавеш;Дешпортиву Шавиш;Шавиш;ЧАВЕС","CHavesh;Chaves;Desportivo de Chaves;GD Chaves;Desportivo Chaves","Portugal","Португалия"),
    VitoriyaG2(3000,"Витория Гимарайнш II;Гимараенш Б;ГИМАРАЙШ;Гимараеш II","VitoriyaG II;Vitoria Guimaraes B;Guimaraes B;Vitoria de Guimaraes B","Portugal","Португалия"),
	
	//ШвейцарияСуперлига
	
    Bazel(1000,"Базель;ФК Базель","Bazel';Basel;FC Basel","Switzerland","Швейцария"),
    Lyutsern(1000,"Люцерн","Lyutsern;Luzern;Lucerne","Switzerland","Швейцария"),
    YoungBoys(1000,"Янг Бойз;ЯНГ БОЙЗ БЕРН","YAng Bojz;Young Boys;Young Boys Bern","Switzerland","Швейцария"),
    Grasshoppers(1000,"Грассхопперс;Грассхоппер;ГК ЦУРИХ;Грассхопер","Grasskhoppers;Grasshopper;Grasshoppers Zurich;Grasshoppers Zürich;Grasshopper Club;Grasshoppers","Switzerland","Швейцария"),
    SentGallen(1000,"Сент Галлен;Ст. Галлен;ФК Сент-Гален;Санкт Галлен;Сент-Галлен;Санкт-Галлен;Ст.Галлен","Sent Gallen;St. Gallen;St.Gallen","Switzerland","Швейцария"),
    Thun(1000,"Тюн;Тун","Tyun;Thun","Switzerland","Швейцария"),
    Zurich(1000,"Цюрих;ФК Цюрих","TSyurikh;FC Zurich;Zurich;FC Zürich","Switzerland","Швейцария"),
    Arau(1000,"Арау;АAРАУ","Arau;Aarau","Switzerland","Швейцария"),
    Sion(1000,"Сьон;ФК СИОН;Сион;ФК Сьон","S'on;Sion;FC Sion","Switzerland","Швейцария"),
    Vaduts(1000,"Вадуц;Вадуз;ФК ВАДУЦ","Vaduts;Vaduz;FC Vaduz","Switzerland","Швейцария"),
	Lugano(1000,"Лугано;ФК Лугано","Lugano;FC Lugano","Switzerland","Швейцария"),
	
	//ГрецияСуперлига
	
    PAOK(1000,"ПАОК;ПАОК Салоники","PAOK;Paok Thessaloniki;PAOK Saloniki;PAOK Thessaloniki FC","Greece","Греция"),
    Olimpiakos(1000,"Олимпиакос;Олимпиакос Пирей;ПИРЕЙ;ФК Олимпиакос","Olimpiakos;Olympiakos CFP;Olympiakos;Olympiacos Piraeus;Olympiacos FC;Olympiacos","Greece","Греция"),
	OlimpiakosV(3000,"Олимпиакос (Волос);Олимпиакос В.","ASK Olimpiakos Volou;Olimpiakos Volou","Greece","Греция"),
    Apollo(3000,"Аполлон;Аполлон См;ФК Аполлон Смирнис","Apollo;Apollon Smyrni;Apollon;Apollon Smyrnis","Greece","Греция"),
    YAnnina(1000,"Яннина;ПАС Джаннина (Янина);Янина;ПАС Джаннина;ПАС Янина","YAnnina;Giannina;PAS Giannina","Greece","Греция"),
    Atromitos(1000,"Атромитос;Атромитос Атинон (Афины);Атромитос Афины;Атромитос Атинон","Atromitos;Atromitos Athinon;Atromitos Athens","Greece","Греция"),
    Levadiakos(1000,"Левадиакос;Левадеякос","Levadiakos;Levadiakos","Greece","Греция"),
    Ksanti(1000,"Ксанти;Шкода Ксанти","Ksanti;Xanthi;Xanthi Skoda;Skoda Xanthi;Skoda Xanthi FC","Greece","Греция"),
    Asteras(1000,"Астерас;Астерас Триполис (Триполи);Астерас Триполис;ТРИПОЛИС;ФК Астерас","Asteras;Asteras Tripolis","Greece","Греция"),
    Panetolikos(1000,"Панетоликос","Panetolikos;Panaitolikos","Greece","Греция"),
    OFIKrit(1000,"ОФИ Крит;ОФИ;КРИТ","OFI Krit;OFI;OFI Crete","Greece","Греция"),
    Panatinaikos(1000,"Панатинаикос;Панатинаикос Афины","Panatinaikos;Panathinaikos;Panathinaikos Athen","Greece","Греция"),
    Ergotelis(1000,"Эрготелис;ФК Эрготелис","Ergotelis;Ergotelis;Ergotelis Chania","Greece","Греция"),
    Platanias(1000,"Платаниас","Platanias","Greece","Греция"),
    Panionios(1000,"Паниониос","Panionios;Panionios;Panionios Athen;Panionios Athens","Greece","Греция"),
    Pantrakikos(1000,"Пантракикос","Pantrakikos;Panthrakikos;Panthrakikos Komotini;Panthrakikos F.C.","Greece","Греция"),
    Kalloni(1000,"Каллони;АЕЛ Каллонис;Каллони Аел;Каллонис","Kalloni;Kallonis;AEL Kalloni;Kalloni AEL FC;Kalloni Lekanopedio","Greece","Греция"),
    Kerkira(1000,"Керкира;ФК Керкира;Кассиопи;Керкира/Кассиопия","Kerkira;Kerkyra;AOK Kerkyra","Greece","Греция"),
    Veriya(1000,"Верия;Вероя","Veriya;Veria;Veroia;Veria FC","Greece","Греция"),
    Niki(1000,"Ники;Ники Волоу;Ники Волос;ВОЛУ;ФК Ники","Niki;Niki Volou","Greece","Греция"),
	Iraklis(3000,"Ираклис;ФК Ираклис;Ираклис 1908;Ираклис 1908 Салоники","Iraklis;Iraklis 1908 FC;Iraklis 1908;Pae Iraklis 1908","Greece","Греция"),
	AEK(3000,"АЕК;ФК АЕК;АЕК Афины;АЕК Аф","AEK;FC AEK;AEK	Athens;AEK Athens FC","Greece","Греция"),
	
	//БельгияПремьерЛига
	
    Varegem(1000,"Варегем;Зюлте-Варегем;ФК Зульте-Варегем;Зульте Варегем;ФК Варегем;Зулте-Варегем","Varegem;Zulte-Waregem;Zulte Waregem;Zulte Waregem;Varegem;SV Zulte Waregem;Zulte","Belgium","Бельгия"),
    Genk(1000,"Генк;Расинг Генк;ФК Генк","Genk;Genk KRC;KRC Genk","Belgium","Бельгия"),
    Bryugge(1000,"Брюгге;ФК Брюгге","Bryugge;Club Brugge;Club Brugge KV;Club Brugge;Bryugge;Club Bruges","Belgium","Бельгия"),
    CercleBrugge(3000,"Серкль;Серкль Брюгге","Cercle Brugge;Cercle Brugge KSV","Belgium","Бельгия"),
    Anderlekht(1000,"Андерлехт;Андерлехт (Брюсcель)","Anderlekht;Anderlecht;RSC Anderlecht","Belgium","Бельгия"),
    Lokeren(1000,"Локерен","Lokeren;KSC Lokeren;Sporting Lokeren","Belgium","Бельгия"),
    StandartL(1000,"СтандартЛ;Стандард;Стандарт Л;Стандард Льеж","StandartL;Standard;Standard de Liege;Standard Liege","Belgium","Бельгия"),
    Kortrijk(1000,"Кортрийк;Кортрейк;ФК Кортрейк","Kortrijk;Kortrijk;KV Kortrijk","Belgium","Бельгия"),
    Gent(1000,"Гент","Gent;Gent KAA;AA Gent;AA Gent;Gent;KAA Gent","Belgium","Бельгия"),
    Muskron(1000,"Мускрон;Мускрон-Перувельц;Перувельс;Мускрон-Перувельз;ФК Мускрон","Muskron;Mouscron;Royal Mouscron Peruwelz;Mouscron-Peruwelz;Mouscron-Péruwelz;Royal Mouscron","Belgium","Бельгия"),
    Lierse(1000,"Льерс","L'ers;Lierse;Lierse SK","Belgium","Бельгия"),
    Sharlerua(1000,"Шарлеруа","SHarlerua;Charleroi;Charleroil;Royal Charleroi;R. Charleroi;Sporting Charleroi","Belgium","Бельгия"),
    Ostende(1000,"Остендэ;Остенде","Ostende;Oostende;KV Oostende;KV Oostende;Ostende","Belgium","Бельгия"),
    Sarkl(1000,"Саркль","Sarkl';Cercle Brugge;Cercle Brugge;Sarkl;Cercle Bruges;Cercle Bruges K.S.V.","Belgium","Бельгия"),
    Mekhelen(1000,"Мехелен;КВ Мехелен;ФК Мехелен","Mekhelen;KV Mechelen;Mechelen","Belgium","Бельгия"),
    Vaasland(1000,"Ваасланд;Васланд;Васланд-Беверен;Васланд (Беверен);Ваасланд-Беверен","Vaasland;Waasland;Waasland-Beveren","Belgium","Бельгия"),
    Vesterlo(1000,"Вестерло","Vesterlo;Westerlo;Westerlo;Vesterlo","Belgium","Бельгия"),
	
	//УкраинаПремьерЛигаПерваяЛига
	
    ShakhterD(1000,"Шахтер ДН;Шахтёр (Донецк);Шахтёр Донецк;Шахтер Д;Шахтер Донецк","Shakhtar Donetsk (n);SHakhterD;Shaktar Donetsk;Shakhtar Donetsk;FC Shakhtar Donetsk;Shakhtar","Ukraine","Украина"),
    Metallist(1000,"Металлист;Металлист Харьков","Metallist;Metalist;Metalist Kharkiv","Ukraine","Украина"),
    MetallurgD(1000,"МеталлургД;Металлург Д;Металлург Донецк","MetallurgD;Metalurg D;Metalurg Donetsk;Metalurg D.","Ukraine","Украина"),
    DinamoKiev(1000,"Динамо Киев;Динамо К","Dinamo K;Dinamo Kiev;Dynamo Kyiv;Dynamo Kiev;Dyn. Kiev","Ukraine","Украина"),
    ChernomoretsO(1000,"ЧерноморецО;Черноморец Одесса;Черноморец О;ЧЕРНОМОРЕЦ;ФК Черноморец;Черноморец Од","CHernomoretsO;Chernomorets Od.;Chernomorets Odessa;Chernomorets","Ukraine","Украина"),
    MetallurgZ(1000,"МеталлургЗ;Металлург З;Металлург Запорожье;Металлург Зп","MetallurgZ;Met. Zaporizhya;Metalurg Zaporizhya;Metalurg Z.;Metalurg Zaporizhzhya;Metalurg Zaporizhia","Ukraine","Украина"),
    Zarya(1000,"Заря;Заря Лг;Заря Луганск","Zarya;Zorya;Zorya Lugansk","Ukraine","Украина"),
    Goverla(1000,"Говерла;Говерла-Закарпатье;Говерла Ужгород","Goverla;Hoverla;FK Goverla Uzhgorod;Zakarpatia Uzgorod;Hoverla Uzhhorod;Hoverla Zakarpattia Uzhhorod;Goverla-Uzhgorod;Goverla-Zakarpattia","Ukraine","Украина"),
    Ilichevets(1000,"Ильичевец","Il'ichevets;Illichivec;Illichivets","Ukraine","Украина"),
    Karpaty(1000,"Карпаты;Карпаты Львов","Karpaty;Karpaty Lviv","Ukraine","Украина"),
    Vorksla(1000,"Ворксла;Ворскла;Ворскла Полтава","Vorksla;Vorskla;Vorskla Poltava","Ukraine","Украина"),
    OlimpikDn(1000,"Олимпик Дн;Олимпик Донецк","Olimpik;Olimpik Donetsk;Ol. Donetsk","Ukraine","Украина"),//ukraine
    Volyn(1000,"Волынь","Volyn';Volyn Lutsk;Volyn","Ukraine","Украина"),
    Dnepr(1000,"Днепр;Днепр Дп;Днепр Днепропетровск","Dnepr;Dnipro;Dnjepr Dnjprpropetrovsk;Dnipro Dnipropetrovsk;Dnjepropetrowsk;Dnepropetrovsk;FC Dnipro Dnipropetrovsk","Ukraine","Украина"),
    Ternopol(1000,"Тернополь;ФК Тернополь","Ternopol';FC Ternopil","Ukraine","Украина"),
    Aleksandriya(1000,"Александрия;ОЛЕКСАНДРИЯ","Aleksandriya;FC Olexandria;FC Oleksandria","Ukraine","Украина"),
    StalA(1000,"Сталь А;Сталь Алчевск","Stal' A;Stal","Ukraine","Украина"),
    Desna(1000,"Десна","Desna","Ukraine","Украина"),
    Gornyak(1000,"Горняк;Горняк Кр. Рог;Горняк-Кривой Рог;ГОРНЯК КР","Gornyak;FC Hirnyk","Ukraine","Украина"),
    Neftyanik(1000,"Нефтяник;Нефтяник А;Нефтяник-Укрнефть","Neftyanik;Naftovik","Ukraine","Украина"),
    Bukovina(1000,"Буковина","Bukovina;Bukovyna Chernivtsi","Ukraine","Украина"),
    ZvezdaKd(1000,"ЭвездаК-д;Звезда К-д;Звезда Кировоград;ЗИРКА","EvezdaK-d;Zirka","Ukraine","Украина"),
    GornyakS(1000,"ГорнякС;;Горняк Спорт;Горняк-Спорт Комсомольск;ГИРНИК","GornyakS;FC Hirnyk-Sport","Ukraine","Украина"),
    Sumy(1000,"Сумы;ФК Сумы","Sumy;FC Sumy","Ukraine","Украина"),
    StalD(1000,"СтальД;Сталь Дндзж;Сталь Днепродзержинск","Stal'D;Stal Dneprodzhezhinsk;Stal Dniprodzerzhynsk;Stal Dneprodzerzh.;Stal Dnepro;Stal Dneprodz.","Ukraine","Украина"),
    Poltava(1000,"Полтава;ФК Полтава","Poltava","Ukraine","Украина"),
    Gelios(1000,"Гелиос","Gelios;Helios Kharkiv","Ukraine","Украина"),
    Niva(1000,"Нива;Нива Терн;Нива Тернополь","Niva;Nyva Ternopil","Ukraine","Украина"),
    Nikolaev(1000,"Николаев;ФК Николаев;МФК Николаев","Nikolaev;MFK Mykolaiv","Ukraine","Украина"),
    DinamoK2(1000,"Динамо2;Динамо-2 Киев;ДИНАМО КИЕВ 2","Dinamo2;Dynamo Kyiv II","Ukraine","Украина"),
	
	//ДанияПремьерЛига
	
    Midtyulend(1000,"Мидтюленд;Мидтьюленд;Мидтъюлланн;Мидтьюлланд;Митъюлланд;ФК МИДТЮЛЛАНД;Мидтиланд","Midtyulend;Midtjylland;FC Midtjylland;Midtjylland","Denmark","Дания"),
    Olborg(1000,"Ольборг;ОЛЬБОРГ БК;ФК Ольборг","Ol'borg;Aalborg;AaB Aalborg;Aalborg BK;AaB Aalborg;Olborg;AaB","Denmark","Дания"),
    Kopengagen(1000,"Копенгаген;ФК Копенгаген","Kopengagen;Copenhagen;FC Copenhagen","Denmark","Дания"),
    Brondbyu(1000,"Брондбю","Brondbyu;Brøndby IF;Brondby IF;Brondby;Brondbyu","Denmark","Дания"),
    Renders(1000,"Рэндерс;Рандерс;Раннерс;ФК Рандерс;РАНДЕРС ФК","Renders;Randers;Randers FC","Denmark","Дания"),
    Nordsellend(1000,"Нордсьелленд;Нордшелланд;Норшелланн;Нордсьелланд","Nords'ellend;Nordsjaelland;FC Nordsjlland;Nordsjlland;Nordsjalland;FC Nordsjaelland","Denmark","Дания"),
    Odense(1000,"Оденсе","Odense;Odense BK;OB Odense;OB","Denmark","Дания"),
    Silkeborg(1000,"Силькеборг;СИЛЬКЕБОРГ ИФ;ФК Силькеборг","Sil'keborg;Silkeborg IF;Silkeborg","Denmark","Дания"),
    Hobro(1000,"Хобро","KHobro;Hobro;Hobro IK","Denmark","Дания"),
    Vestselland(1000,"Вестсьелланд;Вестшелланн;Вестшелланн (Слагельсе);Вестсьяэлланд;ВЕСТСЯЭЛЛАНД","Vests'elland;FC Vestsjaelland;FC Vestsjlland;Vestsjaelland;Vestsjlland","Denmark","Дания"),
    Esberg(1000,"Эсбьерг","Esb'erg;Esbjerg;Esbjerg;Esberg;Esbjerg FB","Denmark","Дания"),
    Senderijske(1000,"Сендерийске;Сондерийске;Сеннерйюск;Сондерьюске;Сённерйюск;ФК Сённерйюск;СЕНДЕРЮСКЕ","Senderijske;SønderjyskE;SonderjyskE;SonderjyskE;Sönderjyske;SoenderjyskE","Denmark","Дания"),
	
	//ФинляндияПремьерЛига
	
    KHIK(1000,"ХИК;Хельсинки","KHIK;HJK;HJK Helsinki","Finland","Финляндия"),
    MyuPA(1000,"МюПА","MyuPA","Finland","Финляндия"),
    InterTurku(1000,"Интер Турку;Интер (Турку);Интер Т;ИнтерТурку","InterTurku;FC Inter Turku;Inter Turku","Finland","Финляндия"),
    VPS(1000,"ВПС;ВПС Вааса;Вааса;Вааса ПС","VPS;Vaasa;Vaasa VPS","Finland","Финляндия"),
    Mariekhamn(1000,"Мариехамн;Марийхамн;ИФК Мхамн;ИФК Мариехамн;Марьяхамн;Марианхамн","Mariekhamn;IFK Mariehamn;Mariehamn","Finland","Финляндия"),
    Kotka(1000,"Хонка;КоТеПе (Котка);КТП Котка;Коткан ТП;КТП","KHonka;FC KTP;KTP;KooTeePee;Honka","Finland","Финляндия"),
    Lakhti(1000,"Лахти","Lakhti;FC Lahti;Lahti","Finland","Финляндия"),
    SIK(1000,"СИК;ФК Сейняйоки;Сейняйоки;Сейнейоки;СЕЙНЯОЕН","SIK;Seinajoen JK;Seinäjoen JK;SJK;Seinajoen;Seinaejoen JK","Finland","Финляндия"),
    RoPS(1000,"РоПС;РоПС (Рованиеми);Рованием ПС;РованиемиПС","RoPS;RoPS Rovaniemi;Rovaniemi PS","Finland","Финляндия"),
    KuPS(1000,"КуПС;КуПС (Куопио);КуопиоПС","KuPS;KuPS Kuopio;Kuopio","Finland","Финляндия"),
    Yaro(1000,"Яро;Йаро","YAro;FF Jaro;Jaro;Jaro FF","Finland","Финляндия"),
	Ilves(3000,"Ильвес;ФК Ильвес;Ильвес-Кассат (Тампере);Ильвес Тампере","Ilves;Ilves Tampere;Ilves Kissat;Fc Ilves;Tampereen Ilves","Finland","Финляндия"),
	XIFK(3000,"ХИФК;ХИФК (Хельсинки)","XIFK;HIFK;HIFK Helsinki","Finland","Финляндия"),
	
	//Марокко
	
    VidadKosoblanka(1000,"Видад Кособланка;ВидадКасабланка","VidadKosoblanka;Wydad Casablanca","Morocco","Марокко"),
    ShabibAtlas(1000,"Шабиб Атлас;ШабибАтлас;Шабаб Атлас Хенифра","SHabibAtlas;Chabab Atlas Khenifra","Morocco","Марокко"),
    Diffaa(1000,"Диффаа;Дифаа Эль-Джадида;Дифаа Эль Джадида","Diffaa;Difaa El Jadida","Morocco","Марокко"),
    Kavkab(1000,"Кавкаб;Кавкаб Марракеш","Kavkab;KACM","Morocco","Марокко"),
    MAS(1000,"КАС;МАС Фес","MAS Fes;MAS","Morocco","Марокко"),
    Magrib(1000,"Магриб;Магриб Фес","Magrib","Morocco","Марокко"),
    Mogreb(1000,"Могреб;Могреб Тетуан;Атлетик Тетуан","Mogreb;MAT Tetouan","Morocco","Марокко"),
    OlimpikSofi(1000,"Олимпик Софи;ОлимпикСофи;Олимпик Сафи","OlimpikSofi;Olympic Club de Safi","Morocco","Марокко"),
    OlimpikKhouribga(1000,"Олимпик Хурибга;ОлимпикХ;Хурибга","OlimpikKH;OCK Khouribga","Morocco","Марокко"),
    Razha(1000,"Ража;Раджа Касабланка;РКА Раджа Касабланка","Razha;Raja Casablanca","Morocco","Марокко"),
    RSB(1000,"РСБ;РСБ Беркане;БЕРКАН;Ренессанс Беркан","RSB;RSB Berkane","Morocco","Марокко"),
    Ittikhad(1000,"Иттихад;Иттихад Хемиссет;ИТИХАД","Ittikhad;Al Ittihad;IZK","Morocco","Марокко"),
    FAR(1000,"ФАР;ФАР Рабат","FAR;FAR Rabat","Morocco","Марокко"),
    FUS(1000,"ФУС;ФУС (Рабат);ФЮС Рабат;ФУС Рабат","FUS;FUS Rabat","Morocco","Марокко"),
    Cassani(1000,"Хассания;Хассания Агадир;Хассани Агадир","KHassaniya;Hassania Agadir","Morocco","Марокко"),
    Kenitra(3000,"Кенитра","Kenitra;KAC Kenitra;KAS","Morocco","Марокко"),
    Shabib(3000,"Шабаб Э-Хосейма;ХОСЕЙМА;Шабаб Риф Аль Хосейма","SHabib;Chabab Rif Al Hoceima;Chabab Rif Hoceima;Chabab Hoceima;CRA de Hoceima","Morocco","Марокко"),
	IttihadTanger(3000,"Иттихад Танжер;ФК Иттихад Танжер","Ittihad Tanger;FC Ittihad Tanger","Morocco","Марокко"),
	
	//ИзраильПремьерЛига
	
    MakkabiT(1000,"Маккаби Т-А;МаккабиТ;Маккаби Тель-Авив;Маккаби Т.А.;Маккаби Т-А.","MakkabiT;Tel Aviv;Maccabi Tel Aviv","Israel","Израиль"),
    Ashdot(1000,"Ашдот;Ашдод","Ashdot;Ashdod;Ashdod SC","Israel","Израиль"),
    KHapoelB(1000,"ХапоэльБ-Ш;Хапоэль Б;Хапоэль Б.Ш.;Хапоэль Беэр-Шева;ШЕВА;Хапоэль Б-Ш.","KHapoel'B;H. Beer Sheva;Hapoel Beer Sheva","Israel","Израиль"),
    KHapoelT(1000,"Хапоэль Т;ХапоэльТ;ФК Хапоэль Тзафририм Холон;Хапоэль Т.А.;Хапоэль Тель-Авив;Хапоэль Т-А.","KHapoel'T;Hapoel Tel Aviv","Israel","Израиль"),
    BnejSakhnin(1000,"Бней Сахнин;Бней-Сахнин;Хапоэль Бней Сахнин","Bnej Sakhnin;Bnei Sakhnin;Bnei Sachnin","Israel","Израиль"),
    KHapoelR(1000,"ХапоэльР;Хапоэль Раанана;РААНАНА;ФК Хапоэль Раанана","KHapoel'R;H. Raanana;Hapoel Raanana;Hap. Raanana","Israel","Израиль"),
    MakkabiKH(1000,"Маккаби Хайфа;МаккабиХайфа;Маккаби Х;Маккаби Х.;Маккаби Хф.","MakkabiKH;Maccabi Haifa;Haifa","Israel","Израиль"),
    KHapoelAkko(1000,"Хапоэль Акко;ХапоэльАкко;АККО","KHapoel'Akko;H. Akko;Hapoel Ironi Acre","Israel","Израиль"),
    KHapoelKH(1000,"Хапоэль Ирони Кирьят-Шмона;Хапоэль Ирони К-Ш;Хапоэль Ирони (Кирьят-Шмона);ХапоэльК.Ш.;Ирони КШ;Ирони Кирьят-Шмона;КИРЬЯТ;Кирьят Шмона","KHapoel'K.Sh.;Hapoel Kiryat Shmone;Hapoel Ironi Kiryat Shmona;Hapoel Kiryat Shmona;Ironi Kiryat Shmona","Israel","Израиль"),
    KHapoelPetahTikva(1000,"ХапоэльП.Т.;Хапоэль П.Т.;Хапоэль Петах-Тиква","KHapoel'P.T.;Hapoel Petach Tikva","Israel","Израиль"),
    BejtarI(1000,"БейтарИ;Беитар Ирони Маале Адумим;Бейтар И;Бейтар Иерусалим;БЕЙТАР;Бейтар Й.","BejtarI;Beitar Jerusalem;Jerusalem","Israel","Израиль"),
    MakkabiNetanya(1000,"МаккабиН.;Маккаби Ирони (Нетивот);Маккаби Н;Маккаби Нетания;НЕТАНИЯ","MakkabiN.;M. Netanya;Maccabi Netanya;Maccabi Ironi kiryat Ataа","Israel","Израиль"),
    MakkabiPT(1000,"МаккабиПТ;Маккаби Петах-Тиква;Маккаби П-Т;Маккаби П;Маккаби ПТ;Маккаби П-Т.","MakkabiPT;Maccabi Petach Tikva;M Petach Tikva;Petach Tikva;Maccabi Petah Tikva","Israel","Израиль"),
    KHapoelX(3000,"Хапоэль Х;Хапоэль Хайфа;ХапоэльХ;Хапоэль Хф.","KHapoelX;H Haifa;Hapoel Haifa","Israel","Израиль"),
	BneiYehuda(3000,"Бней Йегуда;ФК Бней Йегуда;Бней Иегуда","Bnei Yehuda;FC Bnei Yehuda","Israel","Израиль"),
	
	//Голландия
	
    Ayaks(1000,"Аякс","Ayaks;Ajax;Ajax Amsterdam","Netherlands","Голландия"),
    Vitess(1000,"Витесс","Vitess;Vitesse;Vitesse Arnhem","Netherlands","Голландия"),
    Tvente(1000,"Твенте;ФК Твенте","Tvente;Twente;FC Twente Enschede;FC Twente;Twente Enschede","Netherlands","Голландия"),
    Feyenoord(1000,"Фуйеноорд;Фейенорд;Фейеноорд","Fujenoord;Feyenoord;Feyenoord Rotterdam","Netherlands","Голландия"),
    Heerenveen(1000,"Херенвен;ХЕРЕНВЕЕН;ФК Херенвен","KHerenven;Heerenveen;KHerenven;SC Heerenveen","Netherlands","Голландия"),
    PSV(1000,"ПСВ;ПСВ Эйндховен;ФК Эйндховен;ЭЙНДХОВЕН","PSV;PSV Eindhoven;FC Eindhoven","Netherlands","Голландия"),
    Groningen(1000,"Гронинген","Groningen;FC Groningen","Netherlands","Голландия"),
	Alkmar(3000, "АЗ Алкмар;АЗ Алкмаар;АЗ;АЛКМАР;Алкмаар","Alkmar;AZ Alkmaar;AZ","Netherlands","Голландия"),
    Zvolle(1000,"Зволле;ФК Зволле","Zvolle;Zwolle;PEC Zwolle;PEC Zwolle (n)","Netherlands","Голландия"),
    Gerakles(1000,"Гераклес;Хераклес;Эраклес;Гераклес Алмело;Эраклес Альмело;ХЕРАКЛС","Gerakles;Heracles;Heracles Almelo","Netherlands","Голландия"),
    Utrekht(1000,"Утрехт","Utrekht;Utrecht;FC Utrecht","Netherlands","Голландия"),
    GouEkhedIgls(1000,"Гоу Эхед Иглс;Гоу Эхед Иглз;ГоуЭхедИглс;ИГЛЗ","GouEkhedIgls;Go Ahead Eagles;GA Eagles","Netherlands","Голландия"),
    Kambuur(1000,"Камбуур;Камбур;Камбюр","Kambuur;Cambuur;Cambuur Leeuwarden","Netherlands","Голландия"),
    Breda(1000,"Бреда;НАК Бреда","Breda;NAC Breda","Netherlands","Голландия"),
    VillemII(1000,"ВиллемII;Виллем;Виллем II;Виллем 2;Виллем II Тилбург","VillemII;Willem II;Willem II Tilburg","Netherlands","Голландия"),
    DenHaag(1000,"Ден Хааг;ДенХааг;АДО Ден Хааг;Ден Гааг","DenKHaag;ADO Den Haag","Netherlands","Голландия"),
    Ekselsor(1000,"Эксельсор;Эксельсиор;ЭКСЦЕЛСИОР","Eksel'sor;Excelsior;Excelsior SBV;Excelsior;Ekselsor;SBV","Netherlands","Голландия"),
    Dordrekht(1000,"Дордрехт;ФК Дордрехт","Dordrekht;Dordrecht;FC Dordrecht","Netherlands","Голландия"),
	DeGraafschap(1000,"Де Графсхап;ФК де Графсхап;Графсхап;Де Граафсхап;Де	Граафшап","De Graafschap;FC De Graafschap","Netherlands","Голландия"),
	Nijmegen(1000,"Неймеген;ФК Неймеген;НЕК	Неймеген;Ниймеген","Nijmegen;FC Nijmegen;NEC Nijmegen;NEC","Netherlands","Голландия"),
	Roda(1000,"Рода;ФК Рода","Roda;FC Roda;Roda Kerkrade;Roda JC;Roda JC Kerkrade","Netherlands","Голландия"),
	
	//Индия
	
    Bangalor(1000,"Бангалор;Бенгалуру","Bangalor;Bengaluru;Bengaluru FC","India","Индия"),
    SportingGoa(1000,"СпортингГоа;Спортинг Клуб де Гоа;Спортинг Гоа;ДЕ ГОА","SportingGoa;S. Clube de Goa;Sporting Club de Goa","India","Индия"),
    SHillong(1000,"Шиллонг;Шиллонг Лайонг;ЛАЙОНГ","SHillong;Shillong Lajong","India","Индия"),
    Puna(1000,"Пуна;ФК Пуна;ПУНЕ","Puna;Pune;Pune FC","India","Индия"),
    Salgaokar(1000,"Салгаокар","Salgaokar;Salgaocar","India","Индия"),
    IstBengal(1000,"Ист Бенгал;ИстБенгал","IstBengal;East Bengal;East Bengal Club","India","Индия"),
    MokhunBagan(1000,"Мохун Баган;МохунБаган","MokhunBagan;Mohun Bagan","India","Индия"),
    PrayagYUnajted(1000,"Праяг Юнайтед;ПраягЮнайтед","PrayagYUnajted","India","Индия"),
    Mumbai(1000,"Мумбаи;ФК Мумбаи;МУМБАЙ","Mumbai;Mumbai FC","India","Индия"),
    Dempo(1000,"Демпо","Dempo;Dempo SC","India","Индия"),
    Mokhammedan(1000,"Мохаммедан","Mokhammedan","India","Индия"),
    Rangdadzhied(1000,"Рангдаджиед","Rangdadzhied","India","Индия"),
    CHerchill(1000,"Черчилл","CHerchill","India","Индия"),
    RVF(3000,"РВФ","RVF;Royal Wahingdoh","India","Индия"),
    Bharat(3000,"Кальяни Бхарат","Bharat;Kalyani Bharat FC;KALYANI BHARAT","India","Индия"),
	
	//ТурцияСуперлига
	
    Fenerbakhche(1000,"Фенербахче","Fenerbakhche;Fenerbahce;Fenerbahçe","Turkey","Турция"),
    Galatasaraj(1000,"Галатасарай;Галатасарай (Стамбул)","Galatasaraj;Galatasaray;Galatasaraj;Galatasaray Istanbul","Turkey","Турция"),
    Kazimpaza(1000,"Казимпаза;Касымпаша;КАСИМПАСА","Kazimpaza;Kasimpasa","Turkey","Турция"),
    Sivasspor(1000,"Сивасспор;Сивашспор","Sivasspor","Turkey","Турция"),
    Beshiktash(1000,"Бешикташ","Beshiktash;Besiktas;Beşiktaş A.Ş.;Besiktas JK;Besiktas Istanbul;Besiktas (n)","Turkey","Турция"),
    Trabzonspor(1000,"Трабзонспор","Trabzonspor;Trabzonspor A.Ş.","Turkey","Турция"),
    Eskizekhirspor(1000,"Эскизехирспор;Эскишехирспор","Eskizekhirspor;Eskisehirspor","Turkey","Турция"),
    Bursaspor(1000,"Бурсаспор","Bursaspor","Turkey","Турция"),
    Karabukspor(1000,"Карабукспор;Карабюкспор;Кардемир Карабукспор","Karabukspor;Kardemir Karabukspor;Kdc Karabukspor;Karabukspor (in Ankara)","Turkey","Турция"),
    Akkhisar(1000,"Акхисар;Акхисар Беледийе;Акхизар Беледье;ФК Акхисар Беледиеспор;Акхисар Спор","Akkhisar;Akhisar Belediyespor;Akhisar Bld;Akhisar Belediye GS;Akhisar Beledie;Akhisar Bld Spor;Akhisar","Turkey","Турция"),
    Genchlerbirligi(1000,"Генчлербирлиги","Genchlerbirligi;Genclerbirligi;Genchlerbirligi","Turkey","Турция"),
    Gaziantepspor(1000,"Газиантепспор","Gaziantepspor;Gaziantepspor","Turkey","Турция"),
    Mersin(1000,"Мерсин;Мерсин Идманюрду;ЮРДУ;Мерсин Юрду","Mersin;Mersin Idman Yurdu;Mersin IY;Mersin I.Yurdu","Turkey","Турция"),
    Konyaspor(1000,"Коньяспор;ТОРКУ КОНИАСПОР;ФК Коньяспор","Kon'yaspor;Konyaspor;Torku Konyaspor","Turkey","Турция"),
    Stambul(1000,"Стамбул;Истанбул ББ;Башакшехир;Истанбул Башакшехир;Стамбул Башакшехир","Stambul;Istanbul Basaksehir FK;Basaksehir;Istanbul Basaksehir;Basaksehir FK;Istanbul Basaksehir;Istanbul BSB","Turkey","Турция"),
    Rizespor(1000,"Ризеспор","Rizespor;Caykur Rizespor;Rizespor","Turkey","Турция"),
    Belikesirspor(1000,"Беликесирспор;Балыкесирспор;Баликесирспор;БАЛИКЕШИСПОР","Belikesirspor;Balikesirspor","Turkey","Турция"),
    Ersiesspor(1000,"Эрсиесспор;Кайсери Эрджиесспор;Кайсери Эрджиеспор;Эрджиесспор;ЭРДЖИЕСПОР","Ersiesspor;Kayseri Erciyesspor;Kayseri","Turkey","Турция"),
	Antalyaspor(1000,"Антальяспор;ФК Антальяспор","Antalyaspor;FC Antalyaspor","Turkey","Турция"),
	Sanliurfaspor(1000,"Санлиурфаспор;ФК Санлиурфаспор;Шанлиурфаспор","Sanliurfaspor;FC Sanliurfaspor;Urfaspor Sanliurfaspor","Turkey","Турция"),
	
	//АвстрияБундеслига
	
    Zaltsburg(1000,"Зальцбург;Ред Булл Зальцбург;Ред Булл (Зальцбург)","Zal'tsburg;Salzburg;Red Bull Salzburg;RB Salzburg;R.B.Salzburg","Austria","Австрия"),
	Liefering(3000,"Лиферинг","Liefering;FC Liefering","Austria","Австрия"),
	Wacker(3000,"Ваккер","Wacker;FC Wacker Innsbruck;Wacker Innsbruck","Austria","Австрия"),
    Grodig(1000,"Гродиг;Гредиг;ФК Грёдиг;ГРЁДИГ;СВ Грёдиг","Grodig;SV Grodig;SV Scholz Grodig;Grödig;SV Groedig;Grodig SV","Austria","Австрия"),
    RapidVena(1000,"Рапид Вена;Рапид (Вена);Рапид В","Rapid Vena;Rapid Wien;SK Rapid Vienna;Rapid Vienna;Rapid W.","Austria","Австрия"),
    Austriya(1000,"Аустрия;Аустрия Вена;Аустрия (Вена);АВСТРИЯ ВЕНА","Austriya;Austria Wien;Austria W.;FK Austria Vienna;Austria Vienna","Austria","Австрия"),
    Rid(1000,"Рид;ФК Рид;СВ РИД","Rid;SV Ried;Ried;SV Josko Ried","Austria","Австрия"),
    SHturm(1000,"Штурм;Штурм Грац;Штурм Гратц","SHturm;Sturm Graz;SK Sturm Graz","Austria","Австрия"),
    Volfsberg(1000,"Вольфсберг;Вольфсбергер АК;ВАК Ст. Андра;ВАК-Санкт-Андре (Вольфсберг);ВАК Санкт-Андре;ВОЛЬФСБЕРГЕР","Vol'fsberg;Wolfsberger AC;Wolfsberg;RZ Pellets WAC;Wolfsberger","Austria","Австрия"),
    Neustadt(1000,"Нойштадт;Винер Нойштад;СК Винер Нойштадт;Винер-Нойштадт;Винер Нойштадт;ВИНЕР","Nojshtadt;SC Wiener Neustadt;Wiener Neustadt;W. Neustadt;Neustadt;Wr. Neustadt","Austria","Австрия"),
    Altakh(1000,"Альтах;ФК Альтах;Райндорф Альтах;АЛТАХ","Al'takh;Rheindorf Altach;SCR Altach;Altach","Austria","Австрия"),
    Admira(1000,"Адмира;Тренквалдер Адмира;Адмира (Медлинг);Т. Адмира;Адмира Вакер","Admira Wacker Modling;Admira;Admira Wacker;Admira Moedling;FC Trenkwalder Admira;Trenkwalder Admira","Austria","Австрия"),
	Mattersburg(1000,"Маттерсбург; ФК Маттерсбург","Mattersburg;FC Mattersburg;SV Mattersburg","Austria","Австрия"),
	
	//ЧехияГамбринусЛига
	
    SpartaPraga(1000,"СпартаПрага;Спарта Пр;Спарта Прага;Спарта Пр.","SpartaPraga;Sparta Prague;Sparta Praha","Czech","Чехия"),
    ViktoriyaP(1000,"ВикторияП;Виктория Пльзень;Виктория П;ВИКТОРИЯ;Виктория Плзень","ViktoriyaP;Plzen;Viktoria Plzen;Pilsen","Czech","Чехия"),
    Teplitse(1000,"Теплице","Teplitse;Teplice;FK Teplice","Czech","Чехия"),
    DuklaPraga(1000,"Дукла Прага;Дукла Пр;Дукла (Прага);ДУКЛА","Dukla Praga;DuklaPraga;Dukla Prague;Dukla;Dukla Praha","Czech","Чехия"),
    MladaBoleslav(1000,"МладаБолеслав;Млада-Болеслав;Млада Болеслав","MladaBoleslav;Mlada Boleslav;Boleslav;FK Mlada Boleslav;Mlada Boleslav FK","Czech","Чехия"),
    SlovanLiberets(1000,"Либерец;Слован Л;Слован Либерец;Слован (Либерец);СЛОВАН","Slovan Liberets;Slovan Liberec;FK Slovan Liberec;Liberec","Czech","Чехия"),
    YAblonets(1000,"Яблонец;Баумит (Яблонец-над-Нисоу)","YAblonets;Jablonec;Baumit Jablonec;FK Jablonec;FK Baumit Jablonec","Czech","Чехия"),
    Slovatsko(1000,"Словацко","Slovatsko;Slovacko;FC Slovacko","Czech","Чехия"),
    BudejovitseCH(1000,"БудейовицеЧ;Динамо Ческе-Будеевице;Ческе Будейовице (Чехия);Ч. Будеевице;Чешке Будеевице;Динамо Ческе-Будеёвице;Ч. Будейовице;Ческе Будеевице","BudejovitseCH;Ceske Budejovice;C. Budejovice;Budejovice;Dynamo Ceske Budejovice","Czech","Чехия"),
    GradetsK(1000,"ГрадецК;Градец-Кралове;Градец Кралове;ГРАДЕЦ","GradetsK;Hradec Kralove","Czech","Чехия"),
    SlaviyaPraga(1000,"Славия Прага;СлавияПрага;Славия Пр;Славия Пр.","SlaviyaPraga;Slaviya Praga;Slavia Prague;Slavia Praha;SK Slavia Prague","Czech","Чехия"),
    ZHikhlava(1000,"Жихлава;Высочина Йиглава;Иглава;ВЫСОЧИНА","ZHikhlava;Vysocina Jihlava;Jihlava","Czech","Чехия"),
    Brno(1000,"Брно;Збройовка Брно;Зброевка Брно;Збройовка (Брно);Зброёвка Брно","Brno;Zbrojovka Brno","Czech","Чехия"),
    BanikOstrava(1000,"Баник Острава;БаникОстрава;Баник (Острава);Баник","BanikOstrava;Banik Ostrava;Ostrava","Czech","Чехия"),
    Prshibram(1000,"Пршибрам;ПРИБРАМ;ФК Пршибрам","Prshibram;Pribram","Czech","Чехия"),
    Bogemians1905(1000,"Богемианс 1905;Богемиан1905;Богемианс 1905;ФК Богемианс","Bogemians 1905;Bohemians;Bohemians 1905","Czech","Чехия"),
	
	//Армения
	
    AraratErevan(1000,"Арарат Ереван;АраратЕреван;Арарат;Арарат (Ереван)","AraratErevan;Ararat","Armenia","Армения"),
    SHirak(1000,"Ширак;Ширак Гюмри","SHirak;Shirak Gyumri","Armenia","Армения"),
    Gandzasar(1000,"Гандзасар","Gandzasar","Armenia","Армения"),
    Banants(1000,"Бананц","Banants;FC Banants","Armenia","Армения"),
    Mika(1000,"Мика","Mika;FC Mika","Armenia","Армения"),
    PyunikErevan(1000,"Пюник Ереван;Пюник;Пюник (Ереван)","Pyunik Erevan;Pyunik","Armenia","Армения"),
    Uliso(1000,"Улисо;Улисс;Улисс (Ереван);УЛИС;Улиссес","Uliso;Ulisses","Armenia","Армения"),
    Alashkert(1000,"Алашкерт;Алашкерт Мартуни","Alashkert;Alashkert FC","Armenia","Армения"),
	
	//ПольшаЭкстраклас
	
    Legiya(1000,"Легия;Легия Варшава;Легия (Варшава)","Legiya;Legia Warszawa;Legia Warsaw","Poland","Польша"),
    GurnikZb(1000,"ГурникЗб.;Гурник Забже;Гурник Зб;Горник (Забже);ГУРНИК;Гурник Забрже","GurnikZb.;Gornik Zabrze;Górnik Zabrze","Poland","Польша"),
    Visla(1000,"Висла;Висла Краков;Висла К;Висла Кр","Visla;Wisla Krakow","Poland","Польша"),
    Lekh(1000,"Лех;Лех Познань;Лех (Познань)","Lekh;Lech Poznan;Lech Poznań","Poland","Польша"),
    Ruch(1000,"Рух Хожув;РухХожув;Рух (Хожув);Рух Хж;Руч Чоржов;Рух Х;РУХ","RukhKHozhuv;Ruch Chorzow;Ruch","Poland","Польша"),
    Pogon(1000,"Погонь;Погонь Щецин;Погонь (Щецин);Погонь Щ;ПОГОН","Pogon';Pogon Szczecin;Szczecin Pogon;Pogon","Poland","Польша"),
    Krakovi(1000,"Кракови;Краковия;Краковия Краков","Krakovi;Cracovia Krakow;Cracovia;Cracovia Krakau","Poland","Польша"),
    Jagiellonia(1000,"Ягеллония;Ягеллония Белосток;Йагиеллония Биялисток","YAgelloniya;Jagiellonia Białystok;Jagiellonia Bialystok;Jagiellonia","Poland","Польша"),
    ZavishaBydgosch(1000,"Завиша Быдгощ;ЗавишаБыдгощ;Завиша (Быдгощь);Завиша;Зависца","ZavishaBydgosch;Zawisza Bydgoszcz;Zawisza","Poland","Польша"),
    LekhiyaGd(1000,"ЛехияГд.;Лехия Гданьск;Лехия;Лечия Гданьск;Лехия (Гданск);Гданьск;Лехия Гд","LekhiyaGd.;Lechia Gdansk;KS Lechia","Poland","Польша"),
    PyastGlivitse(1000,"ПястГливице;Пяст Гливице;Пяст;Пяст (Гливице);Гливице;Пиаст Гливице","PyastGlivitse;Piast Gliwice;GKS Piast Gliwice","Poland","Польша"),
    Korona(1000,"Корона;Корона (Кельце);Корона Кельце;Корона К","Korona;Korona Kielce;Korona Kielce SA;Kielce Korona","Poland","Польша"),
    Slensk(1000,"Сленск;Шленск Вроцлав;Шленск (Вроцлав);Шленск;Сласк Вроцлав;Шлёнск;Шленск Вроклав","Slensk;Slask Wroclaw;WKS Slask Wroclaw","Poland","Польша"),
    Belshatov(1000,"Белшатов;ГКС Белхатув;Белхатув;Белхатов;СКРА Белхатов;ГКС Белчатов;ГКС Белхатов;ГКС Бельхатув;Белчатов","Belshatov;GKS Belchatow;GKS Bełchatów;PGE GKS Belchatow;Belchatow","Poland","Польша"),
    BelskoByala(1000,"Бельско-Бяла;Подбескидзе Бяльско-Бяла;Подбескидже (Бельско Бяла);Подбескидзье;Подбескидже;Бельско Бяла;Подбескидзе;Подбескидзи Бельско-Бяла;ПОДБЕСКИДЗИ","Bel'sko-Byala;Podbeskidzie Bielsko Biala;Podbeskidzie Bielsko-Biala;Podbeskidzie;TS Podbeskidzie B.;TS Podbeskidzie B;Podbeskidzie B-B;TS Podbeskidzie","Poland","Польша"),
    GurnikL(1000,"Гурник Л;ГурникЛ;Гурник Ленчна;Горник (Ленчна);Горник;Лечна;Богданка Ленчна","GurnikL;Gornik Leczna;Górnik Łęczna","Poland","Польша"),
	Zaglebie(1000,"Заглембе;ФК Заглембе;Заглембе Любин;Заглебие;ФК Заглебие","Zaglebie;FC Zaglebie;Zaglebie Lubin","Poland","Польша"),
	
	//Хорватия1девизион
	
    DinamoZg(1000,"ДинамоЗг.;Динамо Загреб;Динамо З;Динамо Зг;Динамо Зг.;Динамо (Загреб)","DinamoZg.;Dynamo Zagreb;Dinamo Zagreb","Croatia","Хорватия"),
    Rieka(1000,"Риека;ФК Риека","Rieka;Rijeka","Croatia","Хорватия"),
    KhajdukSplit(1000,"Хайдук Сплит;Хайдук (Сплит);Хайдук;ХайдукСплит;ХАЙДУК С","KHajdukSplit;HNK Hajduk Split;Hajduk Split","Croatia","Хорватия"),
    LokomotivZ(1000,"Локомотива;Локомотив Загреб;Локомотив (Загреб);Локомотив З;Локомотив Зг","Lokomotiva;Lokomotiva Zagreb;Lok. Zagreb;Lok Zagreb;NK Lokomotiva;NK Lokomotiva Zagreb;NK Lok. Zagreb","Croatia","Хорватия"),
    Split(1000,"Сплит;РНК Сплит;ФК Сплит","Split;RNK Split;Split","Croatia","Хорватия"),
    Istra1961(1000,"Истра1961;Истра 1961;ИСТРА","Istra1961;Istra;Istra 1961;NK Istra 1961;NK Istra 1961 Pula","Croatia","Хорватия"),
    SlavenBelupo(1000,"СлавенБелупо;Славен Белупо","SlavenBelupo;Slaven;NK Slaven Belupo","Croatia","Хорватия"),
    Zadar(1000,"Задар;НК Задар;ФК Задар","Zadar;NK Zadar;Zadar NK","Croatia","Хорватия"),
    Osiek(1000,"Осиек;ФК Осиек","Osiek;Osijek;NK Osijek","Croatia","Хорватия"),
    Zagreb(1000,"Загреб;ФК Загреб","Zagreb;NK Zagreb","Croatia","Хорватия"),
	InterZapresic(1000,"Интер З.;ФК Интер З.;Интер Запрешич;Интер Запрежич","Inter Zapresic;FC Inter Zapresic","Croatia","Хорватия"),
	
	//ШотландияПремьерЛига
	
    Seltik(1000,"Селтик;Селтик (Глазго)","Seltik;Celtic;Celtic Glasgow;Celtic FC;Seltik","Scotland","Шотландия"),
	Aberdin(1000,"Абердин;ФК Абердин","Aberdin;Aberdeen;Aberdin;Aberdeen","Scotland","Шотландия"),
	Moteruell(1000,"Мотеруэлл;Мазервэлл;ФК Мазервелл;ФК Мотеруэлл;МАЗЕРВЕЛЛ;Мотервелл","Moteruell;Motherwell","Scotland","Шотландия"),
	Inverness(1000,"Инвернесс;ФК Инвернесс Каледониан Тисл;Инвернесс Каледониан Тисл;ИНВЕРНЕС КТ","Inverness;Inverness;Inverness CT;Inverness Caledonian Thistle;Inverness (n)","Scotland","Шотландия"),
	DandiUTD(1000,"Данди UTD;Данди Юнайтед;ФК Данди Юнайтед;ДандиЮнайтед;Данди Юн.","Dundee Utd;Dundee United;DandiUTD;DandiYUnajted","Scotland","Шотландия"),
	SentDzhonson(1000,"Сент-Джонсон;Сент-Джонстон (Перт);Сент-Джонстон;Сейнт Джонстон;СТ. ДЖОНСТОН","Sent-Dzhonson;St. Johnstone;Saint Johnstone;St Johnstone;Johnstone;St.Johnstone","Scotland","Шотландия"),
	Gamilton(1000,"Гамильтон;Гамильтон Академикал (Гамильтон);ФК Гамильтон Академикал;ХАМИЛЬТОН","Gamil'ton;Hamilton;Hamilton Academical","Scotland","Шотландия"),
	Partik(1000,"Партик;Партик Тистл;Партик Тисл","Partik;Partick Thistle","Scotland","Шотландия"),
	Dandi(1000,"Данди;ФК Данди;Данди ФК","Dandi;Dundee FC;Dundee","Scotland","Шотландия"),
	Kilmarnok(1000,"Килмарнок;ФК Килмарнок","Kilmarnok;Kilmarnock;Kilmarnok","Scotland","Шотландия"),
	SentMirren(1000,"Сент-Миррен;ФК Сент Миррен;СТ. МИРРЕН","Sent-Mirren;St. Mirren;SentMirren;Saint Mirren;St Mirren","Scotland","Шотландия"),
    RossKaunti(1000,"Росс Каунти;РоссКаунти;ФК Росс Коунти","RossKaunti;Ross County;Ross Co","Scotland","Шотландия"),
	
	//ИранЛигаПро
	
    Estelal(1000,"Эстелал;Эстеглал;Эстеглаль ФК","Estelal;Esteghlal;Est. Esteghlal","Iran","Иран"),
    Fulad(1000,"Фулад;Фулад Ахваз;Фулад Хузистан","Fulad;Foolad;Foolad Khuzestan","Iran","Иран"),
    Sepakhan(1000,"Сепахан","Sepakhan;Sepahan","Iran","Иран"),
    Persepolis(1000,"Персеполис;Персеполис (Тегеран)","Persepolis;Persepolis FC","Iran","Иран"),
    NaftTegeran(1000,"НафтТегеран;Нафт;Нафт Тегеран","NaftTegeran;Naft Tehran","Iran","Иран"),
    TraktorSazi(1000,"ТракторСази;Терактор Сази;Трактор Сази;ТРАКТОР;Терактор-Сази","TraktorSazi;Tractor;Tractor Sazi Tabriz;Tractor Sazi","Iran","Иран"),
    Malavan(1000,"Малаван","Malavan;Malavan Bandar","Iran","Иран"),
    Sajpa(1000,"Сайпа","Sajpa;SaiPa Karadj;Saipa","Iran","Иран"),
    GostareshFulad(1000,"ГостарешФулад;Гостареш Фулад;GOSTARESH","GostareshFulad;Gostaresh;Gostaresh Foolad FC","Iran","Иран"),
    SabaKvom(1000,"Саба Квом;Саба Ком;Саба Кум;САБА","Saba Kvom;Saba;Saba Qom","Iran","Иран"),
    EstelalKHuzestan(1000,"ЭстелалХузестан;Эстеглаль Хузестан;Эстеглал Хузестан;Эстегаль Хузестан","EstelalKHuzestan;Esteghlal Khuzestan","Iran","Иран"),
    Padidesh(1000,"Падидеш;Падидех Шаандиз;Падидех Хорасан","Padidesh;Padideh FC","Iran","Иран"),
    RakhAkhan(1000,"Рах Ахан;РахАхан","RakhAkhan;Rah Ahan","Iran","Иран"),
    ZobAkhan(1000,"Зоб Ахан;ЗобАхан","ZobAkhan;Zobahan;Zob Ahan","Iran","Иран"),
    Pajkan(1000,"Пайкан;ПЕЙКАН","Pajkan;Peykan","Iran","Иран"),
    NaftMaszhed(1000,"Нафт Масжед;НафтМасжед;ФК Нафт Месджеде-Солейман;Нафт Масджид-Сулейман","NaftMaszhed;Naft Masjed;Naft Masjed Soleyman FC","Iran","Иран"),
	
	//Словения1девизион
	
    Maribor(1000,"Марибор","Maribor;NK Maribor","Slovenia","Словения"),
    Koper(1000,"Копер;ФК Копер","Koper;FC Koper","Slovenia","Словения"),
    Zavrch(1000,"Заврч;ФК Заврч;Заврц","Zavrch;Zavrc;NK Zavrc","Slovenia","Словения"),
    KHitGoritsa(1000,"Хит Горица;ХитГорица;Горица","KHitGoritsa;ND Gorica;Gorica","Slovenia","Словения"),
    Rudar(1000,"Рудар;Рудар Вел;Рудар Веленье","Rudar;Rudar Velenje","Slovenia","Словения"),
    OlimpiyaLyublyana(1000,"ОлимпияЛюбляна;Олимпия (Любляна);Олимпия Л;Олимпия;Олимпия Любляна","OlimpiyaLyublyana;Olimpija Ljubljana;OLimpija;NK Olimpija Ljubljana","Slovenia","Словения"),
    Domzhale(1000,"Домжале;ФК Домжале","Domzhal;NK Domzalee;Domzale","Slovenia","Словения"),
    Kaltser(1000,"Кальцер;Радомлье;Радомле","Kal'tser;Radomlje","Slovenia","Словения"),
	Krka(3000,"Крка","Krka;NK Krka","Slovenia","Словения"),
	Publikum(3000,"Публикум;Публикум Целье;ФК Целе;Целье;Селье;Целе","Publikum;NK Celje;Celje","Slovenia","Словения"),
	Krsko(3000,"Кршко;ФК Кршко;Кршко Посавлье","Krsko;FC Krsko;NK Krsko;Krsko Posavlje","Slovenia","Словения"),
	
	//ОАЭ
	
    AlAkhliDubaj(1000,"Аль Ахли Дубай;АльАхлиДубай;Аль-Ахли;Аль-Ахли Дубай","Al'AkhliDubaj;Al Ahli UAE;Al-Ahli;Al Ahli (UAE)","UAE","ОАЭ"),
    AlSHabibDubai(1000,"Аль Шабиб Дубаи;АльШабибДубаи;Аль-Шабаб;Аль-Шабаб Дубай","Al'SHabibDubai;Al-Shabab","UAE","ОАЭ"),
    AlDzhaziraAbuDabi(1000,"Аль ДжазираАбуДаби;Аль-Джазира;ДЖАЗИРА","Al' DzhaziraAbuDabi;Al Jazira;Al-Jazira","UAE","ОАЭ"),
    AlSHardzha(1000,"Аль Шарджа;АльШарджа;Шарджа;Аль-Шарджа","Al'SHardzha;Sharjah;Al-Sharjah","UAE","ОАЭ"),
    AlAin(1000,"Аль Аин;АльАин;Аль Айн;ФК Эль-Айн;Аль-Айн","Al'Ain;Al-Ain;Al Ain","UAE","ОАЭ"),
    AlNasrDubai(1000,"Аль Наср Дубаи;АльНасрДубаи;Аль Наср Дубай;Аль-Наср;Аль-Наср Дубай;АЛЬ НАСР","Al'NasrDubai;Al-Nasr Dubai [UAE];Al Nasr Dubai;Al Nasr SC Dubai;Al-Nasr Dubai;Al-Nasr SC","UAE","ОАЭ"),
    BaniYAs(1000,"БаниЯс;Бани Яс","BaniYAs;Baniyas;Bani Yas","UAE","ОАЭ"),
    AlVakhdaAbuDabi(1000,"Аль Вахда Абу-Даби;АльВахдаАбуДаби;Аль Вахда (Абу-Даби);Аль-Вахда;Аль-Вахда Абу-Даби","Al'VakhdaAbuDabi;Al Wahda;Al-Wahda","UAE","ОАЭ"),
    AlDafra(1000,"Аль Дафра;АльДафра;Аль-Дафра;АЛЬ ДХАФРА","Al'Dafra;AL Dhafra;Al-Dhafra","UAE","ОАЭ"),
    AlVaslDubai(1000,"Аль Васл Дубаи;АльВаслДубаи;Аль-Васл;АЛЬ ВАСЛ","Al'VaslDubai;Al-Wasl","UAE","ОАЭ"),
    Emirats(1000,"Эмиратс;ФК Эмирейтс;Эмиратес","Emirats;Emirates Club","UAE","ОАЭ"),
    Ajman(1000,"Айман;ФК Аджман;Аджман","Ajman","UAE","ОАЭ"),
    AlFuyaira(1000,"АльФуяира;Аль-Ахли Фуджайра (ОАЭ);Аль-Фуджайра;AHLI AL-FUJIRAH","Al'Fuyaira;Al Fujairah","UAE","ОАЭ"),
    IttikhadOAE(1000,"Иттихад;Аль Иттихад Джидда;Аль-Иттихад;Аль-Иттихад Кальба","Ittikhad;Al Ittihad Kalba","UAE","ОАЭ"),
	
	//Румыния1Лига
	
    CHakhlaul(1000,"Чахлаул;Чахлэул;Чахлаул Пятра-Нямт;Чахлэул Пятра-Нямц","CHakhlaul;Ceahlaul Piatra Neamt;Ceahlaul;Ceahlaul Piatra-Neamt","Romania","Румыния"),
    Rapidbuharest(1000,"Рапид;Рапид Бх;Рапид Бухарест;Рапид Бх.","Rapid B;Rapid Bucuresti;Rapid Bucharest;Rapid B.","Romania","Румыния"),
	DinamoBucharest(3000,"Динамо Бухарест;Динамо Бх.;Динамо Бх","Dinamo Bucharest;Dinamo B.;FC Dinamo Bucuresti;Dinamo Bucuresti","Romania","Румыния"),
    GazMetan(1000,"Газ Метан;Газ Метан М;Газ Метан Медиас;Газ Метан Медиаш","Gaz Metan;Gaz Metan Medias","Romania","Румыния"),
    Styaua(1000,"Стяуа","Styaua;Steaua Bucharest;Steaua B.;Steaua Bucuresti","Romania","Румыния"),
    Petrolul(1000,"Петролул;Петролул П;Петролул Плоешти","Petrolul;Petrolul Ploiesti;FC Petrolul Ploiesti;Petrolul P.","Romania","Румыния"),
    Astra(1000,"Астра;Астра Джурджу;Астра Г;ДЖУРДЖУ","Astra;Astra Giurgiu","Romania","Румыния"),
    CHFR(1000,"ЧФР;ЧФР Клуж;КЛУЖ","CHFR;CFR Cluj","Romania","Румыния"),
    Konkordia(1000,"Конкордиа;Конкордия Кьяжна;Конкордия (Кьяжна);Конкордиа К;Конкордия","Konkordia;Concordia Chiajna;Concordia;CS Concordia Chiajna","Romania","Румыния"),
    Pandurij(1000,"Пандурий;Пандурии (Тыргу-Жиу);Пандурии Линьятул;Пандури","Pandurij;Pandurii Targu Jiu;Pandurii;Panduri Targu","Romania","Румыния"),
    Universitatya(1000,"Университатя;Университатя (Клуж);Университатя Клуж;Университатя","Universitatya;Universitatea Cluj;Uni Cluj;U. Cluj","Romania","Румыния"),
    YAsi(1000,"Яси;КСМС;ЧСМС Яссы;МУ ПОЛИ ИАСИ;Иаси;КСМС Яссы","YAsi;CSMS Iasi","Romania","Румыния"),
    Viitorul(1000,"Вииторул;СК ФК Вииторул Константа;Вииторул К;Вииторул Констанца;ВИТОРУЛ;Виторул Константа","Viitorul;Viitorul Constanta;FC Viitorul Constanta","Romania","Румыния"),
    Krajova(1000,"Крайова;ФК Университатя Крайова;КС Университатя Кр;КС Университатя Крайова;Университатя Кр.;Университатя Крайова;КС Университатя Кр.","Krajova;Universitatea Craiova;CS Universitatea Craiova;CS U Craiova;CS Uni Craiova","Romania","Румыния"),
    Otselul(1000,"Оцелул;Оцелул Галати","Otselul;Otelul Galati;Otelul;FC Otelul Galati","Romania","Румыния"),
    Brashov(1000,"Брашов;Брасов;ФК Брашов","Brashov;Brasov;FC Brasov","Romania","Румыния"),
    Botosani(1000,"Ботосани;Ботошани;ФК Ботошани","Botosani","Romania","Румыния"),
    Targu(1000,"Таргу;Таргу Мурес;Тыргу Муреш;ФК Тыргу-Муреш;Тыргу-Муреш;АСА Тыргу Муреш","Targu;Targu Mures;Tirgu Mures","Romania","Румыния"),
	
	//БоснияИГерцеговина
	
    ZHeleznichar(1000,"Железничар","ZHeleznichar;Zeljeznicar","Bosnia","Босния"),
    Saraevo(1000,"Сараево;ФК Сараево","Saraevo;Sarajevo;FK Sarajevo","Bosnia","Босния"),
    SHirokiBreg(1000,"ШирокиБрег;Широки Бриег","SHirokiBreg;Siroki Brijeg","Bosnia","Босния"),
    Zrinski(1000,"Зриньски","Zrin'ski;Zrinjski;Zrinjski Mostar","Bosnia","Босния"),
    OlimpikS(1000,"Олимпик Сараево","Olimpik Saraevo;Olimpic Sarajevo;Olimpik Sarajevo","Bosnia","Босния"),
    BoratsBanyaLuka(1000,"Борац Банья Лука;БорацБаньяЛука;Борац;Борац Баня-Лука;БАНЯ ЛУКА","BoratsBan'yaLuka;Banja Luka;Borac Banja Luka;B. Banja Luka;FK Borac Banja Luka","Bosnia","Босния"),
    Velez(1000,"Велез;Вележ;Вележ Мостар","Velez;FK Velez Mostar","Bosnia","Босния"),
    TSelik(1000,"Целик;Челик;СЕЛИК ЗЕНИЦА","TSelik;Celik Zenica","Bosnia","Босния"),
    ZvezdaGradachach(1000,"Звезда Градачач;ЗвездаГрадачач;Звезда;Звезда Градачац;ЗВИЕЗДА","ZvezdaGradachach;Zvijezda Gradacac;FK Zvijezda","Bosnia","Босния"),
    RadnikBidzhelina(1000,"Радник Биджелина;РадникБиджелина;Радник;Радник Бьелина","RadnikBidzhelina;Radnik;Radnik Bijeljina","Bosnia","Босния"),
    Vitez(1000,"Витезь;ФК Витез;Витез","Vitez';Vitez","Bosnia","Босния"),
    MladostVelikaObarska(1000,"Младост Велика Обарска;МладостьВеликаОбарска;ФК МЛАДОСТ ВЕЛИКА ОБАРСКА","MladostVelikaObarska;Velika Obarska;FK Mladost Velika Obarska","Bosnia","Босния"),
    Drina(1000,"Дрина;ФК Дрина;Дрина Зворник","Drina;Drina Zvornik;FK Drina Zvornik","Bosnia","Босния"),
    Travnik(1000,"Травник;ФК Травник;НК	Травник","Travnik;NK Travnik","Bosnia","Босния"),
    Sloboda(1000,"Слобода;Слобода Тузла","Sloboda;Sloboda Tuzla","Bosnia","Босния"),
	SlaviyaC(3000,"Славия Сараево;СлавияС;Славия Сараево","SlaviyaC;Slavija Sarajevo","Bosnia","Босния"),
	
	//УэльсПремьерЛига
	
    NyuSeints(1000,"Нью Сеинтс;НьюСеинтс;ТНС;Нью-Сейнтс","N'yuSeints;New Saints;TNS","Wales","Уэльс"),
    EabasYUK(1000,"Эабас ЮК;Эйрбас;Эйрбас ЮК Бротон","Eabas YUK;Airbus UK;Airbus UK Broughton","Wales","Уэльс"),
    KarmartenTaun(1000,"Кармартен Таун;Кармартен;ФК Кармартен Таун;КармартенТаун","KarmartenTaun;Carmarthen Town;Carmarthen","Wales","Уэльс"),
    BangorSiti(1000,"Бангор Сити;Бангор;БангорСити","BangorSiti;Bangor;Bangor City","Wales","Уэльс"),
    AberistvitTaun(1000,"Абериствит Таун;АбериствитТаун;Эбериствит;ФК Аберистуит Таун;Аберистуит Таун;ЭБИРИСТВИТ","AberistvitTaun;Aberystwyth","Wales","Уэльс"),
    Nyutaun(1000,"Ньютаун;ФК Ньютаун","N'yutaun;Newtown;Newtown AFC","Wales","Уэльс"),
    Ril(1000,"Рил;ФК Рил","Ril;Rhyl","Wales","Уэльс"),
    BalaTaun(1000,"Бала Таун;БэлаТаун;Бала Таун;БАЛА","BalaTaun;Bala Town","Wales","Уэльс"),
    PrestatinTaun(1000,"Престатин Таун;Престон;Престатин;ПрестатинТаун","PrestatinTaun;Preston;Prestatyn;Prestatyn Town FC","Wales","Уэльс"),
    PortTelbot(1000,"Порт Тэлбот;ПортТэлбот;Порт Талбот;Порт-Толбот Таун;ТАЛБОТ","PortTelbot;Port Talbot;Port Talbot Town","Wales","Уэльс"),
    KonnakhKuej(1000,"Коннах Куэй;КоннахКуэй;Конна Квэй;ГАП Коннас Куэй;Коннах Кэй;КОННАС;Конна'с Куэй","KonnakhKuej;Connahs Quay;Connah's Quay","Wales","Уэльс"),
    TSefn(1000,"Цефн;Нэви Сефн;Кевн Друидс;Сефн Друидз;Сефн Друидс","TSefn;Cefn Druid;Cefn Druids AFC","Wales","Уэльс"),
	Llandudno(1000,"Лландудно;ФК Лландудно","Llandudno;FC Llandudno;Llandudno Town;Llandudno FC","Wales","Уэльс"),
	Heverfordvest(1000,"Хаверфордвест;ФК Хаверфордвест;Хаверфордуэст;Хаверфордвест Каунти","Heverfordvest;FC Heverfordvest;Haverfordwest C.;Haverfordwest County","Wales","Уэльс"),
    
    //Албания
    
    Skenderbeu(1000,"Скендербеу;ФК Скендербеу","Skenderbeu","Albania","Албания"),
    TeutaDurres(1000,"Теута Дуррес;ТеутаДуррес;Теута","TeutaDurres;Teuta Durres","Albania","Албания"),
    Laki(1000,"Лаки;ФК Лачи;ЛАЧИ","Laki;Laci","Albania","Албания"),
    PartizaniTirana(1000,"Партизани Тирана;ПартизаниТирана;Партизани;ПАРТИЗАН","PartizaniTirana;P. Tirana;Partizani","Albania","Албания"),
    FlamurtariVlore(1000,"ФламуртариВлоре;ФК Фламуртари;ФЛАМУРТАРИ","FlamurtariVlore;Flamurtari","Albania","Албания"),
    Kukesi(1000,"Кукеси;ФК Кукеси","Kukesi","Albania","Албания"),
    Elbasani(1000,"Эльбасани;ФК Эльбасани","El'basani;Elbasani","Albania","Албания"),
    Apoloniya(1000,"Аполония;Апполония","Apoloniya;Fier;Apolonia","Albania","Албания"),
    VlaznyaSHkoder(1000,"Влазня Шкодер;ВлазняШкодер;КС Влазния;ВЛАЗНИЯ","VlaznyaSHkoder;Vllaznia","Albania","Албания"),
    Tirana(1000,"Тирана;ФК Тирана","Tirana;KF Tirana","Albania","Албания"),
    
    //БолгарияАГруппа
    
    Beroe(1000,"Берое","Beroe;Beroe Stara Za;Beroe Stara Zagora","Bulgaria","Болгария"),
    Botev(1000,"Ботев;Ботев Пд;Ботев Пловдив","Botev;Botev Plovdiv","Bulgaria","Болгария"),
    ChernoMore(1000,"Черно Море;ЧерноМоре;ВАРНА","CHernoMore;Cherno More Varna;Cherno More","Bulgaria","Болгария"),
    TSSKASofiya(1000,"ЦСКА София;ЦСКА Сф;ЦСКАСофия;ЦСКА С.","TSSKASofiya;CSKA Sofia","Bulgaria","Болгария"),
    KHaskovo(1000,"Хасково;ФК Хасково","KHaskovo;Haskovo;Haskovo 2009","Bulgaria","Болгария"),
    LevskiSofiya(1000,"Левски София;Левски (София);Левски;ЛевскиСофия","LevskiSofiya;Levski Sofia","Bulgaria","Болгария"),
    LiteksLovech(1000,"Литекс Ловеч;ЛитексЛовеч;Литекс (Ловеч);Литекс","LiteksLovech;Litex Lovech;PFC Litex Lovech","Bulgaria","Болгария"),
    LokomotivPl(1000,"ЛокомотивПл.;Локомотив (Пловдив);Локомотив Пл;Локомотив Пловдив","LokomotivPl.;Lokomotiv Plovdiv","Bulgaria","Болгария"),
    LokomotivSofiya(1000,"Локомотив София;Локомотив Сф;ЛокомотивСофия","LokomotivSofiya;Lok Sofia;Lokomotiv Sofia","Bulgaria","Болгария"),
    Lyudogorets(1000,"Людогорец;Лудогорец Разрад;Лудогорец;Лудогорец 1945 (Разград);Лудогорец Р;Лудогорец Разград","Lyudogorets;Ludogorets Razgrad;Ludogorets;PFC Ludogorets Razgrad","Bulgaria","Болгария"),
    Marek(1000,"Марек;Марек Дупница;ДУПНИЦА","Marek;Marek Dupnitsa","Bulgaria","Болгария"),
    SlaviyaSofia(1000,"Славия; София;Славия Сф;Славия София","Slaviya;Slavia Sofia","Bulgaria","Болгария"),

    //Черногория
    
    Sutzheska(1000,"Сутжеска;Сутьеска","Sutzheska;Sutjeska","Montenegro","Черногория"),
    SelikNiksik(1000,"Селик Никсик;СеликНиксик","SelikNiksik","Montenegro","Черногория"),
    Lovsen(1000,"Ловсен;Ловчен","Lovsen;FK Lovcen","Montenegro","Черногория"),
    Petrovach(1000,"Петровач;ФК Петровац;Петровац","Petrovach;Petrovac","Montenegro","Черногория"),
    Buduchnost(1000,"Будучность;Будучность Подгорица","Buduchnost';Buducnost Podgorica;Buducnost;FK Buducnost","Montenegro","Черногория"),
    Zeta(1000,"Зета","Zeta","Montenegro","Черногория"),
    Grbalzh(1000,"Грбалж;Грбаль","Grbalzh;Grbalj Radanovici","Montenegro","Черногория"),
    RudarPlavlya(1000,"Рудар Плавля;РударПлевля","RudarPlavlya;Rudar Pljevlja","Montenegro","Черногория"),
    Mogren(1000,"Могрен","Mogren","Montenegro","Черногория"),
    Berane(1000,"Беране;ФК Беране","Berane","Montenegro","Черногория"),
    Bokelzh(1000,"Бокелж;Бокель","Bokelzh;FK Bokelj","Montenegro","Черногория"),
    MladostPodgorika(1000,"МладостьПодгорика;Младост;Младост Подгорица","Mladost'Podgorika;Mladost Podgorica","Montenegro","Черногория"),
    Mornar(3000,"Морнар","Mornar","Montenegro","Черногория"),
	Iskra(3000,"Искра;ФК Искра;ФК Искра Даниловград;Искра Даниловград","Iskra;FC Iskra;Iskra Danilovgrad;FK Iskra Danilovgrad","Montenegro","Черногория"),
    
    //Мальта
    
    Birkirkara(1000,"Биркиркара;ФК Биркиркара","Birkirkara;Birkirkara FC","Malta","Мальта"),
    Valetta(1000,"Валетта;ФК Валлетта;ВАЛЛЕТТА","Valetta;Valletta","Malta","Мальта"),
    KHajbernians(1000,"Хайбернианс;Хибернианс;Хибернианс Паола","KHajbernians;Hibernians FC Paola;Hibernians Paola;Hibernians FC;Hibernians","Malta","Мальта"),
    SliemaUonderers(1000,"Слиема;ФК Слиема;Слима Уондерерс;СЛИМА","SliemaUonderers;Sliema Wanderers","Malta","Мальта"),
    Mosta(1000,"Моста;ФК Моста","Mosta","Malta","Мальта"),
    BelzenYUs(1000,"БелзенЮс;Бальцан Ювс;Бальцан Ютс;БАЛЬЗАН","BelzenYUs;Balzan Youths FC;Balzan","Malta","Мальта"),
    NakhkharLions(1000,"Наххар Лионс;НаххарЛионс;Нашшар Лайонс;Наксксар Лайонс;НАКСАР","NakhkharLions;Naxxar Lions FC","Malta","Мальта"),
    Floriana(1000,"Флориана;ФК Флориана","Floriana","Malta","Мальта"),
    Pieta(1000,"Пиета;ФК Пьета Хотспур;Пьета Хотспур;ПИТА","Pieta;FC Pieta","Malta","Мальта"),
    Kvormi(1000,"Кворми;ФК Корми;Корми","Kvormi;Qormi FC;Qormi","Malta","Мальта"),
    Tarksen(1000,"Тарксьен;ФК Тарксьен Райнбоус;Таршин Рейнбоуз;ТАРКСЕН","Tarks'en;Tarxien Rainbows","Malta","Мальта"),
    Zebbuk(1000,"Зеббук;Зеббуг Рейнджерс;Зеббудж Рейнджерс;ЗЕББУГ","Zebbuk;Zebbug Rangers","Malta","Мальта"),
    
    //Эстония
    
    Infonet(1000,"Инфонет","Infonet;FC Infonet","Estonia","Эстония"),
    LokomotivJorvi(1000,"Локомотив Йорви;ЛокомотивЙорви","LokomotivJorvi","Estonia","Эстония"),
    FloraTallinn(1000,"Флора Таллинн;ФлораТаллинн;Флора","FloraTallinn;Flora Tallinn","Estonia","Эстония"),
    KalevTallinn(1000,"Калев Таллинн;КалевТаллинн","KalevTallinn","Estonia","Эстония"),
    Levadiya(1000,"Левадия","Levadiya;Levadia Tallinn","Estonia","Эстония"),
    NommeKalyu(1000,"Номме Калью;НоммеКалью;Калью;Нымме Калью","NommeKal'yu;Nomme JK Kalju;Nomme Kalju","Estonia","Эстония"),
    Paide(1000,"Паиде;ФК Пайде Линнамеесконд;Пайде","Paide;Paide Linnameeskond","Estonia","Эстония"),
    SillamaeKalev(1000,"Силламае Калев;СилламаеКалев;Калев;Инфонет;Силламяэ;КАЛЕВ С.","SillamaeKalev;JK Sillamae Kalev;Sillamae Kalev","Estonia","Эстония"),
    TamekkaTartu(1000,"ТамеккаТарту;Таммека;Таммека Тарту;Мааг Таммека","TamekkaTartu;Tammeka;Tammeka Tartu","Estonia","Эстония"),
    TransNarva(1000,"Транс Нарва;ФК Нарва Транс;ТрансНарва;ТРАНС","TransNarva;Trans Narva","Estonia","Эстония"),
    Vaprus(3000,"Вапрус;Пярну Линнамеесконд;Пярну","Vaprus;Paernu Linnameeskond;Parnu Linnameeskond","Estonia","Эстония"),
    Vilandy(3000,"ФК Вильянди;Вильянди Тулевик;Тулевик","Vilandy;Tulevik Viljandi","Estonia","Эстония"),
	
	//Япония
	
    AlbireksNiigata(1000,"Альбирекс Ниигата;АлбирексНиигата;ФК Альбирекс Ниигата;Альбирекс Ниигата;Ниигата;Ниигата Албирекс","Al'bireksNiigata;Albirex Niigata;Albirex Niigata [JPN];Niigata Albirex;Niigata","Japan","Япония"),
    SeresoOsaka(1000,"Сересо Осака;СересоОсака;Керезо Осака;Чередзо Осака","SeresoOsaka;Sereso Osaka;Cerezo Osaka;C-Osaka","Japan","Япония"),
    Tokio(1000,"Токио;ФК Токио;ФК Токио Верди;Токио-Верди;ФК Токио Верди;Токио Верди","Tokio;FC Tokyo; Tokyo;Tokyo Verdy;Tokyo;Tokyo-V;Tokyo-Verdy;Tokyo Verdy 1969","Japan","Япония"),
    GambaOsaka(1000,"Гамба Осака;ГамбаОсака;ФК Гамба Осака;ГАМБА","GambaOsaka;Gamba Osaka;Gamba O;G-Osaka","Japan","Япония"),
    KasimaAntlers(1000,"Касима Антлерс;КасимаАнтлерс;Кашима Антлерс (Ибараки);Касима;КАШИМА АНТЛЕРС","KasimaAntlers;Kasima Antlers;Kashima;Kashima A;Kashima Antlers","Japan","Япония"),
    KasivaRejsol(1000,"Касива Рейсол;КасиваРейсол;ФК Касива Рейсол;Касива;КАШИВА РЕЙСОЛ","KasivaRejsol;Kashiwa Reysol;Kashiwa;Kashiwa R","Japan","Япония"),
    KavasakiFrontale(1000,"Кавасаки Фронтале;КавасакиФронтале;Кавасаки;ФК Кавасаки Фронтейл","KavasakiFrontale;Kawasaki Frontale;Kawasaki F;Kawasaki","Japan","Япония"),
    NagoyaGrampus(1000,"Нагоя Грампус;НагояГрампус;ФК Нагоя Грампус;Нагоя;Нагоя Грампус Эйт;ФК Нагойа Грампус;ГРАМПУС","NagoyaGrampus;Nagoya Grampus;Nagoya;Nagoya Grampus Eight;Nagoya G","Japan","Япония"),
    OmiyaArdiya(1000,"Омия Ардия;ОмияАрдия;Омия","OmiyaArdiya;Omiya Ardiya;Omiya","Japan","Япония"),
    SaganTosu(1000,"Саган Тосу;СаганТосу;Тосу;Саган (Тосу);Тосу;ФК Саган Тошу;САГАН","SaganTosu;Sagan Tosu;Sagan T;Tosu","Japan","Япония"),
    SanfrechcheKHirosima(1000,"Санфречче Хиросима;СанфреччеХиросима;Хиросима;ФК Хиросима;ФК Санфречче Хиросима;САНФРЕЧЧЕ","SanfrechcheKHirosima;Sanfrecce Hiroshima;Hiroshima Sanfrecce;Hiroshima","Japan","Япония"),
    SimidzuSPals(1000,"СимидзуС-Палс;Симидзу Си Палс;Симидзу;Шимицу С-Пульс;ФК Симидзу Эс-Палс;СИМИДЗУ С-ПАЛС","SimidzuS-Pals;Shimizu S-Pulse;Shimizu","Japan","Япония"),
    TokusimaVortis(1000,"Токусима Вортис;ТокусимаВортис;Вортис (Токусима);Токушима","TokusimaVortis;Tokushima","Japan","Япония"),
    UravaRedDajmonds(1000,"Урава Ред Даймондс;УраваРедДаймондс;Урава;Урава Рэд Даймондз;УРАВА РЕДС","UravaRedDajmonds;Urawa Red Diamonds;Urawa;Urawa RD;Urawa R.D.;Urawa Reds;Urawa R. Diamonds","Japan","Япония"),
    VegaltaSendaj(1000,"Вегалта Сэндай;ВегальтаСендай;Сендай;Вегалта (Сендай);ФК Вегалта Сендай;Вельгата Сендай;ВЕГАЛТА СЭНДАЙ","VegaltaSendaj;Vegalta Sendai;Vegalta S;Sendai","Japan","Япония"),
    VanforeKofu(1000,"Ванфоре Кофу;Кофу;Вентфорет (Кофу);Вентфорет;ВанфореКофу","VanforeKofu;Ventforet Kofu;Kofu;Ventforet K","Japan","Япония"),
    VisselKabe(1000,"Виссел Кабэ;ВисселКабэ;ФК Виссел Кобе;Кобе;Виссель Кобе;Виссел Кобе;ВИССЕЛ","VisselKabe;Vissel Kobe;Vissel K;Kobe","Japan","Япония"),
    IokogamaFMarinos(1000,"ИокогамаФ.Маринос;Иокогама Эф Маринос;ФК Йокогама;Йокогама ФМ;Йокогама Ф-Маринос;Йокогама Маринос;ЙОКОХАМА Ф;Йокогама","IokogamaF.Marinos;Yokohama F.Marinos;Yokohama FC;Yokohama M.;Yokohama F;Yokohama FM;Yokohama F. Marinos","Japan","Япония"),
	Kyoto(3000,"Киото;ФК Киото Санга","Kyoto;Kyoto Sanga;Kyoto Purple Sanga","Japan","Япония"),
	ShonanBellmare(3000,"Шонан;ФК Шонан;Сонан;Шонан Беллмаре;Сёнан Беллмаре;Сенан Бельмаре;Сёнан Бельмаре (Хирацука);Сёнан;Сёнан Бельмаре;Сонан Беллмаре;Шохан","Shonan Bellmare;Shonan","Japan","Япония"),
	Matsumoto(3000,"Мацумото;Матцумото;Мацумото Ямага;Матцумото;МАТСУМОТО","Matsumoto Yamaga;Matsumoto","Japan","Япония"),
	Montedio(3000,"Монтедио;Монтедио Ямагата;Ямагата","Montedio Yamagata;Montedio;Yamagata","Japan","Япония"),
	VarenNagasaki(3000,"Варен Нагасаки;ФК Верен Нагасаки;В-Варен Нагасаки","Varen Nagasaki;FC Varen Nagasaki;V-Varen Nagasaki;Nagasaki;V-varen Nagasaki;V Varen Nagasak","Japan","Япония"),
	
	//БразилияСерияА
	
    Kruzejro(1000,"Крузейро;Крузейру;Крузейру (Белу-Оризонти);ФК Крузейро дель Норте","Kruzejro;Cruzeiro;Kruzejro;Cruzeiro-MG;Cruzeiro MG","Brazil","Бразилия"),
    Gremiu(1000,"Гремиу;Гремио;Гремиу П-А;ГРЕМИО РС;Грэмио РС;ФК Гремиу","Gremiu;Gremio","Brazil","Бразилия"),
	PontePreta(3000,"Понте Прета;Понте-Прета;ПРЕТА","Ponte Preta;Ponte Preta (Campinas)","Brazil","Бразилия"),
    Fluminense(1000,"Флуминенсе;ФЛУМИНЕНС;ФК Флуминенсе","Fluminense;Fluminense RJ;Fluminense-RJ","Brazil","Бразилия"),
    Internasional(1000,"Интернасионал;Интернасьонал;Интернаcьонал-РС;Интернасионал-РС;Интернационал;Интернасьонал-РС;Интернациональ РС","Internasional;Internacional-RS;Internacional RS;Internacional","Brazil","Бразилия"),
	Joinville(3000,"Жоинвиль;ФК Жоинвили;Жоинвилле;Жоинвилль СК;Джойнвилль;ФК Жоинвиль;ЖОИНВИЛЛЬ;Жоинвилли","Joinville;Joinville SC;Joinville-SC","Brazil","Бразилия"),
    Palmejras(1000,"Палмейрас","Palmejras;Palmeiras;Palmeiras SP;Palmeiras-SP","Brazil","Бразилия"),
    Gojas(1000,"Гойас;ФК Гояс;Гояс;ФК Гойас;Гойяс","Gojas;Goias;Goias GO;Goiás EC-GO","Brazil","Бразилия"),
    AtletikoMinejro(1000,"Атлетико Минейро;АтлетикоМинейро;Атлетико Мин.;АТЛЕТИКО МГ","AtletikoMinejro;Atletico Mineiro;Atletico Mineiro-MG;Atlético Mineiro-MG;Atletico MG","Brazil","Бразилия"),
    SanPaulu(1000,"СанПаулу;Сан-Паулу;ФК Сан-Пауло;Сан Паулу","SanPaulu;Sao Paulo;Sao Paulo-SP;Sao Paulo SP;São Paulo-SP","Brazil","Бразилия"),
    Korintians(1000,"Коринтианс;Коринтианс-СП;Коринтианс СП","Korintians;Corinthians;Corinthians-SP","Brazil","Бразилия"),
    Baja(1000,"Байа","Baja","Brazil","Бразилия"),
    Santos(1000,"Сантос;ФК Сантос","Santos;Santos Cape Town;Santos FC SP;Santos SP;Santos-SP;Santos FC","Brazil","Бразилия"),
    SportResife(1000,"СпортРесифе;Спорт Ресифе;Спорт Ресифи;РЕСИФ","SportResife;Sport Recife-PE;Sport Recife PE;Sport Recife;Sport","Brazil","Бразилия"),
    Krikuma(1000,"Крикума","Krikuma;Criciuma","Brazil","Бразилия"),
    AtletikoPar(1000,"Атлетико Пар.;АтлетикоПар.;Атлетико Паранаенсе;Атлетико ПР;Атлетико Паранаэнсе","AtletikoPar.;Atletico PR;Atletico Paranaense;Atlético Paranaense","Brazil","Бразилия"),
    VitoriaBA(1000,"Виториа","Vitoria;Vitoria BA","Brazil","Бразилия"),
    Flamengo(1000,"Фламенго;Фламенго РЖ","Flamengo;Flamengo RJ;Flamengo-RJ","Brazil","Бразилия"),
	Botafago(3000,";Ботафаго РЖ;Ботафого-РЖ;Ботафого Рио-де-Жанейро;Ботафого;Ботафого РЖ","Botafago;Botafogo RJ;Botafogo RJ (n);Botafogo","Brazil","Бразилия"),
    Koritiba(1000,"Коритиба;Куритиба","Koritiba;Coritiba","Brazil","Бразилия"),
    Figujrenshe(1000,"Фигуйренше;Фигуеренсе СК;Фигуеренсе;Фигейренсе;Фигуренсе","Figujrenshe;Figueirense-SC;Figueirense FC;Figueirense SC;Figueirense","Brazil","Бразилия"),
    Chapekoenshe(1000,"Чапекоенше;Шапекоенсе;ФК Шапекоэнсе;Шапекоэнсе;ШАПЕКОНСЕ;Шапекуенсе","CHapekoenshe;Chapecoense;Chapecoense AF;Chapecoense SC","Brazil","Бразилия"),
	VascodaGama(3000,"Васку да Гама;Васко да Гама;Васко да Гама-РЖ;ВАСКО","Vasco da Gama;Vasco Da Gama Cape Town;Vasco da Gama-RJ;Vasco Gama;Vasco Da Gama RJ","Brazil","Бразилия"),
	Avai(3000,"ФК Авай;Аваи;АВАЙ","Avai;Avai FC","Brazil","Бразилия"),
	BoavistaRJ(3000,"Боавишта РЖ;БоавиштаРЖ;ФК Боавишта РЖ;ФК БоавиштаРЖ;Боавишта-РЖ","BoavistaRJ;Boavista RJ;FC Boavista RJ;FC BoavistaRJ","Brazil","Бразилия"),
	Madureira(3000,"Мадуреира;ФК Мадуреира;Мадурейра","Madureira;FC Madureira;Madureira RJ","Brazil","Бразилия"),
	Tupi(3000,"Тупи;ФК Тупи;Тупи Минейро","Tupi;FC Tupi;Tupi MG","Brazil","Бразилия"),
	Lajeadense(3000,"Лежеаденсе;ФК Лежеаденсе;Лажеаденсе;ФК Лажеаденсе","Lajeadense;FC Lajeadense;Lajeadense RS;CE Lajeadense;Lajeadense/RS","Brazil","Бразилия"),
	RioClaro(3000,"Рио Кларо;ФК Рио Кларо;Рио Кларо СП;Риу-Клару","Rio Claro;FC Rio Claro;Rio Claro SP","Brazil","Бразилия"),
	Ypiranga(3000,"Ипиранга;ФК Ипиранга","Ypiranga;FC Ypiranga;Ypiranga Erechim;Ypiranga de Erechim;Ypiranga AP","Brazil","Бразилия"),
	Anapolis(3000,"Анаполис;ФК Анаполис;Анаполис ГО","Anapolis;FC Anapolis;Anapolis GO;Anapolis FC","Brazil","Бразилия"),
	
	//НорвегияПремьерЛига
	
    Olesun(1000,"Олесун;Олезунд;Ользунд;Олесунд;ФК Олесунн;Алесунд","Olesun;Aalesund;Aalesund FK;Aalesunds FK;Alesund","Norway","Норвегия"),
    BodoGlimt(1000,"Бодо Глимт;БодоГлимт;Боде/Глимт;Боде-Глимт (Боде);Боде-Глимт;ФК Будё-Глимт;Буде/Глимт;БУДЕ;Бодё Глимт","BodoGlimt;Bodø/Glimt;FC Bodo/Glimt;Bodo/Glimt;Bodoe/Glimt","Norway","Норвегия"),
    Haugesund(1000,"Хёугесунн;Хогесунд;Хогезунд;ФК Хогесунд;ФК Хёгесунн;ХАУГЕСУНД;Хогесунн","KHjougesunn;Haugesund;FK Haugesund;Haugesund FK","Norway","Норвегия"),
    Start(1000,"Старт","Start;Start IK;Ik Start;Start K.;Start","Norway","Норвегия"),
    Lillestrom(1000,"Лиллестрём;Лиллестрем;Лилльстрём;ФК Лиллестрем","Lillestrjom;Lillestrøm;Lilleström;Lillestroem;Lillestrom;Lillestrom Sk","Norway","Норвегия"),
	Mondalen(3000,"Мьондален;ФК Мьёндален;Мьёндален;Мьендален","Mondalen;Mjondalen;Mjondalen IF;Mjøndalen;Mjoendalen","Norway","Норвегия"),
    Molde(1000,"Мольде;ФК Мёльде;Мёльде;ФК Мольде","Mol'de;Molde;Molde FK","Norway","Норвегия"),
    OddGrenland(1000,"Одд Гренланд;Одд;Удд;Удд Гренланн;ОддГренланд;ОДДС","OddGrenland;Odds;Odd BK;Odds Ballklubb;Odd Grenland II;Odd;Odd Grenland BK;Odd;Odd Ballklubb","Norway","Норвегия"),
    Rusenborg(1000,"Русенборг;Русенборг (Тронхейм)","Rusenborg;Rosenborg;Rosenborg BK","Norway","Норвегия"),
    Sarpsborn08(1000,"Сарпсборн08;Сарпсборг 08;ФК Сарпсборг;САРПСБОРГ","Sarpsborn08;Sarpsborg 08;Sarpsborg","Norway","Норвегия"),
    Brann(1000,"Бранн;ФК Бранн;Бранн Берген","Brann;Brann Bergen;SK Brann","Norway","Норвегия"),
    Stremsgodset(1000,"Стремсгодсет;Стремгодсет;Стрёмсгодсет;Стрёмсгодсет ИФ;СТРЕМС","Stremsgodset;Stromsgodset;Stroemsgodset","Norway","Норвегия"),
    Volerenga(1000,"Волеренга;Валеренга","Volerenga;Vålerenga;Valerenga;Vaalerenga;Vålerenga IF","Norway","Норвегия"),
    Viking(1000,"Викинг;Викинг (Ставангер)","Viking;Viking FK;Viking Stavanger","Norway","Норвегия"),
    Stabek(1000,"Стабек;Стабек (Беккестуа)","Stabek;Stabæk;Stabaek IF;Stabaek Fotball;Stabaek","Norway","Норвегия"),
	Sandefjord(3000,"Сандефьорд;ФК Сандефьорд;Саннефьорд","Sandefjord;Sandefjord Fotball;Sandeford","Norway","Норвегия"),
	SannesUlf(1000,"Саннес Ульф;СаннесУльф","SannesUl'f;Sandnes Ulf","Norway","Норвегия"),
	Tromsjo(3000,"Тромсё;Тромсе;ФК Тромсё;Тромсо;ФК Тромсё ИЛ","Tromso;Tromsø;Tromsoe","Norway","Норвегия"),
	
	//ШвецияПремьерЛига
	
	Malme(1000,"Мальме;Мальмё;ФК Мальмё;ФК Мальме;Мальме ФФ","Mal'me;Malmo;Malmö FF;Malmö;Malmo FF;Malmoe FF","Sweden","Швеция"),
    Norrcheping(1000,"Норрчепинг;Норрчёпинг;ФК Норрчёпинг;ИФК Норркопинг;ФК Норрчепинг;НОРРКЁПИНГ","Norrcheping;IFK Norrkoping;Norrkoping;IFK Norrköping;IFK Norrkoeping","Sweden","Швеция"),
    AIKSolna(1000,"АИК Солна;АИКСолна;АИК","AIKSolna;AIK Solna;AIK","Sweden","Швеция"),
    Otvidaberg(1000,"Отвидаберг;Атвидаберг;Этвидаберг;ФК Отвидаберг ФФ","Otvidaberg;Atvidabergs;Atvidaberg FF;Åtvidabergs FF;Atvidaberg;Aatvidaberg","Sweden","Швеция"),
    Hacken(1000,"Хаккен;ФК Хаккен;Хакен;Хеккен;ФК Хеккен","Hacken;BK Häcken;Hacke;BK Hacken;Haecken","Sweden","Швеция"),
    YUrgorden(1000,"Юргорден","YUrgorden;Djurgaarden;Djurgardens","Sweden","Швеция"),
    Falkenberg(1000,"Фалькенберг;Фалкенбергс;Фалкенберг;ФК Фалькенберг;ФАЛЬКЕНБЕРГС","Fal'kenberg;Falkenbergs;Falkenbergs FF;Falkenberg","Sweden","Швеция"),
    Efle(1000,"Ефле;Гефле;Евле;Эвле;Эфле","Efle;Gefle;Gefle IF","Sweden","Швеция"),
    Halmstad(1000,"Хальмстад;ФК Хальмстад","KHal'mstad;KHalmstad;Halmstads;Halmstad;Halmstads BK","Sweden","Швеция"), 
    Helsingborg(1000,"Хельсинборг;Хельсингборг;ФК Хельсингборг","KHel'sinborg;Helsingborgs;Helsingborg;Helsingborgs IF","Sweden","Швеция"),
	Sundsvall(3000,"Сундсваль;Сундсвал;Сундсвалль;ФК Сундсвалль","Sundsvall;GIF Sundsvall","Sweden","Швеция"),
    Brommapojkarna(1000,"Броммапойкарна","Brommapojkarna","Sweden","Швеция"),
    Elfsborg(1000,"Эльфсборг","El'fsborg;IF Elfsborg;Elfsborg","Sweden","Швеция"),
    Geteborg(1000,"Гетеборг;ФК Гётеборг;Гётеборг;ИФК Гётеборг","Geteborg;IFK Goteborg;IFK Göteborg;IFK Gothenburg","Sweden","Швеция"),	
    Kalmar(1000,"Кальмар;ФК Кальмар","Kal'mar;Kalmar FF;Kalmar","Sweden","Швеция"),
    Melbyu(1000,"Мьельбю","M'el'byu","Sweden","Швеция"),
    Erebru(1000,"Эребру;Эребру СК;Оребро;ФК Эребру","Erebru;Örebro SK;Orebro;Orebro SK;Oerebro","Sweden","Швеция"),
	Hammarbju(3000,"Хаммарбю;Хаммарбю (Стокгольм)","Hammarbru;Hammarby IF;Hammarby","Sweden","Швеция"),
	Ostersunds(3000,"Эстерсунд;ФК Эстерсунд;Остерсунд","Ostersunds;FC Ostersunds;Ostersunds FK","Sweden","Швеция"),
	Jonkopings(3000,"Йёнчёппинг;ФК Йёнчёппинг;Йончопингс Содра;Йёнчёпинг","FC Jonkopings;Jonkopings;Jonkopings Sodra IF;Jonkoping Sodra;Jonkopings Sodra","Sweden","Швеция"),
	
	//США
	
	Vancouver(3000,"Ванкувер; ФК Ванкувер;ФК Ванкувер Уайткэпс II;Ванкувер Уайткэпс","Vancouver;Vancouver Whitecaps;Вайткэпс","United States","США"),
	Dallas(3000,"Даллас;ФК Даллас","Dallas;FC Dallas","United States","США"),
	NewYorkRedBulls(3000,"Нью-Йорк Ред Буллз;ФК Нью-Йорк Ред Буллз;Нью-Йорк Ред Булл;Ред Буллс;РБ Нью Йорк","FC New York Red Bulls;New York Red Bulls;New York RB;NY Red Bulls;Red Bulls NY;Red Bull New York","United States","США"),
	LosAngeles(3000,"Лос-Анджелес;ФК Лос-Анджелес;ФК Лос-Анджелес Галакси;Лос-Анджелес Гэлакси;Гэлэкси;Лос-Анжелес Гэлэкси","FC Los Angeles;Los Angeles Galaxy;LA Galaxy;Los Angeles","United States","США"),
	Washington(3000,"Вашингтон;ФК Вашингтон;Ди-Си Юнайтед;Ди Си Юнайтед;Вашингтон ДЦ;ДК Юнайтед","FC Washington;Washington DC United;Washington DC;DC United;DC Utd;Washington Utd;D.C. United","United States","США"),
	NewEngland(3000,"Нью-Ингланд;Нью Ингланд;ФК Нью-Ингланд;ФК Нью-Ингланд;Нью-Инглэнд Революшн;Нью-Ингленд Революшн;Нью Ингленд;Нью-Инглэнд;НЬЮ-ИНГЛЕНД","FC New England; NewEngland;New England Revolution;New England;New England Rev.","United States","США"),
	Seattle(3000,"Сиэттл;ФК Сиэттл;Сиэтл Саундерс;Сиэтл;Сиэтл Саундерз;ФК Сиэтл Соундерс;Саундерз","Seattle;Seattle Sounders;Seattle Sounders FC","United States","США"),
	Houston(3000,"Хьюстон;ФК Хьюстон;Хьюстон Динамо;Хоустон Динамо","Houston;Houston Dynamo;FC Houston","United States","США"),
	SaltLake(3000,"Реал Солт-Лейк;Солт Лейк;ФК Солт Лейк;Солт Лэйк;Реал Солт Лейк","Salt Lake;Real Salt Lake","United States","США"),
	Portland(3000,"Портленд;ФК Портленд;Портленд Тимберс;Портленд Тимберз;Тимберз","Portland;Portland Timbers","United States","США"),
	Kansas(3000,"Канзас;ФК Канзас;Спортинг Канзас-Сити;Канзас-Сити;Канзас Сити","Kansas;Sporting Kansas City;Kansas City;FC Kansas City;Kansas City Wizards","United States","США"),
	SanJose(3000,"Сан-Хосе;ФК Сан-Хосе;Сан-Хосе Эртквейкс;Сан Хосе","San Jose;San Jose Earthquakes","United States","США"),
	Columbus(3000,"Коламбус;ФК Коламбус;Коламбус Крю","Columbus;Columbus Crew SC;Columbus Crew;FC Columbus","United States","США"),
	Orlando(3000,"Орландо;ФК Орландо;Орландо Сити","Orlando;Orlando City SC;Orlando City;FC Orlando","United States","США"),
	Colorado(3000,"Колорадо,ФК Колорадо;Колорадо Рэпидз","Colorado;Colorado Rapids;FC Colorado","United States","США"),
	NewYorkCity(3000,"Нью Йорк Сити;ФК Нью Йорк Сити;Нью Йорк;Нью-Йорк Сити","New York City;New York City Football Club;New York City FC;NYC FC","United States","США"),
	Chicago(3000,"Чикаго;ФК Чикаго;Чикаго Файр","Chicago;Chicago Fire;FC Chicago","United States","США"),
	Philadelphia(3000,"Филадельфия;ФК Филадельфия;Филадельфия Юнион;Филадельфия Юн","Philadelphia;Philadelphia Union;FC Philadelphia","United States","США"),
	Toronto(3000,"Торонто;ФК Торонто;Торонто ФК","Toronto;Toronto FC","United States","США"),
	Montreal(3000,"Монреаль;ФК Монреаль;Монреаль Импакт;Импакт","Montreal;Montreal Impact;FC Montreal;Montreal Imp.;Impact Montreal","United States","США"),
	
	//Аргентина
	
	BocaJuniors(3000,"Бока Хуниорс;ФК Бока Хуниорс;Бока Хуниорс (Буэнос-Айрес);БОКА Х","Boca Juniors;Club Atletico Boca Juniors","Argentina","Аргентина"),
	RiverPlate(3000,"Ривер Плейт;ФК Ривер Плейт;Ривер Плэйт","River Plate;Club Atletico River Plate","Argentina","Аргентина"),
	Belgrano(3000,"Белграно;ФК Белграно;Бельграно;ФК Бельграно","Belgrano;Club Atletico Belgrano;Belgrano de Cordoba","Argentina","Аргентина"),
	Rosario(3000,"Росарио;ФК Росарио;Росарио Централь;ФК Росарио Сентраль;Росарио Сентраль;РОСАРИО ЦЕНТРАЛ","Rosario;Club Atletico Rosario Central;Rosario Central;CA Rosario Central","Argentina","Аргентина"),
	SanLorenzo(3000,"Сан Лоренцо;ФК Сан Лоренцо;Сан-Лоренцо;Сан-Лоренсо","San Lorenzo;Club Atletico San Lorenzo de Almagro;San Lorenzo de Almagro;CA San	Lorenzo","Argentina","Аргентина"),
	Tigre(3000,"Тигре;ФК Тигре","Tigre;Club Atletico Tigre;Uanl Tigres;CA Tigre;Atlético Tigre","Argentina","Аргентина"),
	RacingClub(3000,"Расинг Клуб;ФК Расинг Клуб;Расинг Клуб (Авельянеда);Расинг Авельянеда;РАСИНГ АВЕЛЛАНЕДА","Racing Club;Racing Club de Avellaneda;Racing","Argentina","Аргентина"),
	NewellsOldBoys(3000,"Ньюэлз Олд Бойз;Ньюэлз Олд Бойз (Росарио);Ньюэллc Олд Бойз;Ньюэлз О.Б.;Ньюэлс Олд Бойз;Ньюэллз Олл Бойз;ОЛД БОЙЗ","Newell's Old Boys;Club Atletico Newell's Old Boys;Newells Old Boys;Newell's OB","Argentina","Аргентина"),
	Banfield(3000,"Банфилд,ФК Банфилд","Banfield;Club Atletico Banfield;CA Banfield","Argentina","Аргентина"),
	Sarmiento(3000,"Сармьенто;ФК Сармьенто;ФК Сармиенто Леонес;Леонес;Сармьенто Хунин;КА Сармиенто;ФК Сармиенто Хунин;Сармиенто Хунин;САРМИЕНТО","Sarmiento;Sarmiento Football Club;CA Sarmiento","Argentina","Аргентина"),
	Lanus(3000,"Ланус;ФК Ланус","Lanus;Club Atletico Lanus;Atletico Lanus","Argentina","Аргентина"),
	Independiente(3000,"Индепендьенте;ФК Индепендьенте;Индепендьенте Р;Индепендьенте Ривадавиа;Индепенденте;Индепендьенте Авельянеда","Independiente;Atletico Independiente;Independiente Rivadavia;Independente PA;CA Independiente","Argentina","Аргентина"),
	SantaFe(3000,"Санта Фе;ФК Санта Фе;СантаФе;Санта-Фе;ФК Унион Санта-Фе;Юнион Де Санта Фе;Унион (Санта-Фе);Унион де Санта-Фе;Унион де СФ;Унион Санта-Фе;Унион СФ","Santa Fe;Atletico Union de Santa Fe;Union de Santa Fe;Union Santa Fe;Union de SF;Union","Argentina","Аргентина"),
	ArgentinosH(3000,"Аргентинос Хуниорс;ФК Аргентинос Хуниорс;Архентинос Джуниорс;Арг.Хуниорс;АРГЕНТИНОС Х","Argentinos H.;Asociación Atletica Argentinos Juniors;Argentinos Jrs;Argentinos Juniors","Argentina","Аргентина"),
	SanMartinHuan(3000,"Сан Мартин;Сан-Мартин;ФК Сан-Мартин;Тукуман;Атлетико Тукуман;Сан Мартин С-Х;Сан Мартин (Сан-Хуан);Сан-Мартин С-Х;Сан Мартин Сан-Хуан","San Martin;Club Atletico San Martín de San Juan;San Martín de San Juan;Atletico Tucuman;San Martin S. J.;San Martin San Juan;San Martin SJ;San Martin de San Juan","Argentina","Аргентина"),
	VelezSarsfield(3000,"Велес Сарсфилд;ФК Велес Сарсфилд","Velez Sarsfield;Club Atletico Velez Sarsfield;Velez S.","Argentina","Аргентина"),
	GimnasiaLP(3000,"Гимназия ЛП;ФК Гимнази ЛП;Химнасия и Эсгрима;Химназия;Химнасия Ла-Плата;ГИМНАСИЯ ЛП;Гимназия Ла Плата;Химнасия (Ла-Плата)","Gimnasia LP;Club de Gimnasia y Esgrima La Plata;Gimnasia;Gimnasia La Plata","Argentina","Аргентина"),
	Aldosivi(3000,"Альдосиви;ФК Альдосиви;КА Альдосиви;Альдосиви (Буэнос Айрес)","Aldosivi;Club Atletico Aldosivi;Aldosivi Mar del Plata","Argentina","Аргентина"),
	Temperley(3000,"Темперлей;ФК Темперлей;Клуб Атлетико Темперли;Темперли","Temperley;CA Temperley;Club Atletico Temperley","Argentina","Аргентина"),
	Estudiantes(3000,"Эстудиантес;ФК Эстудиантес;Эстудиантес (Ла-Плата);Эстудиантес Ла-Плата","Estudiantes;Club Estudiantes de La Plata;Estudiantes LP","Argentina","Аргентина"),
	DefensaHus(3000,"Дефенца Хус.;Дефенса и Хустисия;Дефенса и Юстиция;Дефенса Ю;Дефенса Джустика;Дефенса и Юстисиа;Дефенса;ФК Дефенса и Юстиция","DefensaHus;Defensa;Defensa y Justicia;C.S.D. Defensa y Justicia;C.S.D. Defensa Y Justicia;Defensa Jus.","Argentina","Аргентина"),
	Colon(3000,"Колон;ФК Колон;Колон Де Санта Фе;Колон (Санта Фе);Колон де СФ;Колон Санта-Фе;Колон СФ","Colon;Club Atletico Colon;Colon de Santa Fe;Colon de Santa Fe;Colon de SF;Colon Santa Fe","Argentina","Аргентина"),
	Quilmes(3000,"Килмес;ФК Килмес;Куилмес;ФК Кильмес;Кильмес","Quilmes;Quilmes Atletico Club;Quilmes AC","Argentina","Аргентина"),
	GodoyCruz(3000,"Годой Круз;ФК Годой Круз;ФК Годой-Крус;Годой Крус","Godoy Cruz;Godoy Cruz Antonio Tomba;Godoy Cruz Mza.;Godoy Cruz At;Godoy Cruz A.t.","Argentina","Аргентина"),
	Huracan(3000,"Хуракан;ФК Хуракан;Уракан;ФК Уракан;Уракан (Буэнос-Айрес);КА Хуракан","Huracan;Club Atletico Huracan;Huracan BA;Atletico Huracan","Argentina","Аргентина"),
	ArsenalS(3000,"Арсенал С.;ФК Арсенал С.;Арсенал де Саранди;Арсенал Саранди","Arsenal S.;Arsenal Futbol Club;Arsenal de Sarandí;Arsenal Sarandi","Argentina","Аргентина"),
	Crucero(3000,"Крусеро;Крузейро;ФК Крузейро;ФК Крузеро;Крусеро дель Норте;ФК Крузейро дель Норте;Срусеро Дел Норте;Крузейро-дель-Норте","Crucero;Club Mutual Crucero del Norte;Crucero Del Norte","Argentina","Аргентина"),
	NuevaChicago(3000,"Нуэва Чикаго;ФК Нуэва Чикаго;ФК Нуева Чикаго;Н. ЧИКАГО","Nueva Chicago;Club Atletico Nueva Chicago;Nueva Chicago BA","Argentina","Аргентина"),
	Rafaela(3000,"Рафаэла;ФК Рафаэла;Атлетико Рафаэла;Атлетико (Рафаэла)","Rafaela;Deportiva Atletico de Rafaela;Atletico de Rafaela;Atl. Rafaela;Atletico Rafaela","Argentina","Аргентина"),
	Olimpo(3000,"Олимпо;ФК Олимпо;Олимпо Байя-Бланка","Olimpo;Club Olimpo;Olimpo Bahia Blanca;Olimpo de Bahía Blanca","Argentina","Аргентина"),
	
	//БелоруссияПремьерЛига
	
	Bate(1000,"Бате;БАТЭ;БАТЭ (Борисов)","Bate;BATE Borisov;Bate Borisov","Belarus","Белоруссия"),
    Naftan(1000,"Нафтан;ФК Нафтан;Нафтан Новополоцк","Naftan;Naftan Novopolotsk;Naftan Novopol","Belarus","Белоруссия"),
    SHakhterS(1000,"Шахтер С;Шахтер Солигорск;Шахтёр (Солигорск)","SHakhter;SHakhter S;Shakhtyor Soligorsk;FC Shakhter Soligorsk;Shakhter Soligorsk;ShakhterSoligorsk","Belarus","Белоруссия"),
    Gomel(1000,"Гомель;ФК Гомель","Gomel';Gomel;HK Gomel","Belarus","Белоруссия"),
    Minsk(1000,"Минск;ФК Минск","Minsk;FC Minsk","Belarus","Белоруссия"),
    Neman(1000,"Неман;ФК Неман;Неман Белкард;Неман Гродно","Neman;FC Neman Grodno;Neman Grodno","Belarus","Белоруссия"),
    Slutsk(1000,"Слутск;ФК Слутск;ФК Слуцк;Слуцк;Слуцксахар","Slutsk;FC Slutsk;Slutsksakhar;FK Slutsk;Slutsksakhar Slutsk;FC Slutsc","Belarus","Белоруссия"),
    Belshina(1000,"Белшина;ФК Белшина;Белшина (Бобруйск);БЕЛЬШИНА","Belshina;Belshina Bobruisk","Belarus","Белоруссия"),
    DinamoBr(1000,"ДинамоБр.;ФК Динабо Бр.;Динамо (Брест);Динамо Брест","DinamoBr.;Dinamo Brest;Brest","Belarus","Белоруссия"),
    Granite(3000,"Гранит;ФК Гранит;Гранит Микашевичи","Granite;Granit Mikashevichi","Belarus","Белоруссия"),
	Vitebsk(3000,"Витебск;ФК Витебск","Vitebsk;FK Vitebsk","Belarus","Белоруссия"),
	SlaviaM(3000,"Славия М.;ФК Славия М.;Славия (Мозырь);Славия Мозырь;Славия М;Славия-Мозырь","Slavia M.;Slavia Mozyr","Belarus","Белоруссия"),
	TorpedoZh(3000,"Торпедо Ж.,ФК Торпедо Ж.;Торпедо БелАз;Торпедо-БелАЗ;Торпедо Жодино;Торпедо Ж","Torpedo Zh.;Torpedo Zhodino;Torpedo-Belaz;Torpedo-BelAZ;Torpedo Belaz;Torpedo-Bel.","Belarus","Белоруссия"),
	DinamoMinsk(1000,"Динамо Минск;Динамо (Минск);Динамо Мн;ДинамоМинск;Динамо Мн.","Dinamo Minsk;DinamoMn.","Belarus","Белоруссия"),
	
	//ИрландияПремьерЛига
	
	Dandolk(3000,"Дандолк;ФК Дандолк;ДУНДАЛК;Дандалк","Dandolk;Dundalk","Ireland","Ирландия"),
	CorkCity(3000,"Корк Сити;Корк;ФК Корк;ФК Корк Сити;КОРС","Cork City","Ireland","Ирландия"),
	Shamrock(3000,"Шемрок;ФК Шемрок;Шемрок Роверс;Шeмрок Роверс;ШЭМРОК РОВЕРС","Shamrock;Shamrock Rovers","Ireland","Ирландия"),
	Bohemian(3000,"Богемиан;Бохемианс;Богемианс Дублин;Богемиан ФК;Богемианс Д;БОГЕМИАНС","Bohemian;;Bohemians Dublin;Bohemians D.;Bohemian FC","Ireland","Ирландия"),
	SaintPatrick(3000,"Сент Птрик;Сент-Патрик;ФК Сент Птрик;Сент-Патрикс Атлетик;Сейнт-Патрикс;СТ. ПАТРИКС","Saint Patrick;St Patricks Athletic;St Patricks;St. Patricks;St. Patrick's Athletic;St. Patrick's Ath.","Ireland","Ирландия"),
	DerryCity(3000,"Дерри;Дэрри;Дерри Сити;ФК Дерри","Derry City;Derry","Ireland","Ирландия"),
	Longford(3000,"Лонгфорд;ФК Лонгфорд;ФК Лонгфорд Таун","Longford;Longford Town","Ireland","Ирландия"),
	Drogheda(3000,"Дрогхеда;ФК Дрогхеда;Дроэда Юнайтед;ДРОГЕДА","Drogheda;Drogheda United","Ireland","Ирландия"),
	Brei(3000,"Брэй;ФК Брэй;ФК Брей Уондерерс;Брэй Уондерерс;Брей Уондерерс;БРЕЙ","Brei;Bray;Bray Wanderers","Ireland","Ирландия"),
	SligoRovers(3000,"Слайго Роверс;ФК Слайго Роверс;Слиго Роверс;СЛИГО РОВЕРЗ","Sligo Rovers;Sligo","Ireland","Ирландия"),
	Galway(3000,"Гэлвей;ФК Гэлвей;Галвэй ФК;Голуэй Юнайтед;Гэлвей Юнайтед","Galway;Galway United;Galway FC;Galway Utd;Galway United FC","Ireland","Ирландия"),
	Limerick(3000,"Лимерик;ФК Лимерик;ЛИМЕРИК 37","Limerick;Limerick FC;Limerick 37","Ireland","Ирландия"),
	
	//Сербия
	
	PartizanBelgrade(3000,"Партизан Б.;Партизан Белград;ФК Партизан Б.;Партизан","Partizan Belgrade;Partizan Beograd;Partizan","Serbia","Сербия"),
	CrvenaZvezda(3000,"Црвена Звезда","Crvena Zvezda;FK Crvena Zvezda;Red Star Belgrade;Red Star;RS Belgrade","Serbia","Сербия"),
	MladostLucani(3000,"Младость;ФК Младость;Младость (Лучани);Младость Лучани;Младость Л;Младост Лучани","Mladost Lucani;Mladost Lučani;FK Mladost Lucani;Mladost L","Serbia","Сербия"),
	Cukaricki(3000,"Чукарички;Чукарички Станком;Цукарицки Белград;Чукарички Станком (Белград);Цукарицки Белград","FK Čukarički;Čukarički;FK Cukaricki;Cukaricki;Cukaricki Stankom;FK Cukaricki Stankom","Serbia","Сербия"),
	NoviPazar(3000,"Нови Пазар;Hoви-Пaзap;ФК Нови Пазар;Нови-Пазар","Novi Pazar;FK Novi Pazar","Serbia","Сербия"),
	Vojvodina(3000,"Войводина;Войводина Нови Сад;Воеводина","Vojvodina;FK Vojvodina;Vojvodina Novi Sad","Serbia","Сербия"),
	OFC(3000,"ОФК;ОФК Белград;БЕЛГРАД","OFC;OFK Belgrade;OFK Beograd;OFK Belgrad;Beograd","Serbia","Сербия"),
	Rad(3000,"Рад;Рад Белград","Rad;FK Rad Belgrade;FC Rad;Rad Beograd;Rad Belgrade","Serbia","Сербия"),
	RadnickiNis(3000,"Раднички Нис.;Раднички Ниш;Раднички Нис;Раднички;РАДНИЦКИ НИС","Radnicki Nis;FK Radnicki Nis;FK Radnicki NIS","Serbia","Сербия"),
	Jagodina(3000,"Ягодина;ФК Ягодина;ФК Ягодина 1919","Jagodina;FK Jagodina","Serbia","Сербия"),
	Borac(3000,"Борац;Борац Чачак;Борак Качак","Borac;Borac Cacak;FK Borac Cacak","Serbia","Сербия"),
	Napredak(3000,"Напредак;Напредак К;Напредак Крушевац","Napredak;Napredak Krusevac;FK Napredak Krusevac","Serbia","Сербия"),
	Vozdovac(3000,"Вождовац;Воздовак;Вождовац Белград","Vozdovac;Fk Vozdovac;Vozdovac Beograd;Fk Vozdovac;Vozdovac Belgrade","Serbia","Сербия"),
	DonjiSrem(3000,"Дони Срем;Доньи Срем;Доньи Срем (Печинци);СРЕМ ПЕЦИНИ","Donji Srem;FK Donji Srem Pecini;FK Donji Srem;FK Donji Srem Pecinci","Serbia","Сербия"),
	SpartakZlatibor(3000,"Спартак Златибор;ФК Спартак Суботица;Спартак Суботица;Спартак Златибор Вода;Спартак Злат.","Spartak Zlatibor;FK Spartak Subotica;FK Spartak Zlatibor Voda;Spartak Subotica","Serbia","Сербия"),
	Radnicki(3000,"Раднички 1923 Крагуевац;Раднички 1923;ФК Раднички-1923","Radnicki;FK Radnicki 1923;Radnički 1923 Kragujevac;Fk Radnicki 1923;Radnicki 1923","Serbia","Сербия"),
	Sindelic(3000,"Синделиц;ФК Синделиц;Синдьелиц Белград","Sindelic;FC Sindelic;FK Sindelic Beograd;Sindjelic;FK Sindjelic Beograd","Serbia","Сербия"),
	RadnikSurdulica(3000,"Радник Сурдулица;ФК Радник Сурдулица","Radnik Surdulica;FC Radnik Surdulica","Serbia","Сербия"),
	
	//ЛатвияПремьерЛига
	
	Ventspils(3000,"Вентспилс;ФК Вентспилс","Ventspils;FK Ventspils","Latvia","Латвия"),
	MetallurgLiepaja(3000,"Металлург Л.;ФЕ Металлург Л.;Лиепая;Металлург Лиеп.;ФК ЛИЕПАЯ;Металлург Лиепая","Metallurg Liepaja.;FK Liepaja;Liepaja;L. Metalurgs;FHK Liepajas Metalurgs","Latvia","Латвия"),
	Skonto(3000,"Сконто;ФК Сконто;Сконто Рига","Skonto;Skonto Riga;Skonto FC;FC Skonto Riga","Latvia","Латвия"),
	DaugavaDaugavpils(3000,"Даугава Д.;ФК Даугава Д.;БФЦ Даугавпилс;ДФЦ Даугава;Даугава Даугавпилс;БФК Даугавпилс;ДАУГАВА","Daugava Daugavpils;BFC Daugavpils;BFC Daugava","Latvia","Латвия"),
	Mette(3000,"Метта;ФК Метта;МЕТТА/Латвийский университет;МЕТТА/ЛУ","Mette;FS METTA LU;METTA;FS Metta/LU","Latvia","Латвия"),
	Gulbene(3000,"Гулбене;ФК Гулбене;ФК Гулбине","Gulbene;FB Gulbene;Gulbene 2005","Latvia","Латвия"),
	Eglava(1000,"Еглава;ФК Елгава;Елгава","Eglava;Jelgava;FK Jelgava","Latvia","Латвия"),
	Spartaks(1000,"Спартакс;Спартакс Юрмала;Спартакс Ю;Спартакс (Юрмала);Спартак Юрмала","Spartaks;Spartaks Jurmala;FK Spartaks","Latvia","Латвия"),
	Riga(1000,"Рига;ФК Рига;Карамба;Рига ФК","Riga;FC Riga;FC Caramba/Dinamo;Riga FC;Caramba Riga","Latvia","Латвия"),
    
    //ЛитваЛигаА
    
	Trakai(3000,"Тракай;ФК Тракай","Trakai;Traku FK","Lithuania","Литва"),
	Zalgiris(3000,"Жальгирис;ФК Жальгирис","Zalgiris;VMFD Zalgiris;Zalgiris Vilnius;Zalgiris Wilna","Lithuania","Литва"),
	Suduva(3000,"Судува;ФК Судува","Suduva;FK Suduva","Lithuania","Литва"),
	Atlantas(3000,"Атлантас;ФК Атлантас;АТЛАНТАС КЛАЙПЕДА","Atlantas;FK Atlantas;Atlantas Klaipeda","Lithuania","Литва"),
	Crouau(3000,"Круоя;ФК Круоя;ПАКРУОЙО","Crouau;Kruoja Pakruojis;Kruoja;FK Kruoja","Lithuania","Литва"),
	Stumbras(3000,"Стумбрас;ФК Стумбрас","Stumbras;Stumbras Kaunas","Lithuania","Литва"),
	SpirisKaunas(3000,"Спирия Каунас;Спирис;ФК Спирис;Спирис Каунас;С. КАУНАС","Spiris Kaunas;S. Kaunas;Spyris Kaunas","Lithuania","Литва"),
	Klaipeda(3000,"Клайпеда;ФК Клайпеда;Гранитас;Гранитас Клайпеда","Klaipeda;Granitas Kl.;Granitas Klaipeda","Lithuania","Литва"),
	Utenis(3000,"Утенис;ФК Утенис;Утенис Утена","Utenis;FK Utenis Utena","Lithuania","Литва"),
	Siauliai(3000,"Шайляй;ФК Щауляй;ФК Шауляй;Шауляй;ШЯУЛЯЙ","Siauliai","Lithuania","Литва"),
	
	//КитайСуперлига2015
	
	ShanghaiEastAsia(3000,"Шанхай Ист Азия;ФК Шанхай Ист Азия;ФК Шанхай Теллэйс;Шанхай Ист Эйша;СИПГ","Shanghai East Asia;Shanghai SIPG;Shanghai East Asia FC","China","Китай"),
	GuangzhouPharma(3000,"Гуанчжоу Фарма;Гуачжоу Ф.;ФК Гуанчжоу Эвергранд;Гуанчжоу Эвергранд;Гуангжоу Фармам","Guangzhou Pharma;Guangzhou;Guangzhou Evergrande","China","Китай"),
	BeyzhingGuan(3000,"Бейджинг;ФК Бейджинг;Бейджинг Гуан;Бэйцзин Гоань;Бейджинг Гуань;Бейцзин Гуань;Бэйцзин Г","Beyzhing Guan;Beijing Guoan","China","Китай"),
	HenanGian(3000,"Хеан;ФК Хеан;Хэнань Цзянье;Хенан Джианье;Хэнань Констракшн;ХЕНАН;Хенань Цзанье","Henan Gian;Henan;Henan Jianye","China","Китай"),
	ShanghaiShenhua(3000,"Шанхай Ш.;ФК Шанхай Ш.;Шанхай Шеньхуа;ФК Шанхай Шеньхуа;Шанхай Шенхуа;Шанхай Шэньхуа;ШАНХАЙ Ш","Shanghai Shenhua;Shenhua;Shanghai Greenland Shenhua","China","Китай"),
	GuangzhouRF(3000,"Гуанчжоу РФ;ФК Гуанчжоу РФ;Гуанчжоу Фули;Гуангжоу РФ","Guangzhou RF;Guangzhou R&F;Guangzhou R&F F.C.","China","Китай"),
	YoungsSaynti(3000,"Янгсу;ФК Янгсу;Цзянсу Сайнти;Янгсу Сайнти","Youngs Saynti;Jiangsu;Jiangsu Guoxin-Sainty","China","Китай"),
	ShendongLuneng(3000,"Шендонг Луненг;ФК Шендонг;Шаньдун Лунен;Шэньдун Луненг;Шаньдун Лунэн;Шэндонг Луненг;ШЕНДОН","Shendong Luneng;Shandong Luneng","China","Китай"),
	TiyanzhinKangshifu(3000,"Тиянжин;Тянджин;Тяньцзинь Тэда;Тиянжин Кангшифу;ТЯНЬЦЗИНЬ","Tiyanzhin Kangshifu;Tianjin;Tianjin Teda","China","Китай"),
	Liaoning(3000,"Лиаонинг;Ляонинг;ФК Лиаонинг;Ляонин Хувин","Liaoning;Shandong;Liaoning Hongyun","China","Китай"),
	ChangchunYatai(3000,"Чанчунь;ФК Чанчунь;ФК Чанчунь Ятай;Чанчунь Ятай;Чанчунь Ятаи;ЧАНГЧУН","Changchun Yatai;Changchun","China","Китай"),
	Hangchzhou(3000,"Ханчжоу;ФК Ханчжоу;Ханчжоу Гринтаун;Хангчжоу","Hangchzhou;Hangzhou;Hangzhou Greentown","China","Китай"),
	Shijiazhuang(3000,"Шицзячжуан;ФК Шицзячжуан ЮЦ;Шицзячжуан Джунчанг Джунхау;Шицзячжуан Евер Брайт;Шицзячжуан ЮЦ;Шицзячжуан Юнчан;Шицзячжуан Юнчан Цзюньхао;Фуджиан Джунхао","Shijiazhuang;Shijiazhuang Ever Bright","China","Китай"),
	Guizhou(3000,"Гуйчжоу;ФК Гуйчжоу;Гуйчжоу Жэньхэ;Гуйчжоу Рене;ГУЙЖОУ РЕНХЕ","Guizhou;Guizhou Renhe","China","Китай"),
	ChongqingLifan(3000,"Чонкинг;ФК Чонкинг;Чунцин Лифань;Чунцинь Лифань;Чонкинг Лифан;ЧУНЦИН","Chongqing Lifan;Lifan","China","Китай"),
	ShanghaiShenksin(3000,"Шанхай Шенксин;ФК Шанхай Шанксин;Шанхай Шэньсинь;Шанхай Шеньсинь;Наньчан Шанхай Шэньсинь","Shanghai Shenksin;Shenxin;Shanghai Shenxin","China","Китай"),
	
	//Дания1дивизион
	
	Viborg(3000, "Виборг;ФК Виборг", "Viborg;Viborg FF","Denmark","Дания"),
    Vaile(3000, "Вайле;ФК Вайле Колдинг;ФК Вейле;Вейле", "Vaile;Vejle;Vejle BK;Vejle Boldklub","Denmark","Дания"),
    Lungblu(3000, "Люнгбю;ФК Люнгбю;Лингби", "Lungblu;Lyngby","Denmark","Дания"),
	Aarhus(3000,"Орхус;ФК Орхус;АГФ Орхус;АГФ Архус","Orhus;AGF Orhus;Aarhus;Aarhus FC;Aarhus GF;AGF;AGF Aarhus","Denmark","Дания"),
	Horring(3000,"Хьорринг;ФК Хьорринг;Вендсиссел ФФ;Веннсюссель;ВЕНДСЫССЕЛ","Horring;Horring FC;Vendsyssel FF","Denmark","Дания"),
	Horsens(3000,"Хорсенс;ФК Хорсенс;АК ХОРСЕНС","Horsens;Horsens FC;AC Horsens","Denmark","Дания"),
	Roskilde(3000,"Роскильде;ФК Роскильде;Роскилде;ФК Роскилле;Роскилле","Roskilde;Roskilde FC;FC Roskilde","Denmark","Дания"),
	Skive(3000,"Скиве;ФК Скиве","Skive;Skive FC;Skive Ik","Denmark","Дания"),
	Koge(3000,"Коге;ФК Коге;ХБ Кёге;Кеге;КЁГЕ","Koge;HB Koge;Koge FC;HB Koege","Denmark","Дания"),
	Fredericia(3000,"Фредериция;ФК Фредериция;ФК Фредерисия;Фредерисия;ФРЕДЕРИКА","Fredericia;Fredericia FC","Denmark","Дания"),
	AB(3000,"АБ;ФК АБ;Академиск;АБ (Копенгаген);АБ Гопенгаген;АБ Копенгаген","AB;AB FC;;AB Copenhagen;AB","Denmark","Дания"),
	Brønshøj(3000,"Бронсхой;Броншой;ФК Броншой;Бренсхей","Brønshøj;Brönshöj;Bronshoj;Broenshoej","Denmark","Дания"),
	Naestved(3000,"Нествед;ФК Нествед;Наествед БК;Нэствед","Naestved;FC Naestved;N?stved;Naestved BK","Denmark","Дания"),
	
	//Боливия - BoliviyaApertura

    Bolivar(3000, "Боливар;ФК Боливар", "Bolivar;FC Bolivar","Bolivia","Боливия"),
    Oriente(3000, "Ориенте;ФК Ориенте;Ориенте Петролеро;Ориенте Петролере", "Oriente;FC Oriente;Oriente Petrolero","Bolivia","Боливия"),
	TheStrongest(3000, "Стронгест;ФК Стронгест", "The Strongest;FC The Strongest","Bolivia","Боливия"),
	JorgeWilstermann(3000, "Хорхе Вильстерманн;ФК Хорхе Вильстерманн;Хорхе;Хорхе Вильстерманн (Кочамамба)", "Jorge Wilstermann;FC Jorge Wilstermann;Club Jorge Wilstermann","Bolivia","Боливия"),
	SanJoseOruro(3000, "Сан-Хосе Оруро;ФК Сан-Хосе Оруро;Сан-Хосе (Оруро);Сан Хосе Оруро", "San Jose;FC San Jose;San Jose Oruro","Bolivia","Боливия"),
	Blooming(3000, "Блуминг;ФК Блуминг", "Blooming;FC Blooming","Bolivia","Боливия"),
	Potosi(3000, "Реал Потоси;ФК Реал Потоси;Потоси;ПОТОШИ","Potosi;FC Potosi;Club Bamin Real Potosi;Real Potosi","Bolivia","Боливия"),
	Sucre(3000,"Де Сукре;Университарио де Сукре;Университарио (Сукре);Унив. Сукре;Универ. де Сукре;Университарио;Университарио Сукре;УНИВЕРСИТЕТ ДЕ СУКРЕ","De Sucre;Univer. de Sucre;Universitario de Sucre;U. de Sucre;Universitario","Bolivia","Боливия"),
	NacionalPotosi(3000, "Насьональ Потоси;ФК Насьональ Потоси;Насьональ (Потоси);Насьонал Потоси;НАСЬОНАЛЬ П", "Nacional Potosi;FC Nacional Potosi","Bolivia","Боливия"),
	Petrolero(3000, "Петролеро;ФК Петролеро;Петролеро Якуиба", "Petrolero;FC Petrolero;Club Petrolero","Bolivia","Боливия"),
	SportBoys(3000, "Спорт Бойз Уорнс;ФК Спорт Бойз Уорнс;Спорт Бойз Варнес;СПОРТ БОЙЗ", "Sport Boys;FC Sport Boys;Sport Boys Uorns;Sport Boys Warnes","Bolivia","Боливия"),
	UnivdePando(3000, "Универ. де Пандо;ФК Универ. де Пандо;Университарио де Пандо;Университарио Пандо", "Univ. de Pando;FC Univ. de Pando;Pando;Univer. de Pando;Universitario De Pando","Bolivia","Боливия"),
	
	//ИсландияПремьер - IslandiyaPrem'er
	
	LeiknirR(3000, "Лэйкнир;ФК Лэйкнир;Лейкнир;Лэйкнир Рейкьявик", "Leiknir R;FC Leiknir R;Leiknir Reykjavik;Leiknir Reykjavík","Iceland","Исландия"),
	VikingurR(3000, "Викингур;ФК Викингур;Викингюр Рейкьявик;Викингур Р.", "Vikingur R;FC Vikingur R;Vikingur Reykjavik","Iceland","Исландия"),
	Hafnarfjordur(3000, "Хафнарфьордур;ФК Хафнарфьордур;ФК Хафнарфьордюр;Хабнарфьордюр;Хафнарфьордюр", "Hafnarfjordur;FC Hafnarfjordur;FH Hafnarfjordur","Iceland","Исландия"),
	StjarnanG(3000, "Стярнан;ФК Стярнан;Стьярнан", "Stjarnan G;FC Stjarnan G;Stjarnan;Stjarnan Gardabaer","Iceland","Исландия"),
	FjolnirR(3000, "Фьолнир;ФК Фьолнир;Фьелнир;ФЁЛНИР", "Fjolnir R;FC Fjolnir R;Fjölnir Reykjavik;Fjolnir","Iceland","Исландия"),
	FylkirR(3000, "Фюлкир;ФК Фюлкир;Филкир;ФИЛЬКИР", "Fylkir R;FC Fylkir R;Fylkir","Iceland","Исландия"),
	Breidablik(3000, "Брейаблик;ФК Брейаблик;Брейдаблик", "Breidablik;FC Breidablik","Iceland","Исландия"),
	Vestmannaeyjar(3000, "Вестманнаэйяр;ФК Вестманнаэйяр;ВЕСТМАННАЯР", "Vestmannaeyjar;FC Vestmannaeyjar;IBV Vestmannaeyjar","Iceland","Исландия"),
	IAAkranes(3000, "Акранес;ФК Акранес", "IA Akranes;FC IA Akranes","Iceland","Исландия"),
	KRReykjavik(3000, "КР Рейкъявик;ФК КР Рейкъявик;КР Рейкьявик;Рейкьявик КР", "KR Reykjavik;FC KR Reykjavik;Reykjavik","Iceland","Исландия"),
	Keflavik(3000, "Кефлавик;ФК Кефлавик;ФК Кеблавик;Кеблавик", "Keflavik;FC Keflavik","Iceland","Исландия"),
	ValurR(3000, "Валюр;ФК Валюр", "Valur R;FC Valur R;Valur;Valur Reykjavik","Iceland","Исландия"),
	
	//Исландия1Дивизион - Islandiya1Divizion
	
	VikingurO(3000, "Викингур О;ФК Викингур О;Викингюр Оулафсвик;ВИКИНГУР;Викингур О.", "Vikingur O;FC Vikingur O;Vikingur Olafsvik","Iceland","Исландия"),
	Fjardabyggd(3000, "Фьяллабюггар;ФК Фьяллабюггар;КФ Фьярдабигд;Фьярдабигд;КФ ФЬОРДАБИГД;КФ Фьядлабигд", "Fjardabyggd;FC Fjardabyggd;KF Fjardabyggd","Iceland","Исландия"),
	Grindavik(3000, "Гриндавик;ФК Гриндавик", "Grindavik;FC Grindavik","Iceland","Исландия"),
	FramR(3000, "Фрам;ФК Фрам", "Fram R;FC Fram R;Fram Reykjavik","Iceland","Исландия"),
	BiBolungarvik(3000, "Би Болунгарвик;ФК Би Болунгарвик;ФК Би/Болунгарвик;БИ/Болунгарвик;Болунгарвик", "Bi Bolungarvik;FC Bi Bolungarvik;Bi/Bolungarvik","Iceland","Исландия"),
	Grotta(3000, "Гроутта;ФК Гроутта;Гротта;Гретта", "Grotta;FC Grotta","Iceland","Исландия"),
	ThorA(3000, "Тор;ФК Тор;Тор Акюрейри;Тор Акурейри", "Thor A;FC Thor A;Thor Akureyri","Iceland","Исландия"),
	KaAkureyri(3000, "Акюрейри;ФК Акюрейри;КА Акюрейри;КА Акурейри", "Ka Akureyri;FC Ka Akureyri;Akureyri;Akureyri KA","Iceland","Исландия"),
	Kopavogs(3000, "Копавогур;ФК Копавогур;ХК Коупавогюр", "Kopavogs;FC Kopavogs;HK Kopavogs","Iceland","Исландия"),
	Hafnarfjordur2(3000, "Хаукар;ФК Хаукар", "Hafnarfjordur 2;FC Hafnarfjordur 2;Hafnarfjordur;Haukar","Iceland","Исландия"),
	ThrotturR(3000, "Труттюр;ФК Труттюр;Троттур;Тротюр Рейкьявик", "Throttur R;FC Throttur R;Throttur Reykjavík","Iceland","Исландия"),
	Selfoss(3000, "Селфосс;ФК Селфосс;Сельфосс", "Selfoss;FC Selfoss;UMF Selfoss","Iceland","Исландия"),
	
	
	//КазахстанПремьерЛига - KazahstanPrem'erLiga
	
	Aktobe(3000, "Актобе;ФК Актобе", "Aktobe;FC Aktobe;FK Aktobe","Kazakhstan","Казахстан"),
	Astana(3000, "Астана;ФК Астана", "Astana;FC Astana","Kazakhstan","Казахстан"),
	Ordabasy(3000, "Ордабасы;ФК Ордабасы", "Ordabasy;FC Ordabasy;Ordabasy Shymkent","Kazakhstan","Казахстан"),
	Atyrau(3000, "Атырау;ФК Атырау", "Atyrau;FC Atyrau","Kazakhstan","Казахстан"),
	Almaty(3000, "Кайрат;ФК Кайрат;Кайрат-Алматы", "Almaty;FC Almaty;Kairat Almaty","Kazakhstan","Казахстан"),
	Taraz(3000, "Тараз;ФК Тараз", "Taraz;FC Taraz","Kazakhstan","Казахстан"),
	Okzhetpes(3000, "Окжетпес;ФК Окжетпес", "Okzhetpes;FC Okzhetpes;Okzhetpes Kokshetau","Kazakhstan","Казахстан"),
	KaisarKyzylorda(3000, "Кайсар;ФК Кайсар", "Kaisar Kyzylorda;FC Kaisar Kyzylorda","Kazakhstan","Казахстан"),
	Tobol(3000, "Тобол К;ФК Тобол К;ТОБОЛ", "Tobol;FC Tobol;Tobol Kostanay","Kazakhstan","Казахстан"),
	Irtysh(3000, "Иртыш П;ФК Иртыш П;Иртыш;Иртыш Павлодар", "Irtysh;FC Irtysh;Irtysh Pavlodar","Kazakhstan","Казахстан"),
	Zhetysu(3000, "Жетысу;ФК Жетысу;Жетусы Талдыкорган", "Zhetysu;FC Zhetysu;Zhetysu Taldykorgan","Kazakhstan","Казахстан"),
	ShKaragandy(3000, "Шахтер К;ФК Шахтер К;Шахтер Караганда;Шахтер К-да;Шахтер Кар", "Sh. Karagandy;FC Sh. Karagandy;Shakhter Karagandy;Shak. Karagandy;Shakhtar Karagandy","Kazakhstan","Казахстан"),
	
	//КолумбияАпертура - KolumbiyaApertura

	Huila(3000, "Атлетико Уила;ФК Атлетико Уила;УИЛА", "Huila;FC Huila;Atletico Huila","Colombia","Колумбия"),
	Envigado(3000, "Энвихадо;ФК Энвихадо;ФК Энвигадо;ЭНВИГАДО", "Envigado;FC Envigado","Colombia","Колумбия"),
	DepCali(3000, "Депортиво Кали;ФК Депортиво Кали;КАЛИ", "Dep. Cali;FC Dep. Cali;Deportivo Cali","Colombia","Колумбия"),
	IndMedellin(3000, "Индепендьенте М;ФК Индепендьенте М;ФК Индепендьенте Медельин;Индепендьенте Медельин;МЕДЕЛЛИН", "Ind. Medellin;FC Ind. Medellin;Independiente Medellin","Colombia","Колумбия"),
	SantaFeInd(3000, "Индепендьенте СФ;ФК Индепендьенте СФ;ФК Санта-Фе", "FC Santa Fe","Colombia","Колумбия"),
	CAJunior(3000, "Хуниор;ФК Хуниор;Атлетико Хуниор;Джуниор Барранкилья", "Junior;CA Junior;FC CA Junior;Atletico Junior","Colombia","Колумбия"),
	AtleticoNacional(3000, "Атлетико Насиональ;ФК Атлетико Насиональ;Атлетико Насьональ", "Atletico Nacional;FC Atletico Nacional;Atlético Nacional","Colombia","Колумбия"),
	Millonarios(3000, "Мильонариос;ФК Мильонариос", "Millonarios;FC Millonarios","Colombia","Колумбия"),
	Tolima(3000, "Депортес Толима;ФК Депортес Толима;ТОЛИМА", "Tolima;FC Tolima;Deportes Tolima","Colombia","Колумбия"),
	BoyPatriotas(3000, "Патриотас;ФК Патриотас", "Boy. Patriotas;FC Boy. Patriotas;Patriotas","Colombia","Колумбия"),
	AguilasDoradas(3000, "Дорадас;ФК Дорадас;Агуилас Дорадас;Агилас Дорадас", "Aguilas Doradas;FC Aguilas Doradas","Colombia","Колумбия"),
	LaEquidad(3000, "Ла Экидад;ФК Ла Экидад;ЛЯ ЭКВИДАД", "La Equidad;FC La Equidad;La Equidad Bogota","Colombia","Колумбия"),
	Caldas(3000, "Онсе Кальдас;ФК Онсе Кальдас;КАЛЬДАС", "Caldas;FC Caldas;Once Caldas","Colombia","Колумбия"),
	Cortulua(3000, "Кортулуа;ФК Кортулуа", "Cortulua;FC Cortulua","Colombia","Колумбия"),
	JaguaresK(3000, "Хагуарес Кордоба;ФК Хагуарес Кордоба;Ягуарес Де Кордова;ЖАГУАРЕС", "Jaguares;FC Jaguares;CD Jaguares","Colombia","Колумбия"),
	AlianzaPetrolera(3000, "Альянца Петролера;ФК Альянца Петролера;Альянса Петролера;Альянса Петролеро;АЛИАНЦА ПЕТРОЛЕРА", "Alianza Petrolera;FC Alianza Petrolera","Colombia","Колумбия"),
	BoyacaChico(3000, "Бояка Чико;ФК Бояка Чико;Бойака Чико;ЧИКО", "Boyaca Chico;FC Boyaca Chico;Chico FC","Colombia","Колумбия"),
	Cucuta(3000, "Кукута Депортиво;ФК Кукута Депортиво;КУКУТА", "Cucuta;FC Cucuta","Colombia","Колумбия"),
	Uniautonoma(3000, "Унив. Аутонома;ФК Унив. Аутонома;Униаутонома;УА ДЕЛЬ КАРИБЕ", "Uniautonoma;FC Uniautonoma;Autonoma;Universidad Autonoma","Colombia","Колумбия"),
	Pasto(3000, "Депортиво Пасто;ФК Депортиво Пасто;ПАСТО", "Pasto;FC Pasto;Deportivo Pasto","Colombia","Колумбия"),
	Fortaleza(3000,"Форталеза;ФК Форталеза;Форталеза Сипакира;Форталеса","Fortaleza;FC Fortaleza;Fortaleza CEIF FC;Fortaleza C.E.I.F.;Fortaleza	FC","Colombia","Колумбия"),
	
	//МалайзияСуперлига - MalaysiaSuperliga
	
	Lionsxii(3000, "Лайонс XI;ФК Лайонс XI;Лайонс XII;Лайонз ХII;ЛАЙНС;Сингапур Лайонс XII","Lionsxii;FC Lionsxii;Singapore Lions XII","Malaysia","Малайзия"),
	Selangor(3000, "Селангор;ФК Селангор", "Selangor;Selangor FC;FC Selangor","Malaysia","Малайзия"),
	JohorDT(3000, "Джохор;ФК Джохор;ФК Джохор Дарул Такзим;Дарул Такзим;ДЖОХОР Д. Т.;Джохор Дарул Такзим ФК", "Johor D. T.;FC Johor D. T.;Johor Darul Ta'zim FC","Malaysia","Малайзия"),
	Kelantan(3000, "Келантан;ФК Келантан;Келентан", "Kelantan;FC Kelantan","Malaysia","Малайзия"),
	Pahang(3000, "Паханг;ФК Паханг", "Pahang;FC Pahang","Malaysia","Малайзия"),
	ATM(3000, "АТМ;ФК АТМ;АТМ ФА", "ATM;FC ATM;ATM FA","Malaysia","Малайзия"),
	Perak(3000, "Перак;ФК Перак", "Perak;FC Perak","Malaysia","Малайзия"),
	SelangorPKNS(3000, "ПКНС;ФК ПКНС", "Selangor PKNS;FC Selangor PKNS","Malaysia","Малайзия"),
	Terengganu(3000, "Теренггану;ФК Теренггану;ФК Тренгану;Теренгану;ТРЕНГАНУ", "Terengganu;FC Terengganu","Malaysia","Малайзия"),
	PBDKT(3000, "ПБД КТ;ФК ПБД КТ", "PBD KT;FC PBD KT","Malaysia","Малайзия"),
	Felda(3000, "Фелда;ФК Фелда;Фелда Юнайтед;ФЕЛДА Юн.", "Felda;FC Felda;Felda United FC","Malaysia","Малайзия"),
	NegeriSembilan(3000, "Негери Сембилан;ФК Негери Сембилан", "Negeri Sembilan;FC Negeri Sembilan","Malaysia","Малайзия"),
	PDRM(3000,"ФК ПДРМ;ПДРМ;ПДРМ ФА","PDRM","Malaysia","Малайзия"),
	Saravak(3000,"ФК Саравак;Саравак","Saravak;Sarawak FA","Malaysia","Малайзия"),
	Sime(3000,"Сайм Дарби;Сайм Дерби;СИМЕ ДАРБИ","Sime;Sime Darby","Malaysia","Малайзия"),
	
	//МексикаАпертура - MexicaApertura
    
	Tigres(3000, "Тигрес;ФК Тигрес;УАНЛ Тигрес", "Tigres;FC Tigres","Mexico","Мексика"),
	America(3000, "Америка;ФК Америка", "America;FC America;Sud America;IA South America;CF America","Mexico","Мексика"),
	Atlas(3000, "Атлас;ФК Атлас", "Atlas;FC Atlas","Mexico","Мексика"),
	Toluca(3000, "Толука;ФК Толука", "Toluca;FC Toluca","Mexico","Мексика"),
	Jaguares(3000, "Хагуарес;ФК Хагуарес;Хагуарес Чьяпас", "Jaguares;FC Jaguares","Mexico","Мексика"),
	Monterrey(3000, "Монтеррей;ФК Монтеррей", "Monterrey;FC Monterrey","Mexico","Мексика"),
	Pachuca(3000, "Пачука;ФК Пачука", "Pachuca;FC Pachuca","Mexico","Мексика"),
	UnamPumas(3000, "УНАМ;ФК УНАМ;УНАМ Пумас", "Unam Pumas;FC Unam Pumas;Pumas","Mexico","Мексика"),
	SantosLaguna(3000, "Сантос Лагуна;ФК Сантос Лагуна", "Santos;FC Santos","Mexico","Мексика"),
	Leon(3000, "Леон;ФК Леон", "Leon;FC Leon","Mexico","Мексика"),
	Queretaro(3000, "Керетаро;ФК Керетаро", "Queretaro;FC Queretaro;Queretaro FC","Mexico","Мексика"),
	Tijuana(3000, "Тихуана;ФК Тихуана", "Tijuana;FC Tijuana","Mexico","Мексика"),
	CruzAzul(3000, "Крус Асуль;ФК Крус Асуль;Круз Азул", "Cruz Azul;FC Cruz Azul","Mexico","Мексика"),
	LeonesNegros(3000, "Унив. Гвадалахара;ФК Унив. Гвадалахара;Универсидад де Гвадалахара", "Leones Negros;FC Leones Negros;Universidad Guadalajara","Mexico","Мексика"),
	Puebla(3000, "Пуэбла;ФК Пуэбла", "Puebla;FC Puebla","Mexico","Мексика"),
	GChivas(3000, "Гвадалахара;ФК Гвадалахара;Чивас", "G. Chivas;FC G. Chivas;CD Guadalajara","Mexico","Мексика"),
	Veracruz(3000, "Веракрус;ФК Веракрус", "Veracruz;FC Veracruz;Tiburones Veracruz","Mexico","Мексика"),
	Monarcas(3000, "Монаркас;ФК Монаркас;Монаркас Морелия", "Monarcas;FC Monarcas;Morelia","Mexico","Мексика"),
	

	//ПеруАпертура - PeruApertura

	Garcilaso(3000, "Гаркиласо;ФК Гаркиласо;Реал Гарсиласо", "Garcilaso;FC Garcilaso;Real Garcilaso","Peru","Перу"),
	Melgar(3000, "Мелгар;ФК Мелгар;Мельгар", "Melgar;FC Melgar;FBC Melgar","Peru","Перу"),
	Cienciano(3000, "Сиенсиано;ФК Сиенсиано;Сьенсиано", "Cienciano;FC Cienciano","Peru","Перу"),
	AlianzaAtletico(3000, "Альянца Атлетико;ФК Альянца Атлетико;Альянса Атлетико;АЛЬЯНСА", "Alianza Atletico;FC Alianza Atletico","Peru","Перу"),
	JuanAurich(3000, "Хуан Аурич;ФК Хуан Аурич;ХУАН АУРИХ", "Juan Aurich;FC Juan Aurich","Peru","Перу"),
	Comercio(3000, "Комерцио;ФК Комерцио;Унион Комерсио;КОМЕРСИО", "Comercio;FC Comercio;Union Comercio","Peru","Перу"),
	DepMunicipal(3000, "Депортиво Мунисипал;ФК Депортиво Мунисипал;Депортиво Мунисипаль;ДЕП. МУНИСИПАЛЬ", "Dep. Municipal;FC Dep. Municipal;Deportivo Municipal","Peru","Перу"),
	SportHuancayo(3000, "Спорт Уанкайо;ФК Спорт Уанкайо;ХУАНКАЙО", "Sport Huancayo;FC Sport Huancayo","Peru","Перу"),
	AlianzaLima(3000, "Альянца Лима;ФК Альянца Лима;Альянса;Альянса Лима", "Alianza Lima;FC Alianza Lima","Peru","Перу"),
	SportLoreto(3000, "Спорт Лорето;ФК Спорт Лорето", "Sport Loreto;FC Sport Loreto;CD Sport Loreto","Peru","Перу"),
	SportingCristal(3000, "Спортинг Кр.;ФК Спортинг Кр.;Спортинг Кристал", "Sporting Cristal;FC Sporting Cristal","Peru","Перу"),
	SanMartin(3000, "У.Сан-Мартин;ФК У.Сан-Мартин;Универсидад Сан-Мартин;САН МАРТИН;У. Сан Мартин","San Martin;FC San Martin;Universidad San Martin;Uni San Martin","Peru","Перу"),
	UniversitarioDep(3000, "Университарио Л;ФК Университарио Л;Университарио Лима;Университарио Деп.", "Universitario Dep.;FC Universitario Dep.;Universitario de Deportes;Un. de Deportes","Peru","Перу"),
	CesarVallejo(3000, "Сезар Валльехо;ФК Сезар Валльехо;Универсидад Сесар Вальехо;Сесар Вальехо;СЕЗАР", "Cesar Vallejo;FC Cesar Vallejo;Universidad Cesar Vallejo","Peru","Перу"),
	Cajamarca(3000, "Кахамарка;ФК Кахамарка;УТК", "Cajamarca;FC Cajamarca;CD UT Cajamarca;UTC Cajamarca;Utc de Cajamarca","Peru","Перу"),
	IntiGasAya(3000, "Инти Гас;ФК Инти Гас;Леон де Уануко;Аякучо", "Inti Gas Aya.;FC Inti Gas Aya.;Inti Gas Deportes;Ayacucho FC","Peru","Перу"),
	LeondeHuanuco(3000, "Леон де Хуануко;ФК Леон де Хуануко;Леон де Уануко;ЛЕОН", "Leon de Huanuco;FC Leon de Huanuco","Peru","Перу"),
	
	//СингапурS-Лига - SingapurS-liga
	
	Warriors(3000, "Уорриорс;ФК Уорриорс;Уорриорз ФК;СИНГАПУРСКИЕ ВООРУЖЕННЫЕ СИЛЫ;Уорриорс ФК", "Warriors;FC Warriors;Warriors FC","Singapore","Сингапур"),
	AlbirexNiigata(3000, "Албирекс;ФК Албирекс;Албирекс Ниигата Сингапур", "Albirex Niigata;FC Albirex Niigata;Albirex Niigata FC","Singapore","Сингапур"),
	Brunei(3000, "Бруней;ФК Бруней;Бруней ДПММ;Бруней Дули ПММ;ДПММ", "Brunei;FC Brunei;Brunei DPMM FC;Brunei DPMM","Singapore","Сингапур"),
	BalestierKhalsa(3000, "Балестье;ФК Балестье;Балестье Халса;Бэлестьер Халса;БАЛЕСТИЕР", "Balestier Khalsa;FC Balestier Khalsa;Balestier Khalsa FC","Singapore","Сингапур"),
	GeylangInt(3000, "Гейланг;ФК Гейланг;Гейланг Интернейшнл;ГЕЙЛАНГ ИНТ.;Гейланг Юнайтед", "Geylang Int.;FC Geylang Int.;Geylang International;Geylang International FC","Singapore","Сингапур"),
	TampinesRovers(3000, "Тампинс;ФК Тампинс;Тампайн Роверс;Тампинс Роверс", "Tampines Rovers;FC Tampines Rovers;Tampines Rovers FC","Singapore","Сингапур"),
	HomeUnited(3000, "Хоум Юнайтед;ФК Хоум Юнайтед;ХОУМ ЮН", "Hom;Харимау Мудаe United;FC Home United;Home Utd;Home United FC","Singapore","Сингапур"),
	HarimauMuda(3000, "Харимау;ФК Харимау;Харимау Муда II", "Harimau Muda;FC Harimau Muda","Singapore","Сингапур"),
	Hougang(3000, "Хуганг Юнайтед;ФК Хуганг Юнайтед;Хёган Юнайтед;ХОУГАНГ ЮН.;Хёган Юн.", "Hougang;FC Hougang;Hougang United FC","Singapore","Сингапур"),
	YoungLions(3000, "Янг Лайонс;ФК Янг Лайонс;ЯНГ ЛАЙОНЗ", "Young Lions;FC Young Lions;Courts Young Lions","Singapore","Сингапур"),
	
	//УругвайАпертура - UrugvaiApertura
	
	NacionaldeF(3000, "Насьональ М.;ФК Насьональ М.", "Nacional de F.;FC Nacional de F.","Uruguay","Уругвай"),
	RacingClubMdeo(3000, "Расинг Клуб М.;ФК Расинг Клуб М.;Расинг Монтевидео", "Racing Club Mdeo.;FC Racing Club Mdeo.;Racing Club Montevideo;Racing Club Montevideo (URU);Racing Montevideo;RC Montevideo;Racing Club de Montevideo","Uruguay","Уругвай"),
	Penarol(3000, "Пеньяроль;ФК Пеньяроль;Пеньярол", "Penarol;FC Penarol;Penarol Montevideo;CA Penarol;Penarol de Montevideo;Penarol Montev.;Club Atletico Penarol","Uruguay","Уругвай"),
	CARiverPlate(3000, "Ривер Плейт Уругвай;Ривер Плейт ФК Уругвай;ФК Ривер Плейт Ург.;Ривер Плейт FC;Ривер Плейт Монтевидео;Ривер Плейт (Монтевидео);Атлетико Ривер Плэйт", "River Plate Uru;CA River Plate Uru;FC CA River Plate Uru;River Plate(URU);CA River Plate","Uruguay","Уругвай"),
	ElTanqueS(3000, "Эль Танке;ФК Эль Танке;Эль Танкью Сислей;Эль-Танке Сислей;ТАНКУЭ", "El Tanque S;FC El Tanque S;El Tanque Sisley;El Tanque Sisley","Uruguay","Уругвай"),
	DefensorSp(3000, "Дефенсор;ФК Дефенсор;Дефенсор Спортинг;Дефенсор Спортинг (Монтевидео);Деф.Спортинг", "Defensor Sp.;FC Defensor Sp.;Defensor Sporting","Uruguay","Уругвай"),
	Institucion(3000, "Суд Америка;ФК Суд Америка;АТЛЕТИКА", "Institucion;FC Institucion;Institucion Atletica SA","Uruguay","Уругвай"),
	Juventud(3000, "Хувентуд;ФК Хувентуд;Ювентуд;Хувентуд Лас-Пьедрас", "Juventud;FC Juventud;Juventud de Las Piedras;Juventud LP;Juventude;Juventude (Uru);Juventud Las Piedras;CA Juventud;Juventud de LP","Uruguay","Уругвай"),
	AtenasSanCarlos(3000, "Атенас;ФК Атенас;КА Атенас;АТЕНАС САН КАЛОС", "Atenas San Carlos;FC Atenas San Carlos;CA Atenas de San Carlos;Atenas de San C.;Atenas de San Carlos;CA Atenas;Atenas","Uruguay","Уругвай"),
	Rentistas(3000, "Рентистас;ФК Рентистас;АТЛЕТИКО Р", "Rentistas;FC Rentistas;Club Atletico Rentistas;CA Rentistas","Uruguay","Уругвай"),
	Wanderers(3000, "Уондерерс М;ФК Уондерерс М;Монтевидео Уондерерс;Монтевидео Уондерерз;МОН. УОНДЕРЕРЗ", "Wanderers;FC Wanderers;Montevideo Wanderers","Uruguay","Уругвай"),
	Fenix(3000, "Феникс;ФК Феникс", "Fenix;FC Fenix;CA  Fenix","Uruguay","Уругвай"),
	RamplaJrsFC(3000, "Рампла;ФК Рампла;Рампла Джуниорс;Рампла Джуниорс (Монтевидео);Рампла Юниорс;Рампла Хуниорс","Rampla Jrs.;Rampla Jrs. FC;FC Rampla Jrs.;Rampla Juniors FC;Rampla Jrs;Rampla Juniors (n);Rampla Juniors","Uruguay","Уругвай"),
	Cerro(3000, "Серру;ФК Серру;Серро", "Cerro;FC Cerro","Uruguay","Уругвай"),
	Tacuarembo(3000, "Такуарембо;ФК Такуарембо;Такуарембо ФК", "Tacuarembo;FC Tacuarembo;Tacuarembo FC;Tacuarembó","Uruguay","Уругвай"),
	Danubio(3000,"Данубио","Danubio","Uruguay","Уругвай"),
	
	//ЧилиАпертура - ChiliApertura
	
	Cobresal(3000, "Кобресал;ФК Кобресал;Кобресаль", "Cobresal;FC Cobresal","Chile","Чили"),
	Colo(3000, "Коло Коло;ФК Коло Коло", "Colo;FC Colo;Colo Colo","Chile","Чили"),
	Huachipato(3000, "Хуачипато;ФК Хуачипато;Уачипато", "Huachipato;FC Huachipato","Chile","Чили"),
	UCatolica(3000, "Унив.Католика (ЧИЛ);ФК Унив.Католика (ЧИЛ);Универсидад Католика;Универсидад Католика Сантьяго;КАТОЛИКА", "U. Catolica;FC U. Catolica;Universidad Catolica","Chile","Чили"),
	DeportesUnion(3000, "Унион Ла Калера;ФК Унион Ла Калера;Юнион Ла Калера;ЛА КАЛЕРА", "Deportes Union;FC Deportes Union;U.La Calera;Union La Calera","Chile","Чили"),
	UdeConcep(3000, "Унив.Консепсион;ФК Унив.Консепсион;ФК Универсидад де Консепсьон;Универсидад Консепсьон;У. ДЕ КОНСЕПСЬ", "U. de Concep;FC U. de Concep;Universidad de Concepcion","Chile","Чили"),
	UdeChile(3000, "Унив.де Чили;ФК Унив.де Чили;Универсидад де Чили;У. ДЕ ЧИЛИ", "U. de Chile;FC U. de Chile;Universidad de Chile;Universidad Chile","Chile","Чили"),
	Arica(3000, "Арика;ФК Арика;Сан Маркос Де Арича;Сан-Маркос де Арика", "Arica;FC Arica;San Marcos","Chile","Чили"),
	OHiggins(3000, "О Хиггинс;ФК О Хиггинс;О'Хиггинс", "O Higgins;FC O Higgins;O`Higgins;O`higgins FC;O'Higgins","Chile","Чили"),
	Espanola(3000, "Унион Эспаньола;ФК Унион Эспаньола;ЮНИОН ЭСПАНЬОЛА", "Espanola;FC Espanola;Union Espanola","Chile","Чили"),
	Cobreloa(3000, "Кобрелоа;ФК Кобрелоа", "Cobreloa;FC Cobreloa","Chile","Чили"),
	Iquique(3000, "Икике;ФК Икике;ФК Депортес Икике", "Iquique;FC Iquique;Deportes Iquique","Chile","Чили"),
	Audax(3000, "Итальяно;ФК Итальяно;Аудакс Италиано;Аудакс Итальяно;АУДАКС", "Audax;FC Audax;Audax Italiano","Chile","Чили"),
	Antofagasta(3000, "Антофагаста;ФК Антофагаста", "Antofagasta;FC Antofagasta","Chile","Чили"),
	Nublense(3000, "Ньюбленсе;ФК Ньюбленсе;Нубленсе", "Nublense;FC Nublense;Atletico Nublense","Chile","Чили"),
	Palestino(3000, "Палестино;ФК Палестино", "Palestino;FC Palestino","Chile","Чили"),
	SWanderers(3000, "Сантьяго Уондер.;ФК Сантьяго Уондер.;Сантьяго Уондерерз;Сантьяго Уондерерс;САНТЬЯГО В", "S. Wanderers;FC S. Wanderers;Santiago Wanderers","Chile","Чили"),
	Barnechea(3000, "Барнечеа;ФК Барнечеа", "Barnechea;FC Barnechea","Chile","Чили"),
	
	//АнглияЛига1 - AngliyaLiga1
	
	BristolCity(3000, "Бристоль Сити;ФК Бристоль Сити;БРИСТОЛЬ;ФК Бристоль;Бристоль Р.;Бристоль Роверс;Бристоль Роверс (Бристоль)", "Bristol City;FC Bristol City;Bristol Rovers","England","Англия"),
	MKDons(3000, "Донс;МК Донс;ФК МК Донс;ФК Донс;ФК Милтон Кинс Донс;Милтон-Кинз Донз", "Dons;MK Dons;FC MK Dons;FC Dons;Milton Keynes Dons","England","Англия"),
	Preston(3000, "Престон;ФК Престон;ФК Престон Норт Энд;Престон Норт Энд", "Preston;FC Preston","England","Англия"),
	Swindon(3000, "Суиндон;ФК Суиндон;Суиндон Таун", "Swindon;FC Swindon;Swindon Town","England","Англия"),
	SheffUtd(3000, "Шеффилд Ю;ФК Шеффилд Ю;Шеффилд Юн.", "Sheff Utd;FC Sheff Utd","England","Англия"),
	Chesterfield(3000, "Честерфилд;ФК Честерфилд", "Chesterfield;FC Chesterfield","England","Англия"),
	Bradford(3000, "Брэдфорд;ФК Брэдфорд", "Bradford;FC Bradford","England","Англия"),
	Rochdale(3000, "Рочдэйл;ФК Рочдэйл;РОЧДЕЙЛ", "Rochdale;FC Rochdale","England","Англия"),
	Peterborough(3000, "Питерборо;ФК Питерборо;ФК Питерборо Юнайтед;Питерборо Юнайтед;Петерборо Юн.;Петерборо Юнайтед", "Peterborough;FC Peterborough;Peterborough United","England","Англия"),
	Fleetwood(3000, "Флитвуд;ФК Флитвуд;Флитвуд Таун", "Fleetwood;FC Fleetwood;Fleetwood Town","England","Англия"),
	Barnsley(3000, "Барнсли;ФК Барнсли", "Barnsley;FC Barnsley","England","Англия"),
	Gillingham(3000, "Гиллингем;ФК Гиллингем;ФК Джиллингем;Джиллингем;Гиллингхэм", "Gillingham;FC Gillingham","England","Англия"),
	Doncaster(3000, "Донкастер;ФК Донкастер;Донкастер Роверс;Донкастер Р", "Doncaster;FC Doncaster","England","Англия"),
	Walsall(3000, "Уолсолл;ФК Уолсолл", "Walsall;FC Walsall","England","Англия"),
	Oldham(3000, "Олдхэм;ФК Олдхэм;ФК Олдем Атлетик;Олдэм Атлетик;Олдхем Атлетик;Олдхем", "Oldham;FC Oldham;Oldham Athletic","England","Англия"),
	Scunthorpe(3000, "Сканторп;ФК Сканторп;Сканторп Юнайтед;Сканторп Юн.", "Scunthorpe;FC Scunthorpe;Scunthorpe United;Scunthorpe Utd","England","Англия"),
	CoventryCity(3000, "Ковентри;ФК Ковентри;Ковентри Сити", "Coventry City;FC Coventry City;Coventry","England","Англия"),
	PortVale(3000, "Порт Вэйл;ФК Порт Вэйл;Порт Вэйл (Барслем);Порт Вейл", "Port Vale;FC Port Vale","England","Англия"),
	ColchesterUtd(3000, "Колчестер;ФК Колчестер;Колчестер Юнайтед;КОЛЧЕСТЕР ЮН", "Colchester Utd;FC Colchester Utd;Colchester","England","Англия"),
	Crewe(3000, "Крю;ФК Крю;Кру Александра;Крю Александра", "Crewe;FC Crewe","England","Англия"),
	Notts(3000, "Ноттс Каунти;ФК Ноттс Каунти;Ноттс К;Ноттс;", "Notts;FC Notts;Notts County;Notts Co.","England","Англия"),
	CrawleyTown(3000,"Кроули Таун;Кроули;ФК Кроули Таун;Краули Т.;ФК Краули Т.;ФК Кроули;Краули Таун", "Crawley Town;Crawley;CrawleyTown;Crawley;FC Crawley","England","Англия"),
	Leyton(3000, "Лейтон Ориент;ФК Лейтон Ориент", "Leyton;FC Leyton;Leyton Orient","England","Англия"),
	Yeovil(3000, "Йеувил;ФК Йеувил;Йовил Таун;ЙОВИЛ", "Yeovil;FC Yeovil;Yeovil Town","England","Англия"),
	
	//АнглияЛига2 - AngliyaLiga2
	
	Burton(3000, "Бартон;ФК Бартон;Бертон Альбион;БЁРТОН АЛЬБИОН", "Burton;FC Burton","England","Англия"),
	Shrewsbury(3000, "Шрубэри;ФК Шрубэри;ФК Шрусбери;Шрусбери Таун;ШРУСБЕРИ", "Shrewsbury;FC Shrewsbury;Shrewsbury Town","England","Англия"),
	BuryFC(3000, "Бэри;ФК Бэри;ФК Бери;Бери", "Bury;Bury FC;FC Bury","England","Англия"),
	Wycombe(3000, "Уикомб;ФК Уикомб;ФК Уиком Уондерерс;Уикомб Уондерерз;УИКОМ;Викомб;Уиком Уондерерс", "Wycombe;FC Wycombe;Wycombe Wanderers","England","Англия"),
	Southend(3000, "Саузенд;ФК Саузенд;Саутенд Юнайтед;САУТЕНД", "Southend;FC Southend;Southend	United","England","Англия"),
	Stevenage(3000, "Стивенэйдж;ФК Стивенэйдж;ФК Стивенидж;Стивенедж;СТИВЕНИДЖ БОРОУ", "Stevenage;FC Stevenage","England","Англия"),
	Plymouth(3000, "Плимут;ФК Плимут;Плимут Аргайл", "Plymouth;FC Plymouth","England","Англия"),
	Luton(3000, "Лутон;ФК Лутон;Лутон Таун", "Luton;FC Luton","England","Англия"),
	Newport(3000, "Ньюпорт;ФК Ньюпорт;ФК Ньюпорт Каунти;Ньюпорт Каунти;НЬЮПОРТ КАУНТРИ", "Newport;FC Newport;Newport County","England","Англия"),
	Exeter(3000, "Икзетер;ФК Икзетер;ФК Эксетер Сити;Эксетер Сити;ЭКСЕТЕР;Экзетер;Икзитер Сити", "Exeter;FC Exeter;Exeter City;Exeter City FC","England","Англия"),
	Morecambe(3000, "Моркамб;ФК Моркамб;МОРКАМ", "Morecambe;FC Morecambe","England","Англия"),
	Northampton(3000, "Нортхэмптон;ФК Нортхэмптон;Нортгемптон Таун;НОРТГЕМПТОН", "Northampton;FC Northampton;Northampton Town","England","Англия"),
	OxfordUtd(3000, "Оксфорд;ФК Оксфорд;ФК Оксфорд Юнайтед;Оксфорд Юнайтед;Оксфорд Юн", "Oxford Utd;FC Oxford Utd;Oxford;Oxford United","England","Англия"),
	DagRed(3000, "Даг & Ред;ФК Даг & Ред;Дагенхэм и Рэдбридж;Дагенэм энд Редбридж;ДАГ И РЕД", "Dag & Red;FC Dag & Red;Dag and Red","England","Англия"),
	Wimbledon(3000, "Уимблдон;ФК Уимблдон;АФК Уимблдон", "Wimbledon;FC Wimbledon;AFC Wimbledon","England","Англия"),
	Portsmouth(3000, "Портсмут;ФК Портсмут", "Portsmouth;FC Portsmouth","England","Англия"),
	Accrington(3000, "Акрингтон;ФК Акрингтон;ФК Аккрингтон Стэнли;Аккрингтон Стэнли;АККРИНГТОН", "Accrington;FC Accrington;Accrington Stanley","England","Англия"),
	York(3000, "Йорк;ФК Йорк;ФК Йорк Сити;Йорк Сити", "York;FC York;York City","England","Англия"),
	CambridgeUtd(3000, "Кэмбридж;ФК Кэмбридж;КЕМБРИДЖ ЮНАЙТЕД;Кембридж;Кембридж	Юн.;Кембридж Юн", "Cambridge Utd;FC Cambridge Utd;Cambridge U;Cambridge United","England","Англия"),
	Carlisle(3000, "Кэрлайл Юн.;ФК Кэрлайл Юн.;ФК Карлайл;Карлайл Юнайтед;КАРЛАЙЛ ЮН.;Карлайсл", "Carlisle;FC Carlisle;Carlisle United","England","Англия"),
	Mansfield(3000, "Мансфилд;ФК Мансфилд;Мэнсфилд Таун;Мансфилд Таун", "Mansfield;FC Mansfield;Mansfield Town","England","Англия"),
	Hartlepool(3000, "Хартлпул;ФК Хартлпул;ФК Хартлпул;Хартлпул Юнайтед", "Hartlepool;FC Hartlepool","England","Англия"),
	Cheltenham(3000, "Челтенхэм;ФК Челтенхэм;Челтнэм Таун;ЧЕЛТЕНХЕМ", "Cheltenham;FC Cheltenham","England","Англия"),
	Tranmere(3000, "Транмер;ФК Транмер;Транмир Роверс;ТРАНМИР", "Tranmere;FC Tranmere","England","Англия"),
	Weston(3000,"Вестон Супер Маре;Уэстон-сьюпер-Мер;Уэстон;Вестон","Weston-Super-Mare;Weston Super Mare;Weston-super-Mare AFC","England","Англия"),
	Barnet(3000,"Barnet;FC Barnet","Барнет;ФК Барнет;Барнетт","England","Англия"),
	
	//ГерманияЛига3 - GermaniyaLiga3
	
	Bielefeld(3000, "Арминия;ФК Арминия;Арминия Билефельд", "Bielefeld;FC Bielefeld;Arminia Bielefeld","Germany","Германия"),
	Duisburg(3000, "Дуйсбург;ФК Дуйсбург", "Duisburg;FC Duisburg","Germany","Германия"),
	Kiel(3000, "Холштайн Киль;ФК Холштайн Киль;Хольштайн;Хольштайн Киль;Киль", "Kiel;FC Kiel;Holstein Kiel","Germany","Германия"),
	StuttgarterK(3000, "Штуттгартер Кик.;ФК Штуттгартер Кик.;ФК Штутгартер Кикерс;Штутгартер Киккерс;ШТУТТНАРТЕР К.", "Stuttgarter K.;FC Stuttgarter K.;Stuttgarter Kickers","Germany","Германия"),
	Munster(3000, "Мюнстер;ФК Мюнстер;Пройссен;Пройсен Мюнстер", "Munster;FC Munster;Preußen Muenster","Germany","Германия"),
	Cottbus(3000, "Энерги;ФК Энерги;Энерги Котбус;ЭНЕРГИЯ КОТТБУС", "Cottbus;FC Cottbus;Energie Cottbus","Germany","Германия"),
	Halle(3000, "Галле;ФК Галле;Галлешер;ХАЛЛЕ", "Halle;FC Halle;Hallescher FC","Germany","Германия"),
	Chemnitz(3000, "Хемницер;ФК Хемницер;ФК Кемницер;Хемниц;КЕМНИЦЕР", "Chemnitz;FC Chemnitz;Chemnitzer FC","Germany","Германия"),
	Wehen(3000, "Вехен;ФК Вехен;Веен Висбаден", "Wehen;FC Wehen","Germany","Германия"),
	Dresden(3000, "Динамо Дрезден;ФК Динамо Дрезден;ДРЕЗДЕН", "Dresden;FC Dresden;Dynamo Dresden","Germany","Германия"),
	Osnabruck(3000, "Оснабрюк;ФК Оснабрюк;ФК Оснабрюкк", "Osnabruck;FC Osnabruck","Germany","Германия"),
	Erfurt(3000, "Эрфюрт;ФК Эрфюрт;Рот-Вайсс;Рот-Вайсс Эрфурт;РВ ЭРФУРТ", "Erfurt;FC Erfurt;RW Erfurt","Germany","Германия"),
	FCologne(3000, "Фортуна Кёльн;ФК Фортуна Кёльн;Фортуна Кельн;Ф. КЁЛЬН", "F. Cologne;FC F. Cologne;Fortuna Koeln","Germany","Германия"),
	StuttgartII(3000, "Штуттгарт-люб.;ФК Штуттгарт-люб.;ФК Штутгарт II;Штутгарт II;ШТУТТГАРТ II", "Stuttgart II;FC Stuttgart II;VfB Stuttgart II","Germany","Германия"),
	Sonnenhof(3000, "Сонненхоф;ФК Сонненхоф;Зонненхоф Гроссашпах;Зонненхоф Гроссаспах;СОННЕНХОФ ГРОССАСПАХ", "Sonnenhof;FC Sonnenhof;Sonnenhof Großaspach","Germany","Германия"),
	Rostock(3000, "Ганза;ФК Ганза;Ганза Росток;ХАНСА РОСТОК", "Rostock;FC Rostock;Hansa Rostock","Germany","Германия"),
	Mainz05II(3000, "Майнц 05-люб.;ФК Майнц 05-люб.;ФК Майнц 05 II;Майнц 05 II;МАЙНЗ II", "Mainz 05 II;FC Mainz 05 II","Germany","Германия"),
	Unterhaching(3000, "Унтерхахинг;ФК Унтерхахинг", "Unterhaching;FC Unterhaching","Germany","Германия"),
	DortmundII(3000, "Боруссия Д-люб.;ФК Боруссия Д-люб.;Боруссия II;Боруссия Дортмунд II;Б.ДОРТМУНД II", "Dortmund II;FC Dortmund II;Borussia Dortmund II;Dortmund 2","Germany","Германия"),
	JahnRegensburg(3000, "Ян Регенсбург;ФК Ян Регенсбург;ФК Регенсбург;РЕГЕНСБУРГ", "Jahn Regensburg;FC Jahn Regensburg","Germany","Германия"),
	
	//ШотландияЧемпион-Лига - ShotlandiyaChempion-Liga
	
	Hearts(3000, "Хартс;ФК Хартс;Харт оф Мидлотиан", "Hearts;FC Hearts","Scotland","Шотландия"),
	Hibernian(3000, "Хиберниан;ФК Хиберниан", "Hibernian;FC Hibernian","Scotland","Шотландия"),
	Rangers(1000,"Глазго Рейнджерс;ФК Глазго Рейнджерс;Глазго Рэйнджерс;Рейнджерс;ФК Рейнджерс","Glasgow Rangers;FC Glasgow Rangers;Glasgow Rangers FC;Rangers;FC Rangers","Scotland","Шотландия"),
	QueenofTheSouth(3000,"Куин оф Саут;ФК Куин оф Саут;Куин оф зе Саут;КВИН ОФ САУФ","Queen of The South;FC Queen of The South;Queen of South","Scotland","Шотландия"),
	Falkirk(3000, "Фалкирк;ФК Фалкирк;ФК Фалкирк (Фолкерк);Фолкерк", "Falkirk;FC Falkirk","Scotland","Шотландия"),
	RaithRovers(3000, "Рейт Роверс;ФК Рейт Роверс;Райт Роверс;Рэйт Роверс", "Raith Rovers;FC Raith Rovers","Scotland","Шотландия"),
	Dumbarton(3000, "Дамбартон;ФК Дамбартон", "Dumbarton;FC Dumbarton","Scotland","Шотландия"),
	Livingston(3000, "Ливингстон;ФК Ливингстон", "Livingston;FC Livingston","Scotland","Шотландия"),
	Alloa(3000, "Аллоа;ФК Аллоа;Аллоа Атлетик", "Alloa;FC Alloa;Alloa Athletic","Scotland","Шотландия"),
	Cowdenbeath(3000, "Коуденбет;ФК Коуденбет;ФК Кауденбит;Коуденбит;КОУНДЕБИС", "Cowdenbeath;FC Cowdenbeath","Scotland","Шотландия"),
	
	//ШотландияЛига1 - ShotlandiyaLiga1
	
	GreenockMorton(3000, "Мортон;ФК Мортон;ФК Гринок Мортон;Гринок Мортон", "Greenock Morton;FC Greenock Morton","Scotland","Шотландия"),
	Stranraer(3000, "Странрар;ФК Странрар;СТРАНРАЕР", "Stranraer;FC Stranraer","Scotland","Шотландия"),
	Forfar(3000, "Форфар;ФК Форфар;ФК Форфар Атлетик;Форфар Атлетик", "Forfar;FC Forfar","Scotland","Шотландия"),
	Brechin(3000, "Брешин Сити;ФК Брешин Сити;ФК Брихин Сити;Брихин Сити;БРЕЧИН", "Brechin;FC Brechin","Scotland","Шотландия"),
	Airdrieonians(3000, "Эйрдри;ФК Эйрдри;ФК Эйрдрионианс;Эйрдрионианс", "Airdrieonians;FC Airdrieonians","Scotland","Шотландия"),
	Peterhead(3000, "Питерхед;ФК Питерхед", "Peterhead;FC Peterhead","Scotland","Шотландия"),
	Dunfermline(3000, "Данфермлайн;ФК Данфермлайн;ФК Данфермлайн Атлетик;Данфермлин", "Dunfermline;FC Dunfermline","Scotland","Шотландия"),
	Ayr(3000, "Эр Юнайтед;ФК Эр Юнайтед;Эйр;Эйр Юнайтед;Эйр Юн.;Аир Юнайтед", "Ayr;FC Ayr;Ayr Utd.","Scotland","Шотландия"),
	Stenhousemuir(3000, "Стенхаусмуир;ФК Стенхаусмуир;ФК Стенхаусмюр;Стенхаузмур;СТЕНХАУСМИР", "Stenhousemuir;FC Stenhousemuir","Scotland","Шотландия"),
	Stirling(3000, "Стерлинг А;ФК Стерлинг А;ФК Стерлинг Альбион;Стерлинг Альбион;СТИРЛИНГ", "Stirling;FC Stirling;Stirling Albion","Scotland","Шотландия"),

	//БельгияДивизион2 - BelgiyaDivizion2
	
	StTruiden(3000, "Сент Труйден;ФК Сент Труйден;Сент-Трюйден;Сент-Труйден;СТ. ТРУЙДЕН", "St. Truiden;FC St. Truiden;St.Truiden","Belgium","Бельгия"),
	Lommel(3000, "Ломмель;ФК Ломмель;ФК Ломмель Юнайтед;Ломмель Юнайтед", "Lommel;FC Lommel","Belgium","Бельгия"),
	Eupen(3000, "Эупен;ФК Эупен;ФК Эйпен;Эйпен", "Eupen;FC Eupen","Belgium","Бельгия"),
	RoyalBoussuDB(3000, "Боринаж;ФК Боринаж;Серен Юнайтед (Бельгия);Серен", "Royal Boussu DB;FC Royal Boussu DB;Seraing United;SERAING UNITED","Belgium","Бельгия"),
	OHLeuven(3000, "Хеверле;ФК Хеверле;Ауд-Хеверле Лёвен;Ауд-Хеверле Левен;ХЕВЕРЛИ", "OH Leuven;FC OH Leuven;Oud-Heverlee","Belgium","Бельгия"),
	Virton(3000, "Виртон;ФК Виртон;ФК Экселсьор Виртон;Эксельсиор Виртон", "Virton;FC Virton","Belgium","Бельгия"),
	Tubize(3000, "Тубек;ФК Тубек;ФК Тюбиз;Тюбиз", "Tubize;FC Tubize","Belgium","Бельгия"),
	EendrachtAalst(3000, "Алст;ФК Алст;Эндрахт;Эндрахт Алст", "Eendracht Aalst;FC Eendracht Aalst;Aalst","Belgium","Бельгия"),
	Antwerp(3000, "Антверпен;ФК Антверпен;АНТВЕРП", "Antwerp;FC Antwerp","Belgium","Бельгия"),
	Roeselare(3000, "Розелар;ФК Розелар;ФК Руселаре;Руселаре;РОЕСЕЛАР", "Roeselare;FC Roeselare","Belgium","Бельгия"),
	WhiteStar(3000, "Уайт Стар;ФК Уайт Стар;Уайт Стар Волуве;Уайт Стар Брюссель", "White Star;FC White Star;WS Bruxelles","Belgium","Бельгия"),
	Geel(3000, "Жель;ФК Жель;Вербродеринг;Вербрудеринг Гель;ГЕЕЛ", "Geel;FC Geel","Belgium","Бельгия"),
	Heist(3000, "Хейст;ФК Хейст", "Heist;FC Heist;KSK Heist","Belgium","Бельгия"),
	Dessel(3000, "Дессел;ФК Дессел;Дессел Спорт;Дессель Спорт", "Dessel;FC Dessel","Belgium","Бельгия"),
	Maasmechelen(3000, "Хейзден-Зольдер;ФК Хейзден-Зольдер;ФК Патро Эйсден Масмехелен;Патро Эйсден;ПАТРО", "Maasmechelen;FC Maasmechelen;Patro Eisden","Belgium","Бельгия"),
	Mechelen(3000, "Расинг Мехелен;ФК Расинг Мехелен;ФК Мехелен", "Mechelen;FC Mechelen;KRC Mechelen","Belgium","Бельгия"),
	WoluweZaventem(3000, "Волюве;ФК Волюве;Волув-Завентем;Волюве-Завентем", "Woluwe-Zaventem;FC Woluwe-Zaventem;KV.Woluwe-Zaventem","Belgium","Бельгия"),
	Mons(3000,"ФК Монс;Монс","Mons","Belgium","Бельгия"),
	
	//НорвегияДивизион2 - NorvegiyaDivizion2
	
	Aasane(3000, "Осане;ФК Осане;ФК Асане;АСАН", "Aasane;FC Aasane;Asane","Norway","Норвегия"),
	Follo(3000, "Фолло;ФК Фолло;Фоллу", "Follo;FC Follo","Norway","Норвегия"),
	Sogndal(3000, "Сондаль;ФК Сондаль;ФК Согндал;Согндаль", "Sogndal;FC Sogndal;Sogndall","Norway","Норвегия"),
	Hodd(3000, "Хедд;ФК Хедд;Ходд", "Hodd;FC Hodd;Hoedd","Norway","Норвегия"),
	Sandnes(3000, "Санднес Ульф;ФК Санднес Ульф;ФК Санднес;Саннес Ульф;САНДНЕС УЛФ", "Sandnes;FC Sandnes","Norway","Норвегия"),
	BryneFK(3000, "Брюн;ФК Брюн;Брюне;ФК Брине;ФК Брюне", "Bryne;Bryne FK;FC Bryne FK;Bryne","Norway","Норвегия"),
	Baerum(3000, "Бэрум;ФК Бэрум;ФК Берум;БЕРУМ", "Baerum;FC Baerum","Norway","Норвегия"),
	Kristiansund(3000, "Кристиансунд;ФК Кристиансунд;Кристиансунн", "Kristiansund;FC Kristiansund;Kristiansund BK","Norway","Норвегия"),
	Levanger(3000, "Левангер;ФК Левангер", "Levanger;FC Levanger;Levanger FK","Norway","Норвегия"),
	Jerv(3000, "Ерв;ФК Ерв;ЙЕРВ", "Jerv;FC Jerv","Norway","Норвегия"),
	Strommen(3000, "Строммен;ФК Строммен;ФК Стрёммен;Стреммен", "Strommen;FC Strommen;Stroemmen","Norway","Норвегия"),
	Fredrikstad(3000, "Фредрикстад;ФК Фредрикстад", "Fredrikstad;FC Fredrikstad","Norway","Норвегия"),
	NestSotra(3000, "Нест-Сотра;ФК Нест-Сотра;Нест-Сутра;НЕСТ", "Nest-Sotra;FC Nest-Sotra;Nest Sotra","Norway","Норвегия"),
	Ranheim(3000, "Ранхейм;ФК Ранхейм", "Ranheim;FC Ranheim","Norway","Норвегия"),
	Honefoss(3000, "Хонефосс;ФК Хонефосс;Хенефосс", "Honefoss;FC Honefoss;Honefoss BK;Hoenefoss","Norway","Норвегия"),
	
	//БразилияСерияB - BraziliyaSeriyaB
	
	Oeste(3000, "Оесте;ФК Оесте;Оэсте;Уесте-СП", "Oeste;FC Oeste;Oeste FC","Brazil","Бразилия"),
	Paysandu(3000, "Пайсанду;ФК Пайсанду", "Paysandu;FC Paysandu","Brazil","Бразилия"),
	SantaCruz(3000, "Санта-Круз;ФК Санта-Круз;Санта Крус-ПЕ;САНТА КРУЗ", "Santa Cruz;FC Santa Cruz","Brazil","Бразилия"),
	Ceara(3000, "Кеара;ФК Кеара;Сеара", "Ceara;FC Ceara","Brazil","Бразилия"),
	MacaeEsporte(3000, "Макаэ;ФК Макаэ", "Macae Esporte;FC Macae Esporte;Macae","Brazil","Бразилия"),
	Luverdense(3000, "Луверденсе;ФК Луверденсе;ЛУВЕРДЕНС", "Luverdense;FC Luverdense;Luverdense MT","Brazil","Бразилия"),
	MogiMirim(3000, "Моги Мирим;ФК Моги Мирим;ФК Можи-Мирин;Моджи Мирим;МИРИМ", "Mogi Mirim;FC Mogi Mirim","Brazil","Бразилия"),
	SampaioCorrea(3000, "Сампайо Корреа;ФК Сампайо Корреа;Сампайо Корреа-МА;САМПАЙО", "Sampaio Correa;FC Sampaio Correa","Brazil","Бразилия"),
	Parana(3000, "Парана;ФК Парана", "Parana;FC Parana;Parana Clube","Brazil","Бразилия"),
	Cricium(3000, "Крикюма;ФК Крикюма;Крисиума;КРИШИУМА", "Criciuma;FC Criciuma","Brazil","Бразилия"),
	CRBAL(3000, "КРБ;ФК КРБ;ФК Регатас;Клуб Регатас Бразил", "CRB AL;FC CRB AL;CRB","Brazil","Бразилия"),
	Bragantino(3000, "Брагантино;ФК Брагантино", "Bragantino;FC Bragantino","Brazil","Бразилия"),
	NauticoPE(3000, "Наутико;ФК Наутико", "Nautico PE;FC Nautico PE;Nautico","Brazil","Бразилия"),
	BahiaBA(3000, "Багия;ФК Багия;Баия;Витория Баия;Байя", "Bahia BA;FC Bahia BA;Bahia Salvador BA;Bahia","Brazil","Бразилия"),
	AtleticoGoianiense(3000, "Атлетико ГО;ФК Атлетико ГО;Атлетико Гоияниенсе", "Atletico Goianiense;FC Atletico Goianiense;Atletico GO","Brazil","Бразилия"),
	BotafogoRJB(3000, "Ботафого Б;ФК Ботафого Б", "Botafogo RJ B;FC Botafogo RJ B","Brazil","Бразилия"),
	AmericaMG(3000, "Америка МГ;ФК Америка МГ;Америка Минейро", "America MG;FC America MG","Brazil","Бразилия"),
	BOAMG(3000, "Боа Эспорте;ФК Боа Эспорте;Боа;ЭСПОРТЕ", "BOA MG;FC BOA MG;BOA/MG;Boa Esporte Clube","Brazil","Бразилия"),
	ABC(3000, "АВС;ФК АВС;АБС Натал;АБС-РН;АБЦ", "ABC;FC ABC;ABC RN","Brazil","Бразилия"),
	Vitoria(3000,"Витория","Vitoria","Brazil","Бразилия"),

    //сборные
    Belarus(3000, "Беларусь;Белоруссия","Belarus","",""),
    Spain(3000, "Испания","Spain","",""),
    Ukraine(3000, "Украина","Ukraine","",""),
    Moldova(3000, "Молдова;Молдавия","Moldova","",""),
    Sweden(3000, "Швеция","Sweden","",""),
    Montenegro(3000, "Черногория","Montenegro","",""),
    Russia(3000, "Россия;Российская Федерация","Russia","",""),
    Kazakhstan(3000, "Казахстан","Kazakhstan","",""),
    Iceland(3000, "Исландия","Iceland","",""),
    CzechRepublic(3000, "Чехия","Czech Republic","",""),
    Latvia(3000, "Латвия","Latvia","",""),
    Israel(3000, "Израиль","Israel","",""),
    Wales(3000, "Уэльс","Wales","",""),
    Croatia(3000, "Хорватия","Croatia","",""),
    Norway(3000, "Норвегия","Norway","",""),
    Bulgaria(3000, "Болгария","Bulgaria","",""),
    Italy(3000, "Италия","Italy","", ""),
    Netherlands(3000, "Голландия;Нидерланды","Netherlands","", ""),
    Turkey(3000, "Турция","Turkey","",""),
    NorthernIreland(3000, "Северная Ирландия;Сев. Ирландия","Northern Ireland;NorthernIreland;N.Ireland","",""),
    Finland(3000, "Финляндия","Finland","",""),
    Albania(3000, "Албания","Albania","",""),
    Armenia(3000, "Армения","Armenia","",""),
    Hungary(3000, "Венгрия","Hungary","",""),
    Greece(3000, "Греция","Greece","",""),
    Portugal(3000, "Португалия","Portugal","",""),
    RepublicofIreland(3000, "Ирландия","Republic of Ireland;Rep. Ireland;Rep Of Ireland","",""),
    Poland(3000, "Польша","Poland","",""),
    Serbia(3000, "Сербия","Serbia","",""),
    FYRMacedonia(3000, "Македония", "FYR Macedonia;Macedonia","",""),
    Germanija(3000,"Германия","Germany","",""),
    Gibraltar(3000,"Гибралтар","Gibraltar","",""),
    Austria(3000,"Австрия","Austria","",""),
    Luxembourg(3000,"Люксембург","Luxembourg","",""),
    SanMarino(3000,"Сан Марино;Сан-Марино","San Marino;San-Marino","",""),
    Estonia(3000,"Эстония","Estonia","",""),
    Liechtenstein(3000,"Лихтенштейн","Liechtenstein","",""),
    Malta(3000,"Мальта","Malta","",""),
    Cyprus(3000,"Кипр","Cyprus","",""),
    Andorra(3000,"Андорра","Andorra","",""),
    BosniaandHerzegovina(3000,"Босния и Герцеговина;Босния;Босн. и Герц.;Босния и Гер.","Bosnia and Herzegovina;Bosnia & Herzegovina;Bosnia-Herzegovina","",""),
    Uruguay(3000,"Уругвай","Uruguay","",""),
    Jamaica(3000,"Ямаяка;Ямайка","Jamaica","",""),
    Chile(3000,"Чили","Chile","",""),
    Ecuador(3000,"Эквадор","Ecuador","",""),
    Mexico(3000,"Мексика","Mexico","",""),
    Bolivia(3000,"Боливия","Bolivia","",""),
    Argentina(3000,"Аргентина","Argentina","",""),
    Paraguay(3000,"Парагвай","Paraguay","",""),
    Colombia(3000,"Колумбия","Colombia;Columbia","",""),
    Brazil(3000,"Бразилия","Brazil","",""),
    Peru(3000,"Перу","Peru","",""),
    Ireland(3000,"Ирландия","Ireland;RepublicofIreland","",""),
    Scotland(3000,"Шотландия","Scotland","",""),
    Georgia(3000,"Грузия","Georgia","",""),
    Slovakia(3000,"Словакия","Slovakia","",""),
    England(3000,"Англия","England","",""),
    Slovenia(3000,"Словения","Slovenia","",""),
    Denmark(3000,"Дания","Denmark","",""),
    Romania(3000,"Румыния","Romania","",""),
    Lithuania(3000,"Литва","Lithuania","",""),
    Switzerland(3000,"Швейцария","Switzerland","",""),
    Belgium(3000,"Бельгия","Belgium","",""),
    Azerbaijan(3000,"Азербайджан","Azerbaijan","",""),
    Venezuela(3000,"Венесуэла","Venezuela","",""),
    Guatemala(3000,"Гватемала","Guatemala","",""),
    Cuba(3000,"Куба","Cuba","",""),
    Canada(3000,"Канада","Canada","",""),
    TrinidadandTobago(3000,"Тринидад и Тобаго","Trinidad and Tobago;Trinidad/Tobago;Trinidad & Tobago","",""),
    Salvador(3000,"Сальвадор","Salvador;El Salvador","",""),
    FaroeIslands(3000,"Фаррерские острова;Фарреры;Фаррерские о-ва;Фареры;","Faroe Islands","",""),
    Haiti(3000,"Гаити","Haiti","",""),
    NorwayC(3000,"Норвегия","Norway","",""),
    France(3000,"Франция","France","",""),
    VirginIslands(3000,"Виргинские острова;Виргинские о-ва","Virgin Islands","",""),
    Anguilla(3000,"Ангилья","Anguilla","",""),
    AntiguaandBarbuda(3000,"Антигуа и Барбуда","Antigua and Barbuda","",""),
    Aruba(3000,"Аруба","Aruba","",""),
    Bahamas(3000,"Багамы;Багамские острова;Багамские о-ва","Bahamas","",""),
	Barbados(3000,"Барбадос","Barbados","",""),
	Belize(3000,"Белиз","Belize","",""),
	Bermudas(3000,"Бермуды","Bermudas","",""),
	BritishVirginIslands(3000,"Британские Виргинские Острова","British Virgin Islands","",""),
	Guyana(3000,"Гайана","Guyana","",""),
	Honduras(3000,"Гондурас","Honduras","",""),
	Dominica(3000,"Доминика","Dominica","",""),
	DominicanRepublic(3000,"Доминиканская Республика","Dominican Republic","",""),
	CaymanIslands(3000,"Острова Кайман;Кайманские острова","Cayman Islands","",""),
	CostaRica(3000,"Коста Рика;Коста-Рика","Costa Rica","",""),
	Curacao(3000,"Кюрасао","Curacao","",""),
	Montserrat(3000,"Монтсеррат","Montserrat","",""),
	Nicaragua(3000,"Никарагуа","Nicaragua","",""),
	Panama(3000,"Панама","Panama","",""),
	PuertoRico(3000,"Пуэрто-Рико","Puerto-Rico","",""),
	Vc(3000,"Сент-Винсент и Гренадины","Vc;St. Vincent / Grenadines","",""),
	SaintKittsandNevis(3000,"Сент-Китс и Невис","Saint Kitts and Nevis","",""),
	StLucia(3000,"Сент-Люссия","St. Lucia;St Lucia","",""),
	Surinam(3000,"Суринам","Surinam","",""),
	UnitedStates(3000,"США;Соединенные Штаты Америки","United States;USA","",""),
	TurksandCaicosIslands(3000,"Тёркс и Кайкос","Turks and Caicos Islands","",""),
	Guadeloupe(3000,"Гваделупа","Guadeloupe","",""),
	Martinique(3000,"Мартиника","Martinique","",""),
	SaintMartin(3000,"Сен-Мартен","Saint-Martin","",""),
	SintMaarten(3000,"Синт-Маартен","Sint Maarten","",""),
	Guiana(3000,"Гвиана","Guiana","",""),
	Algeria(3000,"Алжир","Algeria","",""),
	Angola(3000,"Ангола","Angola","",""),
	Benin(3000,"Бенин","Benin","",""),
	Botswana(3000,"Ботсвана","Botswana","",""),
	BurkinaFaso(3000,"Буркина-Фасо;Буркина Фасо","Burkina Faso","",""),
	Burundi(3000,"Бурунди","Burundi","",""),
	Gabon(3000,"Габон","Gabon","",""),
	Gambia(3000,"Гамбия","Gambia","",""),
	Ghana(3000,"Гана","Ghana","",""),
	Guinea(3000,"Гвинея","Guinea","",""),
	GuineaBissau(3000,"Гвинея-Бисау","Guinea-Bissau","",""),
	Djibouti(3000,"Джибути","Djibouti","",""),
	CongoDR(3000,"Демократическая Республика Конго","Congo DR","",""),
	Egypt(3000,"Египет","Egypt","",""),
	Zambia(3000,"Замбия","Zambia","",""),
	Zimbabwe(3000,"Зимбабве","Zimbabwe","",""),
	CapeVerde(3000,"Кабо-Верде","Cape Verde","",""),
	Cameroon(3000,"Камерун","Cameroon","",""),
	Kenya(3000,"Кения","Kenya","",""),
	Comoros(3000,"Коморы","Comoros","",""),
	Congo(3000,"Республика Конго","Congo","",""),
	CotedIvoire(3000,"Кот-д’Ивуар","Cote d'Ivoire","",""),
	Lesotho(3000,"Лесото","Lesotho","",""),
	Liberia(3000,"Либерия","Liberia","",""),
	Libya(3000,"Ливия","Libya","",""),
	Mauritius(3000,"Маврикий","Mauritius","",""),
	Mauritania(3000,"Мавритания","Mauritania","",""),
	Madagascar(3000,"Мадагаскар","Madagascar","",""),
	Malawi(3000,"Малави","Malawi","",""),
	Mali(3000,"Мали","Mali","",""),
	Morocco(3000,"Марокко","Morocco","",""),
	Mozambique(3000,"Мозамбик","Mozambique","",""),
	Namibia(3000,"Намибия","Namibia","",""),
	Niger(3000,"Нигер","Niger","",""),
	Nigeria(3000,"Нигерия","Nigeria","",""),
	Rwanda(3000,"Руанда","Rwanda","",""),
	SaoTomeandPrincipe(3000,"Сан-Томе и Принсипи","São Tomé and Príncipe","",""),
	Swaziland(3000,"Свазиленд","Swaziland","",""),
	Seychelles(3000,"Сейшельские Острова","Seychelles","",""),
	Senegal(3000,"Сенегал","Senegal","",""),
	Somalia(3000,"Сомали","Somalia","",""),
	SouthSudan(3000,"Южный Судан;Республика Южный Судан","South Sudan","Republic of South Sudan",""),
	Sudan(3000,"Судан","Sudan","",""),
	SierraLeone(3000,"Сьерра-Леоне","Sierra Leone","",""),
	Tanzania(3000,"Танзания","Tanzania","",""),
	Togo(3000,"Того","Togo","",""),
	Tunisia(3000,"Тунис","Tunisia","",""),
	Uganda(3000,"Уганда","Uganda","",""),
	CentralAfricanRepublic(3000,"ЦАР","Central African Republic","",""),
	Chad(3000,"Чад","Chad","",""),
	EquatorialGuinea(3000,"Экваториальная Гвинея","Equatorial Guinea","",""),
	Eritrea(3000,"Эритрея","Eritrea","",""),
	Ethiopia(3000,"Эфиопия","Ethiopia","",""),
	SouthAfrica(3000,"ЮАР","South Africa","",""),
	Reunion(3000,"Реюньон","Réunion","",""),
	Zanzibar(3000,"Занзибар","Zanzibar","",""),
	Australia(3000,"Австралия","Australia","",""),
	Afghanistan(3000,"Афганистан","Afghanistan","",""),
	Bahrain(3000,"Бахрейн","Bahrain","",""),
	Bangladesh(3000,"Бангладеш","Bangladesh","",""),
	Bhutan(3000,"Бутан","Bhutan","",""),
	BruneiC(3000,"Бруней","Brunei","",""),
	Vietnam(3000,"Вьетнам","Vietnam","",""),
	HongKong(3000,"Гонконг","Hong Kong","",""),
	Guam(3000,"Гуам","Guam","",""),
	India(3000,"Индия","India","",""),
	Indonesia(3000,"Индонезия","Indonesia","",""),
	Jordan(3000,"Иордания","Jordan","",""),
	Yemen(3000,"Йемен","Yemen","",""),
	Iraq(3000,"Ирак","Iraq","",""),
	Iran(3000,"Иран","Iran","",""),
	Cambodia(3000,"Камбоджа","Cambodia","",""),
	Qatar(3000,"Катар","Qatar","",""),
	China(3000,"КНР;Китай","China;China PR","",""),
	ChineseTaipei(3000,"Тайпей;Китайский Тайбэй;Тайбэй","Chinese Taipei","",""),
	Kuwait(3000,"Кувейт","Kuwait","",""),
	NorthKorea(3000,"КНДР","North Korea","",""),
	Kyrgyzstan(3000,"Киргизия;Киргизская Республика;Кыргызстан","Kyrgyzstan","",""),
	SouthKorea(3000,"Республика Корея","South Korea","",""),
	Laos(3000,"Лаос","Laos","",""),
	Lebanon(3000,"Ливан","Lebanon","",""),
	Malaysia(3000,"Малайзия","Malaysia","",""),
	Maldives(3000,"Мальдивы;Мальдивская Республика","Maldives","",""),
	Macau(3000,"Макао","Macau","",""),
	Mongolia(3000,"Монголия","Mongolia","",""),
	Myanmar(3000,"Мьянма","Myanmar","",""),
	Nepal(3000,"Непал","Nepal","",""),
	UnitedArabEmirates(3000,"ОАЭ","United Arab Emirates;UAE","",""),
	Oman(3000,"Оман","Oman","",""),
	Pakistan(3000,"Пакистан","Pakistan","",""),
	Palestine(3000,"Палестина","Palestine","",""),
	SaudiArabia(3000,"Саудовская Аравия","Saudi Arabia","",""),
	Singapore(3000,"Сингапур","Singapore","",""),
	Syria(3000,"Сирия","Syria","",""),
	Tajikistan(3000,"Таджикистан","Tajikistan","",""),
	Thailand(3000,"Таиланд","Thailand","",""),
	TimorLeste(3000,"Тимор-Лесте;Восточный Тимор;Демократическая Республика Восточный Тимор","Timor-Leste","",""),
	Turkmenistan(3000,"Туркмения","Turkmenistan","",""),
	Uzbekistan(3000,"Узбекистан","Uzbekistan","",""),
	SriLanka(3000,"Шри-Ланка","Sri Lanka","",""),
	Japan(3000,"Япония","Japan","",""),
	NorthernMarianaIslands(3000,"Северные Марианские острова","Northern Mariana Islands","",""),
	AmericanSamoa(3000,"Американское Самоа","American Samoa","",""),
	Vanuatu(3000,"Вануату","Vanuatu","",""),
	NewZealand(3000,"Новая Зеландия","New Zealand","",""),
	NewCaledonia(3000,"Новая Каледония","New Caledonia","",""),
	CookIslands(3000,"Острова Кука","Cook Islands","",""),
	PapuaNewGuinea(3000,"Папуа — Новая Гвинея","Papua New Guinea","",""),
	Samoa(3000,"Самоа","Samoa","",""),
	SolomonIslands(3000,"Соломоновы Острова","Solomon Islands","",""),
	Tahiti(3000,"Таити","Tahiti","",""),
	Tonga(3000,"Тонга","Tonga","",""),
	Fiji(3000,"Фиджи","Fiji","",""),
	Philippines(3000,"Филиппины;Республика Филиппины","Philippines","",""),
	Kiribati(3000,"Кирибати","Kiribati","",""),
	Niue(3000,"Ниуэ","Niue","",""),
	Tuvalu(3000,"Тувалу","Tuvalu","",""),
	Greenland(3000,"Гренландия","Greenland","",""),
	Grenada(3000,"Гренада","Grenada","",""),
	
	//Венгрия
	
	Puskas(3000,"Пушкаш;ФК Пушкаш;Пушкаш Академия;Ференц Пушкаш;Пушкаш Академи","Puskas;FC Puskas;Puskas Akademia;Puskás FC;Puskás Akadémia","Hungary","Венгрия"),
	Videoton(3000,"Видеотон;ФК Видеотон","Videoton;FC Videoton;Videoton FC","Hungary","Венгрия"),
	Vasas(3000,"Вашаш;ФК Вашаш;Васас Будапешт;Вашаш Будапешт","Vasas;FC Vasas;Vasas Budapest","Hungary","Венгрия"),
	Ferencvaros(3000,"Фаренцварош;ФК Фаренцварош","Ferencvaros;FC Ferencvaros;Ferencvarosi TC;Ferencváros","Hungary","Венгрия"),
	Bekescsaba(3000,"Бекешчаба;ФК Бекешчаба;Бекешчаба 1912","Bekescsaba;FC Bekescsaba;Elore FC Bekescsaba;Bekescsabai Elore SE","Hungary","Венгрия"),
	Ujpest(3000,"Уйпешт;ФК Уйпешт","Ujpest;FC Ujpest;Ujpest FC;Ujpest Dozsa","Hungary","Венгрия"),
	MTK(3000,"МТК;ФК МТК;Будапешт МТК;МТК Будапешт","MTK;FC MTK;MTK Budapest;MTK Budapest FC","Hungary","Венгрия"),
	Debrecen(3000,"Дебрецен;ФК Дебрецен","Debrecen;FC Debrecen;Debreceni VSC","Hungary","Венгрия"),
	Honved(3000,"Гонвед;ФК Гонвед;Киспешт Будапешт","Honved;FC Honved;Budapest Honved FC;Budapest Honved;Honvéd;Honved Budapest","Hungary","Венгрия"),
	Paksi(3000,"Пакс;ФК Пакс;Пакси;Пакш","Paksi;FC Paksi;Paksi FC;Paksi SE","Hungary","Венгрия"),
	Haladas(3000,"Халадаш;ФК Халадаш;Халадаш Сомбатхей","Haladas;FC Haladas;Haladas FC;Szombathelyi Haladas FC;Szombathelyi Haladas","Hungary","Венгрия"),
	Doshder(3000,"Диошдьер;ФК Диошдьер;Диошдьёр;ФК Диошдьёр","Doshder;FC Doshder;Diosgyor VTK;Diosgyor;Diosgyori VTK","Hungary","Венгрия"),
	
	//Турция1девизион
	
	Adanaspor(1000,"Аданаспор;ФК Аданаспор","Adanaspor;FC Adanaspor;Adanaspor AS","Turkey","Турция"),
	AdanaDemirspor(1000,"Адана;ФК Адана;Адана Демирспор","Adana Demirspor;FC Adana Demirspor;Adana Demir;Adanademirspor","Turkey","Турция"),
	Alanyaspor(1000,"Аланьяспор;Аланияспор;ФК Аланьяспор;ФК Аланияспор","Alanyaspor;FC Alanyaspor","Turkey","Турция"),
	Boluspor(1000,"Болуспор;ФК Болуспор","Boluspor;FC Boluspor","Turkey","Турция"),
	Elazigspor(1000,"Элазигспор;ФК Элазигспор;Элязыгспор;Елазигспор","Elazigspor;FC Elazigspor","Turkey","Турция"),
	Karsiyaka(1000,"Карсияка;ФК Карсияка;Каршияка;Керсияка","Karsiyaka;FC Karsiyaka;Karsiyaka BEVSEM S.","Turkey","Турция"),

	Giresunspor(1000,"Гиресунспор;ФК Гиресунспор","Giresunspor;FC Giresunspor","Turkey","Турция"),
	Goztepe(1000,"Гезтепе;ФК Гезтепе;Гёзтепеспор","Goztepe;FC Goztepe;Goztepe Izmir;Göztepe AS","Turkey","Турция"),
	Altinordu(1000,"Алтынорду;ФК Алтынорду;Алтинорду","Altinordu;FC Altinordu;Altinordu Spor;Altınordu","Turkey","Турция"),
	Malatyaspor(1000,"Малатьяспор;ФК Малатьяспор;Йени Малатьяспор;Ени Малатьяспор;Малатия Спор","Malatyaspor;FC Malatyaspor;Yeni Malatyaspor","Turkey","Турция"),
	Denizlispor(1000,"Денизлиспор;ФК Денизлиспор","Denizlispor;FC Denizlispor","Turkey","Турция");

    private String russian;

    private String eng;

    private int id;

    private String countryEng;

    private String countryRus;

    private Teams(int id, String rus, String eng, String countryEng, String countryRus) {
        this.eng = eng;
        this.russian = rus;
        this.id = id;
        this.countryEng = countryEng;
        this.countryRus = countryRus;

    }

    //TODO
    public static Teams getTeam(String teamString) {
        Teams team = getByEng(teamString);
        if (team != TEAM_UNKNOWN) {
            return team;
        } else {
            team = getByRussian(teamString);
            return team;
        }
    }

    public static Teams getByRussian(String rus) {
        //check
        if (unknownTeamsRus.get(rus) != null) {
            return TEAM_UNKNOWN;
        }

        final Teams ret = matchedTeams.get(rus);
        if (ret != null) {
            return ret;
        }

        for (Teams t: Teams.values()) {
            String[] names = t.russian.split(";");
            for (String name: names) {
                if (name.equalsIgnoreCase(rus)) {
                    matchedTeams.put(rus, t);
                    return t;
                }
            }
        }
        double k = 0.0;
        for (Teams t: Teams.values()) {
            String[] names = t.russian.split(";");
            for (String name: names) {
                k = StringUtils.getJaroWinklerDistance(rus, name);
                if (k > 0.96) {
                    logger.info("teams matches - " + name + ";" + rus + ";" + k);
                    matchedTeams.put(rus, t);
                    return t;
                }
            }
        }
        unknownTeamsRus.put(rus, TEAM_UNKNOWN);
        return TEAM_UNKNOWN;
    }

    static Map<String, Teams> matchedTeams = new HashMap<String, Teams>();
    static Map<String, Teams> unknownTeamsRus = new HashMap<String, Teams>();
    static Map<String, Teams> unknownTeamsEng = new HashMap<String, Teams>();

    private static final Logger logger = LogManager.getLogger("parser");

    public static Teams getByEng(String engFinal) {
        if (unknownTeamsEng.get(engFinal) != null) {
            return TEAM_UNKNOWN;
        }

        final Teams ret = matchedTeams.get(engFinal);
        if (ret != null) {
            return ret;
        }

        String eng = engFinal.replace(" FC", "").replace("FC ", "").trim();

        for (Teams t: Teams.values()) {
            String[] names = t.eng.split(";");
            for (String name: names) {
                if (name.equalsIgnoreCase(eng)) {
                    matchedTeams.put(engFinal, t);
                    return t;
                }
            }
        }
        double k = 0.0;
        for (Teams t: Teams.values()) {
            String[] names = t.eng.split(";");
            for (String name: names) {
                k = StringUtils.getJaroWinklerDistance(eng, name);
                if (k > 0.96) {
                    logger.info("teams matches - " + name + ";" + eng + ";" + k);
                    matchedTeams.put(engFinal, t);
                    return t;
                }
            }
        }
        unknownTeamsEng.put(engFinal, TEAM_UNKNOWN);
        return TEAM_UNKNOWN;
    }

    public String getRussian() {
        int pos = russian.indexOf(";");
        String finalName = "";
        if (pos == -1) {
            finalName = russian;
        } else {
            finalName = russian.substring(0, pos);
        }
        if (countryRus != null && !countryRus.isEmpty()) {
            finalName = finalName + " (" + countryRus + ")";
        }
        return finalName;
    }

    public String getEnglish() {
        int pos = eng.indexOf(";");
        String finalName = "";
        if (pos == -1) {
            finalName = eng;
        } else {
            finalName = eng.substring(0, pos);
        }
        if (countryEng != null && !countryEng.isEmpty()) {
            finalName = finalName + " (" + countryEng + ")";
        }
        return finalName;
    }

    public String getByLang(String lang) {
        if (lang == null || lang.trim().equalsIgnoreCase("rus")) {
            return getRussian();
        } else {
            return getEnglish();
        }
    }

    public int getId() {
        return id;
    }

    public String getEng() {
        return eng;
    }

    public String getCountryEng() {
        return countryEng;
    }

    public String getCountryRus() {
        return countryRus;
    }
}
