package com.example.n2.controller;

import com.example.n2.service.SteamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/steam")
public class SteamController {
    @Autowired
    private SteamService steamService;


    @PostMapping("/userInfo")
    @ResponseBody
    public HashMap<String, String> userInfo(@RequestBody SteamService steamService) {
        return (HashMap<String, String>) steamService.getUserInfo(steamService.getSteamKey(), steamService.getSteamId());
    }

    @PostMapping("/userFamilyInfo")
    @ResponseBody
    public List<HashMap<String, String>> getUserFamilyInfo(@RequestBody SteamService steamService) {
        return steamService.getUserFamilyInfo(steamService.getSteamKey(), steamService.getSteamId());
    }

    @PostMapping("/gameInfoAchievementsGlobal")
    @ResponseBody
    public HashMap<String, String> getGameInfoAchievementsGlobal(@RequestBody SteamService steamService) {
        return (HashMap<String, String>) steamService.getGameInfoAchievementsGlobal(steamService.getGameId());
    }

    @PostMapping("/gamerStatsAchievements")
    @ResponseBody
    public HashMap<String, String> getGamerStatsAchievements(@RequestBody SteamService steamService) {
        return (HashMap<String,String>) steamService.getGamerStatsAchievements(steamService.getSteamKey(), steamService.getSteamId());
    }

    @GetMapping("/sobre")
    public HashMap<String, String> getProjectInfo(SteamService steamService){
        return steamService.sobre();
    }
}