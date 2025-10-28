package me.fendi.skriptPDCAPI.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.fendi.skriptPDCAPI.utils.PDCUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

public class CondHasPDCTag extends Condition {

    static {
        Skript.registerCondition(CondHasPDCTag.class,
                "%itemtypes/blocks/entities% (has|have) pdc [tag] %string%",
                "%itemtypes/blocks/entities% (doesn't|does not|do not|don't) have pdc [tag] %string%");
    }

    private Expression<Object> holders;
    private Expression<String> tag;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        holders = (Expression<Object>) expressions[0];
        tag = (Expression<String>) expressions[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event event) {
        String tagName = tag.getSingle(event);
        if (tagName == null) return isNegated();

        NamespacedKey key = NamespacedKey.fromString(tagName);
        if (key == null) return isNegated();

        for (Object holder : holders.getArray(event)) {
            if (holder == null) continue;

            PersistentDataContainer container = PDCUtils.getContainer(holder);
            boolean hasTag = container != null && container.getKeys().contains(key);

            if (hasTag == isNegated()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return holders.toString(event, debug) + (isNegated() ? " doesn't have" : " has") +
                " pdc tag " + tag.toString(event, debug);
    }
}