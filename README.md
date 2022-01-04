# Dé secret santa app bij uitstek!

## Inleiding

Aaah, Kerstmis.. Iedereen kijkt toch ook uit naar dit fantastische gegeven dat ieder jaar weer ligt uit te lonken de moment de zomervakantie volledig teneinde lijkt te lopen..
Familiefeestjes, dineren met vrienden, gezellig rondkuieren op kerstmarkten in alle uithoeken van West-Europa.. Fantastisch toch? En laten we het belangerijkste vooral niet vergeten, n.l.
cadeautjes tijd!
Cadeautjes kosten echter geld, soms wel véél geld. Dit wil zeggen dat de cadeaus liefst van een bruikbaar 'kaliber' zijn om ontgoochelingen alom te voorkomen.
Hierdoor is het concept <b>SECRET SANTA</b> in het leven geroepen.

Het concept is eenvoudig, het spel wordt vaak gespeeld met een grote groep.
Men steekt de namen van de deelnemers in een grote pot en beurtelings mag men namen beginnen trekken. Je koopt een cadeau voor de persoon die je trekt.
Er wordt een maximum bedrag dat ieder cadeau mag kosten afgesproken. Zo blijft het betaalbaar voor iedereen én als kers op de taart heb je er dan de bijkomende verassingsfactor!

Deze app tracht dit geweldig spel volledig te digitaliseren, hope you'll like it!

#### NOTE: De app bevindt zich nog in een zéér early alpha fase en is nog niet compleet, stay tuned for more updates!

## Functionaliteiten
- Minimum Android versie 4.1 (API 16)
- Er wordt doorheen heel de applicatie gebruik gemaakt van FireBase, dit omwille van hun eenvoudige no-sql DB.
- Er is wel degelijk zoals gevraagd een gegevens persistentie voorzien, n.l de android SQLdatabase, zoals hierboven genoemd maakt mijn app niet écht gebruik van deze functionaliteit,
maar voor showcasing purposes is deze geimplementeerd. Er is een DbAdapter voorzien die gebruikers tijdens het registreren momenteel ook opslaagt in deze database.
- Gebruikers kunnen een favorieten lijst opstellen, bewerken en/of verwijderen. Dit maakt het eenvoudiger om in de toekomst gebruikers zonder inspiratie een lijdraad te geven
- Er is gebruik gemaakt van Fragments om de oplijsting te voorzien bij de favorieten items (Er is dus een fragment voorzien dat wordt opgeroepen pér oplijsting van dit type)
- Er is een menu navigation voorzien, dit verschillend voor wanneer je bent ingelogd en wanneer de gebruiker zich op de registratie/inlog pagina bevindt. 
In de menu navigatie tijdens het inloggen hebben gebruikers de mogelijkheid de afgespeelde kerstmuziek stop te zetten/af te spelen.
- De afgespeelde kerstmuziek is een 'service' dat gebruik maakt van de MediaPlayer met een lokaal .mp3 bestand
- Er zijn verschillende layouts voorzien die ondersteuning bieden voor zowel landscape als portret modus. De inlogpagina is hier één van.
- De afbeeldingen in deze applicatie zijn voorzien in de verschillende grotes om bij de verschillende resoluties de correcte afbeelding te 'serven'
- Gebruikers kunnen zich registreren, mét de optie om een avatar image in te stellen.
- Gebruikers kunnen aanmelden wanneer gekend in ons systeem. Authenticatie gebeurd wederom door Google FireBase
- Na het succesvol inloggen/registreren komt men op de main pagina, hier wordt adhv Threading de ingestelde afbeelding (of fallback indien niks gekozen) gedownload en weergegeven.
- Gebruikers kunnen groepen aanmaken
- Gebruikers kunnen mensen uitnodigen/groepen delen met een unieke code per groep
- Gebruikers hebben de mogelijkheid om een ANONIEME chat per groep te gebruiken, dit om bv. mensen er anoniem op attent te maken dat hun favorieten lijst nog niet is aangevuld
Deze chat wordt automatisch via callbacks op de hoogte gebracht van eventuele wijzigingen
- De applicatie heeft de ondersteuning van 2 talen, de default lang is Engels, de andere locale is NL (zowel BE als NL), 80% v/d string values zijn vertaald. 
Dit lijkt mij echter momenteel niet prioritair gezien het vroege stadium v/d applicatie
- Junit testen zijn aanwezig, dient in een later stadium nog verder aangevuld te worden. 
( Graag had ik hier FireBaseUnitTesting voorzien, dit is echter momenteel nog een iets te ver uit mijn bed show geziend de scope.)

## De planning

- Groepleiders voorzien, deze heeft de optie om na dat iedereen succesvol in de groep zit de secret santa te starten!
- Groepen verdelen onder 2 statussen en hun respectievelijke views te renderen op de groeppagina (voor de shuffle en na de shuffle)
- Het effectieve 'shuffelen' van de namen en deze aparte geshuffelde namen weer te geven bij iedere individuele gebruiker in de groep
- Iedere gebruiker de mogelijhheid geven om de favorietenlijst op te halen van de getrokken persoon in de groep
- ...
