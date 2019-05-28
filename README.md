# betting-arbitrage

Service for parsing betting sites (football) &amp;  betting arbitrage search 

It contains:
 - football bets parsers for more than 20 betting sites
 - server, that receives all bet values from parcer instances and analyze it for betting arbitrage
 - rest-api for providing betting arbitrage

Parser based on headless Chrome browser/PhantomJs & selenium library. Sometimes uses direct xml or json protocols of betting sites backend.

Requires Debian 7.0, Java 8 SE, RabbitMQ, PostgreSQL, PantomJS, chromium headless browser with xvfb.
 
Allmost all of this (crazy) source code were created in 2015 in four months by spending ~4 hours per day after working day finish at night time.
It was awesome challenge for me. )

Evgeniy Pshenitsin (GeneOpenminder). May 2019.

