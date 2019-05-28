package com.isfootball.parser.direct;

import com.google.gson.Gson;
import com.isfootball.model.BasicEvent;
import com.isfootball.parser.*;
import com.isfootball.pool.BasePoolInterface;
import com.isfootball.processing.EventSender;
import com.isfootball.utils.HttpClientInitializer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * Created by Evgeniy Pshenitsin on 28.03.2015.
 */
public class SMarketsXmlParser extends BaseParser {

    //http://smarkets.s3.amazonaws.com/oddsfeed.xml

    private static final Logger logger = LogManager.getLogger("parser");

    private static Gson gson = new Gson();

    //static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.GERMAN);
    //26 Apr 11:00
    static DateFormat format = new SimpleDateFormat("dd MMMyyyy", Locale.ENGLISH);
    //yyyy-MM-dd

    public SMarketsXmlParser(BasePoolInterface<WebDriver> pool) {
        super(pool);
        this.betSite = BetSite.SMARKETS;
    }

    @Override
    public List<BasicEvent> getEvents() {

        List<BasicEvent> events = new ArrayList<BasicEvent>();
        try {

            GetMethod get = new GetMethod("http://odds.smarkets.com/legacy_oddsfeed.xml");
            //http://odds.smarkets.com/legacy_oddsfeed.xml
            HttpClient httpClient = new HttpClient(HttpClientInitializer.getConnectionManager());
            int returnCode = httpClient.executeMethod(get);
            String ret = null;

            GZIPInputStream gzip = new GZIPInputStream(get.getResponseBodyAsStream(), 1024 * 1024 * 16); //6 MB
            if (returnCode == HttpStatus.SC_OK) {
                ret = IOUtils.toString(new InputStreamReader(gzip));
            }




            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(ret)));

            //doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("event");
            List<Node> nodes = new ArrayList<>();

            for (int temp = 0; temp < nList.getLength(); temp++) {
                nodes.add(nList.item(temp));
            }

            final Date now = new Date();
            events = nodes.parallelStream().map(node -> {
                try {
                    BasicEvent event = new BasicEvent();
                    event.site = BetSite.SMARKETS;

                    //time="12:45:00"
                    event.date = node.getAttributes().getNamedItem("date").getTextContent();
                    event.day = DateFormatter.format(event.date, betSite);

                    if (DateUtils.isSameDay(event.day, now)) {
                        return null;
                    }

                    String[] teams = node.getAttributes().getNamedItem("name").getTextContent().split("vs.");
                    if (teams.length != 2) {
                        return null;
                    }
                    event.team1 = Teams.getTeam(teams[0].trim());
                    event.team2 = Teams.getTeam(teams[1].trim());

                    if (event.team1 == Teams.TEAM_UNKNOWN || event.team2 == Teams.TEAM_UNKNOWN) {
                        logger.warn("SMarketsXmlParser - unknown teams: " + teams[0] + ":" + event.team1 + ";" + teams[1] + ":" + event.team2);
                        return null;
                    }

                    Element element = (Element) node;

                    NodeList marketNodes = element.getElementsByTagName("market");
                    for (int temp = 0; temp < marketNodes.getLength(); temp++) {
                        Node marketNode = marketNodes.item(temp);
                        final String slug = marketNode.getAttributes().getNamedItem("slug").getTextContent();
                        if (slug.equalsIgnoreCase("winner")) {
                            Element contractElement = (Element) marketNode;
                            NodeList contracts = contractElement.getElementsByTagName("contract");
                            for (int c = 0; c < contracts.getLength(); c++) {
                                Node contract = contracts.item(c);
                                final String contractSlug = contract.getAttributes().getNamedItem("slug").getTextContent();

                                Element offersElement = (Element) contract;
                                NodeList listOffers = offersElement.getElementsByTagName("offers");

                                Element oPrice = (Element)listOffers.item(0);

                                NodeList prices = oPrice.getElementsByTagName("price");
                                if (prices.getLength() == 0) {
                                    continue;
                                }

                                List<Offer> offers = new ArrayList<Offer>();
                                for (int o = 0; o < prices.getLength(); o++) {
                                    Node price = prices.item(o);
                                    offers.add(new Offer(price.getAttributes().getNamedItem("decimal").getTextContent()));
                                }
                                if (listOffers.getLength() == 0) {continue;}

                                if (contractSlug.equalsIgnoreCase("home")) {
                                    event.bets.put(BetType.P1,
                                            Double.toString(offers.stream()
                                                    .mapToDouble(offer -> new Double(offer.decimal))
                                                    .max().getAsDouble()));
                                } else if (contractSlug.equalsIgnoreCase("away")) {
                                    event.bets.put(BetType.P2,
                                            Double.toString(offers.stream()
                                                    .mapToDouble(offer -> new Double(offer.decimal))
                                                    .max().getAsDouble()));
                                } else if (contractSlug.equalsIgnoreCase("draw")) {
                                    event.bets.put(BetType.X,
                                            Double.toString(offers.stream()
                                                    .mapToDouble(offer -> new Double(offer.decimal))
                                                    .max().getAsDouble()));
                                }
                            }

                        } else if (slug.contains("over-under")) {
                            Element contractElement = (Element) marketNode;
                            NodeList contracts = contractElement.getElementsByTagName("contract");
                            for (int c = 0; c < contracts.getLength(); c++) {
                                Node contract = contracts.item(c);
                                final String contractSlug = contract.getAttributes().getNamedItem("slug").getTextContent();
                                final String ovUnK = contract.getAttributes().getNamedItem("name").getTextContent().split(" ")[1];

                                Element offersElement = (Element) contract;
                                NodeList listOffers = offersElement.getElementsByTagName("offers");

                                Element oPrice = (Element)listOffers.item(0);

                                NodeList prices = oPrice.getElementsByTagName("price");
                                if (prices.getLength() == 0) {
                                    continue;
                                }

                                List<Offer> offers = new ArrayList<Offer>();
                                for (int o = 0; o < prices.getLength(); o++) {
                                    Node price = prices.item(o);
                                    offers.add(new Offer(price.getAttributes().getNamedItem("decimal").getTextContent()));
                                }
                                if (listOffers.getLength() == 0) {continue;}

                                if (contractSlug.equalsIgnoreCase("over")) {
                                    BetType bet = BetDataMapping.betsTotal.get(ovUnK + "M");
                                    event.bets.put(bet,
                                            Double.toString(offers.stream()
                                                    .mapToDouble(offer -> new Double(offer.decimal))
                                                    .max().getAsDouble()));
                                } else if (contractSlug.equalsIgnoreCase("under")) {
                                    BetType bet = BetDataMapping.betsTotal.get(ovUnK + "L");
                                    event.bets.put(bet,
                                            Double.toString(offers.stream()
                                                    .mapToDouble(offer -> new Double(offer.decimal))
                                                    .max().getAsDouble()));
                                }
                            }
                        }
                    }

                    if (!event.bets.isEmpty()) {
                        EventSender.sendEvent(event);
                   }
                    return event;
                } catch (Exception e) {
                    logger.error("SMarketsParser error", e);
                }
                return null;
            }).filter(event -> event != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("SMarketsParser error", e);
        }
        return events.stream()
                .filter(e -> e != null)
                .filter(e -> e.bets.size() > 0)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static class Offer {

        public String decimal;

        public Offer(String decimal) {
            this.decimal = decimal;
        }

    }

}
