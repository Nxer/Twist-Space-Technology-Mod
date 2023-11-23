package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic_t;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

public class TeamManager {

    private static final Map<String, String> User2Team = new HashMap<>();
    private static final Map<String, List<String>> Team2Users = new HashMap<>();

    public static boolean joinUserTeam(String userName, String teamName) {
        if (!Team2Users.containsKey(teamName)) return false;
        String oldTeam = User2Team.put(userName, teamName);
        DysonSphereManager.dirty();

        if (oldTeam != null) {
            List<String> members = Team2Users.get(oldTeam);
            if (members == null) return true;
            members.remove(userName);
            if (members.isEmpty()) Team2Users.remove(oldTeam);
        }
        return true;
    }

    public static boolean hasTeam(String teamName) {
        return Team2Users.containsKey(teamName);
    }

    public static boolean userHasTeam(String user) {
        return User2Team.containsKey(user);
    }

    public static String getOrCreatTeam(String user) {
        if (!User2Team.containsKey(user)) {
            String teamName = user + "'s Team";
            User2Team.put(user, teamName);
            Team2Users.put(teamName, new ArrayList<>());
            Team2Users.get(teamName)
                .add(user);
            DysonSphereManager.dirty();
        }
        String teamName = User2Team.get(user);
        if (Team2Users.containsKey(teamName)) return teamName;
        Team2Users.put(teamName, new ArrayList<>());
        Team2Users.get(teamName)
            .add(user);
        DysonSphereManager.dirty();
        return teamName;
    }

    public static void clear() {
        Team2Users.clear();
        User2Team.clear();
    }

    public static NBTTagCompound writeToNBT() {
        NBTTagCompound teaminfos = new NBTTagCompound();
        User2Team.forEach((name, teamName) -> {
            if (!teaminfos.hasKey(teamName)) teaminfos.setTag(teamName, new NBTTagList());
            teaminfos.getTagList(teamName, Constants.NBT.TAG_STRING)
                .appendTag(new NBTTagString(name));
        });
        return teaminfos;
    }

    @SuppressWarnings("unchecked")
    public static void readFromNBT(NBTTagCompound teams) {
        teams.func_150296_c()
            .forEach(name -> {
                NBTTagList members = teams.getTagList((String) name, Constants.NBT.TAG_LIST);
                int count = members.tagCount();
                while (count-- > 0) User2Team.put(members.getStringTagAt(count), (String) name);
            });
    }
}
