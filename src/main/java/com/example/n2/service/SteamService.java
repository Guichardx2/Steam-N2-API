package com.example.n2.service;

import com.example.n2.Sobre;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.logging.Logger;

@Service
public class SteamService {
    private static final Logger logger = Logger.getLogger(SteamService.class.getName());
    private Dotenv dotenv;
    private String steamKey;
    private String steamId;
    private String gameId;

    private final WebClient webClientInfo;

    public SteamService() {
        try {
            dotenv = Dotenv.load();
            steamKey = dotenv.get("API_KEY");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load .env file", e);
        }

        this.webClientInfo = WebClient.create("https://api.steampowered.com");
    }

    public String getSteamKey() {
        return steamKey;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public String getSteamId() {
        return steamId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    // Método para pegar informações do usuário
    public Map<String, String> getUserInfo(String steamKey, String steamId) {
        Map<String, String> respostaRequisicao = new HashMap<>();

        Mono<Map> response = webClientInfo.get()
                .uri("/ISteamUser/GetPlayerSummaries/v2/?key=" + steamKey + "&steamids=" + steamId)
                .retrieve()
                .bodyToMono(Map.class);

        Map<String, Object> resposta = response.block();

        if (resposta != null && resposta.get("response") != null) {
            Map<String, Object> responseMap = (Map<String, Object>) resposta.get("response");
            if (responseMap.get("players") != null) {
                Map<String, Object> playerInfo = ((List<Map<String, Object>>) responseMap.get("players")).get(0);
                respostaRequisicao.put("SteamId", (String) playerInfo.get("steamid"));
                respostaRequisicao.put("Nome de usuário", (String) playerInfo.get("personaname"));
                if (playerInfo.get("realname") != null) {
                    respostaRequisicao.put("Nome verdadeiro", (String) playerInfo.get("realname"));
                }
                respostaRequisicao.put("Tempo de conta", playerInfo.get("timecreated").toString());
                respostaRequisicao.put("Local", (String) playerInfo.get("loccountrycode"));
            }
        }
        return respostaRequisicao;
    }

    public List<HashMap<String, String>> getUserFamilyInfo(String steamKey, String steamId) {
        List<HashMap<String, String>> respostaRequisicao = new ArrayList<>();

        Mono<Map> response = webClientInfo.get()
                .uri("/ISteamUser/GetFriendList/v0001/?key=" + steamKey + "&steamid=" + steamId + "&relationship=friend")
                .retrieve()
                .bodyToMono(Map.class);

        Map<String, Object> resposta = response.block();

        if (resposta != null && resposta.get("friendslist") != null) {
            Map<String, Object> responseMap = (Map<String, Object>) resposta.get("friendslist");

            if (responseMap.get("friends") != null) {
                List<Map<String, Object>> friends = (List<Map<String, Object>>) responseMap.get("friends");
                for (Map<String, Object> friend : friends) {
                    HashMap<String, String> friendInfo = new HashMap<>();
                    String steamIdFriend = (String) friend.get("steamid");
                    String friendSince = friend.get("friend_since").toString();
                    String relationship = (String) friend.get("relationship");
                    friendInfo.put("Id Amigo Steam", steamIdFriend);
                    friendInfo.put("Tempo amigos", friendSince);
                    friendInfo.put("Tipo de relação", relationship);
                    respostaRequisicao.add(friendInfo);
                }
            }
        }
        return respostaRequisicao;
    }

    //Método para pegar informações do jogo
    public HashMap<String, String> getGameInfoAchievementsGlobal(String gameId) {
        HashMap<String, String> respostaRequisicao = new HashMap<>();

        Mono<Map> response = webClientInfo.get()
                .uri("/ISteamUserStats/GetGlobalAchievementPercentagesForApp/v0002/?gameid=" + gameId + "&format=json")
                .retrieve()
                .bodyToMono(Map.class);

        Map<String, Object> resposta = response.block();

        if (resposta != null && resposta.get("achievementpercentages") != null) {
            Map<String, Object> achievementPercentages = (Map<String, Object>) resposta.get("achievementpercentages");

            if (achievementPercentages.get("achievements") != null) {
                List<Map<String, Object>> achievements = (List<Map<String, Object>>) achievementPercentages.get("achievements");
                for (Map<String, Object> achievement : achievements) {
                    HashMap<String, String> achievementInfo = new HashMap<>();
                    String nomeConquista = (String) achievement.get("name");
                    String porcentagem = achievement.get("percent").toString();
                    achievementInfo.put("Nome da conquista", nomeConquista);
                    achievementInfo.put("Porcentagem", porcentagem);
                    respostaRequisicao.put(nomeConquista, porcentagem);
                }
            }
        }

        return respostaRequisicao;
    }


    public HashMap<String, String> getGamerStatsAchievements(String steamKey, String steamId) {
        HashMap<String, String> respostaRequisicao = new HashMap<>();

        try {
            // Fazendo a requisição para a API Steam
            Mono<Map> response = webClientInfo.get()
                    .uri("/ISteamUserStats/GetPlayerAchievements/v0001/?appid=440&key=" + steamKey + "&steamid=" + steamId)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> {
                                // Tratamento para status HTTP de erro 4xx ou 5xx
                                if (clientResponse.statusCode().value() == 403) {
                                    return Mono.error(new RuntimeException("Acesso negado: Verifique sua chave de API ou permissões. Pode ser que o perfil esteja privado."));
                                } else if (clientResponse.statusCode().value() == 404) {
                                    return Mono.error(new RuntimeException("Usuário ou aplicação não encontrados."));
                                } else {
                                    return Mono.error(new RuntimeException("Erro no servidor da API Steam."));
                                }
                            }
                    )
                    .bodyToMono(Map.class);

            Map<String, Object> resposta = response.block();

            if (resposta != null && "true".equals(resposta.get("success"))) {
                List<Map<String, Object>> achievements = (List<Map<String, Object>>) resposta.get("achievements");

                if (!achievements.isEmpty()) {
                    Map<String, Object> achievement = achievements.get(0);
                    respostaRequisicao.put("Nome da conquista", (String) achievement.get("apiname"));
                    respostaRequisicao.put("Conquista desbloqueada", achievement.get("achieved").toString());
                    respostaRequisicao.put("Tempo de desbloqueio", achievement.get("unlocktime").toString());
                }
            } else {
                respostaRequisicao.put("Erro", "Não foi possível encontrar as conquistas pelo perfil ser privado.");
            }

        } catch (RuntimeException e) {
            respostaRequisicao.put("Erro", e.getMessage());
        } catch (Exception e) {
            respostaRequisicao.put("Erro", "Ocorreu um erro ao processar a requisição.");
        }

        return respostaRequisicao;
    }


    public LinkedHashMap<String, String> sobre() {
        Sobre sobre = new Sobre();
        LinkedHashMap<String, String> respostaRequisicao = new LinkedHashMap<>();
        respostaRequisicao.put("estudante", sobre.getEstudante());
        respostaRequisicao.put("projeto", sobre.getProjeto());
        return respostaRequisicao;
    }
}