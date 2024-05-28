package de.nehlen.bingo.sidebar;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sidebar {
    public static final int MAX_HEIGHT = 15;

    public static final int MAX_LINE_LENGTH = 30;

    private static final Random RND = new Random();

    private static final Collection<Character> CHARS = new HashSet<>(Arrays.asList(new Character[]{
            Character.valueOf('0'), Character.valueOf('1'), Character.valueOf('2'), Character.valueOf('3'), Character.valueOf('4'), Character.valueOf('5'), Character.valueOf('6'), Character.valueOf('7'), Character.valueOf('8'), Character.valueOf('9'),
            Character.valueOf('a'), Character.valueOf('b'), Character.valueOf('c'), Character.valueOf('d'), Character.valueOf('e'), Character.valueOf('f'), Character.valueOf('k'), Character.valueOf('l'), Character.valueOf('m'), Character.valueOf('n'),
            Character.valueOf('o'), Character.valueOf('r')}));

    private final Scoreboard board = ((ScoreboardManager) Objects.<ScoreboardManager>requireNonNull(Bukkit.getScoreboardManager())).getNewScoreboard();

    private final Objective objective;

    private final List<SidebarLine> lines = new ArrayList<>();

    private final int beginIndex;

    private final SidebarColorPool colorPool = new SidebarColorPool();

    public Sidebar(Player player, String objectiveName, Component displayName, int beginIndex) {
        this.objective = this.board.registerNewObjective(objectiveName, Criteria.DUMMY, displayName);
        this.objective.displayName(displayName);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.beginIndex = beginIndex;
    }

    public Sidebar(Player player, String objectiveName, Component displayName) {
        this(player, objectiveName, displayName, 0);
    }

    public void display(Player player) {
        player.setScoreboard(this.board);
    }

    public void setDisplayName(Component displayName) {
        this.objective.displayName(displayName);
    }

    public void setLines(Component... newLines) {
        if (newLines.length > 15)
            throw new IllegalStateException("size of lines cannot be higher than 15");
        Collections.reverse(Arrays.asList((Object[]) newLines));
        generateLines(newLines.length);
        IntStream.range(0, this.lines.size()).forEach(i -> setText(newLines[i], this.lines.get(i)));
    }

//    @Deprecated
//    public void setLines(Collection<String> lines, Object... replace) {
//        setLines((String[]) ((List) lines.stream().map(s -> replace(s, replace)).collect(Collectors.toList())).toArray((Object[]) new String[lines.size()]));
//    }

    public void setLines(List<Component> lines) {
        setLines(lines.<Component>toArray(new Component[lines.size()]));
    }

    private void generateLines(int length) {
        if (this.lines.size() == length)
            return;
        this.colorPool.clear();
        ArrayList<SidebarLine> newLines = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            SidebarLine line = getLine(i + this.beginIndex);
            if (line == null) {
                line = getNewLine(i + this.beginIndex);
            } else {
                this.colorPool.add(line.getColor());
            }
            newLines.add(line);
        }
        this.lines.clear();
        this.lines.addAll(newLines);
    }

    public Scoreboard getBoard() {
        return this.board;
    }

    public void setText(Component text, SidebarLine line) {
        if (text.equals(line.getText()))
            return;

        /*
            if (text.length() > 30)
                throw new IllegalStateException("The text '" + text + "' is too long! The size must be less than 31");
            if ((text = colored(text)).equals(line.getText()))
                return;
            String suffix = "";
            String prefix = (text.length() < 16) ? text : text.substring(0, 16);
            if (text.length() > 16) {
                if (prefix.endsWith(String.valueOf('§'))) {
                    if (CHARS.contains(Character.valueOf(text.charAt(16)))) {
                        prefix = prefix.substring(0, prefix.length() - 1);
                        suffix = String.valueOf('§');
                    } else {
                        suffix = ChatColor.getLastColors(prefix);
                    }
                } else {
                    suffix = ChatColor.getLastColors(prefix);
                }
                int endIndex = (text.length() < 30) ? text.length() : 30;
                suffix = suffix + text.substring(16, endIndex);
            }
            line.getTeam().displayName();
            line.getTeam().setPrefix(prefix);
            line.getTeam().setSuffix(suffix);
            line.setText(text);
        */
        line.getTeam().prefix(text);
        line.setText(text);
    }

    private SidebarLine getNewLine(int index) {
        Team team = this.board.registerNewTeam("t" + RND.nextInt(99999));
        ChatColor color = this.colorPool.getFreeColor();
        this.colorPool.add(color);
        String entry = color.toString();
        team.addEntry(entry);
        SidebarLine line = new SidebarLine(index, team, entry, color);
        this.objective.getScore(line.getEntry()).setScore(line.getIndex());
        return line;
    }

    private SidebarLine getLine(int index) {
        for (SidebarLine line : this.lines) {
            if (line.getIndex() != index)
                continue;
            return line;
        }
        return null;
    }

    private String replace(String text, Object... replace) {
        ArrayList<String> r = new ArrayList<>();
        ArrayList<String> o = new ArrayList<>();
        IntStream.range(0, replace.length).forEach(i -> {
            if (i % 2 == 0) {
                r.add(String.valueOf(replace[i]));
                return;
            }
            o.add(String.valueOf(replace[i]));
        });
        return replaceList(text, r, o);
    }

    private String replaceList(String text, List<String> r, List<String> o) {
        if (r.size() != o.size())
            throw new IllegalArgumentException("length of replace must be even");
        String output = text;
        for (String s : r)
            output = output.replaceAll(s, o.get(r.indexOf(s)));
        return colored(output);
    }

    private String colored(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
