package com.isfootball.server;

import com.google.gson.Gson;
import com.isfootball.jdbc.Bet;
import com.isfootball.jdbc.BetDao;
import com.isfootball.jdbc.Match;
import com.isfootball.model.BetKey;
import com.isfootball.parser.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.RequestExecutorProvider;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.ContentType;
import org.glassfish.grizzly.http.util.Header;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by e.pshenitsin on 27.02.2015.
 */
public class GetBranchesHandler extends HttpHandler {

    private static final Logger logger = LogManager.getLogger("Handler");

    private static final ContentType CONTENT_TYPE_JSON =
            ContentType.newContentType("application/json", "utf-8").prepare();

    private static final ContentType CONTENT_TYPE_HTML =
            ContentType.newContentType("text/html", "utf-8").prepare();

    public static Gson gson = new Gson();

    @Override
    public void service(final Request request, final Response response)
            throws Exception {
        ThreadContext.put("request", UUID.randomUUID().toString());
        response.setHeader(Header.Server, Server.SERVER_VERSION);

        logger.info("remote addr - " + request.getRemoteAddr());
        if (Method.GET != request.getMethod() && Method.POST != request.getMethod()) {
            logger.warn("Incorrect request - not GET or POST method!");
            response.getOutputStream().write("Use GET&POST methods!".getBytes());
        } else {
            processRequest(request, response);
        }
        ThreadContext.clearAll();
    }

    public final static String COMMAND_START = "start";
    public final static String COMMAND_STATUS = "status";
    public final static String COMMAND_RESULT = "result";



    private void processRequest(final Request request, final Response response) throws Exception {
        String lang = request.getParameter("lang"); //eng/rus 1/2
        logger.info("lang - " + lang);

        String secretCode = request.getParameter("secret_code");
        int userStatus = ParserCore.USER_STATUS_UNKNOWN;
        String userStatusStr = request.getParameter("user_status");
        if (userStatusStr != null && !userStatusStr.isEmpty()) {
            try {
                userStatus = Integer.parseInt(request.getParameter("user_status"));
            } catch (Exception e) {
                logger.warn("Incorrect userStatus: " + userStatusStr);
            }
        }
        logger.info("userStatus - " + userStatus);

        if (secretCode == null || secretCode.isEmpty()) {
            logger.warn("Incorrect request!");
            response.getOutputStream().write("INCORRECT REQUEST!".getBytes());
        } else {
            //final String query = request.getQueryString();
            //logger.info(query);
            final String uri = request.getDecodedRequestURI();
            //logger.info(uri);
            if (uri.contains("getbranches")) {
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_JSON);
                if (Method.GET == request.getMethod()) {
                    logger.info("getbranches GET");

                    String sites = request.getParameter("sites");
                    if (sites == null || sites.isEmpty()) {
                        response.getOutputStream().write(ParserCore.processGetBetsCustom(null).getBytes());
                    } else {
                        String[] sitesList = sites.split(",");
                        List<BetSite> sList = new ArrayList<BetSite>();
                        if (sitesList.length > 0) {
                            for (String siteName : sitesList) {
                                BetSite site = BetSite.valueOf(siteName);
                                if (site != null) {
                                    sList.add(site);
                                }
                            }
                        }
                        response.getOutputStream().write(ParserCore.processGetBetsCustom(sList).getBytes());
                    }
                } else {
                    logger.info("getbranches POST");
                    Buffer body = request.getPostBody(3000);
                    String json = body.toStringContent();
                    try {
                        GetBranchesRequest req = gson.fromJson(json, GetBranchesRequest.class);
                        if (req != null) {
                            Map<BetSite, Double> cms = new HashMap<>();
                            List<BetSite> sList = new ArrayList<BetSite>();
                            if (req.sites != null && !req.sites.isEmpty()) {
                                for (GetBranchesRequest.SiteConfig config: req.sites) {
                                    if (config.cms != null) {
                                        cms.put(config.site, config.cms);
                                    }
                                    sList.add(config.site);
                                }
                            } else {
                                cms = null;
                            }
                            if (cms == null) {
                                if (req.useAllSites) {
                                    response.getOutputStream().write(ParserCore.processGetBetsAllNew(true, userStatus, lang).getBytes());
                                    logger.info("getbranches - without cms; all sites");
                                } else {
                                    logger.info("getbranches - without cms; not all sites");
                                    if (sList != null) {
                                        for (BetSite site : sList) {
                                            logger.info("getbranches - without cms; not all sites:" + site);
                                        }
                                    }
                                    response.getOutputStream().write(ParserCore.processGetBetsCustomNew2(sList, true, userStatus, lang).getBytes());
                                }
                            } else {
                                logger.info("getbranches - with cms; " + (req.useAllSites ? "all sites" : "not all sites"));
                                if (sList != null && !req.useAllSites) {
                                    for (BetSite site : sList) {
                                        logger.info("getbranches - without cms; not all sites:" + site);
                                    }
                                }
                                response.getOutputStream().write(ParserCore.processGetBetsCustomNew2(req.useAllSites ? Arrays.asList(BetSite.values())
                                        : sList, cms, true, userStatus, lang).getBytes());
                            }
                        } else {
                            response.getOutputStream().write("INCORRECT REQUEST - server error!".getBytes());
                        }
                    } catch (Exception e) {
                        logger.info("getbranches - INCORRECT REQUEST", e);
                        response.getOutputStream().write("INCORRECT REQUEST - exception!".getBytes());
                    }

                }
            } else if (uri.contains("getjsonbranches")) {
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_JSON);
                if (Method.GET == request.getMethod()) {
                    logger.info("getjsonbranches GET");
                    response.getOutputStream().write("INCORRECT REQUEST!".getBytes());
                } else {
                    logger.info("getjsonbranches POST");
                    Buffer body = request.getPostBody(30000);
                    String json = body.toStringContent();
                    try {
                        GetBranchesRequest req = gson.fromJson(json, GetBranchesRequest.class);
                        if (req != null) {
                            Map<BetSite, Double> cms = new HashMap<>();
                            List<BetSite> sList = new ArrayList<BetSite>();
                            if (req.sites != null && !req.sites.isEmpty()) {
                                for (GetBranchesRequest.SiteConfig config: req.sites) {
                                    if (config.cms != null) {
                                        cms.put(config.site, config.cms);
                                    }
                                    sList.add(config.site);
                                    /*if (config.site.equals(BetSite.ONEXBET)) {
                                        sList.add(BetSite.WILLIAM_HILL); //TODO remove
                                    }*/
                                }
                            } else {
                                cms = null;
                            }
                            if (cms == null || cms.isEmpty()) {
                                if (req.useAllSites) {
                                    response.getOutputStream().write(ParserCore.processGetBetsAllNew(false, userStatus, lang).getBytes());
                                    logger.info("getbranches - without cms; all sites");
                                } else {
                                    logger.info("getbranches - without cms; not all sites");
                                    if (sList != null) {
                                        for (BetSite site : sList) {
                                            logger.info("getbranches - without cms; not all sites:" + site);
                                        }
                                    }
                                    response.getOutputStream().write(ParserCore.processGetBetsCustomNew2(sList, false, userStatus, lang).getBytes());
                                }
                            } else {
                                logger.info("getbranches - with cms; " + (req.useAllSites ? "all sites" : "not all sites"));
                                if (sList != null && !req.useAllSites) {
                                    for (BetSite site : sList) {
                                        logger.info("getbranches - without cms; not all sites:" + site);
                                    }
                                }
                                response.getOutputStream().write(
                                        ParserCore.processGetBetsCustomNew2(req.useAllSites ? Arrays.asList(BetSite.values())
                                                : sList, cms, false, userStatus, lang).getBytes());
                            }
                        } else {
                            response.getOutputStream().write("INCORRECT REQUEST - server error!".getBytes());
                        }
                    } catch (Exception e) {
                        logger.info("getbranches - INCORRECT REQUEST", e);
                        response.getOutputStream().write("INCORRECT REQUEST - exception!".getBytes());
                    }
                }
            } else if (uri.contains("getjsontunnels")) {
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_JSON);
                if (Method.GET == request.getMethod()) {
                    logger.info("gettunnels GET");
                    response.getOutputStream().write("INCORRECT REQUEST!".getBytes());
                } else {
                    logger.info("gettunnels POST");
                    Buffer body = request.getPostBody(3000);
                    String json = body.toStringContent();
                    try {
                        GetBranchesRequest req = gson.fromJson(json, GetBranchesRequest.class);
                        if (req != null) {
                            Map<BetSite, Double> cms = new HashMap<>();
                            List<BetSite> sList = new ArrayList<BetSite>();
                            if (req.sites != null && !req.sites.isEmpty()) {
                                for (GetBranchesRequest.SiteConfig config: req.sites) {
                                    if (config.cms != null) {
                                        cms.put(config.site, config.cms);
                                    }
                                    sList.add(config.site);
                                }
                            } else {
                                cms = null;
                            }
                            if (cms == null || cms.isEmpty()) {
                                if (req.useAllSites) {
                                    response.getOutputStream().write(ParserCore.processGetTunnelBetsCustomNew(userStatus, lang).getBytes());
                                } else {
                                    response.getOutputStream().write(ParserCore.processGetTunnelBetsCustomNew(sList, userStatus, lang).getBytes());
                                }
                            } else {
                                response.getOutputStream().write(
                                        ParserCore.processGetTunnelBetsCustomNew(req.useAllSites ? Arrays.asList(BetSite.values())
                                                : sList, cms, false, userStatus, lang).getBytes());
                            }
                        } else {
                            response.getOutputStream().write("INCORRECT REQUEST - server error!".getBytes());
                        }
                    } catch (Exception e) {
                        logger.info("gettunnels - INCORRECT REQUEST", e);
                        response.getOutputStream().write("INCORRECT REQUEST - exception!".getBytes());
                    }

                }
            } else if (uri.contains("getrams")) {
                response.setContentType(CONTENT_TYPE_JSON);
                logger.info("getrams");
                response.getOutputStream().write("NO DATA!".getBytes());
            } else if (uri.contains("gethtmlbranches")) {
                response.setContentType(CONTENT_TYPE_HTML);
                response.setStatus(200, "OK");
                logger.info("gethtmlbranches");
                ParserCore.calculateBets();
                response.getOutputStream().write(StaticCache.htmlBranches.getBytes());
            } else if (uri.contains("getupdatetime")) {
                logger.info("getupdatetime");
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_HTML);
                StringBuilder sb = new StringBuilder();
                sb.append("<!DOCTYPE html><html><body>");
                sb.append("*******************************************************").append("<br>");
                for (BetSite b: StaticCache.currentEventsUpdate.keySet()) {
                    sb.append(b).append(": lastUpdateTime - ").append(StaticCache.currentEventsUpdate.get(b).lastUpdate).append("<br>");
                }
                sb.append("*******************************************************");
                sb.append("</body></html>");
                response.getOutputStream().write(sb.toString().getBytes());
            } else if (uri.contains("gethtmlevents")) {
                logger.info("gethtmlevents");
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_HTML);
                ParserCore.calculateBets();
                response.getOutputStream().write(StaticCache.htmlEvents.getBytes());
            } else if (uri.contains("getallline")) {
                logger.info("getallline");
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_HTML);
                response.getOutputStream().write(StaticCache.allLine.getBytes());
            } else if (uri.contains("gethtmltable")) {
                logger.info("gethtmltable");
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_HTML);
                response.getOutputStream().write(ParserCore.calculateBetsTable().getBytes("UTF-8"));
            } else if (uri.contains("getremoteevents")) {
                logger.info("getremoteevents");
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_JSON);
                String command = request.getParameter("command");
                RemoteParser.RemoteEvents result = null;
                if (RemoteParser.getInstance() != null) {
                    if (command.equalsIgnoreCase(COMMAND_START)) {
                        result = RemoteParser.getInstance().startDownload();
                    } else if (command.equalsIgnoreCase(COMMAND_STATUS)) {
                        result = RemoteParser.getInstance().getStatus();
                    } else if (command.equalsIgnoreCase(COMMAND_RESULT)) {
                        result = RemoteParser.getInstance().getResult();
                    } else {
                        result = new RemoteParser.RemoteEvents();
                        result.status = RemoteParser.STATUS_INCORRECT_REQ;
                    }
                }
                response.getOutputStream().write(gson.toJson(result).getBytes());
            } else if (uri.contains("getremoteupdate")) {
                logger.info("getremoteupdate");
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_JSON);
                RemoteParser2.RemoteEvents2 ret = new RemoteParser2.RemoteEvents2();
                Map<BetSite, EventsUpdate> currentEventsUpdate = new HashMap<BetSite, EventsUpdate>();

                StaticCache.currentEventsUpdate.keySet().parallelStream().forEach((BetSite s) ->
                                currentEventsUpdate.put(s, StaticCache.currentEventsUpdate.remove(s))
                );
                ret.currentEvents = currentEventsUpdate;
                response.getOutputStream().write(gson.toJson(ret).getBytes());
            } else if (uri.contains("getmatchesforlines")) {
                logger.info("getmatchesforlines");
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_HTML);
                response.getOutputStream().write(ParserCore.getMatchesForLines().getBytes());
            } else if (uri.contains("getmatchbetlines")) {
                logger.info("getmatchbetlines");
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_HTML);
                final long matchId = Long.parseLong(request.getParameter("match"));
                response.getOutputStream().write(ParserCore.getBetLines(matchId).getBytes());
            } else if (uri.contains("getbettypes")) {
                logger.info("getbettypes");
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_JSON);
                List<BetTypeJson> betTypes = Arrays.asList(BetType.values()).stream().map( bet -> {
                    return new BetTypeJson(bet.toString(), bet.getRusName());
                }).collect(Collectors.toList());
                response.getOutputStream().write(gson.toJson(betTypes).getBytes());
            } else if (uri.contains("getbetsites")) {
                logger.info("getbetsites");
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_JSON);
                response.getOutputStream().write(gson.toJson(Arrays.asList(BetSite.values())).getBytes());
            } else if (uri.contains("getmatches")) {
                logger.info("getmatches");
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_JSON);
                List<MatchJson> matches = BetDao.getMatchesWithoutBets().stream().map( match -> {
                    return new MatchJson(match.getId(), match.getMatchDay(), Teams.valueOf(match.getTeam1()).getRussian(), Teams.valueOf(match.getTeam2()).getRussian());
                }).collect(Collectors.toList());
                response.getOutputStream().write(gson.toJson(matches).getBytes());
            } else if (uri.contains("getmatchbestbets")) {
                logger.info("getmatchbestbets");
                response.setStatus(200, "OK");
                response.setContentType(CONTENT_TYPE_JSON);

                String matchId = request.getParameter("matchid");
                if (matchId == null || matchId.isEmpty()) {
                    response.getOutputStream().write("INCORRECT REQUEST!".getBytes());
                    return;
                }

                Match match = BetDao.getMatchWithBets(Long.parseLong(matchId));
                Map<BetType, MatchBestBet> bets = new HashMap<BetType, MatchBestBet>();

                for (BetSite site : BetSite.values()) {
                    for (BetType betType : match.getBets().get(site).keySet()) {
                        Bet bet = match.getBets().get(site).get(betType);
                        MatchBestBet tempKey = bets.get(betType);
                        if ((tempKey == null) || (tempKey.val < bet.getBetVal())) {
                            bets.put(betType, new MatchBestBet(betType.toString(), bet.getBetVal(), site.toString()));
                        }
                    }
                }

                response.getOutputStream().write(gson.toJson(bets.values()).getBytes());
            } else {
                response.setContentType(CONTENT_TYPE_JSON);
                logger.warn("Incorrect request - wrong URI!");
                response.getOutputStream().write("INCORRECT REQUEST!".getBytes());
            }
        }
    }

    private static class MatchBestBet {

        public String bet;
        public Double val;
        public String site;

        public MatchBestBet(String bet, Double val, String site) {
            this.bet = bet;
            this.val = val;
            this.site = site;
        }

    }

    private static class BetTypeJson {
        public String type;
        public String rus;

        public BetTypeJson(String type, String rusEnum) {
            this.type = type;
            this.rus = rusEnum;
        }

    }

    private static class MatchJson {

        public Long id;

        public Date matchDay;

        public String team1;

        public String team2;

        public MatchJson(Long id, Date matchDay, String team1, String team2) {
            this.id = id;
            this.matchDay = matchDay;
            this.team1 = team1;
            this.team2 = team2;
        }

    }

    @Override
    public RequestExecutorProvider getRequestExecutorProvider() {
        return Server.EXECUTOR_PROVIDER;
    }

}
