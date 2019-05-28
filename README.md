# betting-arbitrage
Service for parsing betting sites (football) &amp;  betting arbitrage search 

It contains two main parts:
 - football bets parsers for more than 20 betting sites. Parser based on headless Chrome browser/PhantomJs & selenium library
 - server, that receives all bet values from parcer instances and analyze it for betting arbitrage
 - rest-api for providing betting arbitrage
 
 Requires Debian 7.0, Java 8 SE, RabbitMQ, PostgreSQL, PantomJS, chromium headless browser with xvfb.
 
 Created in 2015.

